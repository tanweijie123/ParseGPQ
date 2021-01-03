package project.ui;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import project.model.rule.Rule;
import project.model.rule.RuleList;
import project.model.job.JobList;

public class AddEditRule extends Application {
    private Rule newRule;

    @FXML
    private ComboBox cbRuleType;

    @FXML
    private Label lblType;

    @FXML
    private ComboBox cbRuleContent;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnCancel;

    public AddEditRule() { } //default for add.

    @Override
    public void start(Stage primaryStage) throws Exception {
        newRule = null;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/AddEditRule.fxml"));
        fxmlLoader.setController(this);
        primaryStage = fxmlLoader.load();

        cbRuleType.setItems(initRuleType());
        cbRuleContent.setItems(initJobList());
        cbRuleType.getSelectionModel().select(0);
        cbRuleContent.getSelectionModel().select(0);

        cbRuleType.setOnAction(e -> {
            int selected = cbRuleType.getSelectionModel().getSelectedIndex();
            displaySubsequentContent(selected);
        });

        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.showAndWait();
    }

    private ObservableList<String> initRuleType() {
        return FXCollections.observableArrayList(RuleList.FULL_RULE_LIST);
    }

    private ObservableList<String> initJobList() {
        return FXCollections.observableArrayList(JobList.FULL_JOB_LIST);
    }

    private void displaySubsequentContent(int idx) {

        //clear any residue data
        cbRuleContent.getItems().clear();

        switch(idx) {
            case 0: displayAssignJobPerParty();
                break;
            default: throw new UnsupportedOperationException("Display Subsequent Content should not run into default");
        }
    }

    private void displayAssignJobPerParty() {
        lblType.setText("Job");
        cbRuleContent.setItems(initJobList());
    }

    public void btnAddClicked() {
        //validation check
        // currently both set to idx 0. so valid value is given

        newRule = RuleList.getRule(cbRuleType.getSelectionModel().getSelectedIndex(), cbRuleContent.getSelectionModel().getSelectedItem().toString());
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    public void btnCancelClicked() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    public Rule getRule() {
        return newRule;
    }
}
