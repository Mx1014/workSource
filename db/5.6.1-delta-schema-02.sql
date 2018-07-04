
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


-- 通用脚本
-- ADD BY 梁燕龙
-- issue-30013 初始化短信白名单配置项
-- 短信白名单 #30013
CREATE TABLE `eh_phone_white_list` (
	`id` BIGINT NOT NULL COMMENT '主键',
	`namespace_id` INT NOT NULL DEFAULT 0 COMMENT '域空间',
	`phone_number` VARCHAR(128) NOT NULL COMMENT '白名单手机号码',
	`creator_uid` BIGINT COMMENT '记录创建人userID',
	`create_time` DATETIME COMMENT '记录创建时间',
	PRIMARY KEY(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '短信白名单';
-- END BY 梁燕龙