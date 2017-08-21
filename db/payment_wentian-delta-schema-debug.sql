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

INSERT INTO `eh_payment_bill_groups` VALUES ('1', '999985', '240111044331055035', 'community', '租金', '2', '5', '238656', '2017-02-01 11:48:03', null, null, '1');
INSERT INTO `eh_payment_bill_groups` VALUES ('2', '999985', '240111044331055035', 'community', '物业费', '2', '5', '268448', '2017-02-01 11:49:25', null, null, '2');
INSERT INTO `eh_payment_bill_groups_rules` VALUES ('1', '999985', '1', '1', '1', '华润租金1', '{\"1\":\"80\",\"2\":\"0\"}', 'community', '240111044331055036');
INSERT INTO `eh_payment_bill_groups_rules` VALUES ('2', '999985', '2', '2', '2', '华润水费3', '{price:1000}', 'community', '240111044331055036');
INSERT INTO `eh_payment_bill_groups_rules` VALUES ('3', '999985', '2', '3', '3', '华润电费2', '{price:1000}', 'community', '240111044331055036');
INSERT INTO `eh_payment_bill_items` VALUES ('1', '999985', '240111044331055035', 'community', '0', '1', '1', '1000.00', '1000.00', '0.00', 'eh_user', '238716', null, null, '201704', '0', '2017-04-30 14:23:24', null, null, '华润租金1', '1');
INSERT INTO `eh_payment_bill_items` VALUES ('2', '999985', '240111044331055035', 'community', '0', '1', '2', '1000.00', '0.00', '1000.00', 'eh_user', '240374', null, null, '201706', '0', '2017-06-30 14:25:13', null, null, '', '0');
INSERT INTO `eh_payment_bill_items` VALUES ('3', '999985', '240111044331055035', 'community', '0', '2', '3', '1000.00', '1000.00', '0.00', 'eh_user', '238716', null, null, '201708', '0', '2017-08-09 14:27:52', null, null, '华润水费3', '1');
INSERT INTO `eh_payment_bill_items` VALUES ('4', '999985', '240111044331055035', 'community', '0', '3', '3', '1000.00', '0.00', '1000.00', 'eh_user', '238716', null, null, '201708', '0', '2017-08-09 14:28:36', null, null, '华润电费2', '0');
INSERT INTO `eh_payment_bill_items` VALUES ('5', '999985', '240111044331055035', 'community', '0', '3', '4', '500.00', '500.00', '0.00', 'eh_user', '238716', null, null, '201709', '0', '2017-09-12 12:00:00', null, null, '华润电费2', '0');
INSERT INTO `eh_payment_bill_items` VALUES ('100', '999985', '240111044331055035', 'community', '2', '2', '20', '111.00', '0.00', '111.00', null, null, '闫杨', null, '20170118', '238716', '2017-08-18 18:10:25', null, '2017-08-18 18:10:25', '华润水费3', '1');
INSERT INTO `eh_payment_bill_items` VALUES ('101', '999985', '240111044331055035', 'community', '2', '3', '20', '222.00', '0.00', '222.00', null, null, '闫杨', null, '20170118', '238716', '2017-08-18 18:10:25', null, '2017-08-18 18:10:25', '华润电费2', '1');
INSERT INTO `eh_payment_bills` VALUES ('1', '999985', '240111044331055035', 'community', '1', '201704', '0', '1000.00', '1000.00', '0.00', '0.00', '0.00', 'eh_user', '238778', '緋村剣心伪造', '15919770996', '1', '0', '0', null, null, null, null);
INSERT INTO `eh_payment_bills` VALUES ('2', '999985', '240111044331055035', 'community', '1', '201706', '0', '1000.00', '0.00', '1000.00', '0.00', '0.00', 'eh_user', '240374', '王晓贱', '15919770996', '0', '0', '0', null, null, null, null);
INSERT INTO `eh_payment_bills` VALUES ('3', '999985', '240111044331055035', 'community', '2', '201708', '0', '2000.00', '1000.00', '1000.00', '0.00', '0.00', 'eh_user', '238716', '南宫', '15919770996', '0', '0', '1', null, null, null, null);
INSERT INTO `eh_payment_bills` VALUES ('4', '999985', '240111044331055035', 'community', '2', '201709', '0', '500.00', '500.00', '0.00', '0.00', '0.00', 'eh_user', '238716', '南宫', '15919770996', '1', '1', '1', null, null, null, null);
INSERT INTO `eh_payment_bills` VALUES ('5', '999985', '240111044331055035', 'community', '1', '201706', '0', '12000.00', '12000.00', '0.00', '0.00', '0.00', 'eh_user', '238716', '南宫', '15919770996', '1', '0', '0', null, null, null, null);
INSERT INTO `eh_payment_bills` VALUES ('20', '999985', '240111044331055035', 'community', '2', '20170118', '0', '777.00', '0.00', '777.00', '-777.00', '1221.00', null, '0', null, '18566662605', '0', '0', '1', null, null, null, null);
INSERT INTO `eh_payment_charging_item_scopes` VALUES ('1', '1', '999985', '240111044331055035', 'community');
INSERT INTO `eh_payment_charging_item_scopes` VALUES ('2', '2', '999985', '240111044331055035', 'community');
INSERT INTO `eh_payment_charging_item_scopes` VALUES ('3', '3', '999985', '240111044331055035', 'community');
INSERT INTO `eh_payment_charging_items` VALUES ('1', '租金', '238656', '2017-02-01 11:48:03', null, null, '1');
INSERT INTO `eh_payment_charging_items` VALUES ('2', '水费', '238656', '2017-02-01 11:48:03', null, null, '2');
INSERT INTO `eh_payment_charging_items` VALUES ('3', '电费', '238656', '2017-02-01 11:48:03', null, null, '3');
INSERT INTO `eh_payment_charging_standards` VALUES ('1', '租金1', '1', '计费面积*月单价', '2', '2', '2', '238656', '2017-02-01 11:48:03', null, null);
INSERT INTO `eh_payment_charging_standards` VALUES ('2', '水费1', '2', '固定金额', '1', '2', '2', '238656', '2017-02-01 11:48:03', null, null);
INSERT INTO `eh_payment_charging_standards` VALUES ('3', '电费1', '3', '固定金额', '1', '2', '2', '238656', '2017-02-01 11:48:03', null, null);
INSERT INTO `eh_payment_charging_standards_scopes` VALUES ('1', '1', 'community', '240111044331055035', '238656', '2017-02-01 11:48:03', null, null);
INSERT INTO `eh_payment_charging_standards_scopes` VALUES ('2', '2', 'community', '240111044331055035', '238656', '2017-02-01 11:48:03', null, null);
INSERT INTO `eh_payment_charging_standards_scopes` VALUES ('3', '3', 'community', '240111044331055035', '238656', '2017-02-01 11:48:03', null, null);
INSERT INTO `eh_payment_exemption_items` VALUES ('100', '20', '2', null, null, '闫杨', '增收项的描述555', '555.00', '238716', '2017-08-18 18:10:25', null, '2017-08-18 18:10:25');
INSERT INTO `eh_payment_exemption_items` VALUES ('101', '20', '2', null, null, '闫杨', '增收项的描述666', '666.00', '238716', '2017-08-18 18:10:25', null, '2017-08-18 18:10:25');
INSERT INTO `eh_payment_exemption_items` VALUES ('102', '20', '2', null, null, '闫杨', '减免项的描述333', '-333.00', '238716', '2017-08-18 18:10:25', null, '2017-08-18 18:10:25');
INSERT INTO `eh_payment_exemption_items` VALUES ('103', '20', '2', null, null, '闫杨', '减免项的描述444', '-444.00', '238716', '2017-08-18 18:10:25', null, '2017-08-18 18:10:25');
INSERT INTO `eh_payment_variables` VALUES ('1', '1', '1', '月单价', null, null, null, null);
INSERT INTO `eh_payment_variables` VALUES ('2', '1', '1', '计费面积', null, null, null, null);
