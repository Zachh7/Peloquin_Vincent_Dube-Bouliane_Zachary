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

public class Modele {
    private ArrayList courseList;
    private Socket client;

    private ArrayList<String> errors = new ArrayList<String>();

    public Modele(){
        this.courseList = null;
    }

    public void loadCourse(String session){

        try {
            this.client = new Socket("127.0.0.1", PORT);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(client.getOutputStream());
            objectOutputStream.writeObject("CHARGER " + session);
            objectOutputStream.flush();
            ObjectInputStream objectInputStream = new ObjectInputStream(client.getInputStream());
            this.courseList = (ArrayList) objectInputStream.readObject();
        }catch (Exception e) {

        }

    }

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
