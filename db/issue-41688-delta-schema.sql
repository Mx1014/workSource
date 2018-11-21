-- AUTHOR: 梁燕龙 20181115
-- 工作台公司推荐应用
CREATE TABLE `eh_work_platform_apps` (
  `id` BIGINT NOT NULL,
  `app_id` BIGINT NOT NULL,
  `scope_type` TINYINT COMMENT '范围，1-园区，4-公司',
  `scope_id` BIGINT COMMENT '范围对象id',
  `order` INTEGER,
  `visible_flag` TINYINT COMMENT '可见性，0:不可见，1:可见',
  `scene_type` TINYINT COMMENT '1:管理端，2：用户端',
  PRIMARY KEY (`id`),
  KEY `u_eh_recommend_app_scope_id` (`scope_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='工作台公司推荐应用';

CREATE TABLE `eh_service_module_app_entry_profiles` (
  `id` BIGINT NOT NULL,
  `origin_id` BIGINT NOT NULL ,
  `entry_id` BIGINT NOT NULL COMMENT '入口ID',
  `entry_category` TINYINT COMMENT '入口类型,1:移动端工作台,2：移动端服务广场,3:PC端运营后台,4:PC端工作台,5:PC门户',
  `entry_name` VARCHAR(128) COMMENT '入口名称',
  `entry_uri` VARCHAR(1024) COMMENT '入口ICON uri',
  `app_entry_setting` TINYINT COMMENT '入口自定义配置开启，0:不开启，1:开启',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='应用自定义配置应用入口信息';