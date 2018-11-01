
-- AUTHOR: 梁燕龙
-- REMARK: 微信分享配置中增加主题色字段
ALTER TABLE `eh_app_urls` ADD COLUMN `theme_color` VARCHAR(64) COMMENT '主题色';
ALTER TABLE `eh_app_urls` ADD COLUMN `package_name` VARCHAR(64) COMMENT '包名';

ALTER TABLE `eh_rentalv2_site_resources`
MODIFY COLUMN `name`  varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL AFTER `type`;

-- AUTHOR: 吴寒
-- REMARK: 打卡考勤V8.2 - 支持人脸识别关联考勤；支持自动打卡
ALTER TABLE `eh_punch_logs` ADD COLUMN `create_type` TINYINT NOT NULL DEFAULT 0 COMMENT '创建类型 : 0-正常打卡创建 1-自动打卡创建 2-人脸识别打卡创建 4-其他第三方接口创建(通过第三方接口打卡没有带创建类型)' ;

-- 外出打卡表
CREATE TABLE `eh_punch_go_out_logs` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `user_id` BIGINT DEFAULT NULL COMMENT 'user''s id',
  `detail_id` BIGINT DEFAULT NULL COMMENT 'eh_organization_member_details id',
  `organization_id` BIGINT DEFAULT NULL COMMENT 'organization id',
  `namespace_id` INT DEFAULT NULL COMMENT 'NAMESPACE id',
  `longitude` DOUBLE DEFAULT NULL,
  `latitude` DOUBLE DEFAULT NULL,
  `location_info` VARCHAR(1024) DEFAULT NULL COMMENT '打卡用到的地址定位',
  `wifi_info` VARCHAR(1024) DEFAULT NULL COMMENT '打卡用到的WiFi信息(暂时无用)',
  `img_uri` VARCHAR(2048) DEFAULT NULL COMMENT '打卡上传图片uri地址',
  `description` VARCHAR(1024) DEFAULT NULL COMMENT '备注',
  `punch_date` DATE DEFAULT NULL COMMENT 'user punch date',
  `punch_time` DATETIME DEFAULT NULL COMMENT '打卡时间',
  `identification` VARCHAR(255) DEFAULT NULL COMMENT 'unique identification for a phone',
  `status` TINYINT(4) DEFAULT NULL COMMENT '打卡状态 0-正常 1-迟到 2-早退 3-缺勤 14-缺卡',
  `update_date` DATE DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `i_eh_user_id` (`user_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

ALTER TABLE `eh_punch_day_logs` ADD COLUMN `go_out_punch_flag` TINYINT DEFAULT 0 COMMENT '是否外出打卡，1：是 0：否' AFTER `normal_flag`;
ALTER TABLE `eh_punch_statistics` ADD COLUMN `go_out_punch_day_count` INT DEFAULT 0 COMMENT '当月外出打卡天数' AFTER `rest_day_count`;

-- AUTHOR: 李清岩
-- REMARK: 20180930 issue-38336
ALTER TABLE `eh_door_access` ADD COLUMN `firmware_id` bigint(20) DEFAULT NULL COMMENT '门禁设备固件版本id';
ALTER TABLE `eh_door_access` ADD COLUMN `firmware_name` VARCHAR (128) DEFAULT NULL COMMENT '门禁设备固件名';
ALTER TABLE `eh_door_access` ADD COLUMN `device_id` bigint(20) DEFAULT NULL COMMENT '门禁设备类型id';
ALTER TABLE `eh_door_access` ADD COLUMN `device_name` VARCHAR(128) DEFAULT NULL COMMENT '门禁设备固件名';
ALTER TABLE `eh_door_access` ADD COLUMN `city_id` bigint(20) DEFAULT NULL COMMENT '城市id';
ALTER TABLE `eh_door_access` ADD COLUMN `province` VARCHAR(64) DEFAULT NULL COMMENT '省份名';

CREATE TABLE `eh_aclink_device` (
	`id` bigint(20),
	`name` VARCHAR(128) COMMENT '设备类型名称',
	`type` TINYINT(4) DEFAULT NULL COMMENT '设备类型 0：自有设备 1：第三方设备',
	`description` VARCHAR(1024) DEFAULT NULL COMMENT '设备特性',
	`support_bt` TINYINT(4) DEFAULT NULL COMMENT '蓝牙开门 0：不支持 1：支持',
	`support_qr` TINYINT(4) DEFAULT NULL COMMENT '二维码开门 0：不支持 1：支持',
	`support_face` TINYINT(4) DEFAULT NULL COMMENT '人脸识别开门 0：不支持 1：支持',
	`support_tempauth` TINYINT(4) DEFAULT NULL COMMENT '临时授权 0：不支持 1：支持',
	`firmware` VARCHAR(128) DEFAULT NULL COMMENT '固件名称',
	`firmware_id` bigint(20),
	`update` TINYINT(4) DEFAULT NULL COMMENT '默认升级 0：不支持 1：支持',
	`create_time` datetime DEFAULT NULL COMMENT '创建时间',
	`status` tinyint(4) DEFAULT 1 COMMENT '状态 0：失效 1：有效',
  	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='门禁设备类型';

CREATE TABLE `eh_aclink_firmware_new` (
  `id` bigint(20),
	`name` VARCHAR(128) COMMENT '固件名称',
	`version` VARCHAR(128) COMMENT '版本号，例如1.0.0',
	`number` int(11) DEFAULT NULL COMMENT '固件编号',
	`description` VARCHAR(1024) DEFAULT NULL,
  `bluetooth_name` VARCHAR(128) DEFAULT NULL COMMENT '蓝牙名称' ,
	`bluetooth_id` bigint(20),
  `wifi_name` VARCHAR(128) DEFAULT NULL COMMENT 'wifi名称' ,
	`wifi_id` bigint(20),
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
	`status` tinyint(4) DEFAULT NULL COMMENT '状态 0：失效 1：有效',
  	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='门禁固件新表';

CREATE TABLE `eh_aclink_firmware_package` (
  `id` bigint(20),
	`name` VARCHAR(128) COMMENT '程序名称',
  `type` TINYINT(4) DEFAULT NULL COMMENT '程序类型 0：蓝牙 1：wifi',
	`size` int(11),
  `download_url` varchar(1024) DEFAULT NULL COMMENT '存储地址' ,
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
	`status` tinyint(4) DEFAULT NULL COMMENT '状态 0：失效 1：有效',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='门禁固件程序表';


-- AUTHOR: 刘一麟 2018年8月23日
-- REMARK:issue-36233 门禁3.0.2

ALTER TABLE `eh_aclink_cameras` MODIFY COLUMN `door_access_id` BIGINT NULL DEFAULT NULL;
ALTER TABLE `eh_aclink_cameras` ADD COLUMN `owner_id` BIGINT NOT NULL DEFAULT '0' COMMENT '所属组织id' AFTER `server_id`;
ALTER TABLE `eh_aclink_cameras` ADD COLUMN `owner_type` TINYINT NOT NULL DEFAULT '0' COMMENT '所属组织类型' AFTER `server_id`;
ALTER TABLE `eh_aclink_cameras` ADD COLUMN `namespace_id` BIGINT NOT NULL DEFAULT '0' COMMENT '域空间id' AFTER `id`;
ALTER TABLE `eh_aclink_ipads` MODIFY COLUMN `door_access_id` BIGINT NULL DEFAULT NULL;
ALTER TABLE `eh_aclink_ipads` ADD COLUMN `owner_id` BIGINT NOT NULL DEFAULT '0' COMMENT '所属组织id' AFTER `server_id`;
ALTER TABLE `eh_aclink_ipads` ADD COLUMN `owner_type` TINYINT NOT NULL DEFAULT '0' COMMENT '所属组织类型' AFTER `server_id`;
ALTER TABLE `eh_aclink_ipads` ADD COLUMN `namespace_id` BIGINT NOT NULL DEFAULT '0' COMMENT '域空间id' AFTER `id`;
ALTER TABLE `eh_door_access` ADD COLUMN `firmware_version` VARCHAR(64) NULL DEFAULT NULL COMMENT '门禁固件版本' AFTER `id`;

CREATE TABLE `eh_aclink_form_titles` (
`id`  bigint(20) NOT NULL ,
`namespace_id`  int(11) NOT NULL ,
`owner_id`  bigint(20) NULL COMMENT '所属对象id',
`owner_type`  tinyint(4) NULL COMMENT '所属对象类型 0园区 1公司 2家庭 3门禁',
`path` varchar(1024) DEFAULT NULL COMMENT '记录更新人userId',
`name` varchar(64) NULL COMMENT '表单项名称',
`item_type` tinyint(4) NULL COMMENT '表单项类型, 0 表单中间结点 1 文本 2 单选 3 多选',
`status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '0已删除1有效',
`creator_uid` bigint(20) NOT NULL COMMENT '记录创建人userId',
`create_time` datetime NOT NULL COMMENT '记录创建时间',
`operator_uid` bigint(20) DEFAULT NULL COMMENT '记录更新人userId',
`operate_time` datetime DEFAULT NULL COMMENT '记录更新时间',
PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '门禁表单 标题';

CREATE TABLE `eh_aclink_form_values` (
`id` bigint(20) NOT NULL ,
`namespace_id` int(11) NOT NULL ,
`title_id` bigint(20) NOT NULL COMMENT '对应表单标题的id',
`value` varchar(1024) NULL COMMENT '表单项的值',
`type` tinyint(4) NULL COMMENT '值类型, 0 初始值(select,checkbox等) 1 默认值 2 输入值',
`status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '0已删除1有效',
`owner_id` bigint(20) NOT NULL COMMENT '记录所属对象Id',
`owner_type` tinyint(4) NOT NULL COMMENT '记录所属对象类型 0园区 1公司 2家庭 3门禁 4用户 5授权记录',
`creator_uid` bigint(20) NOT NULL COMMENT '记录创建人userId',
`create_time` datetime NOT NULL COMMENT '记录创建时间',
`operator_uid` bigint(20) DEFAULT NULL COMMENT '记录更新人userId',
`operate_time` datetime DEFAULT NULL COMMENT '记录更新时间',
PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '门禁表单 输入值';

CREATE TABLE `eh_aclink_group` (
`id` bigint(20) NOT NULL ,
`namespace_id` int(11) NOT NULL ,
`name` varchar(1024) NULL COMMENT '门禁组名称',
`status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '0已删除1有效',
`owner_id` bigint(20) NOT NULL COMMENT '记录所属对象Id',
`owner_type` tinyint(4) NOT NULL COMMENT '记录所属对象类型 0园区 1公司',
`creator_uid` bigint(20) NOT NULL COMMENT '记录创建人userId',
`create_time` datetime NOT NULL COMMENT '记录创建时间',
`operator_uid` bigint(20) DEFAULT NULL COMMENT '记录更新人userId',
`operate_time` datetime DEFAULT NULL COMMENT '记录更新时间',
PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '门禁组';

ALTER TABLE `eh_door_auth` ADD COLUMN `licensee_type` TINYINT NULL DEFAULT 0 COMMENT '被授权对象的类型 0用户 1组织架构节点 2项目(公司) 3楼栋(公司) 4楼层(公司) 5项目(家庭) 6楼栋(家庭) 7楼层(家庭)' AFTER `user_id`;
ALTER TABLE `eh_door_auth` ADD COLUMN `group_type` TINYINT NULL DEFAULT 0 COMMENT '门禁集合的类型 0 单个门禁 1 新门禁组(门禁3.0) ' AFTER `user_id`;

ALTER TABLE `eh_door_access` ADD COLUMN `adress_detail` varchar(64) NULL COMMENT '办公地点/楼栋_楼层' AFTER `address`;

-- AUTHOR: 张智伟 20180914
-- REMARK: ISSUE-37602 表单关联工作流节点
ALTER TABLE eh_general_approval_vals ADD COLUMN flow_node_id BIGINT COMMENT '表单绑定的工作流节点ID' AFTER flow_case_id;
ALTER TABLE eh_general_approval_vals ADD COLUMN creator_uid BIGINT COMMENT '创建人uid' AFTER create_time;
ALTER TABLE eh_general_approval_vals ADD COLUMN operator_uid BIGINT COMMENT '操作人Uid' AFTER creator_uid;
ALTER TABLE eh_general_approval_vals ADD COLUMN operate_time DATETIME COMMENT '编辑时间' AFTER operator_uid;

ALTER TABLE eh_general_approval_vals ADD INDEX i_eh_flow_case_id(`flow_case_id`);

-- AUTHOR: 杨崇鑫   20181017
-- REMARK: 缴费管理V7.0（新增缴费相关统计报表） 
-- REMARK: 增加项目-时间段（月份）统计结果集表
CREATE TABLE `eh_payment_bill_statistic_community` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER,
  `owner_id` BIGINT,
  `owner_type` VARCHAR(64),  
  `date_str` VARCHAR(10),  
  `amount_receivable` DECIMAL(10,2) COMMENT '应收（含税)',
  `amount_receivable_without_tax` DECIMAL(10,2) COMMENT '应收（不含税）',
  `tax_amount` DECIMAL(10,2) COMMENT '税额',
  `amount_received` DECIMAL(10,2) COMMENT '已收（含税）',
  `amount_received_without_tax` DECIMAL(10,2) COMMENT '已收（不含税）',
  `amount_owed` DECIMAL(10,2) COMMENT '待收（含税）',
  `amount_owed_without_tax` DECIMAL(10,2)  COMMENT '待收（不含税）',
  `amount_exemption` DECIMAL(10,2) COMMENT 'amount reduced',
  `amount_supplement` DECIMAL(10,2) COMMENT 'amount increased',  
  `due_day_count` DECIMAL(10,2) COMMENT '总欠费天数', 
  `notice_times` DECIMAL(10,2) COMMENT '总催缴次数',
  `collection_rate` DECIMAL(10,2) COMMENT '收缴率=已收金额/应收含税金额*100%',
  `create_time` DATETIME ON UPDATE CURRENT_TIMESTAMP,
  `update_time` DATETIME ON UPDATE CURRENT_TIMESTAMP, 
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='项目-时间段（月份）统计结果集表';

-- AUTHOR: 杨崇鑫   20181022
-- REMARK: 缴费管理V7.0（新增缴费相关统计报表） 
-- REMARK: 增加楼宇-时间段（月份）统计结果集表
CREATE TABLE `eh_payment_bill_statistic_building` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER,
  `owner_id` BIGINT,
  `owner_type` VARCHAR(64), 
  `building_id` BIGINT(20),
  `building_name` VARCHAR(256),
  `date_str` VARCHAR(10), 
  `amount_receivable` DECIMAL(10,2) COMMENT '应收（含税)',
  `amount_receivable_without_tax` DECIMAL(10,2) COMMENT '应收（不含税）',
  `tax_amount` DECIMAL(10,2) COMMENT '税额',
  `amount_received` DECIMAL(10,2) COMMENT '已收（含税）',
  `amount_received_without_tax` DECIMAL(10,2) COMMENT '已收（不含税）',
  `amount_owed` DECIMAL(10,2) COMMENT '待收（含税）',
  `amount_owed_without_tax` DECIMAL(10,2)  COMMENT '待收（不含税）',
  `due_day_count` DECIMAL(10,2) COMMENT '总欠费天数', 
  `notice_times` DECIMAL(10,2) COMMENT '总催缴次数',
  `collection_rate` DECIMAL(10,2) COMMENT '收缴率=已收金额/应收含税金额*100%',
  `create_time` DATETIME ON UPDATE CURRENT_TIMESTAMP,
  `update_time` DATETIME ON UPDATE CURRENT_TIMESTAMP, 
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='楼宇-时间段（月份）统计结果集表';

-- AUTHOR: 唐岑   20181021
-- REMARK: 资产管理V3.4（资产统计报表） 
-- REMARK: 项目信息报表结果集（项目-月份） 
CREATE TABLE `eh_property_statistic_community` (
  `id` bigint(20) NOT NULL,
  `namespace_id` int(11),
  `community_id` bigint(20),
  `community_name` varchar(64),
  `date_str` varchar(10) COMMENT '统计月份（格式为xxxx-xx）',
  `building_count` int(11) DEFAULT '0' COMMENT '园区下的楼宇总数',
  `total_apartment_count` int(11) DEFAULT '0' COMMENT '园区下的房源总数',
  `free_apartment_count` int(11) DEFAULT '0' COMMENT '园区下的待租房源数',
  `rent_apartment_count` int(11) DEFAULT '0' COMMENT '园区下的已出租房源数',
  `occupied_apartment_count` int(11) DEFAULT '0' COMMENT '园区下的已占用房源数',
  `living_apartment_count` int(11) DEFAULT '0' COMMENT '园区下的自用房源数',
  `saled_apartment_count` int(11) DEFAULT '0' COMMENT '园区下的已售房源数',
  `area_size` decimal(10,2) DEFAULT '0.00' COMMENT '园区的建筑面积',
  `rent_area` decimal(10,2) DEFAULT '0.00' COMMENT '园区的在租面积',
  `free_area` decimal(10,2) DEFAULT '0.00' COMMENT '园区的可招租面积',
  `rent_rate` decimal(10,2) COMMENT '出租率=在租面积/总的建筑面积*100%',
  `free_rate` decimal(10,2) COMMENT '空置率=可招租面积/总的建筑面积*100% ',
  `status` tinyint(4) DEFAULT '2' COMMENT '该条的记录状态：0-inactive, 1-confirming, 2-active',
  `create_time` datetime ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='项目信息报表结果集（项目-月份）';

-- AUTHOR: 唐岑   20181021
-- REMARK: 资产管理V3.4（资产统计报表） 
-- REMARK: 楼宇信息报表结果集（楼宇-月份） 
CREATE TABLE `eh_property_statistic_building` (
  `id` bigint(20) NOT NULL,
  `namespace_id` int(11),
  `community_id` bigint(20),
  `building_id` bigint(20),
  `building_name` varchar(64),
  `date_str` varchar(10) COMMENT '统计月份（格式为xxxx-xx）',
  `total_apartment_count` int(11) DEFAULT '0' COMMENT '楼宇内的房源总数',
  `free_apartment_count` int(11) DEFAULT '0' COMMENT '楼宇内的待租房源数',
  `rent_apartment_count` int(11) DEFAULT '0' COMMENT '楼宇内的已出租房源数',
  `occupied_apartment_count` int(11) DEFAULT '0' COMMENT '楼宇内的已占用房源数',
  `living_apartment_count` int(11) DEFAULT '0' COMMENT '楼宇内的自用房源数',
  `saled_apartment_count` int(11) DEFAULT '0' COMMENT '楼宇内的已售房源数',
  `area_size` decimal(10,2) DEFAULT '0.00' COMMENT '楼宇的建筑面积',
  `rent_area` decimal(10,2) DEFAULT '0.00' COMMENT '楼宇的在租面积',
  `free_area` decimal(10,2) DEFAULT '0.00' COMMENT '楼宇的可招租面积',
  `rent_rate` decimal(10,2) COMMENT '出租率=在租面积/总的建筑面积*100%',
  `free_rate` decimal(10,2) COMMENT '空置率=可招租面积/总的建筑面积*100% ',
  `status` tinyint(4) DEFAULT '2' COMMENT '该条的记录状态：0-inactive, 1-confirming, 2-active',
  `create_time` datetime ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='楼宇信息报表结果集（楼宇-月份）'; 


-- AUTHOR: 李清岩 20181029
-- REMARK:issue-38336 门禁3.0.2 门禁管理授权
CREATE TABLE `eh_aclink_management` (
`id` bigint NOT NULL ,
`namespace_id` int(11) NOT NULL ,
`door_id` bigint NOT NULL COMMENT '门禁Id',
`owner_id` bigint NOT NULL COMMENT '门禁归属对象Id',
`owner_type` tinyint NOT NULL COMMENT '门禁归属对象类型 0园区 1公司',
`manager_id` bigint NOT NULL COMMENT '授权对象Id',
`manager_type` tinyint NOT NULL COMMENT '授权对象类型 0园区 1公司',
`creator_uid` bigint NOT NULL COMMENT '记录创建人userId',
`create_time` datetime NOT NULL COMMENT '记录创建时间',
`status` tinyint NOT NULL DEFAULT '1' COMMENT '0已删除1有效',
PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '门禁管理授权';

-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: OPERATION
-- DESCRIPTION: 此SECTION放升级相关的操作要求，如调接口、查询数据确认、修改配置文件、更新特殊程序等

-- AUTHOR:xq.tian 20181016
-- REMARK:网关及注册中心部署.
-- DESCRIPTION：http://s.a.com/docs/faq/baseline-21539677844

-- AUTHOR:梁燕龙 20181016
-- REMARK:统一用户上线操作.
-- DESCRIPTION：http://s.a.com/docs/faq/baseline-21539678631

-- AUTHOR:杨崇鑫 20181018
-- REMARK:解决缺陷 #39352: 【全量标准版】【物业缴费】新增收费标准前端提示成功，但实际未新增成功，无相关数据，后台提示“应用开小差”
-- REMARK：备份eh_payment_variables表
-- select * from eh_payment_variables;

-- AUTHOR: 唐岑 2018年10月8日19:56:37
-- REMARK: 在瑞安CM部署时，需执行该任务（issue-38706）同步资产数据。详细步骤咨询 唐岑


-- AUTHOR: 黄明波 2018年10月27日17:31:00
-- REMARK: /yellowPage/transferApprovalToForm 参数ownerId 填 1802， 将返回的字符串发给我确认
-- REMARK: /yellowPage/transferPadItems 参数ownerId 填 1802， 将返回的字符串发给我确认

-- --------------------- SECTION END OPERATION------------------------------------------------


-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境


-- AUTHOR:黄明波
-- REMARK:服务联盟数据迁移 迁移1~迁移8
-- 迁移 start
-- 迁移1.调整ca表的ownerType和ownerId
update eh_service_alliance_categories ca, eh_service_alliances sa 
set ca.owner_type = sa.owner_type, ca.owner_id = sa.owner_id, ca.`type` = ca.id 
,ca.enable_provider = ifnull(sa.integral_tag3, 0) , ca.enable_comment = ifnull(sa.enable_comment, 0)
where ca.parent_id = 0 and sa.`type` = ca.id;


-- 迁移2.调整ca表子类的ownerType ownerId, type
update eh_service_alliance_categories  cag1,  eh_service_alliance_categories  cag2 
set cag1.owner_type = cag2.owner_type, cag1.owner_id = cag2.owner_id, cag1.`type` = cag2.`type` 
where cag1.parent_id = cag2.id;


-- 迁移3.更新ca表skip_rule
update eh_service_alliance_categories ca, eh_service_alliance_skip_rule sr 
set ca.skip_type = 1, ca.delete_uid = -100 where ca.id = sr.service_alliance_category_id and sr.id is not null and ca.namespace_id = sr.namespace_id;


-- 迁移4.tag表填充ownerType ownerId
update eh_alliance_tag tag, eh_service_alliances sa 
set tag.owner_type = sa.owner_type, tag.owner_id = sa.owner_id 
where tag.type = sa.type and sa.parent_id = 0 and tag.type <> 0 ;

-- 迁移5.jumpType应用跳转时，设置为3 
update eh_service_alliances 
set  integral_tag1 = 3
where module_url not like 'zl://approva%' and  module_url not like 'zl://form%' and  module_url is not null and integral_tag1 = 2;

-- 迁移5.1 迁移首页图片
update eh_service_alliance_attachments st, eh_service_alliances sa set st.owner_type = 'EhServiceAllianceCategories', st.owner_id = sa.`type`
where sa.parent_id = 0 and sa.owner_id > 0 and sa.`type` <> 0 and st.owner_type = 'EhServiceAlliances' and st.owner_id = sa.id;

-- 迁移6.工作流
update eh_flows fl, eh_general_approvals ap 
set fl.owner_id = ap.owner_id, fl.owner_type = 'SERVICE_ALLIANCE'
where fl.owner_type = 'GENERAL_APPROVAL' and fl.module_id = 40500 and fl.owner_id = ap.id and fl.owner_type <> 'SERVICE_ALLIANCE' ;


-- 迁移7.添加基础数据
DELIMITER $$  -- 开始符

CREATE PROCEDURE alliance_transfer_add_base_ca(

) -- 声明存储过程

READS SQL DATA
SQL SECURITY INVOKER

BEGIN

DECLARE  no_more_record INT DEFAULT 0;
DECLARE  pName varchar(64);
DECLARE pNamespaceId INT;
DECLARE pType BIGINT(20);

DECLARE  cur_record CURSOR FOR   SELECT  name,  namespace_id, `type` from eh_service_alliance_categories where parent_id = 0;  -- 首先这里对游标进行定义
 DECLARE  CONTINUE HANDLER FOR NOT FOUND  SET  no_more_record = 1; -- 这个是个条件处理,针对NOT FOUND的条件,当没有记录时赋值为1
 
 OPEN  cur_record; -- 接着使用OPEN打开游标
 FETCH  cur_record INTO pName, pNamespaceId, pType; -- 把第一行数据写入变量中,游标也随之指向了记录的第一行
 
 
 SET @max_id = (select max(id) from eh_service_alliance_categories);
 
 WHILE no_more_record != 1 DO
 INSERT  INTO eh_service_alliance_categories(id, name, namespace_id, parent_id, owner_type, owner_id,creator_uid,`status`, `type`)
 VALUES  (@max_id:=@max_id+1, pName, pNamespaceId, 0, 'organaization', -1, 3, 2, pType );
 FETCH  cur_record INTO pName, pNamespaceId, pType;
 
 END WHILE;
 CLOSE  cur_record;  -- 用完后记得用CLOSE把资源释放掉

END

$$

DELIMITER ; -- 结束符

call alliance_transfer_add_base_ca();

DROP PROCEDURE IF EXISTS alliance_transfer_add_base_ca;


-- 迁移8.添加服务与类型的关联到match表
DELIMITER $$  -- 开始符

CREATE PROCEDURE alliance_transfer_add_match(

) -- 声明存储过程

READS SQL DATA
SQL SECURITY INVOKER

BEGIN

DECLARE  no_more_record INT DEFAULT 0;
DECLARE  pServiceId BIGINT(20);
DECLARE  pCategoryId BIGINT(20);
DECLARE  pNamespaceId BIGINT(20);
DECLARE  pOwnerType VARCHAR(50);
DECLARE  pOwnerId BIGINT(20);
DECLARE  pType BIGINT(20);
DECLARE  pCategoryName VARCHAR(64);

-- 首先这里对游标进行定义
DECLARE  cur_record CURSOR FOR  
SELECT  sa.id, sa.category_id, ca.name, ca.namespace_id,  ca.owner_type, ca.owner_id, ca.`type` 
from eh_service_alliances sa, eh_service_alliance_categories ca 
where sa.category_id = ca.id and sa.category_id is not null and sa.parent_id <> 0; 

-- 这个是个条件处理,针对NOT FOUND的条件,当没有记录时赋值为1
DECLARE  CONTINUE HANDLER FOR NOT FOUND  SET  no_more_record = 1; 
 
 OPEN  cur_record; -- 接着使用OPEN打开游标
 FETCH  cur_record INTO pServiceId, pCategoryId, pCategoryName,  pNamespaceId, pOwnerType, pOwnerId, pType; -- 把第一行数据写入变量中,游标也随之指向了记录的第一行
 
 SET @max_id = (select ifnull(max(id),0) from eh_alliance_service_category_match);
 
 WHILE no_more_record != 1 DO
 
 INSERT  INTO eh_alliance_service_category_match(id, namespace_id, owner_type, owner_id, `type`, service_id, category_id, category_name,create_time, create_uid)
 VALUES  (@max_id:=@max_id+1, pNamespaceId, pOwnerType, pOwnerId, pType, pServiceId, pCategoryId, pCategoryName, now(), 3 );
 FETCH  cur_record INTO pServiceId, pCategoryId, pCategoryName,  pNamespaceId, pOwnerType, pOwnerId, pType;
 
 END WHILE;
 CLOSE  cur_record;  -- 用完后记得用CLOSE把资源释放掉

END

$$

DELIMITER ; -- 结束符

call alliance_transfer_add_match(); -- 执行

DROP PROCEDURE IF EXISTS alliance_transfer_add_match;  -- 删除该存储过程
-- 迁移 end


-- AUTHOR:黄明波
-- REMARK:云打印账号迁移
update eh_siyin_print_business_payee_accounts ac set ac.merchant_id = ac.payee_id ;
update eh_service_modules set client_handler_type = 2 where id = 40500;
update eh_service_modules set client_handler_type = 0 where id = 10800;



-- AUTHOR:杨崇鑫 20181018
-- REMARK:解决缺陷 #39352: 【全量标准版】【物业缴费】新增收费标准前端提示成功，但实际未新增成功，无相关数据，后台提示“应用开小差”
delete from eh_payment_variables;
INSERT INTO `eh_payment_variables`(`id`, `charging_standard_id`, `charging_items_id`, `name`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `identifier`) VALUES (1, NULL, NULL, '单价', 0, '2017-11-02 12:51:43', NULL, '2017-11-02 12:51:43', 'dj');
INSERT INTO `eh_payment_variables`(`id`, `charging_standard_id`, `charging_items_id`, `name`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `identifier`) VALUES (2, NULL, 1, '面积', 0, '2017-11-02 12:51:43', NULL, '2017-11-02 12:51:43', 'mj');
INSERT INTO `eh_payment_variables`(`id`, `charging_standard_id`, `charging_items_id`, `name`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `identifier`) VALUES (3, NULL, 6, '固定金额', 0, '2017-11-02 12:51:43', NULL, '2017-11-02 12:51:43', 'gdje');
INSERT INTO `eh_payment_variables`(`id`, `charging_standard_id`, `charging_items_id`, `name`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `identifier`) VALUES (4, NULL, 5, '用量', 0, '2017-11-02 12:51:43', NULL, '2017-11-02 12:51:43', 'yl');
INSERT INTO `eh_payment_variables`(`id`, `charging_standard_id`, `charging_items_id`, `name`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `identifier`) VALUES (5, NULL, 6, '欠费', 0, '2017-10-16 09:31:00', NULL, '2017-10-16 09:31:00', 'qf');
INSERT INTO `eh_payment_variables`(`id`, `charging_standard_id`, `charging_items_id`, `name`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `identifier`) VALUES (7, NULL, NULL, '比例系数', 0, '2018-05-04 21:34:48', NULL, '2018-05-04 21:34:48', 'blxs');
INSERT INTO `eh_payment_variables`(`id`, `charging_standard_id`, `charging_items_id`, `name`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `identifier`) VALUES (8, NULL, NULL, '折扣', 0, '2018-05-23 02:09:38', NULL, '2018-05-23 02:09:38', 'zk');


-- AUTHOR:杨崇鑫 20181015
-- REMARK:补充缴费模块“应用开小差”的错误码
SET @locale_string_id = (SELECT MAX(id) FROM `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) 
	VALUES ((@locale_string_id := @locale_string_id + 1), 'assetv2', '10012', 'zh_CN', '第三方授权异常');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) 
	VALUES ((@locale_string_id := @locale_string_id + 1), 'assetv2', '10013', 'zh_CN', '收费项标准公式不存在');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) 
	VALUES ((@locale_string_id := @locale_string_id + 1), 'assetv2', '10014', 'zh_CN', '收费项标准类型错误');
	
-- 更新 layout
SET @versionCode = '201810110200';

SET @bizAppId = (SELECT IFNULL(MIN(origin_id),0) from eh_service_module_apps WHERE module_id = 92100 AND `namespace_id` = 2);
SET @activityAppId = (SELECT IFNULL(MIN(origin_id),0) from eh_service_module_apps WHERE module_id = 10600 AND `namespace_id` = 2);
SET @forumAppId = (SELECT IFNULL(MIN(origin_id),0) from eh_service_module_apps WHERE module_id = 10100 AND `namespace_id` = 2);
SET @newsAppId = (SELECT IFNULL(MIN(origin_id),0) from eh_service_module_apps WHERE module_id = 10800 AND `namespace_id` = 2);
SET @communityBulletinsAppId = (SELECT IFNULL(MIN(origin_id),0) from eh_service_module_apps WHERE module_id = 10300 AND `namespace_id` = 2);
SET @enterpriseBulletinsAppId = (SELECT IFNULL(MIN(origin_id),0) from eh_service_module_apps WHERE module_id = 57000 AND `namespace_id` = 2);

UPDATE eh_launch_pad_layouts set version_code = @versionCode, layout_json  = CONCAT('{"versionCode":"',@versionCode,'","layoutName":\"ServiceMarketLayout\",\"displayName\":\"工作台\",\"groups\":[{\"defaultOrder\":3,\"groupName\":\"公告\",\"instanceConfig\":{\"itemGroup\":\"EhPortalItemGroups31058\",\"rowCount\":1.0,\"style\":2.0,\"shadow\":1.0,\"moduleId\":57000.0,\"appId\":', @enterpriseBulletinsAppId ,'},\"separatorFlag\":0,\"separatorHeight\":0,\"widget\":\"Bulletins\"},{\"columnCount\":1,\"defaultOrder\":2,\"groupName\":\"园区运营\","title":"园区运营","titleFlag":1,\"instanceConfig\":{\"itemGroup\":\"EhPortalItemGroups31058\",\"paddingTop\":16.0,\"paddingLeft\":16.0,\"paddingBottom\":16.0,\"paddingRight\":16.0,\"lineSpacing\":0.0,\"columnSpacing\":0.0,\"backgroundColor\":\"#ffffff\",\"appType\":1.0},\"separatorFlag\":0,\"separatorHeight\":0,\"style\":\"Default\",\"widget\":\"Card\"},{\"columnCount\":1,\"defaultOrder\":4,\"groupName\":\"企业办公\","title":"企业办公","titleFlag":1,\"instanceConfig\":{\"itemGroup\":\"EhPortalItemGroups31058\",\"paddingTop\":16.0,\"paddingLeft\":16.0,\"paddingBottom\":16.0,\"paddingRight\":16.0,\"lineSpacing\":0.0,\"columnSpacing\":0.0,\"backgroundColor\":\"#ffffff\",\"appType\":0.0},\"separatorFlag\":0,\"separatorHeight\":0,\"style\":\"Default\",\"widget\":\"Card\"}]}') WHERE type = 4 AND namespace_id = 2;
UPDATE eh_launch_pad_layouts set version_code = @versionCode, layout_json  = CONCAT('{"versionCode":"',@versionCode,'","layoutName":\"ServiceMarketLayout\",\"displayName\":\"服务广场\",\"groups\":[{\"defaultOrder\":1,\"groupName\":\"banner图片1\",\"style\":\"Shape\",\"instanceConfig\":{\"itemGroup\":\"EhPortalItemGroups31056\",\"widthRatio\":16.0,\"heightRatio\":9.0,\"shadowFlag\":1.0,\"paddingFlag\":1.0},\"separatorFlag\":0,\"separatorHeight\":0,\"widget\":\"Banners\"},{\"groupId\":0,\"groupName\":\"容器\","title":"容器","titleFlag":0,"titleStyle":101,"titleSize":2,"titleMoreFlag":0,\"columnCount\":4,\"defaultOrder\":2,\"instanceConfig\":{\"itemGroup\":\"EhPortalItemGroups31058\",\"paddingTop\":0.0,\"paddingLeft\":16.0,\"paddingBottom\":0.0,\"paddingRight\":16.0,\"lineSpacing\":0.0,\"columnSpacing\":0.0,\"cssStyleFlag\":1.0,\"backgroundColor\":\"#ffffff\",\"allAppFlag\":1.0},\"separatorFlag\":0,\"separatorHeight\":0,\"style\":\"Default\",\"widget\":\"Navigator\"},{\"defaultOrder\":3,\"groupName\":\"公告\",\"instanceConfig\":{\"itemGroup\":\"EhPortalItemGroups31057\",\"rowCount\":1.0,\"style\":2.0,\"shadow\":1.0,\"moduleId\":10300.0,\"appId\":', @communityBulletinsAppId ,'},\"separatorFlag\":0,\"separatorHeight\":0,\"widget\":\"Bulletins\"},{\"groupName\":\"电商入口\",\"widget\":\"NavigatorTemp\"},{\"defaultOrder\":4,\"groupName\":\"商品精选\","title":"商品精选","titleFlag":1,"titleStyle":101,"titleSize":2,"titleMoreFlag":1,\"instanceConfig\":{\"itemGroup\":\"OPPushBiz\",\"moduleId\":92100.0,\"appId\":', @bizAppId, ',\"entityCount\":5.0,\"appConfig\":{}},\"separatorFlag\":1,\"separatorHeight\":0,\"style\":\"HorizontalScrollSquareView\",\"widget\":\"OPPush\"},{\"defaultOrder\":5,\"groupName\":\"活动\","title":"活动","titleFlag":1,"titleStyle":101,"titleSize":2,"titleMoreFlag":1,\"instanceConfig\":{\"itemGroup\":\"OPPushActivity\",\"entityCount\":5.0,\"subjectHeight\":0.0,\"descriptionHeight\":0.0,\"newsSize\":5.0,\"moduleId\":10600.0,\"appId\":', @activityAppId, ',\"actionType\":61.0,\"appConfig\":{\"categoryId\":1.0,\"publishPrivilege\":1.0,\"livePrivilege\":0.0,\"listStyle\":2.0,\"scope\":3.0,\"style\":4.0}},\"separatorFlag\":1,\"separatorHeight\":0,\"style\":\"HorizontalScrollWideView\",\"widget\":\"OPPush\"},{\"defaultOrder\":7,\"groupName\":\"论坛\","title":"论坛","titleFlag":1,"titleStyle":101,"titleSize":2,"titleMoreFlag":1,\"instanceConfig\":{\"moduleId\":10100.0,\"appId\":', @forumAppId, ',\"actionType\":62.0,\"newsSize\":5.0,\"appConfig\":{}},\"separatorFlag\":1,\"separatorHeight\":0,\"style\":\"TextImageWithTagListView\",\"widget\":\"OPPush\"},{\"defaultOrder\":8,\"groupName\":\"园区快讯\","title":"园区快讯","titleFlag":1,"titleStyle":101,"titleSize":2,"titleMoreFlag":1,\"instanceConfig\":{\"moduleId\":10800,\"appId\":', @newsAppId, ',\"actionType\":48,\"newsSize\":5.0,\"appConfig\":{}},\"separatorFlag\":1,\"separatorHeight\":0,\"style\":\"NewsListView\",\"widget\":\"OPPush\"}]}') WHERE type = 5 AND namespace_id = 2 ;




-- 企业访客 设置oa模块
UPDATE eh_service_modules set app_type = 0 WHERE id in (52100, 52200);
-- 更新应用信息
UPDATE eh_service_module_apps a set app_type = 0 WHERE module_id in (52100, 52200);

-- 默认的微信消息模板Id
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('wx.default.template.id', 'JnTt-ce69Wlie-o8nv4Jhl3CKA0pXaageIsr4aJiWCk', '默认的微信消息模板Id', '0', NULL, '1');
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('wx.default.template.url', 'http://www.zuolin.com/', '默认的微信消息模板url', '0', NULL, '1');


-- AUTHOR: 荣楠
-- REMARK: 组织架构4.6
SET @locale_id = (SELECT MAX(id) FROM `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@locale_id := @locale_id + 1, 'archives', '100015', 'zh_CN', '账号重复');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@locale_id := @locale_id + 1, 'archives', '100016', 'zh_CN', '账号长度不对或格式错误');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@locale_id := @locale_id + 1, 'archives', '100017', 'zh_CN', '账号一经设定，无法修改');


-- AUTHOR: 严军
-- REMARK: 客户端处理方式
update eh_service_modules set client_handler_type = 2 WHERE id in (41700, 20100,40730,41200);
UPDATE eh_service_modules SET client_handler_type = 1 WHERE id in (90100,  180000);


-- AUTHOR: 严军
-- REMARK: 云打印设置为园区应用
UPDATE eh_service_modules set app_type = 1 WHERE id = 41400;
UPDATE eh_service_module_apps a set app_type = 1 WHERE module_id = 41400;

UPDATE eh_service_modules set instance_config = '{"url":"${home.url}/cloud-print/build/index.html#/home#sign_suffix"}' WHERE id = 41400;
UPDATE eh_service_module_apps set instance_config = '{"url":"${home.url}/cloud-print/build/index.html#/home#sign_suffix"}' WHERE module_id = 41400;

-- AUTHOR: 严军
-- REMARK: 工位预定客户端处理方式设置为内部链接
update eh_service_modules set client_handler_type = 2 WHERE id in (40200);


-- AUTHOR: 严军
-- REMARK: 开放“应用入口”菜单
DELETE FROM eh_web_menus WHERE id = 15010000;
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES ('15010000', '基础数据', '15000000', NULL, NULL, '1', '2', '/15000000/15010000', 'zuolin', '20', NULL, '2', 'system', 'classify', NULL);
DELETE FROM eh_web_menus WHERE id = 15025000;
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES ('15025000', '应用入口', '15010000', NULL, 'servicemodule-entry', '1', '2', '/15000000/15010000/15025000', 'zuolin', '30', NULL, '3', 'system', 'module', NULL);

-- AUTHOR: 严军
-- REMARK: 设置默认的应用分类
UPDATE eh_service_modules set app_type = 1 WHERE app_type is NULL;
UPDATE eh_service_module_apps a set a.app_type = IFNULL((SELECT b.app_type from eh_service_modules b where b.id = a.module_id), 1);

update eh_service_modules set client_handler_type = 2 WHERE id = 43000;

-- AUTHOR: xq.tian
-- REMARK: 用户名或密码错误提示 add by xq.tian  2018/10/11
SET @eh_locale_strings_id = (SELECT MAX(id) from `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
	VALUES (@eh_locale_strings_id:=@eh_locale_strings_id+1, 'user', '100020', 'zh_CN', '用户名或密码错误');

-- AUTHOR: 缪洲 20181008
-- REMARK: issue-38650 增加error消息模板
INSERT INTO `eh_locale_strings`(`scope`, `code`, `locale`, `text`) VALUES ('parking', '10034', 'zh_CN', '接口参数缺失');
INSERT INTO `eh_locale_strings`(`scope`, `code`, `locale`, `text`) VALUES ('parking', '10035', 'zh_CN', '接口参数异常');
INSERT INTO `eh_locale_strings`(`scope`, `code`, `locale`, `text`) VALUES ('parking', '10036', 'zh_CN', '订单状态异常');
INSERT INTO `eh_locale_strings`(`scope`, `code`, `locale`, `text`) VALUES ('parking', '10037', 'zh_CN', '文件导出失败');
INSERT INTO `eh_locale_strings`(`scope`, `code`, `locale`, `text`) VALUES ('parking', '10038', 'zh_CN', '工作流未开启');
INSERT INTO `eh_locale_strings`(`scope`, `code`, `locale`, `text`) VALUES ('parking', '10039', 'zh_CN', '对象不存在');

-- AUTHOR: 马世亨 20181008
-- REMARK: issue-38650 增加error消息模板
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('pmtask','10020','zh_CN','同步搜索引擎失败');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('pmtask','10021','zh_CN','查询失败');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('pmtask','10022','zh_CN','文件导出失败');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('pmtask','10023','zh_CN','对象不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('pmtask','10024','zh_CN','第三方返回失败');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('pmtask','10025','zh_CN','接口参数异常');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('visitorsys','1414','zh_CN','同步搜索引擎失败');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('visitorsys','1415','zh_CN','接口参数异常');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('visitorsys','1416','zh_CN','接口参数缺失');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('visitorsys','1417','zh_CN','二维码下载失败');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('visitorsys','1418','zh_CN','对象不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('visitorsys','1419','zh_CN','文件导出失败');

-- by st.zheng
INSERT INTO `eh_locale_templates` ( `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ( 'relocation', '506', 'zh_CN', '非法参数', '非法参数', '0');
INSERT INTO `eh_locale_templates` ( `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ( 'rental', '506', 'zh_CN', '非法参数', '非法参数', '0');
INSERT INTO `eh_locale_templates` ( `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ( 'rental', '507', 'zh_CN', '参数缺失', '参数缺失', '0');
INSERT INTO `eh_locale_templates` ( `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ( 'rental', '508', 'zh_CN', '资源或资源规则缺失', '资源或资源规则缺失', '0');
INSERT INTO `eh_locale_templates` ( `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ( 'rental', '509', 'zh_CN', '找不到订单或订单状态错误', '找不到订单或订单状态错误', '0');
INSERT INTO `eh_locale_templates` ( `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ( 'rental', '510', 'zh_CN', '下单失败', '下单失败', '0');

update eh_rentalv2_pay_accounts set merchant_id = account_id;


-- AUTHOR: 黄明波 20181008
-- REMARK: issue-38650 增加error消息模板
-- yellowPage
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('yellowPage', '10012', 'zh_CN', '评论不存在或已被删除');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('yellowPage', '10013', 'zh_CN', '文件导出失败');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('yellowPage', '10014', 'zh_CN', '跳转链接格式错误');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('yellowPage', '10015', 'zh_CN', '接口参数异常');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('yellowPage', '10016', 'zh_CN', '获取电商模块失败');


-- express
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('express', '10003', 'zh_CN', 'URL加密失败');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('express', '10004', 'zh_CN', '请求失败');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('express', '10005', 'zh_CN', '接口参数缺失');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('express', '10006', 'zh_CN', '接口参数异常');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('express', '10007', 'zh_CN', '订单不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('express', '10008', 'zh_CN', '获取公司失败');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('express', '10009', 'zh_CN', '用户鉴权失败，请重新登录');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('express', '10010', 'zh_CN', '订单异常');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('express', '10011', 'zh_CN', '第三方返回失败');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('express', '10012', 'zh_CN', '支付鉴权失败');

-- news
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('news', '10017', 'zh_CN', '评论不存在或已被删除');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('news', '10018', 'zh_CN', '接口参数异常');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('news', '10019', 'zh_CN', '无效的快讯类型id');

-- print

INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('print', '10000', 'zh_CN', '订单不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('print', '10001', 'zh_CN', '订单异常');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('print', '10002', 'zh_CN', '邮箱地址格式错误');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('print', '10003', 'zh_CN', '接口参数缺失');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('print', '10004', 'zh_CN', '接口参数异常');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('print', '10005', 'zh_CN', '获取打印任务失败');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('print', '10006', 'zh_CN', '订单不存在或已支付');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('print', '10007', 'zh_CN', '打印机解锁失败');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('print', '10008', 'zh_CN', '第三方返回失败');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('print', '10009', 'zh_CN', '扫码失败，请重试');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('print', '10010', 'zh_CN', '有未支付订单，请支付后重试');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('print', '10011', 'zh_CN', '订单已支付');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('print', '10012', 'zh_CN', '锁定订单失败');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('print', '10013', 'zh_CN', '文件导出失败');


-- 马世亨 2018-10-10
-- 访客管理1.3 合并访客应用
update eh_service_modules set instance_config = '{"url":"${home.url}/visitor-management/build/index.html?ns=%s&appId=%s&ownerType=community#/home#sign_suffix"}' where id = 41800;
update eh_service_modules set instance_config = '{"url":"${home.url}/visitor-appointment/build/index.html?ns=%s&appId=%s&ownerType=enterprise#/home#sign_suffix"}' where id = 52100;
delete from eh_service_modules where id in (42100,52200);
-- end

-- 马世亨 2018-10-10
-- 访客管理1.3 企业访客权限
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category`)
VALUES ('52110', '预约管理', '52100', '/100/50000/52100/52110', '1', '4', '2', '0', now(), NULL, NULL, now(), '0', '1', '1', NULL, '', '1', '1', NULL);

INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category`)
VALUES ('52120', '访客管理', '52100', '/100/50000/52100/52120', '1', '4', '2', '0', now(), NULL, NULL, now(), '0', '1', '1', NULL, '', '1', '1', NULL);

INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category`)
VALUES ('52130', '设备管理', '52100', '/100/50000/52100/52130', '1', '4', '2', '0', now(), NULL, NULL, now(), '0', '1', '1', NULL, '', '1', '1', NULL);

INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category`)
VALUES ('52140', '移动端管理', '52100', '/100/50000/52100/52140', '1', '4', '2', '0', now(), NULL, NULL, now(), '0', '1', '1', NULL, '', '1', '1', NULL);


set @privilege_id = (select max(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES (@privilege_id:=@privilege_id+1, '52100', '0', '5210052100', '全部权限', '0', now());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (5210052110, '0', '企业访客 预约管理权限', '企业访客 预约管理权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES (@privilege_id:=@privilege_id+1, '52110', '0', 5210052110, '预约管理权限', '0', now());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (5210052120, '0', '企业访客 访客管理权限', '企业访客 访客管理权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES (@privilege_id:=@privilege_id+1, '52120', '0', 5210052120, '访客管理权限', '0', now());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (5210052130, '0', '企业访客 设备管理权限', '企业访客 设备管理权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES (@privilege_id:=@privilege_id+1, '52130', '0', 5210052130, '设备管理权限', '0', now());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (5210052140, '0', '企业访客 移动端管理权限', '企业访客 移动端管理权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES (@privilege_id:=@privilege_id+1, '52140', '0', 5210052140, '移动端管理权限', '0', now());


-- AUTHOR: 唐岑2018年10月17日20:32:09
-- REMARK: issue-38650 增加error消息模板
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','101','zh_CN','对象不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','102','zh_CN','接口参数异常');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','103','zh_CN','接口参数缺失');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','104','zh_CN','对象不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','105','zh_CN','对象不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','106','zh_CN','对象不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','107','zh_CN','消息内容为空');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','108','zh_CN','对象不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','109','zh_CN','对象不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','110','zh_CN','上传文件为空');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','111','zh_CN','解析文件失败');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','112','zh_CN','账单数据重复');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','113','zh_CN','服务器内部错误');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','114','zh_CN','对象不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','115','zh_CN','该用户未欠费，不能向其发送催缴短信');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','116','zh_CN','支付方式不支持');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','117','zh_CN','订单不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','118','zh_CN','账单无效');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','119','zh_CN','用户权限不足');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','120','zh_CN','对象不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','121','zh_CN','对象不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','122','zh_CN','excel数据格式不正确');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','123','zh_CN','对象不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','124','zh_CN','创建预约计划失败');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','125','zh_CN','对象不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','126','zh_CN','对象不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','127','zh_CN','对象不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('property','128','zh_CN','导出文件失败');

-- end

-- AUTHOR: xq.tian 2018-10-19
-- REMARK: 驳回按钮的默认跟踪
UPDATE eh_locale_strings SET text='任务已被 ${text_tracker_curr_operator_name} 驳回' WHERE scope='flow' AND code='20005';


-- AUTHOR: 严军 2018-10-21
-- REMARK: issue-38924 修改菜单
-- 一级菜单
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`, `scene_type`) VALUES ('25000000', '资产管理系统', '0', NULL, NULL, '1', '2', '/25000000', 'zuolin', '23', NULL, '1', 'system', 'classify', NULL, '1');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`, `scene_type`) VALUES ('26000000', '物业服务系统', '0', NULL, NULL, '1', '2', '/26000000', 'zuolin', '26', NULL, '1', 'system', 'classify', NULL, '1');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`, `scene_type`) VALUES ('27000000', '统计分析', '0', NULL, NULL, '1', '2', '/27000000', 'zuolin', '60', NULL, '1', 'system', 'classify', NULL, '1');
UPDATE eh_web_menus set `name` = '园区运营系统' WHERE id = 16000000;
UPDATE eh_web_menus set `name` = '企业办公系统' WHERE id = 23000000;
-- 资产管理系统
UPDATE eh_web_menus set parent_id = 25000000, sort_num = 10 WHERE id = 16010000;
UPDATE eh_web_menus set parent_id = 25000000, sort_num = 20 WHERE id = 16210000;
UPDATE eh_web_menus SET path = replace(path, '/16000000/', '/25000000/') WHERE parent_id in (16010000, 16210000) OR id in (16010000, 16210000);
-- 物业服务系统
UPDATE eh_web_menus set parent_id = 26000000, sort_num = 10, `name` = '物业服务' WHERE id = 16050000;
UPDATE eh_web_menus SET path = replace(path, '/16000000/', '/26000000/') WHERE parent_id = 16050000 or id = 16050000;
UPDATE eh_web_menus SET `status` = 0 WHERE id = 16050400;
-- 园区运营系统
UPDATE eh_web_menus SET `status` = 2, parent_id = 16400000, path = '/16000000/16400000/16020500' WHERE id = 16020500;
UPDATE eh_web_menus SET `name` = '收款账户管理' WHERE id = 16070000;
-- 统计分析
UPDATE eh_web_menus set parent_id = 27000000, sort_num = 10, `name` = '统计分析' WHERE id = 17000000;
UPDATE eh_web_menus SET path = replace(path, '/16000000/', '/27000000/') WHERE parent_id = 17000000 or id = 17000000;
-- 企业办公系统
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`, `scene_type`) VALUES ('23020000', '协同办公', '23000000', NULL, NULL, '1', '2', '/23000000/23020000', 'zuolin', '10', NULL, '2', 'system', 'classify', NULL, '1');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`, `scene_type`) VALUES ('23030000', '人力资源', '23000000', NULL, NULL, '1', '0', '/23000000/23030000', 'zuolin', '20', NULL, '2', 'system', 'classify', NULL, '1');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`, `scene_type`) VALUES ('23040000', '支付管理', '23000000', NULL, NULL, '1', '2', '/23000000/23040000', 'zuolin', '50', NULL, '2', 'system', 'classify', NULL, '1');
UPDATE eh_web_menus SET parent_id = 23040000, path = '/23000000/23040000/78000001' WHERE id = 78000001;
UPDATE eh_web_menus SET parent_id = 23040000, path = '/23000000/23040000/79100000' WHERE id = 79100000;

-- 资产管理系统
UPDATE eh_web_menus set parent_id = 15000000, sort_num = 25 WHERE id = 20000000;
UPDATE eh_web_menus set parent_id = 15000000, sort_num = 5 WHERE id = 21000000;
UPDATE eh_web_menus set parent_id = 15000000, sort_num = 90 WHERE id = 22000000;
UPDATE eh_web_menus SET path = replace(path, '/11000000/', '/15000000/') WHERE parent_id in (20000000, 21000000, 22000000) OR id in (20000000, 21000000, 22000000);
UPDATE eh_web_menus SET `status` = 0 WHERE id = 11000000;
UPDATE eh_web_menus SET `status` = 0 WHERE id = 23020000;
UPDATE eh_web_menus set parent_id = 25000000, sort_num = 30 WHERE id = 16050000;
UPDATE eh_web_menus SET path = replace(path, '/26000000/', '/25000000/') WHERE parent_id = 16050000 OR id = 16050000;
UPDATE eh_web_menus SET `status` = 0 WHERE id = 26000000;
UPDATE eh_web_menus SET `name` = '资管物业业务' WHERE id = 25000000;
UPDATE eh_web_menus set `name` = '园区运营业务' WHERE id = 16000000;
UPDATE eh_web_menus set `name` = '企业办公业务' WHERE id = 23000000;
UPDATE eh_web_menus set `name` = '统计分析业务' WHERE id = 27000000;



-- AUTHOR: 严军 2018-10-21
-- REMARK: issue-null 增加模块路由
update eh_service_modules set host = 'bulletin'  where id = 	10300;
update eh_service_modules set host = 'activity'  where id = 	10600;
update eh_service_modules set host = 'post'  where id = 	10100;
update eh_service_modules set host = 'group'  where id = 	10750;
update eh_service_modules set host = 'group'  where id = 	10760;
update eh_service_modules set host = 'approval'  where id = 	52000;
update eh_service_modules set host = 'work-report'  where id = 	54000;
update eh_service_modules set host = 'file-management'  where id = 	55000;
update eh_service_modules set host = 'remind'  where id = 	59100;
update eh_service_modules set host = 'meeting-reservation'  where id = 	53000;
update eh_service_modules set host = 'video-conference'  where id = 	50700;
update eh_service_modules set host = 'enterprise-bulletin'  where id = 	57000;
update eh_service_modules set host = 'enterprise-contact'  where id = 	50100;
update eh_service_modules set host = 'attendance'  where id = 	50600;
update eh_service_modules set host = 'salary'  where id = 	51400;
update eh_service_modules set host = 'station'  where id = 	40200;
update eh_service_modules set host = 'news-feed'  where id = 	10800;
update eh_service_modules set host = 'questionnaire'  where id = 	41700;
update eh_service_modules set host = 'hot-line'  where id = 	40300;
update eh_service_modules set host = 'property-repair'  where id = 	20100;
update eh_service_modules set host = 'resource-reservation'  where id = 	40400;
update eh_service_modules set host = 'visitor'  where id = 	41800;
update eh_service_modules set host = 'parking'  where id = 	40800;
update eh_service_modules set host = 'vehicle-release'  where id = 	20900;
update eh_service_modules set host = 'cloud-print'  where id = 	41400;
update eh_service_modules set host = 'item-release'  where id = 	49200;
update eh_service_modules set host = 'decoration'  where id = 	22000;
update eh_service_modules set host = 'service-alliance'  where id = 	40500;
update eh_service_modules set host = 'wifi'  where id = 	41100;
update eh_service_modules set host = 'park-enterprises'  where id = 	33000;
update eh_service_modules set host = 'park-settle'  where id = 	40100;
update eh_service_modules set host = 'property-payment'  where id = 	20400;
update eh_service_modules set host = 'property-inspection'  where id = 	20800;
update eh_service_modules set host = 'quality'  where id = 	20600;
update eh_service_modules set host = 'energy-management'  where id = 	49100;
update eh_service_modules set host = 'customer-management'  where id = 	21100;

update eh_service_modules set host = 'access-control'  where id = 	41000;
update eh_service_modules set client_handler_type = 2  where id = 	40700;
update eh_service_modules set client_handler_type = 2  where id = 	10800;

-- AUTHOR: st.zheng
-- REMARK: 增加商户管理模块及菜单
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category`) VALUES ('210000', '商户管理', '170000', '/200/170000/210000', '1', '3', '2', '110', '2018-03-19 17:52:57', NULL, NULL, '2018-03-19 17:53:11', '0', '0', '0', '0', 'community_control', '1', '1', 'module');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES ('79820000', '商户管理', '16400000', NULL, 'business-management', '1', '2', '/16000000/16400000/79820000', 'zuolin', '120', '210000', '3', 'system', 'module', NULL);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES ('79830000', '商户管理', '56000000', NULL, 'business-management', '1', '2', '/40000040/56000000/79830000', 'park', '120', '210000', '3', 'system', 'module', '2');

-- AUTHOR: st.zheng
-- REMARK: 资源预订3.7.1
ALTER TABLE `eh_rentalv2_resources`
MODIFY COLUMN `aclink_id`  text  NULL AFTER `default_order`;
ALTER TABLE `eh_rentalv2_orders`
MODIFY COLUMN `door_auth_id`  text  NULL AFTER `auth_end_time`;
update eh_rentalv2_orders set pay_channel = 'normal' where pay_channel is null;


-- AUTHOR: 缪洲 20180930
-- REMARK: issue-34780 增加企业支付授权页面
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`, `scene_type`) VALUES (79800000, '企业支付授权', 16300000, NULL, 'payment-privileges', 1, 2, '/16000000/16300000/79800000', 'zuolin', 8, 200000, 3, 'system', 'module', NULL, 1);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`, `scene_type`) VALUES (79810000, '企业支付授权', 55000000, NULL, 'payment-privileges', 1, 2, '/40000040/55000000/79810000', 'park', 2, 200000, 3, 'system', 'module', NULL, 1);
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category`, `app_type`, `client_handler_type`, `system_app_flag`, `icon_uri`) VALUES (200000, '企业支付授权', 140000, '/200/140000', 1, 3, 2, 10, '2018-09-26 16:51:46', '{}', 13, '2018-09-26 16:51:46', 0, 0, '0', NULL, 'community_control', 1, 1, 'module', NULL, 0, NULL, NULL);

-- AUTHOR: liangqishi
-- REMARK: 把原来统一订单的配置项前缀gorder改为prmt 20181006
UPDATE `eh_configurations` SET `value`=REPLACE(`value`, 'gorder', 'prmt') WHERE `name`='gorder.server.connect_url';
UPDATE `eh_configurations` SET `name`='prmt.server.connect_url' WHERE `name`='gorder.server.connect_url';
UPDATE `eh_configurations` SET `name`='prmt.server.app_key' WHERE `name`='gorder.server.app_key';
UPDATE `eh_configurations` SET `name`='prmt.server.app_secret' WHERE `name`='gorder.server.app_secret';
UPDATE `eh_configurations` SET `name`='prmt.default.personal_bind_phone' WHERE `name`='gorder.default.personal_bind_phone';
UPDATE `eh_configurations` SET `name`='prmt.system_id' WHERE `name`='gorder.system_id';

-- AUTHOR: 缪洲 20180930
-- REMARK: issue-34780 增加未支付推送与短信模板
INSERT INTO `eh_locale_templates`(`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (3421, 'sms.default', 83, 'zh_CN', '未支付短信', '您有一笔云打印的订单未支付，请到云打印-打印记录中进行支付。', 0);

