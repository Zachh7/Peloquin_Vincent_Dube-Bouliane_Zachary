package client_fx;

import javafx.scene.control.Alert;
import server.models.Course;
import server.models.RegistrationForm;

import java.io.IOException;
import java.util.ArrayList;

/**
 * La classe Controleur fait le lien entre le modele et la vue.
 */
public class Controleur {
    private Modele modele;
    private Vue vue;

    /**
     * Constructeur du controleur qui ne fait qu'initialiser un modele et une vue
     * @param m Le modele voulu
     * @param v La vue voulu
     */
    public Controleur(Modele m, Vue v){
        this.modele = m;
        this.vue = v;
    }

    /**
     * Communique avec le modele pour chager les cours dans la vue.
     */
    public void loadCourse() {
            this.modele.loadCourse(this.vue.getSession());
            ArrayList<Course> courseList = this.modele.getCourseList();
            this.vue.showCourse(courseList);
    }

    /**
     * Communique avec la vue pour permettre une inscription en communiquant avec le modele.
     * @param prenom Le prenom entre dans la vue.
     * @param nom   Le nom entre dans la vue.
     * @param email Le email entre dans la vue.
     * @param matricule La matricule entre dans la vue.
     * @param course Le cours selectionne sur la vue.
     */
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
