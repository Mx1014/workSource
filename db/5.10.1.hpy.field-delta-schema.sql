CREATE TABLE `eh_var_field_scope_filters` (
  `id` bigint NOT NULL,
  `namespace_id` int,
  `community_id` bigint COMMENT '被筛选的表单所在的园区id',
  `module_name` varchar(32) COMMENT '被筛选的表单的moduleName',
  `group_path` varchar(32) COMMENT '被筛选的表单的group',
  `field_id` bigint COMMENT '被筛选的表单id',
  `user_id` bigint COMMENT '被筛选的表单所属的用户id',
  `create_time` datetime,
  `create_uid` bigint,
  `status` tinyint,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '筛选显示的表单';