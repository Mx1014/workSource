-- 增加咨询电话 by st.zheng
ALTER TABLE `eh_news` ADD COLUMN `phone` BIGINT(20) NULL DEFAULT '0' AFTER `source_url`;

-- 新建标签表 by st.zheng
CREATE TABLE `eh_news_tag` (
  `id` bigint(20) NOT NULL,
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `owner_type` varchar(32) DEFAULT NULL,
  `owner_id` bigint(20) DEFAULT '0',
  `parent_id` bigint(20) DEFAULT '0',
  `name` varchar(32) DEFAULT NULL,
  `is_search` tinyint(8) DEFAULT '0' COMMENT '是否开启筛选',
  `is_default` tinyint(8) DEFAULT '0' COMMENT '是否是默认选项',
  `delete_flag` tinyint(8) NOT NULL DEFAULT '0',
  `default_order` bigint(20) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_news_tag_vals` (
  `id` BIGINT(20) NOT NULL,
  `news_id` BIGINT(20) NOT NULL DEFAULT '0',
  `news_tag_id` BIGINT(20) NOT NULL DEFAULT '0',
  `name` VARCHAR(45) NULL DEFAULT NULL,
  `value` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`id`));
