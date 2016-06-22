#
# member of global parition
# shared among namespaces, no application module specific information
#
DROP TABLE IF EXISTS `eh_business_assigned_namespaces`;
CREATE TABLE `eh_business_assigned_namespaces` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `owner_id` bigint(20) NOT NULL COMMENT 'owner business id',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `visible_flag` tinyint(4) NOT NULL DEFAULT '1' COMMENT 'business can see in namespace or not.0-hide,1-visible',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
	UNIQUE KEY `u_eh_business_namespace_id` (`owner_id`,`namespace_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;