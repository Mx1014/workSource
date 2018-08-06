-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境
-- AUTHOR: 马世亨  20180806
-- REMARK: 物品放行任务状态迁移
update eh_relocation_requests set status = 0 where status = 3;
-- --------------------- SECTION END ---------------------------------------------------------