package project.files;

import project.model.rule.Rule;

import java.util.ArrayList;
import java.util.List;

public class Config {
    private static Config config; //Singleton

    private String googleSheetLink;
    private String downloadedExcelPath;
    private String exportExcelPath;
    private List<Rule> ruleList;

    private Config() {
        googleSheetLink = "";
        downloadedExcelPath = "data/forms.xlsx";
        exportExcelPath = "data/output.xlsx";
        ruleList = new ArrayList<>();
    }

    private static void updateSettings(Config newConfig) {
        if (config == null) {
            config = newConfig;
            return;
        }

        //update all values.
        if (!newConfig.googleSheetLink.isBlank())
            config.googleSheetLink = newConfig.googleSheetLink;
        if (!newConfig.downloadedExcelPath.isBlank())
            config.downloadedExcelPath = newConfig.downloadedExcelPath;
        if (!newConfig.exportExcelPath.isBlank())
            config.exportExcelPath = newConfig.exportExcelPath;
        if(!newConfig.ruleList.isEmpty())
            config.ruleList = newConfig.ruleList;
    }

    public static Config getConfig() {
        if (config == null)
            load();
        return config;
    }

    private static void load() {
        ConfigJsonAdapter.loadSettings().ifPresentOrElse(newS -> updateSettings(newS), () -> {
            if (config == null)
                config = new Config();
        });
    }

    private boolean save() {
        return ConfigJsonAdapter.saveSettings(this);
    }

    public String getGoogleSheetLink() {
        return config.googleSheetLink;
    }

    public String getDownloadedExcelPath() {
        return config.downloadedExcelPath;
    }

    public String getExportExcelPath() {
        return config.exportExcelPath;
    }

    /**
     * Returns a cloned version of the Rule List so that it is not mutable outside of config.
     */
    public List<Rule> getRuleList() {
        return new ArrayList<>(ruleList);
    }

    public boolean setGoogleSheetLink(String path) {
        config.googleSheetLink = path;
        return save();
    }

    public boolean setDownloadExcelPath(String path) {
        config.downloadedExcelPath = path;
        return save();
    }

    public boolean setExportExcelPath(String path) {
        config.exportExcelPath = path;
        return save();
    }

    public boolean setRuleList(List<Rule> newRuleList) {
        if (newRuleList == null)
            newRuleList = new ArrayList<>();

        config.ruleList = newRuleList;
        return save();
    }

}
