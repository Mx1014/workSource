-- add by dengs 20170511 添加服务联盟描述信息是否折叠字段
ALTER TABLE `eh_service_alliances`
	ADD COLUMN `collapse_flag` TINYINT NULL DEFAULT '1' COMMENT 'collapse:1,not collapse:0';