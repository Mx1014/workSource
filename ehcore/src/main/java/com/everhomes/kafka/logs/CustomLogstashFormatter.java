package com.everhomes.kafka.logs;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.core.Context;
import net.logstash.logback.encoder.com.fasterxml.jackson.core.JsonGenerator;
import net.logstash.logback.encoder.com.fasterxml.jackson.databind.JsonNode;
import net.logstash.logback.encoder.com.fasterxml.jackson.databind.ObjectMapper;
import net.logstash.logback.encoder.com.fasterxml.jackson.databind.node.ArrayNode;
import net.logstash.logback.encoder.com.fasterxml.jackson.databind.node.ObjectNode;
import net.logstash.logback.encoder.org.apache.commons.lang.time.FastDateFormat;
import org.slf4j.Marker;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/10.
 */
public class CustomLogstashFormatter {
    private static final ObjectMapper MAPPER;
    private static final FastDateFormat ISO_DATETIME_TIME_ZONE_FORMAT_WITH_MILLIS;

    SimpleDateFormat simpleDateFormat;

    private static final StackTraceElement DEFAULT_CALLER_DATA;
    private boolean includeCallerInfo;
    private JsonNode customFields;

    public CustomLogstashFormatter() {
        this(false);
    }

    public CustomLogstashFormatter(boolean includeCallerInfo) {
        this.includeCallerInfo = includeCallerInfo;
    }

    public CustomLogstashFormatter(boolean includeCallerInfo, JsonNode customFields) {
        this.includeCallerInfo = includeCallerInfo;
        this.customFields = customFields;
    }

    public byte[] writeValueAsBytes(ILoggingEvent event, Context context) throws IOException {
        return MAPPER.writeValueAsBytes(this.eventToNode(event, context));
    }

    public String writeValueAsString(ILoggingEvent event, Context context) throws IOException {
        return MAPPER.writeValueAsString(this.eventToNode(event, context));
    }

    private ObjectNode eventToNode(ILoggingEvent event, Context context) {
        ObjectNode eventNode = MAPPER.createObjectNode();
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        eventNode.put("@timestamp", ISO_DATETIME_TIME_ZONE_FORMAT_WITH_MILLIS.format(event.getTimeStamp()));
        eventNode.put("@version", 1);
        eventNode.put("message", event.getFormattedMessage());
        eventNode.put("time", simpleDateFormat.format(event.getTimeStamp()));

        this.createFields(event, context, eventNode);
        eventNode.put("tags", this.createTags(event));
        return eventNode;
    }

    private void createFields(ILoggingEvent event, Context context, ObjectNode eventNode) {
        eventNode.put("logger_name", event.getLoggerName());
        eventNode.put("thread_name", event.getThreadName());
        eventNode.put("level", event.getLevel().toString());
        eventNode.put("level_value", event.getLevel().toInt());
        if(this.includeCallerInfo) {
            StackTraceElement throwableProxy = this.extractCallerData(event);
            eventNode.put("caller_class_name", throwableProxy.getClassName());
            eventNode.put("caller_method_name", throwableProxy.getMethodName());
            eventNode.put("caller_file_name", throwableProxy.getFileName());
            eventNode.put("caller_line_number", throwableProxy.getLineNumber());
        }

        IThrowableProxy throwableProxy1 = event.getThrowableProxy();
        if(throwableProxy1 != null) {
            eventNode.put("stack_trace", CustomThrowableProxyUtil.asString(throwableProxy1));
        }

        if(context != null) {
            this.addPropertiesAsFields(eventNode, context.getCopyOfPropertyMap());
        }

        this.addPropertiesAsFields(eventNode, event.getMDCPropertyMap());
        this.addCustomFields(eventNode);
    }

    private ArrayNode createTags(ILoggingEvent event) {
        ArrayNode node = null;
        Marker marker = event.getMarker();
        if(marker != null) {
            node = MAPPER.createArrayNode();
            node.add(marker.getName());
            if(marker.hasReferences()) {
                Iterator i = event.getMarker().iterator();

                while(i.hasNext()) {
                    Marker next = (Marker)i.next();
                    node.add(next.getName());
                }
            }
        }

        return node;
    }

    private void addPropertiesAsFields(ObjectNode fieldsNode, Map<String, String> properties) {
        if(properties != null) {
            Iterator i$ = properties.entrySet().iterator();

            while(i$.hasNext()) {
                Map.Entry entry = (Map.Entry)i$.next();
                String key = (String)entry.getKey();
                String value = (String)entry.getValue();
                fieldsNode.put(key, value);
            }
        }

    }

    private StackTraceElement extractCallerData(ILoggingEvent event) {
        StackTraceElement[] ste = event.getCallerData();
        return ste != null && ste.length != 0?ste[0]:DEFAULT_CALLER_DATA;
    }

    private void addCustomFields(ObjectNode eventNode) {
        if(this.customFields != null) {
            Iterator i = this.customFields.fieldNames();

            while(i.hasNext()) {
                String k = (String)i.next();
                JsonNode v = this.customFields.get(k);
                eventNode.put(k, v);
            }
        }

    }

    public boolean isIncludeCallerInfo() {
        return this.includeCallerInfo;
    }

    public void setIncludeCallerInfo(boolean includeCallerInfo) {
        this.includeCallerInfo = includeCallerInfo;
    }

    public void setCustomFields(JsonNode customFields) {
        this.customFields = customFields;
    }

    public JsonNode getCustomFields() {
        return this.customFields;
    }

    static {
        MAPPER = (new ObjectMapper()).configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
        ISO_DATETIME_TIME_ZONE_FORMAT_WITH_MILLIS = FastDateFormat.getInstance("yyyy-MM-dd\'T\'HH:mm:ss.SSSZZ");

        DEFAULT_CALLER_DATA = new StackTraceElement("", "", "", 0);
    }
}
