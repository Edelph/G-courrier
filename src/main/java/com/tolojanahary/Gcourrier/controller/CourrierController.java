package com.tolojanahary.Gcourrier.controller;

import com.tolojanahary.Gcourrier.dao.entity.Courrier;
import com.tolojanahary.Gcourrier.dao.entity.Date_;
import com.tolojanahary.Gcourrier.dao.entity.Destinatair;
import com.tolojanahary.Gcourrier.dao.entity.Expediteur;
import com.tolojanahary.Gcourrier.dao.repository.CourrierRepository;
import com.tolojanahary.Gcourrier.dao.repository.DestinataireRepository;
import com.tolojanahary.Gcourrier.dao.repository.ExpediteurRepository;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

@Component
@RequiredArgsConstructor
public class CourrierController implements Initializable {
    @FXML
    private Button ajout_exp;

    @FXML
    private ComboBox<Destinatair> combo_dest;

    @FXML
    private ComboBox<Expediteur> combo_exped;

    @FXML
    private DatePicker date_env;

    @FXML
    private DatePicker date_recep;

    @FXML
    private AnchorPane form_ecole, form_eleve;

    @FXML
    private Button modifier_exp,supprimer_exp;

    @FXML
    TableView<Courrier> table_expediteur;

    @FXML
    private TableColumn<Courrier, String> table_date, table_dest,table_status,table_expe,table_objet,table_type;

    @FXML
    private TextField txt_contenue,txt_num_exp,txt_numeleve,txt_objet,txt_status,txt_type;

    private Courrier courrierEdit;
    private List<Destinatair> destinataires;
    private List<Expediteur> expediteurs;

    private final DestinataireRepository destinataireRepository;
    private final CourrierRepository courrierRepository;
    private final ExpediteurRepository expediteurRepository;


    @FXML
    void ajout_clic(ActionEvent event) {
        String status = txt_status.getText();
        String type = txt_type.getText();
        String objet = txt_objet.getText();
        String contenue = txt_contenue.getText();
        Date_ date = Date_.builder().date_envoie(date_env.getValue()).date_recep(date_recep.getValue()).build();
        Expediteur expediteur = combo_exped.getValue();
        Destinatair destinatair = combo_dest.getValue();


        if(formIsValid()){
            return;
        }
        Courrier courrier = Courrier.builder()
                .contenu(contenue)
                .date(date)
                .destinatair(destinatair)
                .expediteur(expediteur)
                .objet(objet)
                .statut(status)
                .type(type)
                .build();
        courrierRepository.save(courrier);
        loadTable();
        clearForm();
    }

    @FXML
    void delete_clic(ActionEvent event) {
        if(courrierEdit == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur de suppression");
            alert.setContentText("Veuillez selectionner un expediteur");
            alert.showAndWait();
            return;
        }
        courrierRepository.delete(courrierEdit);
        courrierEdit = null;
        loadTable();
        clearForm();
    }


