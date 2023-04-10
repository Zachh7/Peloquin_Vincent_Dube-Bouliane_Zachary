package client_simple;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import server.Course;

import static client_simple.Client_Simple.PORT;

public class Client {
    /**
     * Constructeur du client, ne fait qu'afficher un message d'accueil.
     */
    public Client() {
        System.out.println("*** Bienvenue au portail d'inscription de l'UDEM ***\n");
    }

    /**
     * Methode pour communiquer avec le serveur et cahrger les cours disponibles et les afficher.
     * @param session La session qu'on veut afficher les cours disponible.
     * @throws IOException  Se lance si une erreur se produit lors de la communication avec le serveur.
     * @throws ClassNotFoundException   Se lance si la classe Course.java n'existe pas.
     */
    public void loadCourse(String session) throws IOException, ClassNotFoundException {
        try{
            Socket client = new Socket("127.0.0.1", PORT);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(client.getOutputStream());
            objectOutputStream.writeObject("CHARGER " + session);
            objectOutputStream.flush();
            ObjectInputStream objectInputStream = new ObjectInputStream(client.getInputStream());
            ArrayList courseList = (ArrayList) objectInputStream.readObject();
            System.out.println("Les cours offert pendant la session d'" + session.toLowerCase() + " sont:\n");
            for ( int i=0; i<courseList.size(); i++ ){
                Course cours = (Course) courseList.get(i);
                System.out.println((i+1)+ ". " + cours.getCode() + "\t" + cours.getName() + "\n");
            }

        } catch (IOException e) {
            System.out.println("Erreur dans la communication avec le serveur");
        } catch (ClassNotFoundException e) {
            System.out.println("La classe Course n'existe pas");
        }

    }

    /**
     * La methode permet de choisir quel session ont veut afficher les cours.
     * @throws IOException Se lance si une erreur se produit lors de la communication avec le serveur.
     * @throws ClassNotFoundException Se lance si la classe Course.java n'existe pas.
     */
    public void chooseSession() throws IOException, ClassNotFoundException {
            System.out.println("Veuillez choisir la session pour laquelle vous voulez consulter la liste des cours:\n" +
                    "1. Automne\n2. Hiver\n3.Ete\nChoix:");
            Scanner scanner = new Scanner(System.in);
            int choix = scanner.nextInt();

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
            System.out.println("Choix:\n1. Consulter les cours offerts pour une autre session\n2. " +
                    "Inscription à un cours\nChoix:");
            Scanner scanner = new Scanner(System.in);
            int choix = scanner.nextInt();
            switch (choix){
                case 1: this.chooseSession();
                        break;
                case 2: this.inscription();
                        break;
            }
    }

    private void inscription() {
        /* TO DO */
    }


}
