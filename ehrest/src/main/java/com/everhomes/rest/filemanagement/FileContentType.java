package com.everhomes.rest.filemanagement;

/**
 * <ul>
 * <li>CATEGORY("category"): 目录</li>
 * <li>FOLDER("folder"): 文件夹</li>
 * <li>FILE("file"): 文件</li>
 * </ul>
 */
public enum FileContentType {
    CATEGORY("category"), FOLDER("folder"), FILE("file");

    private String code;

    private FileContentType(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public static FileContentType fromCode(String code) {
        if (code != null) {
            FileContentType[] values = FileContentType.values();
            for (FileContentType value : values) {
                if (code.equals(value.code)) {
                    return value;
                }
            }
        }

        return null;
    }
}
