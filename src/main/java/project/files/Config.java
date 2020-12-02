package project.files;

public class Config {
    private static Config config; //Singleton

    private String googleSheetLink;
    private String downloadedExcelPath;
    private String exportExcelPath;

    private Config() {
        googleSheetLink = "";
        downloadedExcelPath = "";
        exportExcelPath = "";
    }

    private static void updateSettings(Config newConfig) {
        if (config == null) {
            config = newConfig;
            return;
        }

        //update all values.
        config.googleSheetLink = newConfig.googleSheetLink;
        config.downloadedExcelPath = newConfig.downloadedExcelPath;
        config.exportExcelPath = newConfig.exportExcelPath;
    }

    public static Config getSettings() {
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

    public String getExportExcelPath() {
        return config.exportExcelPath;
    }

    public boolean setGoogleSheetLink(String path) {
        config.googleSheetLink = path;
        return save();
    }

    public boolean setExportExcelLink(String path) {
        config.exportExcelPath = path;
        return save();
    }

}
