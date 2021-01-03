package project.ui;

import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import project.MainApp;
import project.logic.Database;
import project.util.EntryParser;

import java.util.Arrays;

/**
 * This is the second page of the app.
 * It collects the participants on the left textbox, and reflect if the participant is existing in the database.
 */
public class PasteParticipant extends UiPart<AnchorPane> {

    @FXML
    private TextArea tbPasteParticipants;

    @FXML
    private ListView listView;

    public PasteParticipant(MainApp mainWindow) {
        super(mainWindow, "/view/PasteParticipant.fxml");

        listView.setItems(tbPasteParticipants.getParagraphs());
        listView.setCellFactory(cell -> new ListViewCell());

    }

    @Override
    public void btnClickNext() {
        AssignSetting as = new AssignSetting(mainWindow, Arrays.asList(tbPasteParticipants.getText().split("\n")));
        mainWindow.updatePaneTop(as);

    }

    class ListViewCell extends ListCell<StringBuilder> {
        @Override
        protected void updateItem(StringBuilder s, boolean empty) {
            super.updateItem(s, empty);

            if (empty || s == null || s.toString().isBlank()) {
                setGraphic(null);
                setText(null);
                setStyle(null);
            } else {
                String name = EntryParser.parseIgn(s.toString());
                if (Database.containsKey(name)) { //check against database
                    setText(name + " --> [" + Database.get(name).getIgn() + "]");
                    setStyle("-fx-control-inner-background: aquamarine");
                } else {
                    setText(name + " --> [not found]");
                    setStyle("-fx-control-inner-background: lightpink");
                }
            }
        }
    }
}
