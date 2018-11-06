-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境
-- AUTHOR: 吴寒
-- REMARK: 会议管理V1.2
UPDATE eh_configurations SET VALUE = 5000 WHERE NAME ='meeting.record.word.limit';
-- --------------------- SECTION END ---------------------------------------------------------