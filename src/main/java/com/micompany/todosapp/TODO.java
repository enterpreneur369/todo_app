package com.micompany.todosapp;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;

import java.util.function.Consumer;

public class TODO {
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty description;
    private CheckBox select;
    private Button delete;
    private Runnable updateHeaderLabel;
    private Consumer<Integer> deleteTodoById;

    public TODO(Integer id, String description, Runnable updateHeaderLabel, Consumer<Integer> deleteTodoById) {
        this.id = new SimpleIntegerProperty(id);
        this.description = new SimpleStringProperty(description);
        this.select = new CheckBox();
        this.delete = new Button("X");
        this.updateHeaderLabel = updateHeaderLabel;
        this.deleteTodoById = deleteTodoById;

        this.select.setOnAction(event -> updateHeaderLabel.run());
        this.delete.setOnAction(event -> deleteTodoById.accept(id));
    }

    public Integer getId() {
        return this.id.get();
    }

    public void setId(Integer id) {
        this.id.set(id);
    }

    public String getDescription() {
        return this.description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public CheckBox getSelect() {
        return this.select;
    }

    public void setSelect(CheckBox select) {
        this.select = select;
    }

    public Button getDelete() {
        return this.delete;
    }

    public void setDelete(Button delete) {
        this.delete = delete;
    }
}