-- AUTHOR: 缪洲 201801011
-- REMARK: issue-34780 删除打印设置规则
DELETE FROM `eh_service_modules` WHERE parent_id = 41400 AND id = 41430;
DELETE FROM `eh_acl_privileges` WHERE id = 4140041430;
DELETE FROM `eh_service_module_privileges` WHERE privilege_id = 4140041430;

-- AUTHOR: 梁燕龙 20181026
-- REMARK: 行业协会路由修改
UPDATE eh_service_modules SET instance_config = '{"isGuild":1}' WHERE id = 10760;
UPDATE eh_service_module_apps SET instance_config = '{"isGuild":1}' WHERE module_id = 10760;
-- --------------------- SECTION END ALL -----------------------------------------------------


-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: zuolin-base
-- DESCRIPTION: 此SECTION只在左邻基线（非独立署部）执行的脚本

-- AUTHOR: xq.tian
-- REMARK: 把基线的 2 域空间删掉，标准版不执行这个 sql
DELETE FROM eh_namespaces WHERE id=2;

-- AUTHOR: 黄明波
-- REMARK: 更新打印机名称
update eh_siyin_print_printers set printer_name = 'FX-ApeosPort-VI C3370' where reader_name = 'TC101154727022';
update eh_siyin_print_printers set printer_name = 'FX-AP-VI C3370-BJ' where reader_name = 'TC101154727294';
update eh_siyin_print_printers set printer_name = 'FX_AP_VIC3370' where reader_name = 'TC101154727497';
update eh_siyin_print_printers set printer_name = 'Zuolin' where reader_name = 'TC101157736913';
update eh_siyin_print_printers set printer_name = 'APV3373' where reader_name = 'TC100887870538';


