package project;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import project.files.Config;
import project.ui.GetSource;
import project.ui.UiPart;

public class MainApp extends Application {

    private UiPart currentlyShowing;

    @FXML
    private AnchorPane mainPaneTop;

    @FXML
    private AnchorPane mainPaneBtm;

    @FXML
    private Button btnNext;

    @Override
    public void init() throws Exception {
        super.init();
        Config config = Config.getConfig();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/MainApp.fxml"));
        fxmlLoader.setController(this);
        AnchorPane mainWindow = fxmlLoader.load();

        Scene scene = new Scene(mainWindow);
        primaryStage.setScene(scene);
        primaryStage.setMinHeight(600);
        primaryStage.setMinWidth(800);
        primaryStage.setTitle("Parse GPQ");
        primaryStage.show();

        GetSource getSource = new GetSource(this); //first screen is get source
        updatePaneTop(getSource);

    }

    public void updatePaneTop(UiPart uiComponent) {
        mainPaneTop.getChildren().clear();
        currentlyShowing = uiComponent;
        Pane pane = uiComponent.getRoot();

        mainPaneTop.getChildren().add(pane);
        AnchorPane.setTopAnchor(pane, 0.0);
        AnchorPane.setBottomAnchor(pane, 0.0);
        AnchorPane.setLeftAnchor(pane, 0.0);
        AnchorPane.setRightAnchor(pane, 0.0);
    }

    public void btnNextClick() {
        currentlyShowing.btnClickNext();
    }

    public void enableNextButton() {
        btnNext.setDisable(false);
    }

    public void disableNextButton() {
        btnNext.setDisable(true);
    }

    public void setNextButtonText(String text) {
        btnNext.setText(text);
    }

}
