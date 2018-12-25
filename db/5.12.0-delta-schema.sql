-- AUTHOR :黄鹏宇 2018年12月20日
-- REMARK :添加物业配置表
CREATE TABLE `eh_property_configurations` (
  `id` bigint(20) NOT NULL,
  `namespace_id` int(11) DEFAULT NULL,
  `community_id` bigint(20) DEFAULT NULL,
  `name` varchar(64) DEFAULT NULL,
  `value` varchar(255) DEFAULT NULL,
  `module_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 comment '物业通用配置';

-- AUTHOR: 黄鹏宇 20181217
-- REMARK: 物业缴费V7.4(瑞安项目-资产管理对接CM系统) -41302：增加第三方客户分类
ALTER TABLE eh_enterprise_customers ADD COLUMN namespace_customer_group VARCHAR(128) COMMENT '第三方客户分类' AFTER namespace_customer_type;

-- AUTHOR: 杨崇鑫 2018-12-17
-- REMARK: 物业缴费V7.4(瑞安项目-资产管理对接CM系统) ： 一个特殊error描述给左邻系统
ALTER TABLE `eh_payment_bills` ADD COLUMN `third_error_description` VARCHAR(256) COMMENT '一个特殊error描述给左邻系统';

-- AUTHOR: 张智伟
-- REMARK: issue-43700 企业同事圈v1.0
CREATE TABLE eh_enterprise_moments (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL COMMENT '所属域空间',
  `organization_id` BIGINT NOT NULL COMMENT '所属企业ID',
  `content_type` VARCHAR (24) COMMENT '内容的类型，文本等',
  `content` TEXT COMMENT '发布内容',
  `tag_id` BIGINT COMMENT '标签ID',
  `tag_name` VARCHAR (512) COMMENT '标签名称',
  `longitude` DOUBLE COMMENT '经度',
  `latitude` DOUBLE COMMENT '纬度',
  `geohash` VARCHAR (32),
  `location` VARCHAR (1024) COMMENT '定位到的地址',
  `like_count` INTEGER NOT NULL DEFAULT 0 COMMENT '点赞数',
  `comment_count` INTEGER NOT NULL DEFAULT 0 COMMENT '评论数',
  `integral_tag1` BIGINT COMMENT '部门ID',
  `integral_tag2` BIGINT,
  `integral_tag3` BIGINT,
  `integral_tag4` BIGINT,
  `integral_tag5` BIGINT,
  `string_tag1` VARCHAR (128) COMMENT '第一张上传的图片uri，查询优化冗余存储',
  `string_tag2` VARCHAR (128),
  `string_tag3` VARCHAR (128),
  `string_tag4` VARCHAR (128),
  `string_tag5` VARCHAR (128),
  `creator_name` VARCHAR (64) COMMENT '发布人姓名',
  `creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId',
  `create_time` DATETIME NOT NULL COMMENT '记录创建时间',
  `operator_uid` BIGINT COMMENT '记录更新人userId',
  `operate_time` DATETIME COMMENT '记录更新时间',
  `delete_uid` BIGINT NULL DEFAULT 0 COMMENT '删除人的uid',
  `delete_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除状态,0:有效，1:删除',
  `delete_time` DATETIME COMMENT '删除时间',
  PRIMARY KEY (`id`),
  KEY `i_eh_organization_id` (`namespace_id`,`organization_id`,`create_time` DESC )
) ENGINE = INNODB DEFAULT CHARSET = UTF8MB4 COMMENT '同事圈话题表' ;

CREATE TABLE `eh_enterprise_moment_scopes` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `enterprise_moment_id` BIGINT NOT NULL COMMENT '同事圈话题id',
  `source_type` VARCHAR (64) NOT NULL COMMENT 'ORGANIZATION, MEMBERDETAIL',
  `source_id` BIGINT NOT NULL COMMENT 'id of the scope',
  `creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId',
  `create_time` DATETIME NOT NULL COMMENT '记录创建时间',
  PRIMARY KEY (`id`),
  KEY `i_eh_enterprise_moment_id` (`namespace_id`,`enterprise_moment_id`)
) ENGINE = INNODB DEFAULT CHARSET = UTF8MB4 COMMENT '同事圈可见范围' ;

CREATE TABLE `eh_enterprise_moment_attachments` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `enterprise_moment_id` BIGINT NOT NULL COMMENT '同事圈话题id',
  `content_name` VARCHAR (256) NOT NULL COMMENT '文件名',
  `content_suffix` VARCHAR (64) COMMENT '文件后缀',
  `size` INTEGER NOT NULL DEFAULT 0 COMMENT '文件大小',
  `content_type` VARCHAR (32) COMMENT '文件类型',
  `content_uri` VARCHAR (1024) COMMENT 'uri',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT COMMENT '记录更新人userId',
  `operate_time` DATETIME COMMENT '记录更新时间',
  PRIMARY KEY (`id`),
  KEY `i_eh_enterprise_moment_id` (`namespace_id`,`enterprise_moment_id`)
) ENGINE = INNODB DEFAULT CHARSET = UTF8MB4 COMMENT '同事圈附件' ;

