package project.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import project.MainApp;
import project.files.Config;
import project.files.Download;
import project.files.ParseExcel;
import project.logic.Database;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.NoSuchFileException;
import java.util.NoSuchElementException;

public class GetSource extends UiPart<AnchorPane> {

    @FXML
    private ImageView imgViewValid;

    @FXML
    private Button btnCheck;

    @FXML
    private TextField tbGSheetLink;

    @FXML
    private Label lblFeedback;


    public GetSource(MainApp mainWindow) {
        super(mainWindow, "/view/GetSource.fxml");
        mainWindow.disableNextButton();

        //load excel link if found.
        if (!Config.getConfig().getGoogleSheetLink().isBlank()) {
            tbGSheetLink.setText(Config.getConfig().getGoogleSheetLink());
            btnCheckClick();
        }
    }

    public void btnCheckClick() {
        lblFeedback.setText("Checking . . .");
        imgViewValid.setImage(new Image(getClass().getResourceAsStream("/image/loading.png")));

        try {
            String gSheetLink = tbGSheetLink.getText().strip();
            if (!gSheetLink.startsWith("https://docs.google.com/spreadsheets/d/"))
                throw new IllegalArgumentException("");

            URL url = new URL(gSheetLink); //check if given link is a type of URL.

            //replace the download link.
            String[] split = tbGSheetLink.getText().split("/");
            String fileId = split[5];

            url = new URL("https://docs.google.com/spreadsheets/d/" + fileId + "/export?format=xlsx");
            Download download = new Download(url);
            boolean success = download.downloadAsFile(new File(Config.getConfig().getDownloadedExcelPath()));
            if (success) {
                imgViewValid.setImage(new Image(getClass().getResourceAsStream("/image/green-tick.png")));
                lblFeedback.setText("Excel file downloaded!");
                Config.getConfig().setGoogleSheetLink(gSheetLink);
                mainWindow.enableNextButton();
                return; //to prevent setting of red-cross image;
            } else {
                lblFeedback.setText("[ERROR] The GSheet is not shared, or your PC is blocking write access.");
            }
        } catch (MalformedURLException e) {
            lblFeedback.setText("This link is incorrect.");
        } catch (IllegalArgumentException e) {
            lblFeedback.setText("This hyperlink does not start with \"https://docs.google.com/spreadsheets/d/\"");
        }

        imgViewValid.setImage(new Image(getClass().getResourceAsStream("/image/red-cross.png")));
    }


    @Override
    public void btnClickNext() {
        try {
            ParseExcel.readExcel();
        } catch (IOException e) {
            lblFeedback.setText("Downloaded excel file cannot be found.");
            imgViewValid.setImage(new Image(getClass().getResourceAsStream("/image/red-cross.png")));
            mainWindow.disableNextButton();
            return;
        } catch (NoSuchElementException e) {
            lblFeedback.setText("Downloaded excel file seems to be of incorrect format.");
            imgViewValid.setImage(new Image(getClass().getResourceAsStream("/image/red-cross.png")));
            mainWindow.disableNextButton();
            return;
        }
        ParseExcel.pushToDatabase();
        PasteParticipant pp = new PasteParticipant(mainWindow);
        mainWindow.updatePaneTop(pp);
    }
}
