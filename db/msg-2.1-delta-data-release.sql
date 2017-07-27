-- 二维码页面  add by yanjun 20170727
SET @id = (SELECT MAX(id) FROM eh_configurations);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES( @id:= @id + 1,'group.scanJoin.url','/mobile/static/message/src/addGroup.html','group.scanJoin.url','0',NULL);

INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES( @id:= @id + 1,'group.scanDownload.url','/mobile/static/downloadLink/src/downLink.html','group.scanDownload.url','0',NULL);
