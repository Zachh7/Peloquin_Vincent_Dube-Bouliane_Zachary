package client_fx;

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
        try {
            String potentialErrors = this.modele.registration(prenom, nom, email, matricule, course);
        } catch (IOException e) {
            throw new RuntimeException(e); // A modifier
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e); // A modifier
        }
    }
}
