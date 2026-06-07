package com.example.expensetracker;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/dashboard.fxml"));
        Scene scene = new Scene(loader.load(), 1100, 700);
        scene.getStylesheets().add(MainApp.class.getResource("/style.css").toExternalForm());
        stage.setTitle("Expense Tracker ₹");
        // Optional: add an icon if you have one
        // stage.getIcons().add(new Image(MainApp.class.getResourceAsStream("/icon.png")));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
