package com.example.weather;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URISyntaxException;

public class HelloApplication extends Application {


    static final int widht = 500;
    static final int height = 500;
    @Override
    public void start(Stage stage) throws IOException, URISyntaxException {
        ShowModal(stage);
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 335, 550);
        stage.setResizable(false);
        stage.setTitle("Погода Онлайн!");
        stage.setScene(scene);
        stage.show();
    }

    public void ShowModal(Stage stage) throws URISyntaxException {
        Stage modalStage = new Stage();
        modalStage.initModality(Modality.WINDOW_MODAL);
        modalStage.initOwner(stage);
        modalStage.initStyle(StageStyle.UNDECORATED);

        StackPane stackPane = new StackPane();
        stackPane.setAlignment(Pos.CENTER);

        ImageView backView = new ImageView();
        Image backImage = new Image(getClass().getResource("/com/example/weather/Loading.jpg").toExternalForm());
        backView.setImage(backImage);
        backView.setFitHeight(height);
        backView.setFitWidth(widht);
        stackPane.getChildren().add(backView);

        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(30,30,30,30));

        VBox innerVBox = new VBox();
        VBox.setVgrow(innerVBox, Priority.ALWAYS);
        vbox.getChildren().add(innerVBox);

        ImageView frontView = new ImageView();
        Image frontImage = new Image(getClass().getResource("/com/example/weather/gif.gif").toExternalForm());
        frontView.setImage(frontImage);
        frontView.setFitHeight((double) height / 4);
        frontView.setFitWidth((double) widht / 4);
        VBox.setVgrow(frontView, Priority.ALWAYS);
        vbox.getChildren().add(frontView);
        stackPane.getChildren().add(vbox);

        Scene modalScene = new Scene(stackPane, widht, height);

        PauseTransition timer = new PauseTransition(Duration.seconds(3));
        timer.setOnFinished( event -> {
            modalStage.close();
        });
        timer.playFromStart();;


        modalStage.setScene(modalScene);
        modalStage.showAndWait();
    }


    public static void main(String[] args) {
        launch();
    }
}
/*80442090e9bad59438b86babdd86afd8*/