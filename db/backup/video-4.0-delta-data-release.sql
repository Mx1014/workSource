-- 视频会议短信模板，add by wh, 20170615
SET @id =(SELECT MAX(id) FROM eh_locale_templates);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','51','zh_CN','视频会-左邻','53378','0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','52','zh_CN','视测会-左邻','53385','0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','51','zh_CN','视频会-科技园','53386','1000000');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','52','zh_CN','视测会-科技园','53387','1000000');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','51','zh_CN','视频会-海岸','53389','999993');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','52','zh_CN','视测会-海岸','53390','999993');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','51','zh_CN','视频会-深业','53391','999992');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','52','zh_CN','视测会-深业','53393','999992');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','51','zh_CN','视频会-威新','53394','999991');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','52','zh_CN','视测会-威新','53395','999991');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','51','zh_CN','视频会-储能','53396','999990');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','52','zh_CN','视测会-储能','53397','999990');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','51','zh_CN','视频会-ibase','53398','999989');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','52','zh_CN','视测会-ibase','53399','999989');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','51','zh_CN','视频会-爱特家','53400','999988');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','52','zh_CN','视测会-爱特家','53403','999988');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','51','zh_CN','视频会-深圳湾','','999987');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','52','zh_CN','视测会-深圳湾','','999987');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','51','zh_CN','视频会-创源','53777','999986');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','52','zh_CN','视测会-创源','53778','999986');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','51','zh_CN','视频会-华润','53779','999985');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','52','zh_CN','视测会-华润','53781','999985');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','51','zh_CN','视频会-T空间','53783','999982');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','52','zh_CN','视测会-T空间','53784','999982');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','51','zh_CN','视频会-正中会','53785','999983');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','52','zh_CN','视测会-正中会','53786','999983');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','51','zh_CN','视频会-星商汇','53787','999981');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','52','zh_CN','视测会-星商汇','53788','999981');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','51','zh_CN','视频会-全至100','53801','999980');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','52','zh_CN','视测会-全至100','53802','999980');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','51','zh_CN','视频会-光大','53803','999979');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','52','zh_CN','视测会-光大','53804','999979');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','51','zh_CN','视频会-Volgo','53805','1');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','52','zh_CN','视测会-Volgo','53806','1');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','51','zh_CN','视频会-名网邦','53809','999976');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','52','zh_CN','视测会-名网邦','53811','999976');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','51','zh_CN','视频会-荣超','53814','999975');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','52','zh_CN','视测会-荣超','53815','999975');


SET @id := (SELECT MAX(id) FROM `eh_configurations`);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES(@id:=@id+1,'video.help','https://videomeeting.kf5.com/hc/','video help doc','0',NULL);



