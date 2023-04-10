package client_fx;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Vue extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Inscription UdeM");
        HBox root = new HBox();
        Scene scene = new Scene(root, 900, 600);

        VBox left = new VBox();
        left.setMinSize(450,600);
        VBox right = new VBox();
        right.setMinSize(450,600);

        Label titleLeft = new Label("Liste des cours");
        titleLeft.setFont(Font.font(25));


        TableView listCourses = new TableView<>();
        listCourses.setColumnResizePolicy( TableView.CONSTRAINED_RESIZE_POLICY );
        TableColumn code = new TableColumn("Code");
        TableColumn cours = new TableColumn("Cours");
        listCourses.getColumns().addAll(code, cours);


        HBox selectLoad = new HBox();
        ChoiceBox sessionSelect = new ChoiceBox(FXCollections.observableArrayList("Ete", "Automne", "Hiver"));
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
        selectLoad.setSpacing(150);

        root.getChildren().add(left);

        Separator vSeparator = new Separator();
        vSeparator.setOrientation(Orientation.VERTICAL);
        root.getChildren().add(vSeparator);

        root.getChildren().add(right);
        root.setSpacing(10);

        stage.setScene(scene);
        stage.show();
    }
}
