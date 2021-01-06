package project;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.jsoup.Jsoup;
import project.files.Config;
import project.ui.GetSource;
import project.ui.UiPart;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class MainApp extends Application {

    private static final String RELEASE_LINK = "https://tanwj.link/Download";
    private UiPart currentlyShowing;

    @FXML
    private AnchorPane mainPaneTop;

    @FXML
    private AnchorPane mainPaneBtm;

    @FXML
    private Button btnNext;

    @FXML
    private Label lblVersionNo;

    @FXML
    private Button btnUpdateNow;

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

        String ver = MainApp.class.getPackage().getImplementationVersion();
        if (ver == null || ver.isBlank()) {
            lblVersionNo.setText("<Unable to retrieve>");
        } else {
            lblVersionNo.setText(ver);
            try {
                String releaseVer = Jsoup.connect(RELEASE_LINK).execute().parse().select("blockquote").select("a").text();
                if (!releaseVer.strip().equals(ver.strip())) {
                    btnUpdateNow.setVisible(true);
                }
            } catch (Exception e) {
                System.err.println("[ERROR] Unable to connect to download page.");
                e.printStackTrace();
            }
        }

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

    public void btnUpdateClick() {
        new Thread(() -> {
            try {
                java.awt.Desktop.getDesktop().browse(new URL(RELEASE_LINK).toURI());
            } catch (IOException | URISyntaxException e) {
                System.err.println("[ERROR] Unable to launch browser to download page.");
                e.printStackTrace();
            }
        }).start();
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
