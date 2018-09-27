
-- 广告管理 v1.4 加字段    add by xq.tian  2018/03/07
ALTER TABLE eh_banners ADD COLUMN target_type VARCHAR(32) NOT NULL COMMENT 'e.g: NONE, POST_DETAIL, ACTIVITY_DETAIL, APP, URL, ROUTE';
ALTER TABLE eh_banners ADD COLUMN target_data VARCHAR(1024) DEFAULT NULL COMMENT 'It is different by different target_type';

ALTER TABLE eh_banners MODIFY COLUMN scene_type VARCHAR(32) DEFAULT NULL;
ALTER TABLE eh_banners MODIFY COLUMN apply_policy TINYINT DEFAULT NULL;

-- 启动广告 v1.1          add by xq.tian  2018/03/07
ALTER TABLE eh_launch_advertisements ADD COLUMN target_type VARCHAR(32) NOT NULL COMMENT 'e.g: NONE, POST_DETAIL, ACTIVITY_DETAIL, APP, URL, ROUTE';
ALTER TABLE eh_launch_advertisements ADD COLUMN target_data VARCHAR(1024) DEFAULT NULL COMMENT 'It is different by different target_type';
ALTER TABLE eh_launch_advertisements ADD COLUMN content_uri_origin VARCHAR(1024) DEFAULT NULL COMMENT 'Content uri for origin file.';

-- 用户认证 V2.3 #13692
ALTER TABLE `eh_users` ADD COLUMN `third_data` varchar(2048) DEFAULT NULL COMMENT 'third_data for AnBang';



-- 标准item 顺序 by jiarui
ALTER TABLE `eh_equipment_inspection_items` ADD COLUMN `default_order`  int(11) NOT NULL DEFAULT 0 AFTER `value_jason`;

/**
 * Designer:yilin Liu
 * Description:ISSUE#26184 门禁人脸识别
 * Created：2018-4-9
 */

-- 门禁多公司管理
ALTER TABLE `eh_door_access`
ADD `mac_copy` VARCHAR(128) COMMENT '原mac地址';

/**
* End by: yilin Liu
*/

-- Already delete in 5.5.1
ALTER TABLE `eh_organization_member_details` ADD COLUMN `profile_integrity` INTEGER NOT NULL DEFAULT '0';
ALTER TABLE eh_organization_member_details ADD COLUMN department VARCHAR(256) COMMENT '部门';
ALTER TABLE eh_organization_member_details ADD COLUMN department_ids VARCHAR(256) COMMENT '部门Id';
ALTER TABLE eh_organization_member_details ADD COLUMN job_position VARCHAR(256) COMMENT '岗位';
ALTER TABLE eh_organization_member_details ADD COLUMN job_position_ids VARCHAR(256) COMMENT '岗位Id';
ALTER TABLE eh_organization_member_details ADD COLUMN job_level VARCHAR(256) COMMENT '职级';
ALTER TABLE eh_organization_member_details ADD COLUMN job_level_ids VARCHAR(256) COMMENT '职级Id';
-- end Janson

-- 园区表增加namespace_id索引 add by yanjun 20180615
alter table eh_communities add index namespace_id_index(`namespace_id`);

-- fix for zuolinbase only, remove this after 5.5.2
ALTER TABLE `eh_organization_member_details` CHANGE COLUMN `profile_integrity` `profile_integrity` INT(11) NULL DEFAULT '0' ;
-- end Janson