package com.micompany.todosapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class HelloController implements Initializable {
    @FXML
    private Label lblHeader; // Etiqueta para mostrar el estado de los TODOS
    @FXML
    private TextField txtFinder; // Campo de texto para buscar TODOS
    @FXML
    private TableView<TODO> tblTodoList; // Tabla para mostrar la lista de TODOS
    @FXML
    private Button btnNewTODO; // Botón para crear un nuevo TODO

    private ObservableList<TODO> todos; // Lista observable de TODOS

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Configura la etiqueta de cabecera y el campo de búsqueda
        lblHeader.setText("Has completado 0 de 3 TODOS");
        txtFinder.setPromptText("Cortar la cebolla");
        btnNewTODO.setText("Nuevo TODO");

        // Configura las columnas de la tabla
        TableColumn<TODO, Integer> id = new TableColumn<>("ID");
        TableColumn<TODO, String> description = new TableColumn<>("DESCRIPCION");
        TableColumn<TODO, CheckBox> select = new TableColumn<>("¿COMPLETADO?");
        TableColumn<TODO, Button> delete = new TableColumn<>("BORRAR");
        tblTodoList.getColumns().addAll(id, description, select, delete);

        // Inicializa la lista de TODOS
        todos = FXCollections.observableArrayList();
        todos.add(createTodo(1, "Cortar cebolla"));
        todos.add(createTodo(2, "Leer libro"));

        // Asigna la lista de TODOS a la tabla
        tblTodoList.setItems(todos);

        // Configura las propiedades de las columnas
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        select.setCellValueFactory(new PropertyValueFactory<>("select"));
        delete.setCellValueFactory(new PropertyValueFactory<>("delete"));

        // Configura el filtro de búsqueda
        configureFilter();

        // Actualiza la etiqueta de cabecera
        updateHeaderLabel();
    }

    // Método para crear un nuevo TODO
    private TODO createTodo(int id, String description) {
        return new TODO(id, description, this::updateHeaderLabel, this::deleteTodoById);
    }

    // Método para actualizar la etiqueta de cabecera
    private void updateHeaderLabel() {
        long completedCount = todos.stream().filter(todo -> todo.getSelect().isSelected()).count();
        lblHeader.setText("Has completado " + completedCount + " de " + todos.size() + " TODOS");
    }

    // Método para eliminar un TODO por ID
    private void deleteTodoById(int id) {
        TODO todoToDelete = todos.stream().filter(todo -> todo.getId() == id).findFirst().orElse(null);
        if (todoToDelete != null) {
            todos.remove(todoToDelete);
            updateHeaderLabel();
        }
    }

    // Método para configurar el filtro de búsqueda
    private void configureFilter() {
        // Crea un listener para el campo de texto de búsqueda
        txtFinder.textProperty().addListener((observable, oldValue, newValue) -> {
            // Filtra la lista de TODOS basado en la descripción ingresada
            filterTodos(newValue);
        });
    }

    // Método para filtrar la lista de TODOS
    private void filterTodos(String filterText) {
        // Si no hay texto en el filtro, muestra todos los TODOS
        if (filterText == null || filterText.isEmpty()) {
            tblTodoList.setItems(todos);
        } else {
            // Aplica un filtro basado en la descripción del TODO
            Predicate<TODO> todoFilter = todo -> todo.getDescription().toLowerCase().contains(filterText.toLowerCase());
            ObservableList<TODO> filteredTodos = todos.filtered(todoFilter);
            tblTodoList.setItems(filteredTodos);
        }
    }

    // Método para mostrar el modal de creación de un nuevo TODO
    @FXML
    private void showNewTodoModal(ActionEvent event) {
        try {
            // Carga el archivo FXML del modal
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("modal-view.fxml"));
            Parent root = fxmlLoader.load();

            // Configura el nuevo escenario para el modal
            Stage stage = new Stage();
            stage.setTitle("Crear nuevo TODO");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));

            // Obtén el controlador del modal y pásale la referencia de la lista de TODOs y los métodos
            NewTodoController newTodoController = fxmlLoader.getController();
            newTodoController.setTodos(todos);
            newTodoController.setStage(stage);
            newTodoController.setUpdateHeaderLabel(this::updateHeaderLabel);
            newTodoController.setDeleteTodoById(this::deleteTodoById);

            // Muestra el modal y espera a que se cierre
            stage.showAndWait();
            updateHeaderLabel(); // Actualiza la etiqueta de cabecera después de cerrar el modal

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}