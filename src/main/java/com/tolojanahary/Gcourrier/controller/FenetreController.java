package com.tolojanahary.Gcourrier.controller;

import com.tolojanahary.Gcourrier.GCourrierApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


@RequiredArgsConstructor
@Component
public class FenetreController implements Initializable {
    @FXML
    private Button courrier_clic;

    @FXML
    private Button dest_clic;

    @FXML
    private Button exp_clic;

    @FXML
    private BorderPane main;

    @FXML
    private AnchorPane main_form, backTitle;

    @FXML
    private Label title;

    private Stage stage;

    @FXML
    void fonction_nu_courrier(ActionEvent event) {
        main.setCenter(courrierView);
        title.setText("Courrier".toUpperCase());
    }

    @FXML
    void fonction_nu_destinataire(ActionEvent event) {
        main.setCenter(destinataireView);
        title.setText("Destinataire".toUpperCase());

    }

    @FXML
    void fonction_nu_expediteur(ActionEvent event) {
        title.setText("Expediteur".toUpperCase());
        main.setCenter(expediteurView);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        load();
        main.setCenter(courrierView);
        title.setText("Courrier".toUpperCase());
        title.setMaxWidth(Double.MAX_VALUE);
        title.setPadding(new javafx.geometry.Insets(8, 0, 8, 0));
        AnchorPane.setLeftAnchor(title, 0.0);
        AnchorPane.setRightAnchor(title, 0.0);
        title.setAlignment(Pos.CENTER);
    }

    private Node courrierView,expediteurView,destinataireView;
    private Node getExpediteurView() throws IOException {
        if (expediteurView == null) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(GCourrierApplication.class.getResource("expediteur.fxml"));
            loader.setControllerFactory(GCourrierApplication.context::getBean);
            expediteurView = loader.load();
        }
        return expediteurView;
    }

    private Node getCourrierView() throws IOException {
        if (courrierView == null) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(GCourrierApplication.class.getResource("courrier.fxml"));
            loader.setControllerFactory(GCourrierApplication.context::getBean);
            courrierView = loader.load();
        }
        return courrierView;
    }

    private Node getDestinataireView() throws IOException {
        if (destinataireView == null) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(GCourrierApplication.class.getResource("destinataire.fxml"));
            loader.setControllerFactory(GCourrierApplication.context::getBean);
            destinataireView = loader.load();
            ((DestinataireController)loader.getController()).setStage(stage);
        }
        return destinataireView;
    }

    void load()  {
        try {
            getCourrierView();
            getDestinataireView();
            getExpediteurView();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}


