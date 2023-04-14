package client_fx;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import server.models.Course;

import java.util.ArrayList;

/**
 * L'affichage et les methode en rapport avec l'affichage du client_fx.
 */
public class Vue extends Application {

    private ChoiceBox sessionSelect;
    private TableView listCourses;

    /**
     * Methode pour connaitre quel session est selectionner dans le client.
     * @return Un String qui est le nom de la session.
     */
    public String getSession(){
        return (String) this.sessionSelect.getSelectionModel().getSelectedItem();
    }

    /**
     * Affiche les cours de la session dans le tableau des cours.
     * @param courseList La liste des cours a afficher pour une session en particulier
     */
    public void showCourse(ArrayList<Course> courseList){
        ObservableList<Course> coursListData = FXCollections.observableArrayList(courseList);
        this.listCourses.setItems(coursListData);
    }

    /**
     * La metode pour lancer le client_fx et va afficher l'affichage
     * @param stage Le stage de base pour l'affichage.
     */
    @Override
    public void start(Stage stage) {

        Controleur controleur = new Controleur(new Modele(), this);

            stage.setTitle("Inscription UdeM");
            HBox root = new HBox();
            Scene scene = new Scene(root, 900, 600);

            VBox left = new VBox();
            left.setMinSize(450, 600);
            VBox right = new VBox();
            right.setMinSize(450, 600);

            Label titleLeft = new Label("Liste des cours");
            titleLeft.setFont(Font.font(25));


            this.listCourses = new TableView<>();
            this.listCourses.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            TableColumn code = new TableColumn("Code");
            code.setCellValueFactory(new PropertyValueFactory<>("code"));
            TableColumn cours = new TableColumn("Cours");
            cours.setCellValueFactory(new PropertyValueFactory<>("name"));
            listCourses.getColumns().addAll(code, cours);


            HBox selectLoad = new HBox();
            this.sessionSelect = new ChoiceBox(FXCollections.observableArrayList("Ete", "Automne", "Hiver"));
            this.sessionSelect.setValue("Automne");
            Button load = new Button("Charger");

            left.setSpacing(5);
            left.setPadding(new Insets(10, 0, 0, 10));
            left.setAlignment(Pos.CENTER);
            left.getChildren().add(titleLeft);
            left.getChildren().add(listCourses);
            left.getChildren().add(new Separator());
            left.getChildren().add(selectLoad);
            selectLoad.getChildren().addAll(sessionSelect, load);
            selectLoad.setAlignment(Pos.CENTER);
            this.sessionSelect.setTranslateY(20);
            load.setTranslateY(20);
            selectLoad.setSpacing(150);

            root.getChildren().add(left);

            Separator vSeparator = new Separator();
            vSeparator.setOrientation(Orientation.VERTICAL);
            root.getChildren().add(vSeparator);

            Label titleRight = new Label("Formulaire d'inscription");
            titleRight.setFont(Font.font(25));
            right.getChildren().add(titleRight);

            GridPane formulaire = new GridPane();

            Label prenomTxt = new Label("Prenom");
            prenomTxt.setFont(Font.font(16));
            TextField prenomIn = new TextField();
            formulaire.add(prenomTxt, 0, 0);
            formulaire.add(prenomIn, 1, 0);

            Label nomTxt = new Label("Nom");
            nomTxt.setFont(Font.font(16));
            TextField nomIn = new TextField();
            formulaire.add(nomTxt, 0, 1);
            formulaire.add(nomIn, 1, 1);

            Label emailTxt = new Label("Email");
            emailTxt.setFont(Font.font(16));
            TextField emailIn = new TextField();
            formulaire.add(emailTxt, 0, 2);
            formulaire.add(emailIn, 1, 2);

            Label matriculeTxt = new Label("Matricule");
            matriculeTxt.setFont(Font.font(16));
            TextField matriculeIn = new TextField();
            formulaire.add(matriculeTxt, 0, 3);
            formulaire.add(matriculeIn, 1, 3);

            Button envoyerBut = new Button("Envoyer");
            formulaire.add(envoyerBut, 1, 4);

            formulaire.setAlignment(Pos.TOP_CENTER);
            envoyerBut.setTranslateX(20);
            envoyerBut.setMinSize(100, 30);
            formulaire.setHgap(40);
            formulaire.setVgap(20);

            titleRight.setTranslateY(70);
            formulaire.setTranslateY(70);

            right.getChildren().add(formulaire);
            right.setSpacing(20);
            right.setAlignment(Pos.TOP_CENTER);

            root.getChildren().add(right);
            root.setSpacing(10);


            load.setOnAction((action) -> {
                controleur.loadCourse();
            });

            envoyerBut.setOnAction((action) -> {
                String prenom = prenomIn.getText();
                String nom = nomIn.getText();
                String email = emailIn.getText();
                String matricule = matriculeIn.getText();
                Course desiredCourse = (Course) this.listCourses.getSelectionModel().getSelectedItem();
                controleur.registerCourse(prenom, nom, email, matricule, desiredCourse);
            });

            stage.setScene(scene);
            stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
