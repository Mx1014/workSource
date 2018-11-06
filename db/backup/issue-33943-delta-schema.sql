 
-- AUTHOR: 吴寒
-- REMARK: issue-33943 日程提醒1.2
ALTER TABLE eh_remind_settings ADD COLUMN app_version VARCHAR(32) DEFAULT '5.8.0' COMMENT '对应app版本(历史数据5.8.0),根据APP版本选择性展示';
ALTER TABLE eh_remind_settings ADD COLUMN before_time BIGINT COMMENT '提前多少时间(毫秒数)不超过1天的部分在这里减';