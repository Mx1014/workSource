package com.everhomes.activity;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.everhomes.util.DateHelper;
import com.everhomes.util.StringHelper;


public class TemplatesConvert {
    public static String convert(String template, Map<String, String> variables, String defaultVal) {
        String pattern = "\\$\\{(.*?)\\}";
        Pattern match = Pattern.compile(pattern);
        Matcher m = match.matcher(template);
        while (m.find()) {
            String name = m.group(1);
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
    
    public static void main(String[] args) {
        ActivityPostCommand cmd=new ActivityPostCommand();
        cmd.setChangeVersion(1);
        cmd.setContactPerson("xxdes");
        cmd.setEndTimeMs(DateHelper.currentGMTTime().getTime()+24*60*60*1000);
        cmd.setStartTimeMs(DateHelper.currentGMTTime().getTime());
        cmd.setContactNumber("18565600064");
        cmd.setCreatorUid(10016L);
        cmd.setDescription("malegebi");
        cmd.setGroupDiscriminator("EhActivity");
        cmd.setGroupId(1534L);
        cmd.setSubject("nimeiceshi");
        cmd.setMaxAttendeeCount(10000);
        cmd.setLocation("shenzhen");
        cmd.setSignupFlag((byte)1);
        cmd.setNamespaceId(0);
        System.out.println(StringHelper.toJsonString(cmd));
    }

}
