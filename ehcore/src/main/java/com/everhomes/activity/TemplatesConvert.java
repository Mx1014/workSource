package com.everhomes.activity;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemplatesConvert {
    public static String convert(String template, Map<String, String> variables, String defaultVal) {
        String pattern = "\\$\\{(.*?)\\}";
        Pattern match = Pattern.compile(pattern);
        Matcher m = match.matcher(template);
        while (m.find()) {
            String name = m.group(1);
            System.out.println(name);
            template = template.replace(String.format("${%s}", name), getValue(variables, name, ""));
        }
        return template;
    }

    private static String getValue(Map<String, String> map, String key, String defaultVal) {
        if (!map.containsKey(key)) {
            return defaultVal;
        }
        return map.get(key);
    }

}
