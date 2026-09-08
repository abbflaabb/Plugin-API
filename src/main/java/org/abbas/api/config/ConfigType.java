package org.abbas.api.config;

public enum ConfigType {
    MAIN("config.yml"),
    MESSAGES("messages.yml");

    private final String fileName;

    ConfigType(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return  fileName;
    }
}
