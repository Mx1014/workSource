-- AUTHOR: tangcen
-- REMARK: 增加楼宇资产管理统计字段
ALTER TABLE eh_property_statistic_community ADD COLUMN signedup_apartment_count int(11) DEFAULT 0 COMMENT '园区下的待签约房源数' AFTER saled_apartment_count;
ALTER TABLE eh_property_statistic_community ADD COLUMN waitingroom_apartment_count int(11) DEFAULT 0 COMMENT '园区下的待接房房源数' AFTER signedup_apartment_count;
ALTER TABLE eh_property_statistic_building ADD COLUMN signedup_apartment_count int(11) DEFAULT 0 COMMENT '楼宇内的待签约房源数' AFTER saled_apartment_count;
ALTER TABLE eh_property_statistic_building ADD COLUMN waitingroom_apartment_count int(11) DEFAULT 0 COMMENT '楼宇内的待接房房源数' AFTER signedup_apartment_count;
