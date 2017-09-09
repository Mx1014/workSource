/*
Navicat MySQL Data Transfer

Source Server         : myVM_MySQL
Source Server Version : 50626
Source Host           : 10.1.10.39:3306
Source Database       : ehcore

Target Server Type    : MYSQL
Target Server Version : 50626
File Encoding         : 65001

Date: 2017-08-21 16:55:51
*/


-- 给华润配任务管理
-- SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
-- INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
-- VALUES ((@menu_scope_id := @menu_scope_id + 1), 70000, '', 'EhNamespaces', 999985, 2);
-- INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
-- VALUES ((@menu_scope_id := @menu_scope_id + 1), 70100, '', 'EhNamespaces', 999985, 2);