CREATE TABLE eh_enterprise_moment_tags (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL COMMENT '所属域空间',
  `organization_id` BIGINT NOT NULL COMMENT '所属企业ID',
  `name` VARCHAR (512) NOT NULL COMMENT '标签名称',
  `default_order` INTEGER NOT NULL DEFAULT 0 COMMENT '排序',
  `creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId',
  `create_time` DATETIME NOT NULL COMMENT '记录创建时间',
  `operator_uid` BIGINT COMMENT '记录更新人userId',
  `operate_time` DATETIME COMMENT '记录更新时间',
  `delete_uid` BIGINT NULL DEFAULT 0 COMMENT '删除人的uid',
  `delete_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除状态,0:有效，1:删除',
  `delete_time` DATETIME COMMENT '删除时间',
  PRIMARY KEY (`id`),
  KEY `i_eh_organization_id` (`namespace_id`,`organization_id`)
) ENGINE = INNODB DEFAULT CHARSET = UTF8MB4 COMMENT '同事圈标签设置' ;

CREATE TABLE eh_enterprise_moment_favourites (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL COMMENT '所属域空间',
  `organization_id` BIGINT NOT NULL COMMENT '所属企业ID',
  `enterprise_moment_id` BIGINT NOT NULL COMMENT '同事圈话题id',
  `user_id` BIGINT NULL COMMENT '用户ID',
  `detail_id` BIGINT NOT NULL COMMENT '用户detailId',
  `creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId',
  `create_time` DATETIME NOT NULL COMMENT '记录创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_enterprise_moment_id` (`namespace_id`,`organization_id`,`enterprise_moment_id`,`user_id`)
) ENGINE = INNODB DEFAULT CHARSET = UTF8MB4 COMMENT '我点赞的同事圈话题' ;

CREATE TABLE eh_enterprise_moment_comments (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL COMMENT '所属域空间',
  `organization_id` BIGINT NOT NULL COMMENT '所属企业ID',
  `enterprise_moment_id` BIGINT NOT NULL COMMENT '同事圈话题id',
  `content_type` VARCHAR (32) COMMENT '评论内容的类型，文本等',
  `content` TEXT COMMENT '评论内容',
  `reply_to_comment_id` BIGINT COMMENT '回复指向的评论Id',
  `reply_to_user_id` BIGINT COMMENT '回复目标用户',
  `reply_to_name` VARCHAR (64) COMMENT '回复目标姓名',
  `creator_name` VARCHAR (64) COMMENT '发布人姓名',
  `creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId',
  `create_time` DATETIME NOT NULL COMMENT '记录创建时间',
  `operator_uid` BIGINT COMMENT '记录更新人userId',
  `operate_time` DATETIME COMMENT '记录更新时间',
  PRIMARY KEY (`id`),
  KEY `i_eh_enterprise_moment_id` (`namespace_id`,`organization_id`,`enterprise_moment_id`,`create_time` DESC )
) ENGINE = INNODB DEFAULT CHARSET = UTF8MB4 COMMENT '同事圈评论表' ;

CREATE TABLE `eh_enterprise_moment_comment_attachments` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `comment_id` BIGINT NOT NULL COMMENT '评论ID',
  `content_type` VARCHAR (32) COMMENT '文件类型',
  `content_uri` VARCHAR (1024) COMMENT 'uri',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  PRIMARY KEY (`id`),
  KEY `i_eh_comment_id` (`namespace_id`,`comment_id`)
) ENGINE = INNODB DEFAULT CHARSET = UTF8MB4 COMMENT '同事圈评论附件' ;

CREATE TABLE eh_enterprise_moment_messages (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL COMMENT '所属域空间',
  `organization_id` BIGINT NOT NULL COMMENT '所属企业ID',
  `enterprise_moment_id` BIGINT NOT NULL COMMENT '同事圈话题id',
  `receiver_uid` BIGINT NOT NULL COMMENT '消息接收者用户ID',
  `message_type` TINYINT NOT NULL COMMENT '消息类型：1：点赞 2：评论',
  `message` TEXT COMMENT '消息内容 = 评论内容',
  `source_type` VARCHAR(124) COMMENT '点赞或评论表的实体类',
  `source_id` BIGINT COMMENT '点赞或评论id',
  `source_delete_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '关联评论的状态,0:有效，1:删除',
  `integral_tag1` BIGINT,
  `integral_tag2` BIGINT,
  `integral_tag3` BIGINT,
  `integral_tag4` BIGINT,
  `integral_tag5` BIGINT,
  `string_tag1` VARCHAR (128),
  `string_tag2` VARCHAR (128),
  `string_tag3` VARCHAR (128),
  `string_tag4` VARCHAR (128),
  `string_tag5` VARCHAR (128),
  `operator_name` VARCHAR (64) COMMENT '操作人姓名',
  `operator_uid` BIGINT COMMENT '记录更新人userId',
  `operate_time` DATETIME COMMENT '记录更新时间',
  PRIMARY KEY (`id`),
  KEY `i_eh_enterprise_moment_id` (`namespace_id`,`organization_id`,`enterprise_moment_id`),
  KEY `i_eh_enterprise_receiver_uid` (`namespace_id`,`organization_id`,`receiver_uid`)
) ENGINE = INNODB DEFAULT CHARSET = UTF8MB4 COMMENT '同事圈历史消息表' ;

