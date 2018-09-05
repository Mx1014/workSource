alter table eh_contracts add sponsor_uid BIGINT COMMENT '发起人id';
alter table eh_contracts add sponsor_time DATETIME COMMENT '发起时间';

alter table eh_addresses add apartment_rent DECIMAL(10,2) DEFAULT NULL COMMENT '房源租金（元）';
alter table eh_addresses add apartment_rent_type tinyint(4) DEFAULT NULL COMMENT '房源租金类型（1:每天(总面积); 2:每月(总面积); 3:每个季度(总面积); 4:每年(总面积); 5:每天(每平,收费面积); 6:每月(每平,收费面积); 7:每个季度(每平,收费面积); 8:每年(每平,收费面积))';
