CREATE TABLE `eh_me_web_menus` (
  `id` bigint(20) NOT NULL,
  `namespace_id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `action_path` varchar(255) NOT NULL,
  `action_data` varchar(255) DEFAULT NULL,
  `icon_uri` varchar(255) DEFAULT NULL,
  `status` tinyint(4) DEFAULT '2' COMMENT '0: inactive, 2: active',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;