-- --------------------- SECTION END zuolin-base ---------------------------------------------


-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: dev
-- DESCRIPTION: 此SECTION只在开发库中执行的脚本
-- AUTHOR:
-- REMARK:
-- --------------------- SECTION END dev -----------------------------------------------------


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
-- --------------------- SECTION END guangda -------------------------------------------------


-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: szbay
-- DESCRIPTION: 此SECTION只在深圳湾-999966执行的脚本
-- AUTHOR:
-- REMARK:
-- --------------------- SECTION END szbay ---------------------------------------------------


-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: chuangyechang
-- DESCRIPTION: 此SECTION只在成都创业场-999964执行的脚本
-- AUTHOR:
-- REMARK:
-- --------------------- SECTION END chuangyechang -------------------------------------------


-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: anbang
-- DESCRIPTION: 此SECTION只在安邦物业-999949执行的脚本
-- AUTHOR:
-- REMARK:
-- --------------------- SECTION END anbang---------------------------------------------------


-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: nanshanquzhengfu
-- DESCRIPTION: 此SECTION只在南山区政府-999931执行的脚本
-- --------------------- SECTION END nanshanquzhengfu ----------------------------------------


-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: guanzhouyuekongjian
-- DESCRIPTION: 此SECTION只在广州越空间-999930执行的脚本

