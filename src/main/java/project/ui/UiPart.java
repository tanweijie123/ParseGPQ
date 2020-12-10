package project.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import project.MainApp;

import java.io.IOException;

public abstract class UiPart<T> {
    protected MainApp mainWindow;
    protected FXMLLoader fxmlLoader;

    protected UiPart(MainApp mainWindow, String fxmlFilePath) {
        this.mainWindow = mainWindow;
        this.fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFilePath));
        this.fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    public Pane getRoot() {
        return this.fxmlLoader.getRoot();
    }

    public abstract void btnClickNext();
}
