 
-- AUTHOR: 吴寒
-- REMARK: issue-33943 日程提醒1.2
ALTER TABLE eh_reminds ADD COLUMN plan_time BIGINT DEFAULT VALUE 32400000 COMMENT '计划开始时间-默认早上9点(历史数据)';
ALTER TABLE eh_remind_settings ADD COLUMN app_version VARCHAR(32) DEFAULT VALUE '5.8.0' COMMENT '对应app版本(历史数据5.8.0),根据APP版本选择性展示';