-- AUTHOR: xq.tian
-- REMARK: 越空间独立部署的 root 用户的密码修改为: eh#1802
UPDATE eh_users SET password_hash='4eaded9b566765a1e70e2e0dc45204c14c4b9df41507a6b72c7cc7fe91d85341', salt='3023538e14053565b98fdfb2050c7709'
WHERE account_name='root' AND namespace_id=0;

-- --------------------- SECTION END guanzhouyuekongjian -------------------------------------


-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ruianxintiandi
-- DESCRIPTION: 此SECTION只在上海瑞安新天地-999929执行的脚本
-- AUTHOR:梁燕龙  20181022
-- REMARK: 瑞安个人中心跳转URL
INSERT INTO eh_configurations (name, value, description, namespace_id, display_name)
VALUES ('ruian.point.url','https://m.mallcoo.cn/a/user/10764/Point/List','瑞安积分跳转URL',999929, '瑞安积分跳转URL');
INSERT INTO eh_configurations (name, value, description, namespace_id, display_name)
VALUES ('ruian.vip.url','https://m.mallcoo.cn/a/custom/10764/xtd/Rights','瑞安会员跳转URL',999929, '瑞安会员跳转URL');
INSERT INTO eh_configurations (name, value, description, namespace_id, display_name)
VALUES ('ruian.order.url','/zl-ec/rest/service/front/logon?sourceUrl=https%3a%2f%2fbiz.zuolin.com%2fnar%2fbiz%2fweb%2fmall%2findex.html#sign_suffix','瑞安订单跳转URL',999929, '瑞安订单跳转URL');
INSERT INTO eh_configurations (name, value, description)
VALUES ('ruian.coupon.url','https://inno.xintiandi.com/promotion/app-coupon?systemId=16#/','瑞安新天地卡券链接');


