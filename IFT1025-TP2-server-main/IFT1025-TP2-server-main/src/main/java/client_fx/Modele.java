package client_fx;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import static client_simple.Client_Simple.PORT;
import server.models.Course;
import server.models.RegistrationForm;

public class Modele {
    private ArrayList courseList;
    private Socket client;

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

    public ArrayList getCourseList() {
        return courseList;
    }
}
