package com.tolojanahary.Gcourrier.controller;

import com.tolojanahary.Gcourrier.dao.entity.Destinatair;
import com.tolojanahary.Gcourrier.dao.entity.Expediteur;
import com.tolojanahary.Gcourrier.dao.repository.ExpediteurRepository;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Component
@RequiredArgsConstructor
public class ExpediteurController implements Initializable {
    @FXML
    private Button ajout_exp,modifier_exp,supprimer_exp;

    @FXML
    private AnchorPane form_ecole, form_eleve;

    @FXML
    private AnchorPane main_form;

    @FXML
    private TableColumn<Expediteur, String> table_exp_adresse , table_exp_email,
            table_exp_idexp,table_exp_nom,table_exp_prenom,table_exp_telephone;

    @FXML
    private TableView<Expediteur> table_expediteur;

    @FXML
    private javafx.scene.control.TextField txt_adresse_exp, txt_email_exp,
            txt_nom_exp,txt_num_exp,
            txt_prenom_exp,txt_telephone_exp;

    private final ExpediteurRepository expediteurRepository;
    private Expediteur expediteurEdit;

    private FileChooser fileChooser;

    @Setter
    Stage stage;

    @FXML
    void ajout_clic(ActionEvent event) {
        String nom = txt_nom_exp.getText();
        String prenom = txt_prenom_exp.getText();
        String adresse = txt_adresse_exp.getText();
        String telephone = txt_telephone_exp.getText();
        String email = txt_email_exp.getText();
        if(formIsValid()){
            return;
        }

        Expediteur expediteur = Expediteur.builder()
                .nom(nom)
                .prenom(prenom)
                .adresse(adresse)
                .telephone(telephone)
                .email(email)
                .build();
        expediteurRepository.save(expediteur);
        loadTable();
        clearForm();
    }

    @FXML
    void delete_clic(ActionEvent event) {
        if(expediteurEdit == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur de suppression");
            alert.setContentText("Veuillez selectionner un expediteur");
            alert.showAndWait();
            return;
        }
        expediteurRepository.delete(expediteurEdit);
        expediteurEdit = null;
        loadTable();
        clearForm();
    }

    @FXML
    void btn_importHandle(ActionEvent event) throws IOException {
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            FileInputStream fileInputStream = new FileInputStream(selectedFile);
            Workbook workbook = new XSSFWorkbook(fileInputStream);

            Sheet sheet = workbook.getSheetAt(0);

            for(Row row : sheet){
                String nom = row.getCell(0).getStringCellValue();
                String prenom = row.getCell(1).getStringCellValue();
                String adresse = row.getCell(2).getStringCellValue();
                String email = row.getCell(3).getStringCellValue();
                Cell cellTelephone = row.getCell(4);
                String telephone;
                if(cellTelephone.getCellType() == CellType.NUMERIC){
                    telephone = String.valueOf(row.getCell(4).getNumericCellValue());
                }else {
                    telephone = row.getCell(4).getStringCellValue();
                }
                Expediteur expediteur = Expediteur.builder()
                        .nom(nom)
                        .prenom(prenom)
                        .adresse(adresse)
                        .telephone(telephone)
                        .email(email)
                        .build();

                expediteurRepository.save(expediteur);
            }
        }
        loadTable();
    }


    @FXML
    void update_clic(ActionEvent event) {
        if(expediteurEdit == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur de modification");
            alert.setContentText("Veuillez selectionner un expediteur");
            alert.showAndWait();
            return;
        }
        if(formIsValid()){
            return;
        }
        String nom = txt_nom_exp.getText();
        String prenom = txt_prenom_exp.getText();
        String adresse = txt_adresse_exp.getText();
        String telephone = txt_telephone_exp.getText();
        String email = txt_email_exp.getText();

        expediteurEdit.setNom(nom);
        expediteurEdit.setPrenom(prenom);
        expediteurEdit.setAdresse(adresse);
        expediteurEdit.setTelephone(telephone);
        expediteurEdit.setEmail(email);
        expediteurRepository.save(expediteurEdit);
        expediteurEdit = null;
        loadTable();
        clearForm();


    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fileChooser = new FileChooser();
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Excel files", "*.xlsx"));
        table_exp_nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        table_exp_prenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        table_exp_adresse.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        table_exp_telephone.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        table_exp_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        table_exp_idexp.setCellValueFactory(new PropertyValueFactory<>("id_expediteur"));
        table_expediteur.setEditable(false);
        loadTable();
        onTableClick();
    }


    private void loadTable(){
        List<Expediteur> expediteurs = expediteurRepository.findAll();
        System.out.println(expediteurs.size());
        table_expediteur.setItems(FXCollections.observableList(expediteurs));
    }
    private void clearForm(){
        txt_nom_exp.clear();
        txt_prenom_exp.clear();
        txt_adresse_exp.clear();
        txt_telephone_exp.clear();
        txt_email_exp.clear();
    }

    private void onTableClick(){
        table_expediteur.setOnMouseClicked(mouseEvent -> {
            expediteurEdit = table_expediteur.getSelectionModel().getSelectedItem();
            if (expediteurEdit == null) return;
            txt_nom_exp.setText(expediteurEdit.getNom());
            txt_prenom_exp.setText(expediteurEdit.getPrenom());
            txt_adresse_exp.setText(expediteurEdit.getAdresse());
            txt_telephone_exp.setText(expediteurEdit.getTelephone());
            txt_email_exp.setText(expediteurEdit.getEmail());
            txt_num_exp.setText(String.valueOf(expediteurEdit.getId_expediteur()));
        });
    }
    private boolean formIsValid() {
        if (txt_nom_exp.getText().isEmpty() || txt_prenom_exp.getText().isEmpty() || txt_adresse_exp.getText().isEmpty() || txt_telephone_exp.getText().isEmpty() || txt_email_exp.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur de saisie");
            alert.setContentText("Veuillez remplir tous les champs");
            alert.showAndWait();
            return true;
        }
        return false;
    }
}
