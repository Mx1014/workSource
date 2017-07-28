package com.everhomes.rest.userOrganization;

import java.sql.Timestamp;

/**
 * Created by Administrator on 2017/6/19.
 */
public class CreateUserOrganizationCommand {

    private Long id;
    private Long user_id;
    private Long organization_id;
    private String group_path;
    private String group_type;
    private Byte stauts;
    private Long namespace_id;
    private Timestamp createTime;
    private Byte visibleFlag;
    private Timestamp updateTime;
}
