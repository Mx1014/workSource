#
# 组织架构和角色关系表
#
DROP TABLE IF EXISTS `eh_organization_role_map`;
CREATE TABLE `eh_organization_role_map`(
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `owner_type` VARCHAR(64),
    `owner_id` BIGINT,
    `role_id` BIGINT NOT NULL,
    `private_flag` TINYINT(4) NOT NULL DEFAULT 0 COMMENT '0: public, 1: private',
    `status` TINYINT(4) NOT NULL DEFAULT '2' COMMENT '0: inactive, 2: active',
    `create_time` DATETIME,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;