-- AUTHOR:黄良铭  20181025
-- REMARK: 瑞安活动对接配置项
-- 默认
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name` )
VALUES ( 'mall.ruian.publickey', 'd2NP2Z','publickey','999929','');
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name`)
VALUES ( 'mall.ruian.privatekey', 'a6cfff2c4aa370f8','privatekey','999929','');
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name` )
VALUES ( 'mall.ruian.appid', '5b5046c988ce7e5ad49c9b10','appid','999929','');

-- 上海新天地
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name` )
VALUES ( 'mall.ruian.publickey.1', 'd2NP2Z','上海新天地publickey','999929','');
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name` )
VALUES ( 'mall.ruian.privatekey.1', 'a6cfff2c4aa370f8','上海新天地privatekey','999929','');
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name` )
VALUES ( 'mall.ruian.appid.1', '5b5046c988ce7e5ad49c9b10','上海新天地appid','999929','');
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name` )
VALUES ( 'ruian.mall.id.1', '10764','上海新天地mallid','999929','');


-- 重庆天地
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name`)
VALUES ( 'mall.ruian.publickey.2', 'o7Oep_','重庆天地publickey','999929','');
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name` )
VALUES ( 'mall.ruian.privatekey.2', '7d57f43738f546e2','重庆天地privatekey','999929','');
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name` )
VALUES ( 'mall.ruian.appid.2', '5b505c8688ce7e238c3c3a2a','重庆天地appid','999929','');
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name` )
VALUES ( 'ruian.mall.id.2', '10782','重庆天地mallid','999929','');

-- 岭南天地
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name` )
VALUES ( 'mall.ruian.publickey.3', 'bOT1fy','岭南天地publickey','999929','');
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name` )
VALUES ( 'mall.ruian.privatekey.3', 'ef94c7e11445aebd','岭南天地privatekey','999929','');
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name` )
VALUES ( 'mall.ruian.appid.3', '5b505b8e3ae74e465c93447b','岭南天地appid','999929','');
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name` )
VALUES ( 'ruian.mall.id.3', '10778','岭南天地mallid','999929','');


-- 虹桥天地
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name`)
VALUES ( 'mall.ruian.publickey.4', '6pqMSA','虹桥天地publickey','999929','');
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name` )
VALUES ( 'mall.ruian.privatekey.4', '761604f49636c418','虹桥天地privatekey','999929','');
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name` )
VALUES ( 'mall.ruian.appid.4', '5b505ac188ce7e238c3c3a28','虹桥天地appid','999929','');
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name` )
VALUES ( 'ruian.mall.id.4', '10743','虹桥天地mallid','999929','');

-- 创智天地
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name` )
VALUES ( 'mall.ruian.publickey.5', 'XWLzKN','创智天地publickey','999929','');
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name` )
VALUES ( 'mall.ruian.privatekey.5', 'cfeb935979f50825','创智天地privatekey','999929','');
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name` )
VALUES ( 'mall.ruian.appid.5', '5b5048333ae74e58743209f7','创智天地appid','999929','');
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name` )
VALUES ( 'ruian.mall.id.5', '10776','创智天地mallid','999929','');

-- 瑞虹天地
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name` )
VALUES ( 'mall.ruian.publickey.6', 'ydVQ7f','瑞虹天地publickey','999929','');
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name` )
VALUES ( 'mall.ruian.privatekey.6', '24f36ef07865a906','瑞虹天地privatekey','999929','');
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name` )
VALUES ( 'mall.ruian.appid.6', '5b5047103ae74e58743209f3','瑞虹天地appid','999929','');
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name` )
VALUES ( 'ruian.mall.id.6', '10775','瑞虹天地mallid','999929','');


INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name` )
VALUES ( 'activity.butt.url.getcategorylist', 'https://openapi10.mallcoo.cn/Event/Activity/V1/GetCategoryList/','获取活动分类','999929','');
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name` )
VALUES ( 'activity.butt.url.getactivitylist', 'https://openapi10.mallcoo.cn/Event/Activity/V1/GetList/','获取活动列表','999929','');
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name`)
VALUES ( 'activity.butt.url.getactivity', 'https://openapi10.mallcoo.cn/Event/Activity/V1/GetDetail/','获取活动详情','999929','');
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name` )
VALUES ( 'mall.ruian.url.activity', 'https://m.mallcoo.cn/a/custom/10764/xtd/activitylist','瑞安活动列表面URL','999929','');

-- AUTHOR: 唐岑
-- REMARK: 修改楼宇资产管理web menu的module id
UPDATE eh_web_menus SET module_id=38000 WHERE id=16010100;

-- AUTHOR: 唐岑
-- REMARK: 创建新的园区
-- 添加园区
INSERT INTO `eh_communities` (`id`, `uuid`, `city_id`, `city_name`, `area_id`, `area_name`, `name`, `alias_name`, `address`, `zipcode`, `description`, `detail_description`, `apt_segment1`, `apt_segment2`, `apt_segment3`, `apt_seg1_sample`, `apt_seg2_sample`, `apt_seg3_sample`, `apt_count`, `creator_uid`, `operator_uid`, `status`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `community_type`, `default_forum_id`, `feedback_forum_id`, `update_time`, `namespace_id`, `area_size`, `shared_area`, `charge_area`, `build_area`, `rent_area`, `namespace_community_type`, `namespace_community_token`, `community_number`, `free_area`) VALUES ('240111044332063579', 'ca94e3a2-f36e-4033-9cbc-869881b643a4', '21977', '南京市', '21978', '玄武', 'SOP Office', NULL, '南京市玄武区珠江路未来城', NULL, 'TPQ项目', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '1', NULL, '2', '2018-08-27 14:20:21', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1', '196430', '196431', NULL, '999929', NULL, NULL, NULL, NULL, NULL, 'ruian_cm', '299', NULL, NULL);
INSERT INTO `eh_communities` (`id`, `uuid`, `city_id`, `city_name`, `area_id`, `area_name`, `name`, `alias_name`, `address`, `zipcode`, `description`, `detail_description`, `apt_segment1`, `apt_segment2`, `apt_segment3`, `apt_seg1_sample`, `apt_seg2_sample`, `apt_seg3_sample`, `apt_count`, `creator_uid`, `operator_uid`, `status`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `community_type`, `default_forum_id`, `feedback_forum_id`, `update_time`, `namespace_id`, `area_size`, `shared_area`, `charge_area`, `build_area`, `rent_area`, `namespace_community_type`, `namespace_community_token`, `community_number`, `free_area`) VALUES ('240111044332063580', '8c5043ee-846f-4927-bb17-3ef65b999f0d', '21977', '南京市', '21978', '玄武', 'Inno Office', NULL, '南京市玄武区珠江路未来城', NULL, 'INNO创智', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '1', NULL, '2', '2018-08-27 16:03:02', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1', '196430', '196431', NULL, '999929', NULL, NULL, NULL, NULL, NULL, 'ruian_cm', '441', NULL, NULL);
INSERT INTO `eh_communities` (`id`, `uuid`, `city_id`, `city_name`, `area_id`, `area_name`, `name`, `alias_name`, `address`, `zipcode`, `description`, `detail_description`, `apt_segment1`, `apt_segment2`, `apt_segment3`, `apt_seg1_sample`, `apt_seg2_sample`, `apt_seg3_sample`, `apt_count`, `creator_uid`, `operator_uid`, `status`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `community_type`, `default_forum_id`, `feedback_forum_id`, `update_time`, `namespace_id`, `area_size`, `shared_area`, `charge_area`, `build_area`, `rent_area`, `namespace_community_type`, `namespace_community_token`, `community_number`, `free_area`) VALUES ('240111044332063581', '43877d13-8995-46ed-9bcb-4f8a307c41f2', '21977', '南京市', '21978', '玄武', 'Inno Work', NULL, '南京市玄武区珠江路未来城', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '1', NULL, '2', '2018-08-27 16:18:54', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1', '196430', '196431', NULL, '999929', NULL, NULL, NULL, NULL, NULL, 'ruian_cm', '442', NULL, NULL);
INSERT INTO `eh_communities` (`id`, `uuid`, `city_id`, `city_name`, `area_id`, `area_name`, `name`, `alias_name`, `address`, `zipcode`, `description`, `detail_description`, `apt_segment1`, `apt_segment2`, `apt_segment3`, `apt_seg1_sample`, `apt_seg2_sample`, `apt_seg3_sample`, `apt_count`, `creator_uid`, `operator_uid`, `status`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `community_type`, `default_forum_id`, `feedback_forum_id`, `update_time`, `namespace_id`, `area_size`, `shared_area`, `charge_area`, `build_area`, `rent_area`, `namespace_community_type`, `namespace_community_token`, `community_number`, `free_area`) VALUES ('240111044332063586', '6b61ab1c-efeb-4624-b160-543f2bb6363f', '21977', '南京市', '21978', '玄武', 'INNO创智A栋 Office', NULL, '南京市玄武区珠江路未来城', NULL, 'INNO创智', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '1', NULL, '2', '2018-08-27 16:29:51', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1', '196430', '196431', NULL, '999929', NULL, NULL, NULL, NULL, NULL, 'ruian_cm', '443', NULL, NULL);
INSERT INTO `eh_communities` (`id`, `uuid`, `city_id`, `city_name`, `area_id`, `area_name`, `name`, `alias_name`, `address`, `zipcode`, `description`, `detail_description`, `apt_segment1`, `apt_segment2`, `apt_segment3`, `apt_seg1_sample`, `apt_seg2_sample`, `apt_seg3_sample`, `apt_count`, `creator_uid`, `operator_uid`, `status`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `community_type`, `default_forum_id`, `feedback_forum_id`, `update_time`, `namespace_id`, `area_size`, `shared_area`, `charge_area`, `build_area`, `rent_area`, `namespace_community_type`, `namespace_community_token`, `community_number`, `free_area`) VALUES ('240111044332063583', '239e0eac-c818-41d7-887d-651a037acc09', '21977', '南京市', '21978', '玄武', 'INNO创智B栋 Office', NULL, '南京市玄武区珠江路未来城', NULL, 'INNO创智', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '1', NULL, '2', '2018-08-27 16:36:00', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1', '196430', '196431', NULL, '999929', NULL, NULL, NULL, NULL, NULL, 'ruian_cm', '444', NULL, NULL);
INSERT INTO `eh_communities` (`id`, `uuid`, `city_id`, `city_name`, `area_id`, `area_name`, `name`, `alias_name`, `address`, `zipcode`, `description`, `detail_description`, `apt_segment1`, `apt_segment2`, `apt_segment3`, `apt_seg1_sample`, `apt_seg2_sample`, `apt_seg3_sample`, `apt_count`, `creator_uid`, `operator_uid`, `status`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `community_type`, `default_forum_id`, `feedback_forum_id`, `update_time`, `namespace_id`, `area_size`, `shared_area`, `charge_area`, `build_area`, `rent_area`, `namespace_community_type`, `namespace_community_token`, `community_number`, `free_area`) VALUES ('240111044332063584', '88d29ccf-8cf4-4ff9-8dfc-896e8159af99', '21977', '南京市', '21978', '玄武', 'INNO创智A栋 Retail', NULL, '南京市玄武区珠江路未来城', NULL, 'INNO创智', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '1', NULL, '2', '2018-08-27 16:38:27', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1', '196430', '196431', NULL, '999929', NULL, NULL, NULL, NULL, NULL, 'ruian_cm', '445', NULL, NULL);
INSERT INTO `eh_communities` (`id`, `uuid`, `city_id`, `city_name`, `area_id`, `area_name`, `name`, `alias_name`, `address`, `zipcode`, `description`, `detail_description`, `apt_segment1`, `apt_segment2`, `apt_segment3`, `apt_seg1_sample`, `apt_seg2_sample`, `apt_seg3_sample`, `apt_count`, `creator_uid`, `operator_uid`, `status`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `community_type`, `default_forum_id`, `feedback_forum_id`, `update_time`, `namespace_id`, `area_size`, `shared_area`, `charge_area`, `build_area`, `rent_area`, `namespace_community_type`, `namespace_community_token`, `community_number`, `free_area`) VALUES ('240111044332063585', '59e51ce5-5f1d-4150-9bd6-78741c6afd83', '21977', '南京市', '21978', '玄武', 'INNO创智B栋 Retail', NULL, '南京市玄武区珠江路未来城', NULL, 'INNO创智', NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '1', NULL, '2', '2018-08-27 16:43:52', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '1', '196430', '196431', NULL, '999929', NULL, NULL, NULL, NULL, NULL, 'ruian_cm', '446', NULL, NULL);

-- 园区和域空间关联
set @eh_namespace_resources_id = IFNULL((select MAX(id) from eh_namespace_resources),1);
INSERT INTO `eh_namespace_resources` (`id`, `namespace_id`, `resource_type`, `resource_id`, `create_time`, `default_order`) VALUES (@eh_namespace_resources_id := @eh_namespace_resources_id + 1, '999929', 'COMMUNITY', '240111044332063579', '2018-08-27 14:20:21', NULL);
INSERT INTO `eh_namespace_resources` (`id`, `namespace_id`, `resource_type`, `resource_id`, `create_time`, `default_order`) VALUES (@eh_namespace_resources_id := @eh_namespace_resources_id + 1, '999929', 'COMMUNITY', '240111044332063580', '2018-08-27 16:03:02', NULL);
INSERT INTO `eh_namespace_resources` (`id`, `namespace_id`, `resource_type`, `resource_id`, `create_time`, `default_order`) VALUES (@eh_namespace_resources_id := @eh_namespace_resources_id + 1, '999929', 'COMMUNITY', '240111044332063581', '2018-08-27 16:18:54', NULL);
INSERT INTO `eh_namespace_resources` (`id`, `namespace_id`, `resource_type`, `resource_id`, `create_time`, `default_order`) VALUES (@eh_namespace_resources_id := @eh_namespace_resources_id + 1, '999929', 'COMMUNITY', '240111044332063586', '2018-08-27 16:29:52', NULL);
INSERT INTO `eh_namespace_resources` (`id`, `namespace_id`, `resource_type`, `resource_id`, `create_time`, `default_order`) VALUES (@eh_namespace_resources_id := @eh_namespace_resources_id + 1, '999929', 'COMMUNITY', '240111044332063583', '2018-08-27 16:36:00', NULL);
INSERT INTO `eh_namespace_resources` (`id`, `namespace_id`, `resource_type`, `resource_id`, `create_time`, `default_order`) VALUES (@eh_namespace_resources_id := @eh_namespace_resources_id + 1, '999929', 'COMMUNITY', '240111044332063584', '2018-08-27 16:38:27', NULL);
INSERT INTO `eh_namespace_resources` (`id`, `namespace_id`, `resource_type`, `resource_id`, `create_time`, `default_order`) VALUES (@eh_namespace_resources_id := @eh_namespace_resources_id + 1, '999929', 'COMMUNITY', '240111044332063585', '2018-08-27 16:43:52', NULL);

-- 创建经纬度数据
set @eh_community_geopoints_id = IFNULL((select MAX(id) from eh_community_geopoints),1);
INSERT INTO `eh_community_geopoints` (`id`, `community_id`, `description`, `longitude`, `latitude`, `geohash`) VALUES (@eh_community_geopoints_id := @eh_community_geopoints_id + 1, '240111044332063579', NULL, '118.804035', '32.054084', 'wtsqr7q2wdm5');
INSERT INTO `eh_community_geopoints` (`id`, `community_id`, `description`, `longitude`, `latitude`, `geohash`) VALUES (@eh_community_geopoints_id := @eh_community_geopoints_id + 1, '240111044332063580', NULL, '118.804035', '32.054084', 'wtsqr7q2wdm5');
INSERT INTO `eh_community_geopoints` (`id`, `community_id`, `description`, `longitude`, `latitude`, `geohash`) VALUES (@eh_community_geopoints_id := @eh_community_geopoints_id + 1, '240111044332063581', NULL, '118.804035', '32.054084', 'wtsqr7q2wdm5');
INSERT INTO `eh_community_geopoints` (`id`, `community_id`, `description`, `longitude`, `latitude`, `geohash`) VALUES (@eh_community_geopoints_id := @eh_community_geopoints_id + 1, '240111044332063586', NULL, '118.804035', '32.054084', 'wtsqr7q2wdm5');
INSERT INTO `eh_community_geopoints` (`id`, `community_id`, `description`, `longitude`, `latitude`, `geohash`) VALUES (@eh_community_geopoints_id := @eh_community_geopoints_id + 1, '240111044332063583', NULL, '118.804035', '32.054084', 'wtsqr7q2wdm5');
INSERT INTO `eh_community_geopoints` (`id`, `community_id`, `description`, `longitude`, `latitude`, `geohash`) VALUES (@eh_community_geopoints_id := @eh_community_geopoints_id + 1, '240111044332063584', NULL, '118.804035', '32.054084', 'wtsqr7q2wdm5');
INSERT INTO `eh_community_geopoints` (`id`, `community_id`, `description`, `longitude`, `latitude`, `geohash`) VALUES (@eh_community_geopoints_id := @eh_community_geopoints_id + 1, '240111044332063585', NULL, '118.804035', '32.054084', 'wtsqr7q2wdm5');

-- 添加企业可见园区,管理公司可以看到添加的园区
set @eh_organization_communities_id = IFNULL((select MAX(id) from eh_organization_communities),1);
INSERT INTO `eh_organization_communities` (`id`, `organization_id`, `community_id`) VALUES (@eh_organization_communities_id := @eh_organization_communities_id + 1, '1051547', '240111044332063579');
INSERT INTO `eh_organization_communities` (`id`, `organization_id`, `community_id`) VALUES (@eh_organization_communities_id := @eh_organization_communities_id + 1, '1051547', '240111044332063580');
INSERT INTO `eh_organization_communities` (`id`, `organization_id`, `community_id`) VALUES (@eh_organization_communities_id := @eh_organization_communities_id + 1, '1051547', '240111044332063581');
INSERT INTO `eh_organization_communities` (`id`, `organization_id`, `community_id`) VALUES (@eh_organization_communities_id := @eh_organization_communities_id + 1, '1051547', '240111044332063586');
INSERT INTO `eh_organization_communities` (`id`, `organization_id`, `community_id`) VALUES (@eh_organization_communities_id := @eh_organization_communities_id + 1, '1051547', '240111044332063583');
INSERT INTO `eh_organization_communities` (`id`, `organization_id`, `community_id`) VALUES (@eh_organization_communities_id := @eh_organization_communities_id + 1, '1051547', '240111044332063584');
INSERT INTO `eh_organization_communities` (`id`, `organization_id`, `community_id`) VALUES (@eh_organization_communities_id := @eh_organization_communities_id + 1, '1051547', '240111044332063585');		

-- AUTHOR: 杨崇鑫
-- REMARK: 配置客户V4.1瑞安CM对接的访问地址
SET @id = ifnull((SELECT MAX(id) FROM `eh_configurations`),0);
INSERT INTO `eh_configurations`(`id`, `name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) 
	VALUES (@id := @id + 1, 'RuiAnCM.sync.url', 'http://10.50.12.39/cm/WebService/OfficeApp-CM/OfficeApp_CMService.asmx', '瑞安新天地对接的第三方地址', 0, NULL, 1);
INSERT INTO `eh_configurations`(`id`, `name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) 
	VALUES (@id:=@id+1, 'contractService', '999929', NULL, 999929, NULL, 1);
	
-- AUTHOR: 杨崇鑫
-- REMARK: 初始化瑞安CM对接的默认账单组，由于该账单组是默认账单组，所以不允许删除
set @id = 1000000;
INSERT INTO `eh_payment_bill_groups`(`id`, `namespace_id`, `owner_id`, `owner_type`, `name`, `balance_date_type`, `bills_day`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `default_order`, `due_day`, `due_day_type`, `brother_group_id`, `bills_day_type`, `category_id`, `biz_payee_type`, `biz_payee_id`, `is_default`) 
	select @id:=@id+1, 999929, id, 'community', '缴费', 2, 5, 67663, UTC_TIMESTAMP(), NULL, UTC_TIMESTAMP(), 1, 5, 1, NULL, 4, 3, NULL, NULL, 1
		from eh_communities;
-- REMARK:瑞安CM对接 账单区分数据来源
SET @eh_locale_strings_id = (SELECT MAX(id) from `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) 
	VALUES (@eh_locale_strings_id:=@eh_locale_strings_id+1, 'asset.source', '10005', 'zh_CN', '瑞安CM产生');
