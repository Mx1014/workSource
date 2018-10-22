-- AUTHOR: 杨崇鑫   20181017
-- REMARK: 缴费管理V7.0（新增缴费相关统计报表） 
-- REMARK: 增加项目-时间段（月份）统计结果集表
CREATE TABLE `eh_payment_bill_statistic_community` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER,
  `owner_id` BIGINT,
  `owner_type` VARCHAR(64),  
  `date_str` VARCHAR(10),  
  `amount_receivable` DECIMAL(10,2) COMMENT '应收（含税)',
  `amount_receivable_without_tax` DECIMAL(10,2) COMMENT '应收（不含税）',
  `tax_amount` DECIMAL(10,2) COMMENT '税额',
  `amount_received` DECIMAL(10,2) COMMENT '已收（含税）',
  `amount_received_without_tax` DECIMAL(10,2) COMMENT '已收（不含税）',
  `amount_owed` DECIMAL(10,2) COMMENT '待收（含税）',
  `amount_owed_without_tax` DECIMAL(10,2)  COMMENT '待收（不含税）',
  `amount_exemption` DECIMAL(10,2) COMMENT 'amount reduced',
  `amount_supplement` DECIMAL(10,2) COMMENT 'amount increased',  
  `due_day_count` DECIMAL(10,2) COMMENT '总欠费天数', 
  `notice_times` DECIMAL(10,2) COMMENT '总催缴次数',
  `collection_rate` DECIMAL(10,2) COMMENT '收缴率=已收金额/应收含税金额*100%',
  `create_time` DATETIME ON UPDATE CURRENT_TIMESTAMP,
  `update_time` DATETIME ON UPDATE CURRENT_TIMESTAMP, 
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='项目-时间段（月份）统计结果集表';

-- AUTHOR: 唐岑   20181021
-- REMARK: 资产管理V3.4（资产统计报表） 
-- REMARK: 项目信息报表结果集（项目-月份） 
CREATE TABLE `eh_property_statistic_community` (
  `id` bigint(20) NOT NULL,
  `namespace_id` int(11),
  `community_id` bigint(20),
  `community_name` varchar(64),
  `date_str` varchar(10) COMMENT '统计月份（格式为xxxx-xx）',
  `building_count` int(11) DEFAULT '0' COMMENT '园区下的楼宇总数',
  `total_apartment_count` int(11) DEFAULT '0' COMMENT '园区下的房源总数',
  `free_apartment_count` int(11) DEFAULT '0' COMMENT '园区下的待租房源数',
  `rent_apartment_count` int(11) DEFAULT '0' COMMENT '园区下的已出租房源数',
  `occupied_apartment_count` int(11) DEFAULT '0' COMMENT '园区下的已占用房源数',
  `living_apartment_count` int(11) DEFAULT '0' COMMENT '园区下的自用房源数',
  `saled_apartment_count` int(11) DEFAULT '0' COMMENT '园区下的已售房源数',
  `area_size` decimal(10,2) DEFAULT '0.00' COMMENT '园区的建筑面积',
  `rent_area` decimal(10,2) DEFAULT '0.00' COMMENT '园区的在租面积',
  `free_area` decimal(10,2) DEFAULT '0.00' COMMENT '园区的可招租面积',
  `rent_rate` decimal(10,2) COMMENT '出租率=在租面积/总的建筑面积*100%',
  `free_rate` decimal(10,2) COMMENT '空置率=可招租面积/总的建筑面积*100% ',
  `status` tinyint(4) DEFAULT '2' COMMENT '该条的记录状态：0-inactive, 1-confirming, 2-active',
  `create_time` datetime ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='项目信息报表结果集（项目-月份）';

-- AUTHOR: 唐岑   20181021
-- REMARK: 资产管理V3.4（资产统计报表） 
-- REMARK: 楼宇信息报表结果集（楼宇-月份） 
CREATE TABLE `eh_property_statistic_building` (
  `id` bigint(20) NOT NULL,
  `namespace_id` int(11),
  `building_id` bigint(20),
  `building_name` varchar(64),
  `date_str` varchar(10) COMMENT '统计月份（格式为xxxx-xx）',
  `total_apartment_count` int(11) DEFAULT '0' COMMENT '楼宇内的房源总数',
  `free_apartment_count` int(11) DEFAULT '0' COMMENT '楼宇内的待租房源数',
  `rent_apartment_count` int(11) DEFAULT '0' COMMENT '楼宇内的已出租房源数',
  `occupied_apartment_count` int(11) DEFAULT '0' COMMENT '楼宇内的已占用房源数',
  `living_apartment_count` int(11) DEFAULT '0' COMMENT '楼宇内的自用房源数',
  `saled_apartment_count` int(11) DEFAULT '0' COMMENT '楼宇内的已售房源数',
  `area_size` decimal(10,2) DEFAULT '0.00' COMMENT '楼宇的建筑面积',
  `rent_area` decimal(10,2) DEFAULT '0.00' COMMENT '楼宇的在租面积',
  `free_area` decimal(10,2) DEFAULT '0.00' COMMENT '楼宇的可招租面积',
  `rent_rate` decimal(10,2) COMMENT '出租率=在租面积/总的建筑面积*100%',
  `free_rate` decimal(10,2) COMMENT '空置率=可招租面积/总的建筑面积*100% ',
  `status` tinyint(4) DEFAULT '2' COMMENT '该条的记录状态：0-inactive, 1-confirming, 2-active',
  `create_time` datetime ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='楼宇信息报表结果集（楼宇-月份）'; 