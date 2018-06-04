// @formatter:off
package com.everhomes.rest.sensitiveWord;

import com.everhomes.util.StringHelper;

public class FilterWordsCommand {

    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
