-- Designer: zhiwei zhang
-- Description: ISSUE#24392 固定资产管理V1.0（支持对内部各类固定资产进行日常维护）


INSERT INTO eh_locale_strings(scope,code,locale,text)
SELECT r.scope,r.code,r.locale,r.text FROM(
SELECT 'fixedAsset' AS scope,10000 AS code,'zh_CN' AS locale,'分类名称已存在' AS text UNION ALL
SELECT 'fixedAsset' AS scope,10001 AS code,'zh_CN' AS locale,'存在资产属于此分类，请清空后再删除。' AS text UNION ALL
SELECT 'fixedAsset' AS scope,10002 AS code,'zh_CN' AS locale,'资产编号已经存在' AS text UNION ALL
SELECT 'fixedAsset' AS scope,10003 AS code,'zh_CN' AS locale,'父分类不存在' AS text UNION ALL
SELECT 'fixedAsset' AS scope,10004 AS code,'zh_CN' AS locale,'上传了空文件' AS text UNION ALL
SELECT 'fixedAsset' AS scope,10005 AS code,'zh_CN' AS locale,'没有资产分类，请先创建资产分类' AS text UNION ALL

SELECT 'fixedAsset' AS scope,100000 AS code,'zh_CN' AS locale,'保存资产时出错' AS text UNION ALL
SELECT 'fixedAsset' AS scope,100001 AS code,'zh_CN' AS locale,'请输入编号' AS text UNION ALL
SELECT 'fixedAsset' AS scope,100002 AS code,'zh_CN' AS locale,'编号：请输入不多于20字' AS text UNION ALL
SELECT 'fixedAsset' AS scope,100003 AS code,'zh_CN' AS locale,'分类：系统不存在此分类' AS text UNION ALL
SELECT 'fixedAsset' AS scope,100004 AS code,'zh_CN' AS locale,'请输入名称' AS text UNION ALL
SELECT 'fixedAsset' AS scope,100005 AS code,'zh_CN' AS locale,'名称：请输入不多于20字' AS text UNION ALL
SELECT 'fixedAsset' AS scope,100006 AS code,'zh_CN' AS locale,'规格：请输入不多于50字' AS text UNION ALL
SELECT 'fixedAsset' AS scope,100007 AS code,'zh_CN' AS locale,'单价：请输入数字' AS text UNION ALL
SELECT 'fixedAsset' AS scope,100008 AS code,'zh_CN' AS locale,'单价：最大支持999,999,999.99' AS text UNION ALL
SELECT 'fixedAsset' AS scope,100009 AS code,'zh_CN' AS locale,'购买时间：格式错误，请输入xxxx-xx-xx' AS text UNION ALL
SELECT 'fixedAsset' AS scope,100010 AS code,'zh_CN' AS locale,'所属供应商：请输入不多于50字' AS text UNION ALL
SELECT 'fixedAsset' AS scope,100013 AS code,'zh_CN' AS locale,'其他：请输入不多于200字' AS text UNION ALL
SELECT 'fixedAsset' AS scope,100014 AS code,'zh_CN' AS locale,'请输入状态' AS text UNION ALL
SELECT 'fixedAsset' AS scope,100021 AS code,'zh_CN' AS locale,'状态为使用中的领用时间必填，使用部门、使用人至少填写一项' AS text UNION ALL
SELECT 'fixedAsset' AS scope,100016 AS code,'zh_CN' AS locale,'部门不存在或格式错误:上下级部门间用"/"隔开，且从最上级部门开始，例如"左邻/深圳研发中心/研发部"' AS text UNION ALL
SELECT 'fixedAsset' AS scope,100017 AS code,'zh_CN' AS locale,'使用人不存在或关联（使用部门）下不存在（使用人）' AS text UNION ALL
SELECT 'fixedAsset' AS scope,100018 AS code,'zh_CN' AS locale,'领用时间：格式错误，请输入xxxx-xx-xx' AS text UNION ALL
SELECT 'fixedAsset' AS scope,100022 AS code,'zh_CN' AS locale,'使用部门、使用人至少填写一项' AS text UNION ALL
SELECT 'fixedAsset' AS scope,100019 AS code,'zh_CN' AS locale,'存放地点：请输入不多于50字' AS text UNION ALL
SELECT 'fixedAsset' AS scope,100020 AS code,'zh_CN' AS locale,'备注信息：请输入不多于200字' AS text UNION ALL
SELECT 'fixedAsset' AS scope,100023 AS code,'zh_CN' AS locale,'分类：请选择分类' AS text UNION ALL
SELECT 'fixedAsset' AS scope,100024 AS code,'zh_CN' AS locale,'请输入来源' AS text UNION ALL
SELECT 'fixedAsset' AS scope,100025 AS code,'zh_CN' AS locale,'状态为使用中时，使用部门、领用时间必填' AS text UNION ALL
SELECT 'fixedAsset' AS scope,100026 AS code,'zh_CN' AS locale,'使用人填写时，使用部门、领用时间必填' AS text UNION ALL
SELECT 'fixedAsset' AS scope,100027 AS code,'zh_CN' AS locale,'使用部门、领用时间需要同时填写' AS text UNION ALL
SELECT 'fixedAsset' AS scope,100028 AS code,'zh_CN' AS locale,'请输入正确的领用时间' AS text UNION ALL
SELECT 'fixedAsset' AS scope,100029 AS code,'zh_CN' AS locale,'请输入正确的购买时间' AS text
)r LEFT JOIN eh_locale_strings s ON r.scope=s.scope AND r.code=s.code AND r.locale=s.locale
WHERE s.id IS NULL;


