// @formatter:off
package com.everhomes.rest.launchpadbase;

/**
 * <ul>
 *     <li>BANNERS((byte) 1, "Banners"): 广告组件</li>
 *     <li>BULLETINS((byte) 2, "Bulletins"): 公告组件</li>
 *     <li>NAVIGATOR((byte) 3, "Navigator"): NAVIGATOR</li>
 *     <li>OPPUSH((byte) 4, "OPPush"): 运营板块</li>
 *     <li>NEWSFLASH((byte) 5, "NewsFlash"): 新闻（原则上以后要合并到运营板块）</li>
 *     <li>NEWS((byte) 6, "News"): 新闻（原则上以后要合并到运营板块）</li>
 *     <li>TAB((byte) 7, "Tab"): 社群tab</li>
 * </ul>
 */
public enum WidgetType {
    BANNERS((byte) 1, "Banners"), BULLETINS((byte) 2, "Bulletins"), NAVIGATOR((byte) 3, "Navigator"), OPPUSH((byte) 4, "OPPush"),
    NEWSFLASH((byte) 5, "NewsFlash"), NEWS((byte) 6, "News"), TAB((byte) 7, "Tab");

    private byte code;
    private String text;

    private WidgetType(byte code, String text) {
        this.code = code;
        this.text = text;
    }

    public byte getCode() {
        return this.code;
    }

    public String getText() {
        return this.text;
    }

    public static WidgetType fromCode(Byte code) {
        if (code != null) {
            WidgetType[] values = WidgetType.values();
            for (WidgetType value : values) {
                if (value.getCode() == code.byteValue()) {
                    return value;
                }
            }
        }

        return null;
    }

    public static WidgetType fromText(String text) {
        if (text != null) {
            WidgetType[] values = WidgetType.values();
            for (WidgetType value : values) {
                if (value.getText().equals(text)) {
                    return value;
                }
            }
        }

        return null;
    }
}
