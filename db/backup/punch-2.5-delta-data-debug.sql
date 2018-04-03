 
-- 打卡/审批状态的文字描述，add by wh, 20170411
SET @id =(SELECT MAX(id) FROM eh_locale_strings); 
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'punch.status','0','zh_CN','正常');
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'punch.status','1','zh_CN','迟到');
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'punch.status','2','zh_CN','早退');
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'punch.status','3','zh_CN','缺勤');
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'punch.status','4','zh_CN','迟到且早退');
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'punch.status','5','zh_CN','事假');
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'punch.status','6','zh_CN','病假');
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'punch.status','7','zh_CN','调休');
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'punch.status','8','zh_CN','公出');
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'punch.status','9','zh_CN','加班');
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'punch.status','10','zh_CN','半天事假'); 
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'punch.status','11','zh_CN','半天病假'); 
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'punch.status','12','zh_CN','半天调休'); 
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'punch.status','13','zh_CN','半天公出'); 
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'punch.status','14','zh_CN','忘打卡'); 
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'punch.status','15','zh_CN','已离职'); 
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'punch.status','16','zh_CN','未入职'); 
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'punch.excel','schedule','zh_CN','排班表'); 
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'punch.excel','rule','zh_CN','排班');  
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'punch.push','1','zh_CN','考勤提醒：还有5分钟就要上班了，别忘记打卡哦');   
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'punch.default','timeRuleName','zh_CN','休息');  
 

-- 考勤排班的菜单
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`) VALUES('50634','查看排班表','50630',NULL,'punch--viewScheduling','0','2','/50000/50600/50630/50634','park',556,NULL);

SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 50634, '', 'EhNamespaces', 1000000, 2); 
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 50634, '', 'EhNamespaces', 999992, 2);



SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 10039, 50634, '考勤管理', 1, 1, '考勤管理 全部权限', 710);