-- AUTHOR: 谢旭双
-- REMARK: issue-43700 企业同事圈
CREATE TABLE eh_enterprise_moment_access_records (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL COMMENT '所属域空间',
  `organization_id` BIGINT NOT NULL COMMENT '所属企业ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `last_visit_time` DATETIME NOT NULL COMMENT '最近一次访问时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_user_id` (`namespace_id`,`organization_id`,`user_id`)
) ENGINE = INNODB DEFAULT CHARSET = UTF8MB4 COMMENT '同事圈用户最近一次访问时间记录表' ;

-- 资源预约3.8.3
-- by st.zheng
ALTER TABLE `eh_rentalv2_resource_types`
ADD COLUMN `detail_page_type`  tinyint(4) NULL COMMENT '资源详情样式 0:默认样式 1:时间轴样式' AFTER `page_type`;
ALTER TABLE `eh_rentalv2_default_rules`
ADD COLUMN `preview_using_image_uri`  varchar(255) NULL COMMENT '资源概览 使用中图片' AFTER `file_flag`;
ALTER TABLE `eh_rentalv2_default_rules`
ADD COLUMN `preview_idle_image_uri`  varchar(255) NULL COMMENT '资源概览 空闲图片' AFTER `preview_using_image_uri`;
ALTER TABLE `eh_rentalv2_default_rules`
ADD COLUMN `shop_name`  varchar(255) NULL COMMENT '关联商铺名' AFTER `preview_idle_image_uri`;
ALTER TABLE `eh_rentalv2_default_rules`
ADD COLUMN `shop_no`  varchar(255) NULL COMMENT '关联商铺号' AFTER `shop_name`;
ALTER TABLE `eh_rentalv2_default_rules`
ADD COLUMN `shop_url`  varchar(255) NULL COMMENT '关联商铺地址' AFTER `shop_no`;


-- AUTHOR: 刘一麟 2018-12-19
-- REMARK: 人脸识别v1.8 照片注册结果通知
CREATE TABLE `eh_aclink_photo_sync_result` (
    `id` BIGINT NOT NULL COMMENT '主键',
    `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT '域空间ID',
    `server_id` BIGINT NOT NULL COMMENT '内网服务器id',
    `photo_id` BIGINT NOT NULL COMMENT '照片id',
    `res_code` TINYINT NOT NULL COMMENT '同步结果',
    `creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId',
    `create_time` DATETIME NOT NULL COMMENT '记录创建时间',
    `operate_time` DATETIME COMMENT '记录更新时间',
    `operator_uid` BIGINT COMMENT '记录更新人userId',
    PRIMARY KEY (`id`)
)  ENGINE=INNODB DEFAULT CHARSET=UTF8MB4 COMMENT='照片同步结果表';

ALTER TABLE eh_door_auth_level ADD COLUMN `name` varchar(128) COMMENT '自动授权对象名称' AFTER `level_type`;

-- AUTHOR: 梁燕龙 20181219
-- REMARK: 动态主页签
ALTER TABLE eh_portal_navigation_bars MODIFY config_json VARCHAR(1024);
ALTER TABLE eh_portal_navigation_bars ADD COLUMN `top_bar_style` TINYINT COMMENT '主页签顶栏样式，1：透明渐变，2：不透明形变，3：不透明固定';
ALTER TABLE eh_launch_pad_indexs ADD COLUMN `preview_version_id` BIGINT COMMENT '预览版本号';

-- AUTHOR: 马世亨 20181225
-- REMARK: issue-45817 政策服务标题，概要，内容字段扩大
ALTER TABLE `eh_policies` MODIFY COLUMN `title` varchar(200);
ALTER TABLE `eh_policies` MODIFY COLUMN `outline` varchar(2000);
ALTER TABLE `eh_policies` MODIFY COLUMN `content` mediumtext;