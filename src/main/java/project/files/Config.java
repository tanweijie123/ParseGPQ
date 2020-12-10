package project.files;

public class Config {
    private static Config config; //Singleton

    private String googleSheetLink;
    private String downloadedExcelPath;
    private String exportExcelPath;

    private Config() {
        googleSheetLink = "";
        downloadedExcelPath = "data/forms.xlsx";
        exportExcelPath = "data/output.xlsx";
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

}