-- REMARK: 瑞安CM对接 APP只支持查费，隐藏掉缴费
SET @id = ifnull((SELECT MAX(id) FROM `eh_payment_app_views`),0); 
INSERT INTO `eh_payment_app_views`(`id`, `namespace_id`, `community_id`, `has_view`, `view_item`, `remark1_type`, `remark1_identifier`, `remark2_type`, `remark2_identifier`, `remark3_type`, `remark3_identifier`) 
VALUES (@id := @id + 1, 999929, NULL, 0, 'PAY', NULL, NULL, NULL, NULL, NULL, NULL);	

-- AUTHOR: 黄明波
-- REMARK: 填写瑞安打印机名称
update eh_siyin_print_printers set printer_name = 'Sys_NJ_INNO_2F02' where reader_name = 'TC101152723470';
update eh_siyin_print_printers set printer_name = 'Sys_NJ_INNO_3F01' where reader_name = 'TC101152723540';
update eh_siyin_print_printers set printer_name = 'Sys_NJ_INNO_2F01' where reader_name = 'TC101152723478';

-- AUTHOR: 刘一麟
-- REMARK: 访客二维码短信模板
UPDATE eh_locale_templates set `text` = '${username}已授权给你${doorname}门禁二维码，请点击以下链接使用：${link}/aclink/v?id=${id}（24小时有效）' where `code` = 8 and `scope` in ('sms.default','sms.default.yzx') and `description` like '%门禁%';

-- AUTHOR: 缪洲
-- REMARK: 停车缴费收款账号迁移
update eh_parking_business_payee_accounts ac set ac.merchant_id = ac.payee_id ;

-- --------------------- SECTION END ruianxintiandi ------------------------------------------


-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: wanzhihui
-- DESCRIPTION: 此SECTION只在万智汇-999953执行的脚本
-- --------------------- SECTION END wanzhihui ------------------------------------------

-- 标注版zuolin-base-2.1之前的脚本


