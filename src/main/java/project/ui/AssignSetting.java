package project.ui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import project.MainApp;
import project.files.Config;
import project.logic.Assignment;
import project.model.rule.Rule;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class AssignSetting extends UiPart<AnchorPane> {

    private List<String> pasteList;
    private ObservableList<Rule> obsRuleList;

    @FXML
    private ListView listViewRules;

    @FXML
    private Button btnNewRule;

    @FXML
    private Button btnClearRule;

    public AssignSetting(MainApp mainWindow, List<String> pasteList) {
        super(mainWindow, "/view/AssignSetting.fxml");
        this.pasteList = pasteList;

        mainWindow.setNextButtonText("Generate Excel");

        /*
        tbNote.setText("This screen is currently WIP.\n" +
                "For now, click \"Generate\" and \n" +
                "   1. This program will close automatically. \n" +
                "   2. Excel program will open in place of this program \n" +
                "   3. Do be patient (3-5s), as I did not add a loading bar :P\n");
        */

        List<Rule> ruleList = Config.getConfig().getRuleList();
        obsRuleList = FXCollections.observableList(ruleList);

        listViewRules.setPlaceholder(new Label("No custom rules set."));
        listViewRules.setItems(obsRuleList);
        listViewRules.setCellFactory(listView -> new RuleViewCell());

    }

    public void btnNewRuleClicked() throws Exception {
        AddEditRule app = new AddEditRule();
        app.start(null);

        Rule newRule = app.getRule();
        if (newRule != null)
            obsRuleList.add(newRule);

    }

    public void btnClearRuleClicked() {
        obsRuleList.clear();
    }

    @Override
    public void btnClickNext() {
        //save new custom rules
        if (!obsRuleList.equals(Config.getConfig().getRuleList()))
            Config.getConfig().setRuleList(obsRuleList);

        Assignment.loadParticipantList(pasteList);
        Assignment.loadTunnel();
        Assignment.printAllParticipants();

        for (Rule rule : obsRuleList) {
            Assignment.runRule(rule);
        }

        Assignment.startDefaultAssignment();
        Assignment.compactEveryTunnel();
        Assignment.printTunnelDetails();
        Assignment.exportToExcel();

        new Thread(() -> {
            try {
                java.awt.Desktop.getDesktop().open(new File(Config.getConfig().getExportExcelPath()));
            } catch (IOException e) {
                System.err.println("Unable to open excel file.");
                e.printStackTrace();
            }
        }).start();

        Platform.exit();
    }

    private class RuleViewCell extends ListCell<Rule> {

        HBox hbox = new HBox();
        Label label = new Label("");
        Pane pane = new Pane();
        Button upBtn = new Button("Up");
        Button downBtn = new Button("Down");
        //Button editBtn = new Button("Edit"); //does not support edit for now
        Button delBtn = new Button("Del");

        public RuleViewCell() {
            super();
            hbox.getChildren().addAll(label, pane, upBtn, downBtn, delBtn);
            HBox.setHgrow(pane, Priority.ALWAYS);
            delBtn.setOnAction(event -> getListView().getItems().remove(getItem()));

            upBtn.setOnAction(event -> {
                if (this.getIndex() > 0) {
                    Rule thisItem = getItem();
                    getListView().getItems().set(getIndex(), getListView().getItems().get(getIndex() -1));
                    getListView().getItems().set(getIndex() - 1, thisItem);
                }
            });

            downBtn.setOnAction(event -> {
                if (this.getIndex() < obsRuleList.size() - 1) {
                    Rule thisItem = getItem();
                    getListView().getItems().set(getIndex(), getListView().getItems().get(getIndex() + 1));
                    getListView().getItems().set(getIndex() + 1, thisItem);
                }
            });
        }

        @Override
        protected void updateItem(Rule rule, boolean empty) {
            super.updateItem(rule, empty);

            setGraphic(null);
            setText(null);

            if (rule != null && !empty) {
                label.setText(rule.toString());
                setGraphic(hbox);
            }
        }
    }
}
