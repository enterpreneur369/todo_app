package com.micompany.todosapp;


import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.util.function.Consumer;

public class NewTodoController {
    @FXML
    private Label lblNewTODO; // Label header del modal
    @FXML
    private TextArea txaTODODescription; // Área de texto para la descripción del nuevo TODO
    @FXML
    private Button btnCancelCreateNewTODO; // Botón para cancelar la creación del nuevo TODO
    @FXML
    private Button btnCreateNewTODO; // Botón para crear un nuevo TODO

    private ObservableList<TODO> todos; // Lista observable de TODOS
    private Stage stage; // Referencia a la ventana del modal
    private Runnable updateHeaderLabel; // Función para actualizar la etiqueta de cabecera
    private Consumer<Integer> deleteTodoById; // Función para eliminar un TODO por ID

    // Método para establecer la lista de TODOS
    public void setTodos(ObservableList<TODO> todos) {
        this.todos = todos;
    }

    // Método para establecer la referencia a la ventana del modal
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    // Método para establecer la función de actualización de la etiqueta de cabecera
    public void setUpdateHeaderLabel(Runnable updateHeaderLabel) {
        this.updateHeaderLabel = updateHeaderLabel;
    }

    // Método para establecer la función de eliminación de un TODO por ID
    public void setDeleteTodoById(Consumer<Integer> deleteTodoById) {
        this.deleteTodoById = deleteTodoById;
    }

    // Método inicializador que se llama después de que se carga el archivo FXML
    @FXML
    private void initialize() {
        lblNewTODO.setText("Crear nuevo TODO");
        btnCancelCreateNewTODO.setText("Cancelar");
        btnCreateNewTODO.setText("Agregar");
        // Configura el evento para cerrar la ventana al hacer clic en el botón de cancelar
        btnCancelCreateNewTODO.setOnAction(event -> stage.close());

        // Configura el evento para crear un nuevo TODO al hacer clic en el botón de crear
        btnCreateNewTODO.setOnAction(event -> createNewTodo());
    }

    // Método para crear un nuevo TODO
    private void createNewTodo() {
        String description = txaTODODescription.getText(); // Obtiene la descripción del texto ingresado

        // Verifica que la descripción no esté vacía antes de crear el TODO
        if (!description.isEmpty()) {
            // Crea un nuevo TODO usando el tamaño actual de la lista como ID
            TODO newTodo = new TODO(todos.size() + 1, description, updateHeaderLabel, deleteTodoById);

            // Agrega el nuevo TODO a la lista de TODOS
            todos.add(newTodo);

            // Ejecuta la función para actualizar la etiqueta de cabecera después de agregar el TODO
            updateHeaderLabel.run();

            // Cierra la ventana del modal después de crear el TODO
            stage.close();
        }
    }
}