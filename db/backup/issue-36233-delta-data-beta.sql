-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: OPERATION
-- DESCRIPTION: 此SECTION放升级相关的操作要求，如调接口、查询数据确认、修改配置文件、更新特殊程序等
-- AUTHOR: 张智伟
-- REMARK: 调用接口/evh/punch/punchDayLogInitializeByMonth 不需要输入参数 初始化某个月的每日统计数据,上线时手动调用进行初始化


-- --------------------- SECTION END ---------------------------------------------------------

-- --------------------- SECTION BEGIN -------------------------------------------------------

-- AUTHOR: 刘一麟 2018年8月17日
-- REMARK: 人脸识别摄像头新增所属组织 补全namespace
update `eh_aclink_cameras` SET `owner_id` = (SELECT `owner_id` from eh_door_access where `eh_door_access`.`id` = `eh_aclink_cameras`.`door_access_id` ),
`owner_type` = (SELECT `owner_type` from eh_door_access where `eh_door_access`.`id` = `eh_aclink_cameras`.`door_access_id` ),
`namespace_id` = (SELECT `namespace_id` from eh_door_access where `eh_door_access`.`id` = `eh_aclink_cameras`.`door_access_id` )  where owner_id is NULL or owner_id = 0;

update `eh_aclink_ipads` SET `owner_id` = (SELECT `owner_id` from eh_door_access where `eh_door_access`.`id` = `eh_aclink_ipads`.`door_access_id` ),
`owner_type` = (SELECT `owner_type` from eh_door_access where `eh_door_access`.`id` = `eh_aclink_ipads`.`door_access_id` ),
`namespace_id` = (SELECT `namespace_id` from eh_door_access where `eh_door_access`.`id` = `eh_aclink_ipads`.`door_access_id` )  where owner_id is NULL or owner_id = 0;

-- --------------------- SECTION END ---------------------------------------------------------

-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: zuolin-base
-- DESCRIPTION: 此SECTION只在左邻基线（非独立署部）执行的脚本
-- AUTHOR:
-- REMARK:

-- --------------------- SECTION END ---------------------------------------------------------

-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: dev
-- DESCRIPTION: 此SECTION只在开发库中执行的脚本
-- AUTHOR:
-- REMARK:

-- --------------------- SECTION END ---------------------------------------------------------

-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: zijing
-- DESCRIPTION: 此SECTION只在清华信息港(紫荆)-999984执行的脚本
-- AUTHOR:
-- REMARK:

-- --------------------- SECTION END ---------------------------------------------------------

-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: guangda
-- DESCRIPTION: 此SECTION只在光大-999979执行的脚本
-- AUTHOR:
-- REMARK:

-- --------------------- SECTION END ---------------------------------------------------------

-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: szbay
-- DESCRIPTION: 此SECTION只在深圳湾-999966执行的脚本
-- AUTHOR:
-- REMARK:

-- --------------------- SECTION END ---------------------------------------------------------

-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: chuangyechang
-- DESCRIPTION: 此SECTION只在成都创业场-999964执行的脚本
-- AUTHOR:
-- REMARK:

-- --------------------- SECTION END ---------------------------------------------------------

-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: anbang
-- DESCRIPTION: 此SECTION只在安邦物业-999949执行的脚本
-- AUTHOR:
-- REMARK:

-- --------------------- SECTION END ---------------------------------------------------------

