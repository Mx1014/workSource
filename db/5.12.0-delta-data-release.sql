-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: OPERATION
-- DESCRIPTION: 此SECTION放升级相关的操作要求，如调接口、查询数据确认、修改配置文件、更新特殊程序等
-- AUTHOR:
-- REMARK:
-- --------------------- SECTION END OPERATION------------------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境

-- AUTHOR: 李清岩
-- REMARK：访客来访提示
INSERT INTO eh_locale_templates ( `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ('aclink.notification', '2', 'zh_CN', '访客使用了临时授权二维码进入门禁A。', '访客${visitorName}使用了临时授权二维码进入${doorName}。', '0');

-- --------------------- SECTION END ALL -----------------------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: zuolin-base
-- DESCRIPTION: 此SECTION只在左邻基线（非独立署部）执行的脚本
-- AUTHOR:2018年12月13日 黄鹏宇
-- REMARK:关联缺陷#40159 修复中天的数据并且根据任务43743的代码更改数据库
update eh_enterprise_customers set source_item_id = 14788 where source_item_id = 14559 and community_id = 240111044332061061;
update eh_enterprise_customers set source_item_id = 14787 where source_item_id = 14558 and community_id = 240111044332061061;
update eh_enterprise_customers set source_item_id = 14786 where source_item_id = 14557 and community_id = 240111044332061061;
update eh_enterprise_customers set source_item_id = 14789 where source_item_id = 14560 and community_id = 240111044332061061;

update eh_enterprise_customers set source_item_id = 14788 where source_item_id = 14784 and community_id = 240111044332061061;
update eh_enterprise_customers set source_item_id = 14698 where source_item_id = 14744 and community_id = 240111044332063624;

update eh_var_field_item_scopes set status = 0 where module_name = 'investment_promotion'and field_id = 6 and community_id in (240111044332061061 ,240111044332061062 ,240111044332061063 ,240111044332061064 ,240111044332063624 ,240111044332063625 ,240111044332063626 );
update eh_var_field_item_scopes set module_name = 'enterprise_customer' where module_name = 'investment_promotion' and field_id = 6 and community_id is null and namespace_id = 999944;

update eh_var_field_item_scopes set module_name = 'enterprise_customer' where module_name = 'investment_promotion' and field_id = 24 and namespace_id = 999944;

update eh_var_field_item_scopes set status = 0 where namespace_id in (0,999954) and status = 2 and module_name = 'investment_promotion' and field_id = 24;
update eh_var_field_item_scopes set module_name = 'enterprise_customer' where namespace_id= 0 and status = 2 and module_name = 'investment_promotion' and field_id = 6;

update eh_var_field_item_scopes set status = 0 where status = 2 and module_name = 'enterprise_customer' and field_id = 5;

update eh_var_field_item_scopes set module_name = 'enterprise_customer' where status = 2 and module_name = 'investment_promotion' and field_id = 5;

update eh_var_field_item_scopes set module_name = 'enterprise_customer' where module_name = 'investment_promotion' and field_id = 12113 and status = 2 ;

-- --------------------- SECTION END zuolin-base ---------------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: dev
-- DESCRIPTION: 此SECTION只在开发库中执行的脚本
-- AUTHOR:
-- REMARK:
-- --------------------- SECTION END dev -----------------------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: zijing
-- DESCRIPTION: 此SECTION只在清华信息港(紫荆)-999984执行的脚本
-- AUTHOR:
-- REMARK:
-- --------------------- SECTION END ---------------------------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: guangda
-- DESCRIPTION: 此SECTION只在光大-999979执行的脚本
-- AUTHOR:
-- REMARK:
-- --------------------- SECTION END guangda -------------------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: szbay
-- DESCRIPTION: 此SECTION只在深圳湾-999966执行的脚本
-- AUTHOR:
-- REMARK:
-- --------------------- SECTION END szbay ---------------------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: chuangyechang
-- DESCRIPTION: 此SECTION只在成都创业场-999964执行的脚本
-- AUTHOR:
-- REMARK:
-- --------------------- SECTION END chuangyechang -------------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: anbang
-- DESCRIPTION: 此SECTION只在安邦物业-999949执行的脚本
-- AUTHOR:
-- REMARK:
-- --------------------- SECTION END anbang---------------------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: nanshanquzhengfu
-- DESCRIPTION: 此SECTION只在南山区政府-999931执行的脚本
-- --------------------- SECTION END nanshanquzhengfu ----------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: guanzhouyuekongjian
-- DESCRIPTION: 此SECTION只在广州越空间-999930执行的脚本
-- --------------------- SECTION END guanzhouyuekongjian -------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ruianxintiandi
-- DESCRIPTION: 此SECTION只在上海瑞安新天地-999929执行的脚本
-- --------------------- SECTION END ruianxintiandi ------------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: wanzhihui
-- DESCRIPTION: 此SECTION只在万智汇-999953执行的脚本
-- --------------------- SECTION END wanzhihui ------------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: jinmao
-- DESCRIPTION: 此SECTION只在上海金茂-智臻生活 -999925执行的脚本
-- --------------------- SECTION END jinmao ------------------------------------------