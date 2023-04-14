package client_fx;

import javafx.scene.control.Alert;
import server.models.Course;
import server.models.RegistrationForm;

import java.io.IOException;
import java.util.ArrayList;

public class Controleur {
    private Modele modele;
    private Vue vue;

    public Controleur(Modele m, Vue v){
        this.modele = m;
        this.vue = v;
    }


    public void loadCourse() {
            this.modele.loadCourse(this.vue.getSession());
            ArrayList<Course> courseList = this.modele.getCourseList();
            this.vue.showCourse(courseList);
    }

    public void registerCourse(String prenom, String nom, String email, String matricule, Course course) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Echec d'inscription");
        try {
            this.modele.registration(prenom, nom, email, matricule, course);
        } catch (IOException e) {
            alert.setContentText("Probleme de communication avec le serveur.");
        } catch (ClassNotFoundException e) {
            alert.setContentText("La classe Course ou RegistrationForm n'existe pas.");
        }
    }
}