    @FXML
    void update_clic(ActionEvent event) {
        if(courrierEdit == null){
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
        String status = txt_status.getText();
        String type = txt_type.getText();
        String objet = txt_objet.getText();
        String contenue = txt_contenue.getText();
        Date_ date = Date_.builder().date_envoie(date_env.getValue()).date_recep(date_recep.getValue()).build();
        Expediteur expediteur = combo_exped.getValue();
        Destinatair destinatair = combo_dest.getValue();



        courrierEdit.setContenu(contenue);
        courrierEdit.setDate(date);
        courrierEdit.setDestinatair(destinatair);
        courrierEdit.setExpediteur(expediteur);
        courrierEdit.setObjet(objet);
        courrierEdit.setStatut(status);
        courrierEdit.setType(type);

        courrierRepository.save(courrierEdit);
        courrierEdit = null;
        loadTable();
        clearForm();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        destinataires = destinataireRepository.findAll();
        expediteurs = expediteurRepository.findAll();

        table_date.setCellValueFactory(data -> {
            Date_ date = data.getValue().getDate();
            if (date != null) return new SimpleStringProperty(date.getDate_envoie().toString());
            return new SimpleStringProperty("");});

        table_dest.setCellValueFactory(data -> {
            Destinatair destinatair = data.getValue().getDestinatair();
            if (destinatair != null) return new SimpleStringProperty(destinatair.getEmail_dest());
            return new SimpleStringProperty("");});

        table_objet.setCellValueFactory(new PropertyValueFactory<>("objet"));

        table_expe.setCellValueFactory(data -> {
            Expediteur expediteur = data.getValue().getExpediteur();
            if (expediteur != null) return new SimpleStringProperty(expediteur.getEmail());
            return new SimpleStringProperty("");});

        table_status.setCellValueFactory(new PropertyValueFactory<>("statut"));
        table_type.setCellValueFactory(new PropertyValueFactory<>("type"));
        table_expediteur.setEditable(false);

        loadTable();
        onTableClick();
        setComboToEditable();
    }

    private void loadTable(){
        List<Courrier> courriers = courrierRepository.findAll();
        table_expediteur.setItems(FXCollections.observableList(courriers));
    }
    private void clearForm(){
        txt_contenue.clear();
        txt_numeleve.clear();
        txt_objet.clear();
        txt_status.clear();
        txt_type.clear();
        date_env.setValue(null);
        date_recep.setValue(null);
        combo_dest.setValue(null);
        combo_exped.setValue(null);
    }

    private void onTableClick(){
        table_expediteur.setOnMouseClicked(mouseEvent -> {
            courrierEdit = table_expediteur.getSelectionModel().getSelectedItem();
            if(courrierEdit == null) return;
            txt_contenue.setText(courrierEdit.getContenu());
            txt_objet.setText(courrierEdit.getObjet());
            txt_status.setText(courrierEdit.getStatut());
            date_env.setValue(courrierEdit.getDate().getDate_envoie());
            date_recep.setValue(courrierEdit.getDate().getDate_recep());
            txt_type.setText(courrierEdit.getType());
            combo_dest.setValue(courrierEdit.getDestinatair());
            combo_exped.setValue(courrierEdit.getExpediteur());
            txt_num_exp.setText(String.valueOf(courrierEdit.getID_courrier()));
        });
    }
    private boolean formIsValid() {
        if(txt_contenue.getText().isEmpty() || txt_objet.getText().isEmpty() || txt_status.getText().isEmpty() || txt_type.getText().isEmpty() || combo_dest.getValue() == null || combo_exped.getValue() == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur de saisie");
            alert.setContentText("Veuillez saisir le nom de l'expediteur");
            alert.showAndWait();
            return true;
        }
        return false;
    }


    private  void setComboToEditable(){
        combo_exped.setEditable(true);
        combo_dest.setEditable(true);

        combo_exped.setConverter(new StringConverter<Expediteur>() {

            @Override
            public String toString(Expediteur expediteur) {
                if(expediteur == null || expediteur.getId_expediteur() == null) return "";
                return expediteur.getEmail();
            }

            @Override
            public Expediteur fromString(String email) {
                Optional<Expediteur> etudiantOptional = expediteurs.stream().filter(p -> Objects.equals(p.getEmail(), email)).findFirst();
                return etudiantOptional.orElseGet(Expediteur::new);
            }
        });

        combo_dest.setConverter(new StringConverter<Destinatair>() {

            @Override
            public String toString(Destinatair destinatair) {
                if(destinatair == null || destinatair.getId_destinataire() == null) return "";
                return destinatair.getEmail_dest();
            }

            @Override
            public Destinatair fromString(String email) {
                Optional<Destinatair> destinatairOptional = destinataires.stream().filter(p -> Objects.equals(p.getEmail_dest(), email)).findFirst();
                return destinatairOptional.orElseGet(Destinatair::new);
            }
        });

    }

    @FXML
    void destHandle(KeyEvent event) {
        String query = combo_dest.getEditor().getText();
        if("".equals(query.trim())) destinataires = destinataireRepository.findAll();
        else destinataires = destinataireRepository.findByEmail_destContains(query);
        combo_dest.setItems(FXCollections.observableList(destinataires));
    }

    @FXML
    void expeHandle(KeyEvent event) {
        String query = combo_exped.getEditor().getText();
        if("".equals(query.trim())) expediteurs = expediteurRepository.findAll();
        else expediteurs = expediteurRepository.findByEmailContains(query);
        combo_exped.setItems(FXCollections.observableList(expediteurs));
    }
}
