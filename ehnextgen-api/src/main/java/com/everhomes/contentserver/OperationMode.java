package com.everhomes.contentserver;

public enum OperationMode {
    LOOKUP("lookup"), UPLOADED("uploaded"), DELETE("delete"), UNKOWN("unknown"), AUTH("auth");
    private String message;

    OperationMode(String message) {
        this.message = message;
    }

    public String getCode() {
        return message;
    }

    public static OperationMode fromStringCode(String message) {
        for (OperationMode type : OperationMode.values()) {
            if (type.message.equals(message)) {
                return type;
            }
        }
        return UNKOWN;
    }
}
