package server;

import java.io.Serializable;

public class Course implements Serializable {
    private String session;
    private String nomCours;
    private String codeCours;

    public Course(String session, String nomCours, String codeCours) {
        this.session = session;
        this.nomCours = nomCours;
        this.codeCours = codeCours;
    }
}
