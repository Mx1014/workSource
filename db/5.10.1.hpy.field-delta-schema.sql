CREATE TABLE `eh_var_field_scope_filters` (
  `id` bigint(20) NOT NULL,
  `namespace_id` int(11) DEFAULT NULL,
  `field_id` bigint(20) DEFAULT NULL COMMENT '被筛选的表单id',
  `community_id` bigint(20) DEFAULT NULL COMMENT '被筛选的表单所在的园区id',
  `user_id` bigint(20) DEFAULT NULL COMMENT '被筛选的表单所属的用户id',
  `create_time` datetime DEFAULT NULL,
  `create_uid` bigint(20) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;