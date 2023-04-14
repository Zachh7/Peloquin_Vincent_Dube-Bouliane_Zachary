package client_fx;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import static client_simple.Client_Simple.PORT;

import javafx.scene.control.Alert;
import server.models.Course;
import server.models.RegistrationForm;

/**
 * La classe modele contient les methodes pour communiquer avec le serveur.
 */
public class Modele {
    private ArrayList courseList;
    private Socket client;

    private ArrayList<String> errors = new ArrayList<String>();

    /**
     * Constructeur de Modele qui seulement initialiser la liste des cours a null.
     */
    public Modele(){
        this.courseList = null;
    }

    /**
     * Methode pour communiquer avec le serveur et cahrger les cours disponibles et les afficher.
     * @param session La session pour laquelle ont veut recevoir la liste des cours
     */
    public void loadCourse(String session) {

        try {
            this.client = new Socket("127.0.0.1", PORT);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(client.getOutputStream());
            objectOutputStream.writeObject("CHARGER " + session);
            objectOutputStream.flush();
            ObjectInputStream objectInputStream = new ObjectInputStream(client.getInputStream());
            this.courseList = (ArrayList) objectInputStream.readObject();

            if (courseList == null){ throw new IOException() ; }


        }catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur dans la communication avec le serveur");
                alert.setContentText("Une erreur est survenue pendant la communication avec le serveur");
                alert.show();
        }catch (ClassNotFoundException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur dans la communication avec le serveur");
            alert.setContentText("Une erreur est survenue pendant la communication avec le serveur");
            alert.show();
        }

    }

    /**
     * La methode inscription permet d'inscrire un etudiant a un cours si les donnees saisies sont valides.
     * @param prenom Le prenom de la personne qui veut s'inscrire.
     * @param nom Le nom de la personne qui veut s'inscrire.
     * @param email Le email de la personne qui veut s'inscrire.
     * @param matricule La matricule de la personne qui veut s'inscrire.
     * @param cours Le cours dans lequel la personne veut s'inscrire.
     * @throws IOException  Lance une erreur si un probleme de connexion avec le serveur se produit
     * @throws ClassNotFoundException Lance une erreur si la classe Course ou RegistrationForm n'existe pas.
     */
    public void registration(String prenom, String nom, String email, String matricule, Course cours)
            throws IOException, ClassNotFoundException {

        if (prenom.equals("")){
            this.errors.add("Veuillez entrer un prenom.");
        }

        if (nom.equals("")){
            this.errors.add("Veuillez entrer un nom.");
        }

        if (email.equals("")){
            this.errors.add("Veuillez entrer une adresse courriel.");
                    }
        else {
            if (!(email.matches("[^@\\s]+@[^@\\s]+\\.\\w+"))){
                        this.errors.add("Veuillez entrer une adresse courriel valide.");
                }
            }

        if (matricule.equals("")){
            this.errors.add("Veuillez entrer un matricule.");
                    }
        else {
            if (!(matricule.matches("^[0-9]{8}$"))){
                        this.errors.add("Veuillez entrer un matricule compose de 8 chiffres.");
                    }
            }

        if (cours == null){
            this.errors.add("Veuillez selectionner un cours dans la liste.");
        }

        String returnMessage;
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Confirmation");

        if (errors.isEmpty()) {
            RegistrationForm registrationForm = new RegistrationForm(prenom, nom, email, matricule, cours);

            this.client = new Socket("127.0.0.1", PORT);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(this.client.getOutputStream());
            objectOutputStream.writeObject("INSCRIRE");
            objectOutputStream.flush();
            objectOutputStream.writeObject(registrationForm);
            objectOutputStream.flush();

            ObjectInputStream ois = new ObjectInputStream(this.client.getInputStream());
            returnMessage = (String) ois.readObject();
        }
        else {
            returnMessage = "Echec d'inscription: \n";
            for ( int i=0; i<this.errors.size(); i++ ) {
                returnMessage += this.errors.get(i) + "\n";
                alert.setAlertType(Alert.AlertType.ERROR);
                alert.setTitle("Echec d'inscription: ");
            }
            this.errors.clear();

        }

        alert.setContentText(returnMessage);
        alert.show();
    }

    public ArrayList getCourseList() {
        return courseList;
    }
}