INSERT INTO eh_locale_templates(scope,code,locale,description,text,namespace_id)
SELECT r.scope,r.code,r.locale,r.description,r.text,0
FROM(
SELECT 'fixedAsset' AS scope,100012 AS code,'zh_CN' AS locale,'来源仅支持：购入、自建、租赁、捐赠、其他' AS description,'来源仅支持：${addFromList}' AS text UNION ALL
SELECT 'fixedAsset' AS scope,100015 AS code,'zh_CN' AS locale,'状态仅支持：闲置、使用中、维修中、已出售、已报废、遗失' AS description,'状态仅支持：${statusList}' AS text
)r LEFT JOIN eh_locale_templates t ON r.scope=t.scope AND r.code=t.code AND r.locale=t.locale
WHERE t.id IS NULL;


INSERT INTO eh_fixed_asset_default_categories(id,name,parent_id,path,default_order,creator_uid,create_time)
SELECT r.id,r.name,r.parent_id,r.path,r.default_order,0,NOW() FROM(
SELECT 1 AS id,'土地、房屋及构筑物' AS name,0 AS parent_id,'/1' AS path,1 AS default_order UNION ALL
SELECT 2 AS id,'通用设备' AS name,0 AS parent_id,'/2' AS path,2 AS default_order UNION ALL
SELECT 3 AS id,'专用设备' AS name,0 AS parent_id,'/3' AS path,3 AS default_order UNION ALL
SELECT 4 AS id,'交通运输设备' AS name,0 AS parent_id,'/4' AS path,4 AS default_order UNION ALL
SELECT 5 AS id,'电器设备' AS name,0 AS parent_id,'/5' AS path,5 AS default_order UNION ALL
SELECT 6 AS id,'电子产品及通信设备' AS name,0 AS parent_id,'/6' AS path,6 AS default_order UNION ALL
SELECT 7 AS id,'仪器仪表、计量标准器具及量具、衡器' AS name,0 AS parent_id,'/7' AS path,7 AS default_order UNION ALL
SELECT 8 AS id,'文艺体育设备' AS name,0 AS parent_id,'/8' AS path,8 AS default_order UNION ALL
SELECT 9 AS id,'图书文物及陈列品' AS name,0 AS parent_id,'/9' AS path,9 AS default_order UNION ALL
SELECT 10 AS id,'家具用具及其他类' AS name,0 AS parent_id,'/10' AS path,10 AS default_order
)r LEFT JOIN eh_fixed_asset_default_categories t ON r.id=t.id
WHERE t.id IS NULL;


INSERT INTO eh_service_modules(id,name,parent_id,path,type,level,STATUS,create_time,creator_uid,operator_uid,action_type,multiple_flag,module_control_type,default_order)
VALUE(59000,'固定资产管理',50000,'/50000/59000',1,2,2,NOW(),0,0,72,0,'org_control',0);

-- 增加固定资产模块与域空间的关联信息
SET @id = (SELECT MAX(id) FROM eh_service_module_scopes);
INSERT INTO eh_service_module_scopes(id,namespace_id,module_id,module_name,owner_type,owner_id,apply_policy)
VALUE(@id+1,1,59000,'固定资产管理','EhNamespaces',1,2);

-- 新增菜单配置
INSERT INTO eh_web_menus(id,name,parent_id,data_type,leaf_flag,status,path,type,sort_num,module_id,level,condition_type,category)
SELECT r.id,r.name,r.parent_id,r.data_type,r.leaf_flag,r.status,r.path,r.type,r.sort_num,r.module_id,r.level,'system','module' FROM(
SELECT 16041800 AS id,'固定资产管理' AS name,16040000 AS parent_id,'permanent-asset' AS data_type,1 AS leaf_flag,2 AS status,'/16000000/16040000/16041800' AS path,'zuolin' AS type,18 AS sort_num,59000 AS module_id,3 AS level UNION ALL
SELECT 48180000 AS id,'固定资产管理' AS name,48000000 AS parent_id,'permanent-asset' AS data_type,1 AS leaf_flag,2 AS status,'/48000000/48180000' AS path,'park' AS type,18 AS sort_num,59000 AS module_id,2 AS level UNION ALL
SELECT 72180000 AS id,'固定资产管理' AS name,72000000 AS parent_id,'permanent-asset' AS data_type,1 AS leaf_flag,2 AS status,'/72000000/72180000' AS path,'organization' AS type,18 AS sort_num,59000 AS module_id,2 AS level
)r LEFT JOIN eh_web_menus m ON r.id=m.id
WHERE m.id IS NULL;

-- End by: zhiwei zhang