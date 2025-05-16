package controllers;

import DAO.CategoryDAO;
import DAO.ProductDAO;
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
import models.Produit;
import models.Category;
import models.DatabaseConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ProductController {

    @FXML
    private TableView<Produit> productTable;
    @FXML
    private TableColumn<Produit, Integer> colId;
    @FXML
    private TableColumn<Produit, String> colName;
    @FXML
    private TableColumn<Produit, Integer> colQuantite;
    @FXML
    private TableColumn<Produit, Double> colPrice;
    @FXML
    private TableColumn<Produit, Void> colAction;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtQuantite;
    @FXML
    private TextField txtPrice;
    @FXML
    private TextField txtCategoryId;
    @FXML
    private TextField searchField;
    @FXML
    private Button searchButton;

    private ProductDAO productDAO;
    private Produit currentProduct; // Pour suivre le produit en cours d'édition
    @FXML private Button addButton;
    @FXML private Button backButton; 
    @FXML
    private ComboBox<Category> categoryComboBox;

    @FXML
    public void initialize() {
        try {
            addButton.setText("AJOUTER PRODUIT");
            Connection conn = DatabaseConnection.getConnection();
            productDAO = new ProductDAO(conn);
            setupTable();
            loadProducts();
            loadCategories();
            setupSearch();
        } catch (Exception e) {
            showAlert("Erreur", "Impossible de se connecter à la base de données", e.getMessage());
        }
    }

    private void setupSearch() {
        // Recherche en temps réel lors de la frappe
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterProducts(newValue);
        });
        
        // Recherche lors du clic sur le bouton (optionnel)
        searchButton.setOnAction(event -> {
            filterProducts(searchField.getText());
        });
    }
    
    private void filterProducts(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            loadProducts(); // Recharger tous les produits si le champ est vide
            return;
        }
        
        try {
            ObservableList<Produit> filteredList = FXCollections.observableArrayList(
                productDAO.searchProductsByName(searchText)
            );
            productTable.setItems(filteredList);
        } catch (SQLException e) {
            showAlert("Erreur", "Problème lors de la recherche", e.getMessage());
        }
    }

    private void loadCategories() {
    try {
        CategoryDAO categoryDAO = new CategoryDAO(DatabaseConnection.getConnection());
        List<Category> categories = categoryDAO.getAllCategorys();

        categoryComboBox.getItems().addAll(categories);

        // Afficher le nom dans la liste
        categoryComboBox.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Category item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNom());
            }
        });

        // Afficher le nom sélectionné
        categoryComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Category item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNom());
            }
        });

    } catch (SQLException e) {
        e.printStackTrace();
    }
}

    private void setupTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("prix"));
        colQuantite.setCellValueFactory(new PropertyValueFactory<>("quantite"));

        // Ajout des boutons d'action
        addActionButtons();

    }

    private void loadProducts() {
        try {
            ObservableList<Produit> productList = FXCollections.observableArrayList(productDAO.getAllProducts());
            productTable.setItems(productList);
        } catch (Exception e) {
            showAlert("Erreur", "Impossible de charger les produits", e.getMessage());
        }
    }

    @FXML
    private void onAddProduct() {
        try {
            // Validation des champs
            if (txtName.getText().isEmpty() || txtQuantite.getText().isEmpty() || 
                txtPrice.getText().isEmpty() || categoryComboBox.getValue() == null) {
                showAlert("Erreur", "Champs manquants", "Veuillez remplir tous les champs");
                return;
            }

            Category selectedCategory = categoryComboBox.getValue();
            if (selectedCategory == null) {
                // Affiche un message d'erreur
                System.out.println("Veuillez sélectionner une catégorie.");
                return;
            }

            String name = txtName.getText();
            int quantite = Integer.parseInt(txtQuantite.getText());
            double price = Double.parseDouble(txtPrice.getText());
            int categoryId = selectedCategory.getId();

            if (currentProduct == null) {
                // Mode ajout
                Produit p = new Produit(0, name, price, quantite, categoryId);
                productDAO.insertProduct(p);
            } else {
                // Mode édition
                currentProduct.setNom(name);
                currentProduct.setQuantite(quantite);
                currentProduct.setPrix(price);
                currentProduct.setCategory(categoryId);
                
                productDAO.updateProduct(currentProduct);
                currentProduct = null; // Réinitialiser après modification
                addButton.setText("AJOUTER PRODUIT"); // Remettre le texte original
            }

            loadProducts();
            clearForm();

        } catch (NumberFormatException e) {
            showAlert("Erreur", "Format incorrect", "Veuillez entrer des valeurs numériques valides");
        } catch (Exception e) {
            showAlert("Erreur", "Opération échouée", e.getMessage());
        }
    }

    private void clearForm() {
        txtName.clear();
        txtQuantite.clear();
        txtPrice.clear();
        categoryComboBox.getSelectionModel().clearSelection();
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
        Callback<TableColumn<Produit, Void>, TableCell<Produit, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Produit, Void> call(final TableColumn<Produit, Void> param) {
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
                            Produit produit = getTableView().getItems().get(getIndex());
                             editProduct(produit);
                        });

                        deleteBtn.setOnAction(event -> {
                            Produit produit = getTableView().getItems().get(getIndex());
                            deleteProduct(produit);
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

    private void editProduct(Produit produit) {
        txtName.setText(produit.getNom());
        txtQuantite.setText(String.valueOf(produit.getQuantite()));
        txtPrice.setText(String.valueOf(produit.getPrix()));
    
        // Sélectionner la bonne catégorie dans la ComboBox
        for (Category category : categoryComboBox.getItems()) {
            if (category.getId() == produit.getCategory()) {
                categoryComboBox.getSelectionModel().select(category);
                break;
            }
        }
    
        currentProduct = produit;
        addButton.setText("MODIFIER PRODUIT");
    }
    

    private void deleteProduct(Produit produit) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer le produit");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer le produit " + produit.getNom() + "?");
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                productDAO.deleteProduct(produit.getId());
                loadProducts(); // Recharger la liste
            } catch (Exception e) {
                showAlert("Erreur", "Impossible de supprimer le produit", e.getMessage());
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