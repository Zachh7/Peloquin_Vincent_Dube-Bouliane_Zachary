package client_fx;

import server.models.Course;

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
}
