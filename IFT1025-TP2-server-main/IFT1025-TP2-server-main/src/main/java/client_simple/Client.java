package client_simple;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import server.models.Course;
import server.models.RegistrationForm;

import static client_simple.Client_Simple.PORT;

public class Client {
    /**
     * Constructeur du client, ne fait qu'afficher un message d'accueil.
     */
    public Client() {
        System.out.println("*** Bienvenue au portail d'inscription de l'UDEM ***\n");
    }

    private Socket client;
    private ArrayList courseList;
    private ArrayList<String> errors = new ArrayList<String>();

    private Scanner scanner = new Scanner(System.in);

    /**
     * Methode pour communiquer avec le serveur et cahrger les cours disponibles et les afficher.
     * @param session La session qu'on veut afficher les cours disponible.
     * @throws IOException  Se lance si une erreur se produit lors de la communication avec le serveur.
     * @throws ClassNotFoundException   Se lance si la classe Course.java n'existe pas.
     */
    public void loadCourse(String session) throws IOException, ClassNotFoundException {
        try{
            this.client = new Socket("127.0.0.1", PORT);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(client.getOutputStream());
            objectOutputStream.writeObject("CHARGER " + session);
            objectOutputStream.flush();
            ObjectInputStream objectInputStream = new ObjectInputStream(client.getInputStream());
            this.courseList = (ArrayList) objectInputStream.readObject();
            System.out.println("Les cours offert pendant la session d'" + session.toLowerCase() + " sont:\n");
            for ( int i=0; i<courseList.size(); i++ ){
                Course cours = (Course) courseList.get(i);
                System.out.println((i+1)+ ". " + cours.getCode() + "\t" + cours.getName());
            }
            System.out.print("\n");

        } catch (IOException e) {
            System.out.println("Erreur dans la communication avec le serveur");
        } catch (ClassNotFoundException e) {
            System.out.println("La classe Course ou RegistrationForm n'existe pas");
        }

    }

    /**
     * La methode permet de choisir quel session ont veut afficher les cours.
     * @throws IOException Se lance si une erreur se produit lors de la communication avec le serveur.
     * @throws ClassNotFoundException Se lance si la classe Course.java n'existe pas.
     */
    public void chooseSession() throws IOException, ClassNotFoundException {
            System.out.print("Veuillez choisir la session pour laquelle vous voulez consulter la liste des cours:\n" +
                    "1. Automne\n2. Hiver\n3. Ete\nChoix: ");
            //Scanner scanner = new Scanner(System.in);
            int choix = this.scanner.nextInt();
            System.out.print("\n");

            switch (choix) {
                case 1:
                    this.loadCourse("Automne");
                    break;
                case 2:
                    this.loadCourse("Hiver");
                    break;
                case 3:
                    this.loadCourse("Ete");
                    break;
            }
            this.cmd();
    }

    /**
     * La methode permet de choisir entre s'inscrire et charger les cours d'une autre session.
     */
    public void cmd() throws IOException, ClassNotFoundException {
            System.out.print("Choix:\n1. Consulter les cours offerts pour une autre session\n2. " +
                    "Inscription à un cours\nChoix: ");
            //Scanner scanner = new Scanner(System.in);
            int choix = this.scanner.nextInt();
            this.scanner.nextLine();
            System.out.print("\n");

            switch (choix){
                case 1: this.chooseSession();
                        break;
                case 2: this.inscription();
                        break;
            }
    }

    /**
     * La methode inscription permet d'inscrire un etudiant a un cours si les donnees saisies sont valides
     * @throws IOException Se lance si une erreur se produit lors de la communication avec le serveur.
     * @throws ClassNotFoundException Se lance si la classe Course.java ou RegistrationForm n'existe pas.
     */
    public void inscription() throws IOException, ClassNotFoundException  {
            try { //Determiner pourquoi quand on saisit une mauvaise valeur on ne peut pas la modifier en 2e essai
                System.out.print("Veuillez saisir votre prenom: ");
                String prenom = this.scanner.nextLine();
                System.out.print("Veuillez saisir votre nom: ");
                String nom = this.scanner.nextLine();
                System.out.print("Veuillez saisir votre email: ");
                String email = this.scanner.nextLine();
                System.out.print("Veuillez saisir votre matricule: ");
                Integer matricule = this.scanner.nextInt();
                this.scanner.nextLine();
                System.out.print("Veuillez saisir le code du cours: ");
                String code = this.scanner.nextLine();
                System.out.println();


                if (!(matricule instanceof Integer) || !(matricule.toString().length() == 8)) {
                    this.errors.add("Votre matricule" + matricule + "est invalide.");
                }
                for ( int i=0; i<courseList.size(); i++ ){
                    Course cours = (Course) courseList.get(i);
                    if (cours.getCode().equals(code)) {
                        break;}
                    if (courseList.size()-1 == i){
                        this.errors.add("Le cours saisi n'est pas disponible durant la session désirée.");
                    }
                }

                if (0 < this.errors.size()){
                    IllegalArgumentException f = new IllegalArgumentException();
                    throw f;
                }

                for ( int i=0; i<courseList.size(); i++ ) {
                    Course cours = (Course) courseList.get(i);
                    if (cours.getCode().equals(code)) {
                        RegistrationForm inscription = new RegistrationForm(prenom, nom, email, matricule.toString(),
                                cours);
                        this.client = new Socket("127.0.0.1", PORT);
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(this.client.getOutputStream());
                        objectOutputStream.writeObject("INSCRIRE");
                        objectOutputStream.flush();
                        objectOutputStream.writeObject(inscription);
                        objectOutputStream.flush();
                        System.out.println("Inscription au cours " + cours.getName() + " confirmee\n");
                        break;
                    }
                }
            }
            catch (IllegalArgumentException f) {
                System.out.println("Echec d'inscription: ");
                for ( int i=0; i<errors.size(); i++ ) {
                    System.out.println(this.errors.get(i));
                }
                System.out.println();
            }
            catch (InputMismatchException e){
                System.out.println("\nEchec d'inscription: \nVotre matricule est invalide2.\n");
            }
            finally{this.cmd();}
    }


}
