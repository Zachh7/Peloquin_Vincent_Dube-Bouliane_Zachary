package server;

import javafx.util.Pair;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import server.models.Course;
import server.models.RegistrationForm;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

/**
 * La classe Server est le serveur qui sera demarre pour repondre aux requetes des clients qui se connecte.
 *
 */
public class Server {

    public final static String REGISTER_COMMAND = "INSCRIRE";
    public final static String LOAD_COMMAND = "CHARGER";
    private final ServerSocket server;
    private Socket client;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private final ArrayList<EventHandler> handlers;

    /**
     * Constructeur pour le serveur qui est appele lorsque le serveur est demarré.
     * @param port Le port utilisé par le serveur.
     * @throws IOException Lance une erreur si une erreur se produit lors de l'ouverture du serveur.
     */
    public Server(int port) throws IOException {
        this.server = new ServerSocket(port, 1);
        this.handlers = new ArrayList<EventHandler>();
        this.addEventHandler(this::handleEvents);
    }

    /**
     * Ajoute un EventHandler a la liste d'EventHandler.
     * @param h Un EventHandler a ajouter.
     */
    public void addEventHandler(EventHandler h) {
        this.handlers.add(h);
    }

    /**
     * Averti l'eventHandler lorsque qu'une commande est recu dans le serveur.
     * @param cmd La commande recu.
     * @param arg Les arguments de la commande.
     */
    private void alertHandlers(String cmd, String arg) {
        for (EventHandler h : this.handlers) {
            h.handle(cmd, arg);
        }
    }

    /**
     * La methode appelee pour demarrer le serveur et mets le serveur en attente de connexion par un client.
     */
    public void run() {
        while (true) {
            try {
                client = server.accept();
                System.out.println("Connecté au client: " + client);
                objectInputStream = new ObjectInputStream(client.getInputStream());
                objectOutputStream = new ObjectOutputStream(client.getOutputStream());
                listen();
                disconnect();
                System.out.println("Client déconnecté!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Reste en attente pour recevoir l'entree du client pour avertir les handlers
     * @throws IOException si une erreur se produit dans l'entree envoye par le client.
     * @throws ClassNotFoundException si l'objet lu n'existe pas pour le serveur
     */
    public void listen() throws IOException, ClassNotFoundException {
        String line;
        if ((line = this.objectInputStream.readObject().toString()) != null) {
            Pair<String, String> parts = processCommandLine(line);
            String cmd = parts.getKey();
            String arg = parts.getValue();
            this.alertHandlers(cmd, arg);
        }
    }

    /**
     * Prends la ligne de commande envoyée par le client et la transforme en paire en separant
     * la commande et l'argument.
     * @param line La ligne ecrite par le client qui est envoyé au serveur.
     * @return Une pair contenant la commande comme clé et l'argument comme valeur.
     */
    public Pair<String, String> processCommandLine(String line) {
        String[] parts = line.split(" ");
        String cmd = parts[0];
        String args = String.join(" ", Arrays.asList(parts).subList(1, parts.length));
        return new Pair<>(cmd, args);
    }

    /**
     * Déconnecte le client lorsque le client a fini.
     * @throws IOException si une erreur se produit pendant la fermeture
     */
    public void disconnect() throws IOException {
        objectOutputStream.close();
        objectInputStream.close();
        client.close();
    }

    /**
     * S'occupe de gerer les differentes commandes qui pourraient etre envoyer par le client.
     * @param cmd la commande envoyé par le client.
     * @param arg les parametres de la commande.
     */
    public void handleEvents(String cmd, String arg) {
        if (cmd.equals(REGISTER_COMMAND)) {
            handleRegistration();
        } else if (cmd.equals(LOAD_COMMAND)) {
            handleLoadCourses(arg);
        }
    }

    /**
     Lire un fichier texte contenant des informations sur les cours et les transofmer en liste d'objets 'Course'.
     La méthode filtre les cours par la session spécifiée en argument.
     Ensuite, elle renvoie la liste des cours pour une session au client en utilisant l'objet 'objectOutputStream'.
     La méthode gère les exceptions si une erreur se produit lors de la lecture du fichier ou de l'écriture de l'objet dans le flux.
     @param arg la session pour laquelle on veut récupérer la liste des cours
     */
    public void handleLoadCourses(String arg) {
        try {
            FileReader coursFile = new FileReader("cours.txt");
            BufferedReader courseRead = new BufferedReader(coursFile);
            ArrayList<Course> listeCours = new ArrayList<>();
            String cours;

            while ( (cours = courseRead.readLine()) != null ) {
                String[] coursArr = cours.split("\t");
                ArrayList<String> session = new ArrayList<>();
                session.add("Ete"); session.add("Automne"); session.add("Hiver");
                if ( (coursArr.length != 3) || !(session.contains(coursArr[2])) || !(coursArr[0].matches("[A-Z]{3}[0-9]{4}")) ) {
                    throw new IOException();
                }
                if (Objects.equals(coursArr[2], arg)) {
                    Course coursObj = new Course(coursArr[1], coursArr[0], coursArr[2]);
                    listeCours.add(coursObj);
                }
            }
            courseRead.close();
            objectOutputStream.writeObject(listeCours);
            objectOutputStream.flush();

        } catch (FileNotFoundException e) {
            System.err.println("Fichier 'Cours.txt' introuvable");
        } catch (IOException e) {
            System.err.println("Probleme de lecture du fichier");
        }

    }

    /**
     Récupérer l'objet 'RegistrationForm' envoyé par le client en utilisant 'objectInputStream', l'enregistrer dans un fichier texte
     et renvoyer un message de confirmation au client.
     La méthode gére les exceptions si une erreur se produit lors de la lecture de l'objet, l'écriture dans un fichier ou dans le flux de sortie.
     */
    public void handleRegistration() {
        String returnMessage = "Confirmation de l'inscription.";
        String erreur = "Erreur du serveur:\n";
        try {
            RegistrationForm registration = (RegistrationForm) this.objectInputStream.readObject();

            String formattedRegistration = "\n" + registration.getCourse().getSession() + "\t" +
                    registration.getCourse().getCode() + "\t" +
                    registration.getMatricule() + "\t" +
                    registration.getPrenom() + "\t" +
                    registration.getNom() + "\t" +
                    registration.getEmail();

            File registry = new File("inscription.txt");
            if (!(registry.exists())){
                throw new FileNotFoundException();
            }

            FileWriter fw = new FileWriter("inscription.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.append(formattedRegistration);
            bw.close();
        } catch (FileNotFoundException g) {
            returnMessage = erreur + "Le fichier inscription n'existe pas.";
        }
        catch (IOException e) {
            returnMessage = erreur + "Probleme de lecture de l'objet.";
            System.out.println(returnMessage);

        }
        catch (ClassNotFoundException f) {
            returnMessage = erreur + "La classe registration ou course n'existe pas.";
            System.out.println(returnMessage);

        } finally {
            try {
                System.out.println(returnMessage);
                this.objectOutputStream.writeObject(returnMessage);
            }
            catch (IOException e) {
                System.out.println("Probleme d'ecriture du message de retour d'inscription.");
            }

        }
    }
}