-- -- 广告管理 v1.4 加字段    add by xq.tian  2018/03/07
-- ALTER TABLE eh_banners ADD COLUMN target_type VARCHAR(32) NOT NULL COMMENT 'e.g: NONE, POST_DETAIL, ACTIVITY_DETAIL, APP, URL, ROUTE';
-- ALTER TABLE eh_banners ADD COLUMN target_data VARCHAR(1024) DEFAULT NULL COMMENT 'It is different by different target_type';
--
-- ALTER TABLE eh_banners MODIFY COLUMN scene_type VARCHAR(32) DEFAULT NULL;
-- ALTER TABLE eh_banners MODIFY COLUMN apply_policy TINYINT DEFAULT NULL;
--
-- -- 启动广告 v1.1          add by xq.tian  2018/03/07
-- ALTER TABLE eh_launch_advertisements ADD COLUMN target_type VARCHAR(32) NOT NULL COMMENT 'e.g: NONE, POST_DETAIL, ACTIVITY_DETAIL, APP, URL, ROUTE';
-- ALTER TABLE eh_launch_advertisements ADD COLUMN target_data VARCHAR(1024) DEFAULT NULL COMMENT 'It is different by different target_type';
-- ALTER TABLE eh_launch_advertisements ADD COLUMN content_uri_origin VARCHAR(1024) DEFAULT NULL COMMENT 'Content uri for origin file.';
--
-- -- 用户认证 V2.3 #13692
-- ALTER TABLE `eh_users` ADD COLUMN `third_data` varchar(2048) DEFAULT NULL COMMENT 'third_data for AnBang';
--
--
--
-- -- 标准item 顺序 by jiarui
-- ALTER TABLE `eh_equipment_inspection_items` ADD COLUMN `default_order`  int(11) NOT NULL DEFAULT 0 AFTER `value_jason`;
--
-- /**
--  * Designer:yilin Liu
--  * Description:ISSUE#26184 门禁人脸识别
--  * Created：2018-4-9
--  */
--
-- -- 门禁多公司管理
-- ALTER TABLE `eh_door_access`
-- ADD `mac_copy` VARCHAR(128) COMMENT '原mac地址';
--
-- /**
-- * End by: yilin Liu
-- */
--
-- -- Already delete in 5.5.1
--
-- -- TODO 这里本来是注释的，因为后面报错了，现在先放开  201807131646
-- -- ALTER TABLE `eh_organization_member_details` ADD COLUMN `profile_integrity` INTEGER NOT NULL DEFAULT '0';
-- -- ALTER TABLE eh_organization_member_details ADD COLUMN department VARCHAR(256) COMMENT '部门';
-- -- ALTER TABLE eh_organization_member_details ADD COLUMN department_ids VARCHAR(256) COMMENT '部门Id';
-- -- ALTER TABLE eh_organization_member_details ADD COLUMN job_position VARCHAR(256) COMMENT '岗位';
-- -- ALTER TABLE eh_organization_member_details ADD COLUMN job_position_ids VARCHAR(256) COMMENT '岗位Id';
-- -- ALTER TABLE eh_organization_member_details ADD COLUMN job_level VARCHAR(256) COMMENT '职级';
-- -- ALTER TABLE eh_organization_member_details ADD COLUMN job_level_ids VARCHAR(256) COMMENT '职级Id';
-- -- end Janson
--
-- -- 园区表增加namespace_id索引 add by yanjun 20180615
-- alter table eh_communities add index namespace_id_index(`namespace_id`);
--
-- -- fix for zuolinbase only, remove this after 5.5.2
-- -- ALTER TABLE `eh_organization_member_details` CHANGE COLUMN `profile_integrity` `profile_integrity` INT(11) NULL DEFAULT '0' ;
-- -- end Janson
--
-- -- 通用脚本
-- -- ADD BY 梁燕龙
-- -- issue-30013 初始化短信白名单配置项
-- -- 短信白名单 #30013
-- CREATE TABLE `eh_phone_white_list` (
-- 	`id` BIGINT NOT NULL COMMENT '主键',
-- 	`namespace_id` INT NOT NULL DEFAULT 0 COMMENT '域空间',
-- 	`phone_number` VARCHAR(128) NOT NULL COMMENT '白名单手机号码',
-- 	`creator_uid` BIGINT COMMENT '记录创建人userID',
-- 	`create_time` DATETIME COMMENT '记录创建时间',
-- 	PRIMARY KEY(`id`)
-- ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '短信白名单';
-- -- END BY 梁燕龙
--
--


-- -----------------------------------------------------  以上为 5.6.1-02 的脚本 ----------------------------------------

-- -----------------------------------------------------  以下为 5.6.1 新增的脚本 ----------------------------------------

 -- 应用公司项目授权表 by lei.lv
 CREATE TABLE `eh_service_module_app_authorizations` (
  `id` BIGINT(20) NOT NULL,
  `namespace_id` INT(11) DEFAULT NULL,
  `owner_id` BIGINT(20) NOT NULL DEFAULT '0' COMMENT 'owner_id',
  `organization_id` BIGINT(20) NOT NULL DEFAULT '0' COMMENT 'organization_id',
  `project_id` BIGINT(20) NOT NULL DEFAULT '0' COMMENT 'community_id',
  `app_id` BIGINT(20)  NOT NULL DEFAULT '0' COMMENT 'app_id',
  `control_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'control type',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 公司安装应用表
CREATE TABLE `eh_organization_apps` (
  `id` bigint(20) NOT NULL,
  `app_origin_id` bigint(20) DEFAULT NULL,
  `org_id` bigint(20) DEFAULT NULL,
  `visibility_flag` tinyint(4) DEFAULT NULL,
  `display_name` varchar(255) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL,
  `creator_uid` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `operator_uid` bigint(20) DEFAULT NULL,
  `operator_time` datetime DEFAULT NULL,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

ALTER TABLE `eh_organization_apps` ADD INDEX `org_app_orgid` (`org_id`) ;
ALTER TABLE `eh_organization_apps` ADD INDEX `org_app_appid` (`app_origin_id`) ;

-- 园区应用配置表（不跟随管理公司时的自定义配置）
CREATE TABLE `eh_app_community_configs` (
  `id` bigint(20) NOT NULL,
  `app_origin_id` bigint(20) DEFAULT NULL COMMENT 'app_origin_id',
  `community_id` bigint(20) DEFAULT NULL,
  `visibility_flag` tinyint(4) DEFAULT NULL,
  `display_name` varchar(255) DEFAULT NULL,
  `creator_uid` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `operator_uid` bigint(20) DEFAULT NULL,
  `operator_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `community_id` (`community_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 应用档案表
CREATE TABLE `eh_service_module_app_profile` (
  `id` bigint(20) NOT NULL,
  `origin_id` bigint(20) NOT NULL,
  `app_no` varchar(255) DEFAULT NULL,
  `display_version` varchar(128) DEFAULT NULL,
  `description` varchar(1024) DEFAULT NULL,
  `mobile_flag` tinyint(4) DEFAULT 0,
  `mobile_uris` varchar(1024) DEFAULT NULL,
  `pc_flag` tinyint(4) DEFAULT 0,
  `pc_uris` varchar(1024) DEFAULT NULL,
  `app_entry_infos` varchar(2048) DEFAULT NULL,
  `independent_config_flag` tinyint(4) DEFAULT 0,
  `dependent_app_ids` varchar(128) DEFAULT NULL,
  `support_third_flag` tinyint(4) DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 增加应用类型字段 0-oa应用、1-园区应用、2-服务应用   add by yanjun 201804081501
ALTER TABLE `eh_service_modules` ADD COLUMN `app_type`  tinyint(4) NULL COMMENT 'app type, 0-oaapp,1-communityapp,2-serviceapp';
ALTER TABLE `eh_service_module_apps` ADD COLUMN `app_type`  tinyint(4) NULL COMMENT 'app type, 0-oaapp,1-communityapp,2-serviceapp';

-- 新增修改时间的字段 add by lei.lv 201804091401
ALTER TABLE `eh_service_module_app_authorizations` ADD COLUMN `create_time`  datetime NULL COMMENT 'create_time';
ALTER TABLE `eh_service_module_app_authorizations` ADD COLUMN `update_time`  datetime NULL COMMENT 'update_time';

-- 新增开发者字段
ALTER TABLE `eh_service_module_app_profile` ADD COLUMN `develop_id`  bigint(20) NULL COMMENT 'developer owner id';

-- 增加 企业超级管理员id、是否开启工作台标志  add by yanjun 20180412

-- 定制版已经执行过，在ehcore-server-schema.sql中有了。 edit by jun.yan
-- ALTER TABLE `eh_organizations` ADD COLUMN `admin_target_id`  bigint(20) NULL ;
ALTER TABLE `eh_organizations` ADD COLUMN `work_platform_flag`  tinyint(4) NULL COMMENT 'open work platform flag, 0-no, 1-yes' ;

-- 默认园区标志
ALTER TABLE `eh_communities` ADD COLUMN `default_community_flag`  tinyint(4) NULL COMMENT 'is the default community in his namespace, 0-no, 1-yes';

-- 主页签信息
CREATE TABLE `eh_launch_pad_indexs` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `type` tinyint(4) NOT NULL,
  `name` varchar(64) DEFAULT NULL,
  `config_json` text,
  `default_order` int(11) NOT NULL DEFAULT '0',
  `icon_uri` varchar(1024) DEFAULT NULL,
  `selected_icon_uri` varchar(1024) DEFAULT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `operator_uid` bigint(20) NOT NULL,
  `creator_uid` bigint(20) NOT NULL,
  `description` varchar(1024) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

alter table eh_launch_pad_indexs add index namespace_id_index(`namespace_id`);

-- 一卡通实现
CREATE TABLE `eh_smart_card_keys` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `name` varchar(256) DEFAULT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0 无效, 1 有效',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


DELETE FROM eh_portal_navigation_bars;
ALTER TABLE `eh_portal_navigation_bars` CHANGE COLUMN `target_type` `type`  tinyint(4) NOT NULL;
ALTER TABLE `eh_portal_navigation_bars` CHANGE COLUMN `target_id` `config_json`  varchar(1024) NOT NULL;
ALTER TABLE `eh_portal_navigation_bars` ADD COLUMN `version_id`  bigint(20) NULL ;
ALTER TABLE `eh_portal_navigation_bars` ADD COLUMN `default_order`  int(11) NOT NULL DEFAULT '0' ;

-- layout 类型、背景颜色
ALTER TABLE `eh_launch_pad_layouts` ADD COLUMN `type`  tinyint(4) NULL;
ALTER TABLE `eh_launch_pad_layouts` ADD COLUMN `bg_color`  varchar(255) NULL ;
-- layout 类型
ALTER TABLE `eh_portal_layouts` ADD COLUMN `bg_color`  varchar(255) NULL;

-- 功能模块入口列表
CREATE TABLE `eh_service_module_entries` (
  `id` bigint(20) NOT NULL,
  `module_id` bigint(20) NOT NULL,
  `module_name` varchar(256) DEFAULT NULL,
  `entry_name` varchar(256) DEFAULT NULL,
  `terminal_type` tinyint(4) NOT NULL COMMENT '终端列表，1-mobile,2-pc',
  `location_type` tinyint(4) NOT NULL COMMENT '位置，参考枚举ServiceModuleLocationType',
  `scene_type` tinyint(4) NOT NULL COMMENT '形态，1-管理端，2-客户端，参考枚举ServiceModuleSceneType',
  `second_app_type` int(11) NOT NULL DEFAULT '0',
  `default_order` int(11) NOT NULL DEFAULT '0',
  `icon_uri` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 应用二级分类
CREATE TABLE `eh_second_app_types` (
  `id` bigint(22) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `app_type` tinyint(4) DEFAULT NULL COMMENT '一级分类，0-oa，1-community，2-service。参考ServiceModuleAppType',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

ALTER TABLE `eh_service_module_entries` ADD INDEX `module_entry_module_id` (`module_id`);


-- 增加字段member_range人员规模
-- add by lei yuan
alter table eh_organization_details add member_range varchar(25) default null comment '人员规模';
-- 增加字段 pm_flag 是否是管理公司 1-是，0-否
ALTER TABLE eh_organization_details ADD COLUMN `pm_flag` tinyint(4) DEFAULT NULL COMMENT '是否是管理公司 1-是，0-否';
-- 增加字段 service_support_flag 是否是服务商 1-是，0-否
ALTER TABLE eh_organization_details ADD COLUMN `service_support_flag` tinyint(4) DEFAULT NULL COMMENT '是否是服务商 1-是，0-否';
-- 增加字段 pm_flag 是否是管理公司 1-是，0-否
ALTER TABLE eh_organizations ADD COLUMN `pm_flag` tinyint(4) DEFAULT NULL COMMENT '是否是管理公司 1-是，0-否';
-- 增加字段 service_support_flag 是否是服务商 1-是，0-否
ALTER TABLE eh_organizations ADD COLUMN `service_support_flag` tinyint(4) DEFAULT NULL COMMENT '是否是服务商 1-是，0-否';


-- 增加办公地点表
-- add by leiyuan
CREATE TABLE `eh_organization_workplaces` (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `organization_id` bigint(20) DEFAULT NULL COMMENT '组织id',
  `workplace_name` varchar(50) DEFAULT NULL COMMENT '办公点名称',
  `community_id` bigint(20) DEFAULT NULL COMMENT '所在项目id' ,
  `create_time` datetime NOT NULL DEFAULT now() COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- 增加办公地点与楼栋门牌的关系表
-- DROP TABLE IF EXISTS `eh_communityandbuilding_relationes`;
-- DROP TABLE IF EXISTS `eh_communityAndbuilding_relationes`;

CREATE TABLE `eh_communityandbuilding_relationes` (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `building_id` bigint(20) DEFAULT NULL COMMENT '楼栋id',
  `community_id` bigint(20) DEFAULT NULL COMMENT '所在项目id' ,
  `address_id` bigint(20) DEFAULT NULL COMMENT '地址id' ,
  `create_time` datetime NOT NULL DEFAULT now() COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- 修复 workplace 的问题 janson TODO 这里需要弄新的分支
ALTER TABLE `eh_communityandbuilding_relationes` ADD COLUMN `workplace_id` BIGINT(20) NOT NULL DEFAULT 0 AFTER `update_time`;
-- by janson end

-- 增加 应用icon信息  add by yanjun 20180426
ALTER TABLE `eh_service_module_app_profile` ADD COLUMN `icon_uri`  varchar(255) NULL;

-- 标准版里app的配置是否跟随默认配置
ALTER TABLE `eh_communities` ADD COLUMN `app_self_config_flag`  tinyint(4) NULL ;


-- 标准版本的 key 增加用户 ID
ALTER TABLE `eh_smart_card_keys` ADD COLUMN `user_id` BIGINT(20) NOT NULL DEFAULT 0 AFTER `namespace_id`;
ALTER TABLE `eh_smart_card_keys` ADD COLUMN `cardkey` VARCHAR(1024) AFTER `namespace_id`;

-- 客户端处理方式0-native, 1-outside url, 2-inside url, 3-offline package  add by yanjun 201805171140
ALTER TABLE `eh_service_modules` ADD COLUMN `client_handler_type`  tinyint(4) NULL DEFAULT 0 COMMENT '0-native, 1-outside url, 2-inside url, 3-offline package' AFTER `app_type`;

-- 标准版本的 add by yuanlei
alter table eh_organization_workplaces add column province_id bigint(20) default null comment '省份id';
alter table eh_organization_workplaces add column city_id bigint(20) default null comment '城市id';
alter table eh_organization_workplaces add column area_id bigint(20) default null comment '区域id';
alter table eh_organization_workplaces add column whole_address_name varchar(128) default null comment '地址详细名称';

-- 增加省份字段  add by yanjun 201805251851
-- 增加省份字段  add by yanjun 201805251851
ALTER TABLE `eh_communities` ADD COLUMN `province_id`  bigint(20) NULL AFTER `uuid`;
ALTER TABLE `eh_communities` ADD COLUMN `province_name`  varchar(64) NULL AFTER `province_id`;

-- 系统应用标志、默认安装应用标志 add by yanjun 201805280955
ALTER TABLE `eh_service_modules` ADD COLUMN `system_app_flag`  tinyint(4) NULL COMMENT '0-no, 1-yes';
ALTER TABLE `eh_service_module_apps` ADD COLUMN `system_app_flag`  tinyint(4) NULL COMMENT '0-no, 1-yes';
ALTER TABLE `eh_service_module_apps` ADD COLUMN `default_app_flag`  tinyint(4) NULL COMMENT 'installed when organiation was created, 0-no, 1-yes';

-- 修改appId名字，实际为应用originId
ALTER TABLE `eh_banners` CHANGE COLUMN `appId` `app_id`  bigint(20) NULL DEFAULT NULL;

-- 园区广场电商 add by yanjun 20180703
CREATE TABLE `eh_community_bizs` (
  `id` bigint(20) NOT NULL,
  `organization_id` bigint(20) DEFAULT NULL,
  `community_id` bigint(20) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `biz_url` varchar(255) DEFAULT NULL,
  `logo_uri` varchar(255) DEFAULT NULL,
  `status` tinyint(4) DEFAULT '2' COMMENT '0-delete，1-disable，2-enable',
  PRIMARY KEY (`id`),
  KEY `community_id` (`community_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- 定制版已经执行过，在ehcore-server-schema.sql中有了。 edit by jun.yan
-- -- 通用脚本
-- -- ADD BY 黄良铭
-- -- 20180522-huangliangming-配置项管理-#30016
-- -- 创建配置项信息变更记录表
-- CREATE TABLE `eh_configurations_record_change` (
--   `id` INT(11)  NOT NULL COMMENT '主键',
--   `namespace_id` INT(11) NOT NULL COMMENT '域空间ID',
--   `conf_pre_json` VARCHAR(1024)  COMMENT '变动前信息JSON字符串',
--   `conf_aft_json` VARCHAR(1024)  COMMENT '变动后信息JSON字符串',
--   `record_change_type` INT(3) COMMENT '变动类型。0，新增；1，修改；3，删除',
--   `operator_uid` BIGINT(20)   COMMENT '操作人userId',
--   `operate_time` DATETIME    COMMENT '操作时间',
--   `operator_ip` VARCHAR(50)   COMMENT '操作者的IP地址',
--
--   PRIMARY KEY(`id`)
-- ) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '配置项信息变更记录表';
--
-- -- 配置项信息表新增一列（字段 ） is_readyonly
-- ALTER  TABLE eh_configurations  ADD  is_readonly  INT(3)  COMMENT '是否只读：1，是 ；null 或其他值为 否';
-- -- END BY 黄良铭


-- -----------------------------------------------------  以上为 5.6.3 以前的脚本 ----------------------------------------

-- -----------------------------------------------------  以下为 5.6.3 新增的脚本 ----------------------------------------


-- 定制版已经执行过，在ehcore-server-schema.sql中有了。 edit by jun.yan
-- -- 人事档案 2.7 (基线已经执行过) start by ryan
-- ALTER TABLE `eh_organization_member_details` DROP COLUMN `profile_integrity`;
-- ALTER TABLE `eh_organization_member_details` DROP COLUMN `department`;
-- ALTER TABLE `eh_organization_member_details` DROP COLUMN `department_ids`;
-- ALTER TABLE `eh_organization_member_details` DROP COLUMN `job_position`;
-- ALTER TABLE `eh_organization_member_details` DROP COLUMN `job_position_ids`;
-- ALTER TABLE `eh_organization_member_details` DROP COLUMN `job_level`;
-- ALTER TABLE `eh_organization_member_details` DROP COLUMN `job_level_ids`;
--
-- ALTER TABLE `eh_organization_member_details` ADD COLUMN `check_in_time_index` VARCHAR(64) NOT NULL DEFAULT '0000' COMMENT'only month&day like 0304' AFTER `check_in_time`;
-- ALTER TABLE `eh_organization_member_details` ADD COLUMN `birthday_index` VARCHAR(64) COMMENT'only month like 0304' AFTER `birthday`;
--
-- ALTER TABLE `eh_archives_notifications` DROP COLUMN `notify_emails`;
-- ALTER TABLE `eh_archives_notifications` CHANGE COLUMN `notify_hour` `notify_time` INTEGER COMMENT 'the hour of sending notifications';
-- ALTER TABLE `eh_archives_notifications` ADD COLUMN `mail_flag` TINYINT DEFAULT 0 NOT NULL COMMENT 'email sending, 0-no 1-yes' AFTER `notify_time`;
-- ALTER TABLE `eh_archives_notifications` ADD COLUMN `message_flag` TINYINT DEFAULT 0 NOT NULL COMMENT 'message sending, 0-no 1-yes' AFTER `mail_flag`;
-- ALTER TABLE `eh_archives_notifications` ADD COLUMN `notify_target` TEXT COMMENT 'the target email address' AFTER `message_flag`;
--
-- ALTER TABLE `eh_organization_member_details` MODIFY `check_in_time` DATE COMMENT '入职日期';
-- ALTER TABLE `eh_organization_member_details` MODIFY `check_in_time_index` VARCHAR(64) COMMENT '入职日期索引字段';
--
-- -- DROP TABLE IF EXISTS `eh_archives_operational_configurations`;
-- CREATE TABLE `eh_archives_operational_configurations` (
-- 	`id` BIGINT NOT NULL,
-- 	`namespace_id` INT NOT NULL DEFAULT '0',
-- 	`organization_id` BIGINT NOT NULL DEFAULT '0',
--   `detail_id` BIGINT NOT NULL COMMENT 'the detail id that belongs to the employee which is the change target',
--   `operation_type` TINYINT NOT NULL COMMENT 'the type of operation',
--   `operation_date` DATE COMMENT 'the date of executing the operation',
--   `additional_info` TEXT COMMENT 'the addition information for the operation',
--   `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0-cancel, 1-pending, 2-complete',
--   `create_time` DATETIME DEFAULT NULL COMMENT 'create time',
--   `operator_uid` BIGINT DEFAULT NULL COMMENT 'the id of the operator',
-- 	PRIMARY KEY (`id`)
-- ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
--
-- -- DROP TABLE IF EXISTS `eh_archives_operational_logs`;
-- CREATE TABLE `eh_archives_operational_logs` (
--   `id` BIGINT NOT NULL COMMENT 'id of the log',
--   `namespace_id` INT NOT NULL DEFAULT '0',
--   `organization_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'the id of the organization',
--   `detail_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'the detail id that belongs to the employee',
--   `operation_type` TINYINT NOT NULL COMMENT 'the type of the operate',
--   `operation_time` DATE NOT NULL COMMENT 'the time of the operate',
--   `string_tag1` VARCHAR(2048) COMMENT 'redundant information for the operate',
--   `string_tag2` VARCHAR(2048) COMMENT 'redundant information for the operate',
--   `string_tag3` VARCHAR(2048) COMMENT 'redundant information for the operate',
--   `string_tag4` VARCHAR(2048) COMMENT 'redundant information for the operate',
--   `string_tag5` VARCHAR(2048) COMMENT 'redundant information for the operate',
--   `string_tag6` VARCHAR(2048) COMMENT 'redundant information for the operate',
--   `operator_uid` BIGINT NOT NULL DEFAULT '0' COMMENT 'the id of the operator',
--   `operator_name` VARCHAR(64) NOT NULL DEFAULT '0' COMMENT 'the id of the operator',
--   `create_time` DATETIME DEFAULT NULL COMMENT 'create time',
-- 	PRIMARY KEY (`id`)
-- ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
--
-- -- end

-- 定制版已经执行过，在ehcore-server-schema.sql中有了。 edit by jun.yan
-- -- 下载中心 搬迁代码  by yanjun start
-- -- 注意：core已经上线过了，此处是搬迁代码过来的。以后合并分支的时候要注意
--
-- -- 任务中心添加执行开始时间和上传开始时间  add by yanjun 201805241345
-- ALTER TABLE `eh_tasks` ADD COLUMN `execute_start_time`  datetime NULL;
-- ALTER TABLE `eh_tasks` ADD COLUMN `upload_file_start_time`  datetime NULL;
-- ALTER TABLE `eh_tasks` ADD COLUMN `upload_file_finish_time`  datetime NULL;

-- 下载中心 搬迁代码  by yanjun end

-- 定制版已经执行过，在ehcore-server-schema.sql中有了。 edit by jun.yan
-- -- 修复 workplace 的问题 janson TODO 这里需要弄新的分支
-- ALTER TABLE `eh_communityandbuilding_relationes` ADD COLUMN `workplace_id` BIGINT(20) NOT NULL DEFAULT 0 AFTER `update_time`;
-- -- by janson end






-- ------------------------------------------------- 以下为zuolin-base-2.1(5.8.2)新增的schema脚本   start ---------------------------------


-- 模块icon
ALTER TABLE `eh_service_modules` ADD COLUMN `icon_uri`  varchar(255) NULL;

-- 分类结构
ALTER TABLE `eh_second_app_types` ADD COLUMN `parent_id`  bigint(22) NOT NULL DEFAULT 0 ;
ALTER TABLE `eh_second_app_types` ADD COLUMN `location_type`  tinyint(4) NULL COMMENT '参考枚举ServiceModuleLocationType';
ALTER TABLE `eh_second_app_types` ADD COLUMN `default_order`  bigint(22) NULL DEFAULT 0;


CREATE TABLE `eh_app_categories` (
  `id` bigint(22) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `parent_id` bigint(22) NOT NULL DEFAULT '0',
  `location_type` tinyint(4) DEFAULT NULL COMMENT '参考枚举ServiceModuleLocationType',
  `app_type` tinyint(4) DEFAULT NULL COMMENT '一级分类，0-oa，1-community，2-service。参考ServiceModuleAppType',
  `default_order` bigint(22) DEFAULT '0',
  `leaf_flag` tinyint(4) DEFAULT NULL COMMENT 'is leaf category, 0-no, 1-yes',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


ALTER TABLE `eh_service_module_entries` CHANGE COLUMN `second_app_type` `app_category_id`  bigint(22) NOT NULL DEFAULT 0;

-- 用户自定义的广场应用
CREATE TABLE `eh_user_apps` (
  `id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `app_id` bigint(20) NOT NULL,
  `location_type` tinyint(4) DEFAULT NULL COMMENT '位置信息，参考枚举ServiceModuleLocationType',
  `location_target_id` bigint(20) DEFAULT NULL COMMENT '位置对应的对象Id，eg：广场是communityId，工作台企业办公是organizationId',
  `order` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `u_eh_user_app_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户自定义显示的应用';


-- 用户自定义的广场应用
CREATE TABLE `eh_recommend_apps` (
  `id` bigint(20) NOT NULL,
  `app_id` bigint(20) NOT NULL,
  `scope_type` tinyint(4) DEFAULT NULL COMMENT '范围，1-园区，4-公司',
  `scope_id` bigint(20) DEFAULT NULL COMMENT '范围对象id',
  `order` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `u_eh_recommend_app_scope_id` (`scope_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户自定义显示的应用';

-- 服务广场通用配置表
CREATE TABLE `eh_launch_pad_configs` (
  `id` bigint(20) NOT NULL,
  `owner_type` tinyint(4) NOT NULL,
  `owner_id` bigint(20) NOT NULL,
  `navigator_all_icon_uri` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `i_eh_owner_id` (`owner_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 新接口使用group_id代替itemGroup和itemLocation  add by yanjun 20180828
ALTER TABLE `eh_launch_pad_items` ADD COLUMN `group_id`  bigint(20) NULL AFTER `app_id`;

ALTER TABLE `eh_item_service_categries` ADD COLUMN `group_id`  bigint(20) NULL;

ALTER TABLE `eh_user_launch_pad_items` ADD COLUMN `group_id`  bigint(20) NULL AFTER `item_id`;

-- 通用脚本
-- 增加动态表单的ownerId
ALTER TABLE `eh_var_field_scopes` ADD COLUMN `owner_id`  bigint(20) NOT NULL  DEFAULT  0  AFTER `namespace_id`;
ALTER TABLE `eh_var_field_item_scopes` ADD COLUMN `owner_id`  bigint(20) NOT NULL  DEFAULT  0 AFTER `namespace_id`;
ALTER TABLE `eh_var_field_group_scopes` ADD COLUMN `owner_id`  bigint(20) NOT NULL  DEFAULT  0 AFTER `namespace_id`;


ALTER TABLE `eh_var_field_scopes` ADD COLUMN `owner_type`  VARCHAR(1024)  NULL  AFTER `owner_id`;
ALTER TABLE `eh_var_field_item_scopes` ADD COLUMN `owner_type`  VARCHAR(1024)  NULL AFTER `owner_id`;
ALTER TABLE `eh_var_field_group_scopes` ADD COLUMN `owner_type`  VARCHAR(1024)  NULL AFTER `owner_id`;
-- end

-- 合同参数配置增加owner
ALTER TABLE  eh_contract_params ADD COLUMN  `owner_id`  BIGINT(20) NOT NULL  DEFAULT  0 AFTER  `namespace_id`;
ALTER TABLE  eh_contract_params ADD COLUMN  `ownerType` VARCHAR(1024) NULL AFTER  `namespace_id`;
ALTER TABLE  eh_contract_templates add COLUMN  `org_id`  BIGINT(20) NOT NULL  DEFAULT  0 AFTER  `namespace_id`;


-- 缴费收费项增加orgId
ALTER  TABLE  eh_payment_charging_item_scopes ADD  COLUMN  `org_id` BIGINT(20) NOT NULL   DEFAULT 0;
ALTER  TABLE  eh_payment_charging_standards_scopes ADD  COLUMN  `org_id` BIGINT(20) NOT NULL  NULL  DEFAULT 0;
ALTER  TABLE  eh_payment_bill_groups ADD  COLUMN  `org_id` BIGINT(20) NOT NULL  NULL  DEFAULT 0;


-- 通用脚本
-- AUHOR:jiarui 20180730
-- REMARK:物业巡检通知参数设置增加targetId,targetType
ALTER  TABLE  `eh_pm_notify_configurations` ADD  COLUMN `target_id` BIGINT(20) NOT NULL COMMENT 'organization id' DEFAULT  0 AFTER  `owner_type`;
ALTER  TABLE  `eh_pm_notify_configurations` ADD  COLUMN `target_type` VARCHAR(1024) NULL AFTER  `target_id`;
ALTER  TABLE  `eh_equipment_inspection_review_date` ADD  COLUMN `target_id` BIGINT(20) NOT NULL COMMENT 'organization id' DEFAULT  0 AFTER  `owner_type`;
ALTER  TABLE  `eh_equipment_inspection_review_date` ADD  COLUMN `target_type` VARCHAR(1024) NULL AFTER  `target_id`;
-- end

--
-- 工作流 key-value 表  add by xq.tian  20180814
--
DROP TABLE IF EXISTS `eh_flow_kv_configs`;
CREATE TABLE `eh_flow_kv_configs` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `project_type` VARCHAR(64) NOT NULL DEFAULT '',
  `project_id` BIGINT NOT NULL DEFAULT 0,
  `module_type` VARCHAR(64) NOT NULL,
  `module_id` BIGINT NOT NULL COMMENT 'the module id',
  `owner_type` VARCHAR(64) NOT NULL,
  `owner_id` BIGINT NOT NULL,
  `key` VARCHAR(64) NOT NULL,
  `value` VARCHAR(64) NOT NULL,
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: invalid, 1:waiting_for_approval, 2: valid',
  `create_time` DATETIME(3) NOT NULL COMMENT 'record create time',
  `creator_uid` BIGINT NOT NULL DEFAULT 0,
  `update_time` DATETIME(3) COMMENT 'record update time',
  `updater_uid` BIGINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

--
-- 表单 key-value 表  add by xq.tian  20180814
--
DROP TABLE IF EXISTS `eh_general_form_kv_configs`;
CREATE TABLE `eh_general_form_kv_configs` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `project_type` VARCHAR(64) NOT NULL DEFAULT '',
  `project_id` BIGINT NOT NULL DEFAULT 0,
  `module_type` VARCHAR(64) NOT NULL,
  `module_id` BIGINT NOT NULL COMMENT 'the module id',
  `owner_type` VARCHAR(64) NOT NULL,
  `owner_id` BIGINT NOT NULL,
  `key` VARCHAR(64) NOT NULL,
  `value` VARCHAR(64) NOT NULL,
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: invalid, 1:waiting_for_approval, 2: valid',
  `create_time` DATETIME(3) NOT NULL COMMENT 'record create time',
  `creator_uid` BIGINT NOT NULL DEFAULT 0,
  `update_time` DATETIME(3) COMMENT 'record update time',
  `updater_uid` BIGINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

ALTER TABLE eh_general_forms ADD COLUMN project_type VARCHAR(64) NOT NULL DEFAULT 'EhCommunities';
ALTER TABLE eh_general_forms ADD COLUMN project_id BIGINT NOT NULL DEFAULT 0;

ALTER TABLE `eh_service_alliances` CHANGE COLUMN `integral_tag1` `integral_tag1` BIGINT(20) NULL DEFAULT NULL COMMENT '跳转类型 0-不跳转 2-表单/表单+工作流 3-跳转应用' ;
ALTER TABLE `eh_service_alliances` 	ADD COLUMN `form_id` BIGINT NULL DEFAULT NULL COMMENT '表单id' ;
ALTER TABLE `eh_service_alliances` 	ADD COLUMN `flow_id` BIGINT NULL DEFAULT NULL COMMENT '工作流id' ;
ALTER TABLE `eh_service_alliance_categories` ADD COLUMN `skip_type` TINYINT NOT NULL DEFAULT '0' COMMENT '1-当该服务类型下只有一个服务时，点击服务类型直接进入服务。0-反之';

ALTER TABLE `eh_service_alliance_categories` ADD COLUMN `type` BIGINT(20) NOT NULL DEFAULT '0' COMMENT '服务联盟类型' ;

ALTER TABLE `eh_alliance_tag` ADD COLUMN `owner_type` VARCHAR(15) NOT NULL DEFAULT 'organization';

ALTER TABLE `eh_alliance_tag` ADD COLUMN `owner_id` BIGINT(20) NOT NULL DEFAULT '0' ;


-- by st.zheng 允许表单为空
ALTER TABLE `eh_lease_form_requests`
MODIFY COLUMN `source_id`  bigint(20) NULL AFTER `owner_type`;


-- 工位预订 城市管理 通用修改 shiheng.ma 20180824
ALTER TABLE `eh_office_cubicle_cities` ADD COLUMN `org_id` BIGINT(20) DEFAULT NULL COMMENT '所属管理公司Id';
ALTER TABLE `eh_office_cubicle_cities` ADD COLUMN `owner_type` VARCHAR(128) DEFAULT NULL COMMENT '项目类型';
ALTER TABLE `eh_office_cubicle_cities` ADD COLUMN `owner_id` BIGINT(20) DEFAULT NULL COMMENT '项目Id';

CREATE TABLE `eh_office_cubicle_configs` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `org_id` BIGINT NOT NULL DEFAULT 0 COMMENT '管理公司Id',
  `owner_type` VARCHAR(64) NOT NULL,
  `owner_id` BIGINT NOT NULL,
  `customize_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '0: general configure, 1:customize configure',
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: invalid, 1:waiting_for_approval, 2: valid',
  `create_time` DATETIME(3) NOT NULL COMMENT 'record create time',
  `creator_uid` BIGINT NOT NULL DEFAULT 0,
  `update_time` DATETIME(3) COMMENT 'record update time',
  `updater_uid` BIGINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;
-- END 工位预订



-- 菜单增加“管理端”、“用户端”分类
ALTER TABLE `eh_web_menus` ADD COLUMN `scene_type`  tinyint(4) NOT NULL DEFAULT 1 COMMENT '形态，1-管理端，2-客户端，参考枚举ServiceModuleSceneType';


-- ------------------------------------------------- zuolin-base-2.1(5.8.2)新增的数据脚本   end ---------------------------------






-- ------------------------------------------------- 5.8.4.20180925 新增的数据脚本   start ---------------------------------


-- 如下所说，和5.9.0后面加上的重复了。

-- -- --------------企业OA相关功能提前融合到标准版，在5.9.0全量合并到标准版发布时需要跳过这部分脚本的执行-----------
--
-- -- AUTHOR: 张智伟 20180822
-- -- REMARK: issue-36367 考勤规则新增打卡提醒设置
-- ALTER TABLE eh_punch_rules ADD COLUMN punch_remind_flag TINYINT NOT NULL DEFAULT 0 COMMENT '是否开启上下班打卡提醒：1 开启 0 关闭' AFTER china_holiday_flag;
-- ALTER TABLE eh_punch_rules ADD COLUMN remind_minutes_on_duty INT NOT NULL DEFAULT 0 COMMENT '上班提前分钟数打卡提醒' AFTER punch_remind_flag;
--
-- -- AUTHOR: 张智伟 20180822
-- -- REMARK: issue-36367 考勤规则新增打卡提醒设置,该表保存生成的提醒记录
-- CREATE TABLE `eh_punch_notifications` (
--   `id` BIGINT NOT NULL COMMENT '主键',
--   `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT '域空间',
--   `enterprise_id` BIGINT NOT NULL COMMENT '总公司id',
--   `user_id` BIGINT NOT NULL COMMENT '被提醒人的uid',
--   `detail_id` BIGINT NOT NULL COMMENT '被提醒人的detailId',
--   `punch_rule_id` BIGINT NOT NULL COMMENT '所属考勤规则',
--   `punch_type` TINYINT(4) NOT NULL DEFAULT '0' COMMENT '0- 上班打卡 ; 1- 下班打卡',
--   `punch_interval_no` INT(11) DEFAULT '1' COMMENT '第几次排班的打卡',
--   `punch_date` DATE NOT NULL COMMENT '打卡日期',
--   `rule_time` DATETIME NOT NULL COMMENT '规则设置的该次打卡时间',
--   `except_remind_time` DATETIME NOT NULL COMMENT '规则设置的打卡提醒时间',
--   `act_remind_time` DATETIME NULL COMMENT '实际提醒时间',
--   `invalid_reason` VARCHAR(512) COMMENT '提醒记录失效的原因',
--   `invalid_flag` TINYINT(4) NOT NULL DEFAULT '0' COMMENT '0- 有效 ; 1- 无效',
--   `create_time` DATETIME NOT NULL COMMENT '记录创建时间',
--   `update_time` DATETIME NULL COMMENT '记录创建时间',
--   PRIMARY KEY (`id`),
--   KEY i_eh_enterprise_detail_id(`namespace_id`,`enterprise_id`,`detail_id`)
-- ) ENGINE=INNODB DEFAULT CHARSET=UTF8MB4 COMMENT='打卡提醒队列，该数据只保留一天';
--
-- -- AUTHOR: 张智伟 20180822
-- -- REMARK: issue-36367 打卡记录报表排序
-- ALTER TABLE eh_punch_logs ADD COLUMN detail_id BIGINT COMMENT '员工 的detail Id' AFTER user_id;
-- ALTER TABLE eh_punch_log_files ADD COLUMN detail_id BIGINT COMMENT '员工 的detail Id' AFTER user_id;
--
--
-- -- AUTHOR: 吴寒
-- -- REMARK: issue-36405 公告1.8 修改表结构
-- ALTER TABLE `eh_enterprise_notices` ADD COLUMN `stick_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '是否置顶，0-否，1-是';
-- ALTER TABLE `eh_enterprise_notices` ADD COLUMN `stick_time` DATETIME;
--
-- -- AUTHOR: 吴寒
-- -- REMARK: issue-33887: 增加操作人姓名到目录/文件表
-- ALTER TABLE `eh_file_management_contents` ADD COLUMN `operator_name`  VARCHAR(256) ;
-- ALTER TABLE `eh_file_management_catalogs` ADD COLUMN `operator_name`  VARCHAR(256) ;
-- -- REMARK: issue-33887: 给文件表增加索引
-- ALTER TABLE `eh_file_management_contents` ADD INDEX  `i_eh_content_catalog_id` (`catalog_id`);
-- ALTER TABLE `eh_file_management_contents` ADD INDEX  `i_eh_content_parent_id` (`parent_id`);
--
-- -- AUTHOR: 吴寒
-- -- REMARK: issue-33943 日程提醒1.2
-- ALTER TABLE eh_remind_settings ADD COLUMN app_version VARCHAR(32) DEFAULT '5.8.0' COMMENT '对应app版本(历史数据5.8.0),根据APP版本选择性展示';
-- ALTER TABLE eh_remind_settings ADD COLUMN before_time BIGINT COMMENT '提前多少时间(毫秒数)不超过1天的部分在这里减';
--
--
-- -- AUTHOR: 吴寒
-- -- REMARK: 会议管理V1.2
-- ALTER TABLE `eh_meeting_reservations`  CHANGE `content` `content` TEXT COMMENT '会议详细内容';
-- ALTER TABLE `eh_meeting_reservations`  ADD COLUMN `attachment_flag` TINYINT DEFAULT 0 COMMENT '是否有附件 1-是 0-否';
-- ALTER TABLE `eh_meeting_records`  ADD COLUMN `attachment_flag` TINYINT DEFAULT 0 COMMENT '是否有附件 1-是 0-否';
--
-- -- 增加附件表 会议预定和会议纪要共用
-- CREATE TABLE `eh_meeting_attachments` (
--   `id` BIGINT NOT NULL COMMENT 'id of the record',
--   `namespace_id` INTEGER NOT NULL DEFAULT 0,
--   `owner_type` VARCHAR(32) NOT NULL COMMENT 'owner type EhMeetingRecords/EhMeetingReservations',
--   `owner_id` BIGINT NOT NULL COMMENT 'key of the owner',
--   `content_name` VARCHAR(1024) COMMENT 'attachment object content name like: abc.jpg',
--   `content_type` VARCHAR(32) COMMENT 'attachment object content type',
--   `content_uri` VARCHAR(1024) COMMENT 'attachment object link info on storage',
--   `content_size` INT(11)  COMMENT 'attachment object size',
--   `content_icon_uri` VARCHAR(1024) COMMENT 'attachment object link of content icon',
--   `creator_uid` BIGINT NOT NULL,
--   `create_time` DATETIME NOT NULL,
--   PRIMARY KEY (`id`)
-- ) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;
--
-- -- AUTHOR: 荣楠
-- -- REMARK: issue-34029 工作汇报1.2
-- ALTER TABLE `eh_work_report_val_receiver_map` ADD COLUMN `organization_id` BIGINT DEFAULT 0 NOT NULL COMMENT 'the orgId for the user' AFTER `namespace_id`;
-- ALTER TABLE `eh_work_report_val_receiver_map` ADD INDEX `i_work_report_receiver_id` (`receiver_user_id`) ;
--
-- ALTER TABLE `eh_work_reports` ADD COLUMN `validity_setting` VARCHAR(512) COMMENT 'the expiry date of the work report' AFTER `form_version`;
-- ALTER TABLE `eh_work_reports` ADD COLUMN `receiver_msg_type` TINYINT NOT NULL DEFAULT 0 COMMENT 'the type of the receiver message settings' AFTER `validity_setting`;
-- ALTER TABLE `eh_work_reports` ADD COLUMN `receiver_msg_seeting` VARCHAR(512) COMMENT 'the time range of the receiver message' AFTER `receiver_msg_type`;
-- ALTER TABLE `eh_work_reports` ADD COLUMN `author_msg_type` TINYINT NOT NULL DEFAULT 0 COMMENT 'the type of the author message settings' AFTER `receiver_msg_seeting`;
-- ALTER TABLE `eh_work_reports` ADD COLUMN `author_msg_seeting` VARCHAR(512) COMMENT 'the time range of the author message' AFTER `author_msg_type`;
--
-- ALTER TABLE `eh_work_report_vals` ADD COLUMN `receiver_avatar` VARCHAR(1024) COMMENT 'the avatar of the fisrt receiver' AFTER `report_type`;
-- ALTER TABLE `eh_work_report_vals` ADD COLUMN `applier_avatar` VARCHAR(1024) COMMENT 'the avatar of the author' AFTER `receiver_avatar`;
--
-- ALTER TABLE `eh_work_report_vals` MODIFY COLUMN `report_time` DATE COMMENT 'the target time of the report';
--
--
-- CREATE TABLE `eh_work_report_val_receiver_msg` (
--   `id` BIGINT NOT NULL,
--   `namespace_id` INTEGER,
--   `organization_id` BIGINT NOT NULL DEFAULT 0,
--   `report_id` BIGINT NOT NULL COMMENT 'the id of the report',
--   `report_val_id` BIGINT NOT NULL COMMENT 'id of the report val',
--   `report_name` VARCHAR(128) NOT NULL,
--   `report_type` TINYINT COMMENT '0-Day, 1-Week, 2-Month',
--   `report_time` DATE NOT NULL COMMENT 'the target time of the report',
--   `reminder_time` DATETIME COMMENT 'the reminder time of the record',
--   `receiver_user_id` BIGINT NOT NULL COMMENT 'the id of the receiver',
--   `create_time` DATETIME COMMENT 'record create time',
--
--   KEY `i_eh_work_report_val_receiver_msg_report_id`(`report_id`),
--   KEY `i_eh_work_report_val_receiver_msg_report_val_id`(`report_val_id`),
--   KEY `i_eh_work_report_val_receiver_msg_report_time`(`report_time`),
--   PRIMARY KEY (`id`)
-- ) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;
--
-- CREATE TABLE `eh_work_report_scope_msg` (
--   `id` BIGINT NOT NULL,
--   `namespace_id` INTEGER,
--   `organization_id` BIGINT NOT NULL DEFAULT 0,
--   `report_id` BIGINT NOT NULL COMMENT 'the id of the report',
--   `report_name` VARCHAR(128) NOT NULL,
--   `report_type` TINYINT COMMENT '0-Day, 1-Week, 2-Month',
--   `report_time` DATE NOT NULL COMMENT 'the target time of the report',
--   `reminder_time` DATETIME COMMENT 'the reminder time of the record',
--   `end_time` DATETIME COMMENT 'the deadline of the report',
--   `scope_ids` TEXT COMMENT 'the id list of the receiver',
--   `create_time` DATETIME COMMENT 'record create time',
--
--   KEY `i_eh_work_report_scope_msg_report_id`(`report_id`),
--   KEY `i_eh_work_report_scope_msg_report_time`(`report_time`),
--   PRIMARY KEY (`id`)
-- ) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;
-- -- END issue-34029
-- -- --------------企业OA相关功能提前融合到标准版，END 张智伟 -----------

-- 用户启用自定义配置的标记 add by yanjun 20180920
CREATE TABLE `eh_user_app_flags` (
  `id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `location_type` tinyint(4) DEFAULT NULL COMMENT '位置信息，参考枚举ServiceModuleLocationType',
  `location_target_id` bigint(20) DEFAULT NULL COMMENT '位置对应的对象Id，eg：广场是communityId，工作台企业办公是organizationId',
  PRIMARY KEY (`id`),
  KEY `u_eh_user_app_flag_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户启用自定义配置的标记';

-- AUTHOR: xq.tian
-- REMARK: 漏掉的工作流表, 需要删除原来的表重建
DROP TABLE IF EXISTS `eh_flow_scripts`;
CREATE TABLE `eh_flow_scripts` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `module_type` VARCHAR(64) NOT NULL,
  `module_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'the module id',
  `owner_type` VARCHAR(64),
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `script_category` VARCHAR(64) NOT NULL COMMENT 'system_script, user_script',
  `script_type` VARCHAR(64) NOT NULL COMMENT 'javascript, groovy, java and other',
  `script_main_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'ref eh_flow_scripts',
  `script_version` INTEGER NOT NULL DEFAULT 0 COMMENT 'script version',
  `name` VARCHAR(128) COMMENT 'script name',
  `description` TEXT COMMENT 'script description',
  `script` LONGTEXT COMMENT 'script content',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: invalid, 1: valid',
  `create_time` datetime(3),
  `creator_uid` BIGINT,
  `update_time` DATETIME(3),
  `update_uid` BIGINT,
  `string_tag1` VARCHAR(128),
  `string_tag2` VARCHAR(128),
  `string_tag3` VARCHAR(128),
  `string_tag4` VARCHAR(128),
  `string_tag5` VARCHAR(128),
  `integral_tag1` BIGINT NOT NULL DEFAULT 0,
  `integral_tag2` BIGINT NOT NULL DEFAULT 0,
  `integral_tag3` BIGINT NOT NULL DEFAULT 0,
  `integral_tag4` BIGINT NOT NULL DEFAULT 0,
  `integral_tag5` BIGINT NOT NULL DEFAULT 0,
  `last_commit` VARCHAR(40) COMMENT 'repository last commit id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='flow scripts in dev mode';

-- 合同字段名称修改 add by jiarui 20180925
ALTER TABLE  eh_contract_params CHANGE  ownerType owner_type VARCHAR(1024);


-- ------------------------------------------------- 5.8.4.20180925 新增的数据脚本   end ---------------------------------













