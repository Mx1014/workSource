 -- add by dengs 20170511 添加服务联盟描述信息折叠高度
ALTER TABLE `eh_service_alliances` ADD COLUMN `description_height` INT NULL DEFAULT '2' COMMENT '0:not collapse , N: collapse N lines';
