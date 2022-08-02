package view.settings;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.profile.UserInfo;

public class ProfileMenu extends VBox {
    protected TextField nickField = createNickField();
    protected Label levelLabel = createLevelLabel();
    
    protected ProfileMenu() {
        setMaxHeight(500.0);
        setMaxWidth(700.0);
        setPrefWidth(700.0);
        setPrefHeight(500.0);

        arrangeElements();
        getStyleClass().add("profile-menu");
    }

    private TextField createNickField() {
        TextField nickField = new TextField();
        nickField.setPromptText(UserInfo.getNick());
        return nickField;
    }

    private Label createLevelLabel() {
        Label levelLabel = new Label("Level " + Integer.toString(UserInfo.getLevel()));
        levelLabel.setId("level-label");
        return levelLabel;
    }

    private void arrangeElements() {
        HBox first = new HBox();
        first.getChildren().addAll(nickField, levelLabel);
        first.setSpacing(20.0);
        
        getChildren().addAll(first);
    }

    protected void updateInfo() {
        nickField.setPromptText(UserInfo.getNick());
        levelLabel.setText("Level " + Integer.toString(UserInfo.getLevel()));
    }
}
