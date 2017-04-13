package com.everhomes.kafka.logs;


import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.LayoutBase;

import java.io.IOException;

public class CustomLogstashLayout extends LayoutBase<ILoggingEvent> {
    private final CustomLogstashFormatter formatter = new CustomLogstashFormatter();

    public CustomLogstashLayout() {
    }

    public String doLayout(ILoggingEvent event) {
        try {
            return this.formatter.writeValueAsString(event, this.getContext());
        } catch (IOException var3) {
            this.addWarn("Error formatting logging event", var3);
            return null;
        }
    }
}
