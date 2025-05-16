package controllers;

import DAO.CategoryDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.collections.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import models.Category;
import models.DatabaseConnection;
import java.sql.Connection;
import java.util.Optional;

public class CategoryController {

    @FXML
    private TableView<Category> categoryTable;
    @FXML
    private TableColumn<Category, Integer> colId;
    @FXML
    private TableColumn<Category, String> colName;
    @FXML
    private TableColumn<Category, String> colDescription;
  
    @FXML
    private TableColumn<Category, Void> colAction;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtDescription;
   

    private CategoryDAO categoryDAO;
    private Category currentCategory; // Pour suivre le produit en cours d'édition
    @FXML private Button addButton;
    @FXML private Button backButton; 

    @FXML
    public void initialize() {
        try {
            addButton.setText("AJOUTER CATEGORY");
            Connection conn = DatabaseConnection.getConnection();
            categoryDAO = new CategoryDAO(conn);
            setupTable();
            loadCategorys();
        } catch (Exception e) {
            showAlert("Erreur", "Impossible de se connecter à la base de données", e.getMessage());
        }
    }

    private void setupTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        

        // Ajout des boutons d'action
        addActionButtons();

    }

    private void loadCategorys() {
        try {
            ObservableList<Category> categoryList = FXCollections.observableArrayList(categoryDAO.getAllCategorys());
            categoryTable.setItems(categoryList);
        } catch (Exception e) {
            showAlert("Erreur", "Impossible de charger les produits", e.getMessage());
        }
    }

    @FXML
    private void onAddCategory() {
        try {
            // Validation des champs
            if (txtName.getText().isEmpty() || txtDescription.getText().isEmpty() ) {
                showAlert("Erreur", "Champs manquants", "Veuillez remplir tous les champs");
                return;
            }

            String name = txtName.getText();
            String description = txtDescription.getText();
            

            if (currentCategory == null) {
                // Mode ajout
                Category c = new Category(0, name, description);
                categoryDAO.insertProduct(c);
            } else {
                // Mode édition
                currentCategory.setNom(name);
                currentCategory.setDescription(description);
               
                
                categoryDAO.updateCategory(currentCategory);
                currentCategory = null; // Réinitialiser après modification
                addButton.setText("AJOUTER CATEGORY"); // Remettre le texte original
            }

            loadCategorys();
            clearForm();

        } catch (NumberFormatException e) {
            showAlert("Erreur", "Format incorrect", "Veuillez entrer des valeurs numériques valides");
        } catch (Exception e) {
            showAlert("Erreur", "Opération échouée", e.getMessage());
        }
    }

    private void clearForm() {
        txtName.clear();
        txtDescription.clear();
       
    }

     @FXML
    private void returnToMain() {
        try {
            // Méthode 1: Si vous voulez revenir à la fenêtre principale
            Stage stage = (Stage) backButton.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/views/MainView.fxml"));
            stage.setScene(new Scene(root));
            
            // Méthode 2: Si vous utilisez un système de navigation avec StackPane
            // ((Stage) backButton.getScene().getWindow()).getScene().setRoot(FXMLLoader.load(getClass().getResource("/views/MainView.fxml")));
            
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de revenir à l'accueil", e.getMessage());
        }
    }


    private void addActionButtons() {
        Callback<TableColumn<Category, Void>, TableCell<Category, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Category, Void> call(final TableColumn<Category, Void> param) {
                return new TableCell<>() {
                    private final Button editBtn = new Button();
                    private final Button deleteBtn = new Button();

                    {
                        // Configuration des icônes
                        Image editIcon = new Image(getClass().getResourceAsStream("/icones/edit.png"));
                        Image deleteIcon = new Image(getClass().getResourceAsStream("/icones/delete.png"));

                        editBtn.setGraphic(new ImageView(editIcon));
                        deleteBtn.setGraphic(new ImageView(deleteIcon));

                        // Style des boutons
                        editBtn.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
                        deleteBtn.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");

                        // Taille des icônes
                        editBtn.setGraphicTextGap(0);
                        deleteBtn.setGraphicTextGap(0);
                        ((ImageView) editBtn.getGraphic()).setFitWidth(16);
                        ((ImageView) editBtn.getGraphic()).setFitHeight(16);
                        ((ImageView) deleteBtn.getGraphic()).setFitWidth(16);
                        ((ImageView) deleteBtn.getGraphic()).setFitHeight(16);

                        // Actions des boutons
                        editBtn.setOnAction(event -> {
                            Category category = getTableView().getItems().get(getIndex());
                             editCategory(category);
                        });

                        deleteBtn.setOnAction(event -> {
                            Category category = getTableView().getItems().get(getIndex());
                            deleteCategory(category);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            HBox buttons = new HBox(5, editBtn, deleteBtn);
                            buttons.setAlignment(Pos.CENTER);
                            setGraphic(buttons);
                        }
                    }
                };
            }
        };

        colAction.setCellFactory(cellFactory);

    }

    private void editCategory(Category category) {
        // Remplir le formulaire avec les données du produit sélectionné
        txtName.setText(category.getNom());
        txtDescription.setText(category.getDescription());
        
        
        // Garder une référence du produit en cours de modification
        currentCategory = category;
        
        // Changer le texte du bouton
        addButton.setText("MODIFIER CATEGORY");
    }

    private void deleteCategory(Category category) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer le category");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer le category " + category.getNom() + "?");
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                categoryDAO.deleteCategory(category.getId());
                loadCategorys(); // Recharger la liste
            } catch (Exception e) {
                showAlert("Erreur", "Impossible de supprimer le category", e.getMessage());
            }
        }
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    
}