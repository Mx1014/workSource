-- AUTHOR: 梁燕龙 20181115
-- 工作台公司推荐应用
CREATE TABLE `eh_work_platform_apps` (
  `id` BIGINT NOT NULL,
  `app_id` BIGINT NOT NULL,
  `scope_type` TINYINT COMMENT '范围，1-园区，4-公司',
  `scope_id` BIGINT COMMENT '范围对象id',
  `order` INTEGER,
  `visible_flag` TINYINT COMMENT '可见性，0:不可见，1:可见',
  `scene_type` INTEGER COMMENT '1:管理端，2：用户端',
  PRIMARY KEY (`id`),
  KEY `u_eh_recommend_app_scope_id` (`scope_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='工作台公司推荐应用';