// @formatter:off
package com.everhomes.rest.ui.privilege;

/**
 * <ul>权限 入口
 * <li>TASK_ALL_LIST("task_all_list")：查看全部的任务列表入口</li>
 * <li>TASK_GUARANTEE_LIST("task_guarantee_list"): 查看保修的任务列表入口</li>
 * <li>TASK_SEEK_HELP_LIST("task_seek_help_list"): 查看紧急求助的任务列表入口</li>
 * </ul>
 */
public enum EntrancePrivilege {
    TASK_ALL_LIST("task_all_list"), TASK_GUARANTEE_LIST("task_guarantee_list"), TASK_SEEK_HELP_LIST("task_seek_help_list");
    
    private String code;
       
    private EntrancePrivilege(String code) {
        this.code = code;
    }
       
    public String getCode() {
        return this.code;
    }
       
    public static EntrancePrivilege fromCode(String code) {
        EntrancePrivilege[] values = EntrancePrivilege.values();
        for(EntrancePrivilege value : values) {
            if(value.code.equals(code)) {
                return value;
            }
        }
       
        return null;
    }
}
