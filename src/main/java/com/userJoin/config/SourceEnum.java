package com.userJoin.config;

public enum SourceEnum {
    H_NAN("e94699ff2631485d93b86461d3e2e4ef","河南"),
    Z_ZHOU("1651651651651", "郑州");

    private String type;
    private String message;

    SourceEnum(String type, String message) {
        this.type = type;
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }
}
