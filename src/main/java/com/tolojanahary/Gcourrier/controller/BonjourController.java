package com.tolojanahary.Gcourrier.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.springframework.stereotype.Component;

@Component
public class BonjourController {
    @FXML
    private Label label;

    public void sayHello(){
        this.label.setText("Hello, JavaFX");
    }
}
