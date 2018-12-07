-- 资源预约3.8.3
-- by st.zheng
ALTER TABLE `eh_rentalv2_resource_types`
ADD COLUMN `detail_page_type`  tinyint(4) NULL COMMENT '资源详情样式 0:默认样式 1:时间轴样式' AFTER `page_type`;