package project.ui;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import project.MainApp;
import project.files.Config;
import project.logic.Assignment;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class AssignSetting extends UiPart<AnchorPane> {

    private List<String> pasteList;

    @FXML
    private TextArea tbNote;

    public AssignSetting(MainApp mainWindow, List<String> pasteList) {
        super(mainWindow, "/view/AssignSetting.fxml");
        this.pasteList = pasteList;

        mainWindow.setNextButtonText("Generate Excel");
        tbNote.setText("This screen is currently WIP.\n" +
                "For now, click \"Generate\" and \n" +
                "   1. This program will close automatically. \n" +
                "   2. Excel program will open in place of this program \n" +
                "   3. Do be patient (3-5s), as I did not add a loading bar :P\n");
    }

    @Override
    public void btnClickNext() {
        Assignment.loadParticipantList(pasteList);
        Assignment.printAllParticipants();
        Assignment.startDefaultAssignment();
        Assignment.compactEveryTunnel();
        Assignment.printTunnelDetails();
        Assignment.exportToExcel();

        new Thread(() -> {
            try {
                Desktop.getDesktop().open(new File(Config.getConfig().getExportExcelPath()));
            } catch (IOException e) {
                System.err.println("Unable to open excel file.");
                e.printStackTrace();
            }
        }).start();

        Platform.exit();
    }
}
