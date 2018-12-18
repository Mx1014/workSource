-- 资源预约3.8.3
-- by st.zheng
ALTER TABLE `eh_rentalv2_resource_types`
ADD COLUMN `detail_page_type`  tinyint(4) NULL COMMENT '资源详情样式 0:默认样式 1:时间轴样式' AFTER `page_type`;
ALTER TABLE `eh_rentalv2_default_rules`
ADD COLUMN `preview_using_image_uri`  varchar(255) NULL COMMENT '资源概览 使用中图片' AFTER `file_flag`;
ALTER TABLE `eh_rentalv2_default_rules`
ADD COLUMN `preview_idle_image_uri`  varchar(255) NULL COMMENT '资源概览 空闲图片' AFTER `preview_using_image_uri`;
ALTER TABLE `eh_rentalv2_default_rules`
ADD COLUMN `shop_name`  varchar(255) NULL COMMENT '关联商铺名' AFTER `preview_idle_image_uri`;
ALTER TABLE `eh_rentalv2_default_rules`
ADD COLUMN `shop_no`  varchar(255) NULL COMMENT '关联商铺号' AFTER `shop_name`;
ALTER TABLE `eh_rentalv2_default_rules`
ADD COLUMN `shop_uRL`  varchar(255) NULL COMMENT '关联商铺地址' AFTER `shop_no`;
