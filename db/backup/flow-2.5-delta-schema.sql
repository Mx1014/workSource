-- 二维码加路由   add by xq.tian  2017/11/15
ALTER TABLE eh_qrcodes ADD COLUMN `route_uri` VARCHAR(256) COMMENT 'route uri, like zl://xxx/xxx';
ALTER TABLE eh_qrcodes ADD COLUMN `handler` VARCHAR(32) COMMENT 'module handler';
ALTER TABLE eh_qrcodes ADD COLUMN `extra` TEXT COMMENT 'module handler';

ALTER TABLE eh_flow_cases ADD COLUMN `route_uri` VARCHAR(128) COMMENT 'route uri';