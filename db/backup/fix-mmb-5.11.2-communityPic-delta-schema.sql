-- AUTHOR: 莫谋斌 20181213
-- REMARK: 数据库修改为保存uri
ALTER TABLE eh_communities  CHANGE background_img_url background_img_uri varchar(500) DEFAULT '' COMMENT '小区或园区项目的图片链接';
