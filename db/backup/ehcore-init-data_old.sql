SET foreign_key_checks = 0;

use ehcore;

#
# System forum
#
INSERT INTO `eh_forums`(`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(1, UUID(), 0, 2, '', 0, '社区论坛', '', 0, 0, UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_forums`(`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(2, UUID(), 0, 2, '', 0, '意见反馈论坛', '', 0, 0, UTC_TIMESTAMP(), UTC_TIMESTAMP());	

#
# Root categories
#
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(10000, NULL, '商务', '商务', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(10001, NULL, '兴趣', '兴趣', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(10002, NULL, '应用', '应用', 0, 2, UTC_TIMESTAMP());

#
# Business categories
#
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(0, 10000, '分享', '商务/分享', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(1, 10000, '餐饮', '商务/餐饮', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(2, 10000, '休闲', '商务/休闲', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(3, 10000, '便利店', '商务/便利店', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(4, 10000, '拼车租车', '商务/拼车租车', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(5, 10000, '配送', '商务/配送', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(7, 10000, '教育', '商务/教育', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(8, 10000, '公共', '商务/公共', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(9, 10000, '物业', '商务/物业', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(10, 10000, '家政', '商务/家政', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(11, 10000, '维修', '商务/维修', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(12, 10000, '二手与换物', '商务/二手与换物', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(13, 10000, '租售', '商务/租售', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(14, 10000, '旅游', '商务/旅游', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(15, 10000, '宠物', '商务/宠物', 0, 2, UTC_TIMESTAMP());

INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(51, 0, '亲子与教育', '商务/分享/亲子与教育', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(52, 0, '运动与音乐', '商务/分享/运动与音乐', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(53, 0, '美食与厨艺', '商务/分享/美食与厨艺', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(54, 0, '美容化妆', '商务/分享/美容化妆', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(55, 0, '家庭装饰', '商务/分享/家庭装饰', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(56, 0, '名牌汇', '商务/分享/名牌汇', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(57, 0, '宠物会', '商务/分享/宠物会', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(58, 0, '旅游与摄影', '商务/分享/旅游与摄影', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(59, 0, '拼车', '商务/分享/拼车', 0, 2, UTC_TIMESTAMP());
    
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(101, 1, '火锅', '商务/餐饮/火锅', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(102, 1, '粤菜', '商务/餐饮/粤菜', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(103, 1, '茶餐厅', '商务/餐饮/茶餐厅', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(104, 1, '自助餐', '商务/餐饮/自助餐', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(105, 1, '川菜', '商务/餐饮/川菜', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(106, 1, '湘菜', '商务/餐饮/湘菜', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(107, 1, '日韩料理', '商务/餐饮/日韩料理', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(108, 1, '西餐', '商务/餐饮/西餐', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(109, 1, '烧烤烤肉', '商务/餐饮/烧烤烤肉', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(110, 1, '江浙菜', '商务/餐饮/江浙菜', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(111, 1, '甜品饮料', '商务/餐饮/甜品饮料', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(112, 1, '小吃快餐', '商务/餐饮/小吃快餐', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(113, 1, '蛋糕', '商务/餐饮/蛋糕', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(114, 1, '海鲜', '商务/餐饮/海鲜', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(115, 1, '东南亚菜', '商务/餐饮/东南亚菜', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(116, 1, '客家菜', '商务/餐饮/客家菜', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(117, 1, '西北菜', '商务/餐饮/西北菜', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(118, 1, '东北菜', '商务/餐饮/东北菜', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(119, 1, '咖啡厅', '商务/餐饮/咖啡厅', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(120, 1, '清真', '商务/餐饮/清真', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(121, 1, '江西菜', '商务/餐饮/江西菜', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(122, 1, '湖北菜', '商务/餐饮/湖北菜', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(123, 1, '素菜', '商务/餐饮/素菜', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(190, 1, '其他', '商务/餐饮/其他', 0, 2, UTC_TIMESTAMP());
    
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(201, 2, 'KTV', '商务/休闲/KTV', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(202, 2, '足浴', '商务/休闲/足浴', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(203, 2, '按摩', '商务/休闲/按摩', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(204, 2, '美容美发', '商务/休闲/美容美发', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(205, 2, '美甲', '商务/休闲/美甲', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(206, 2, '健身', '商务/休闲/健身', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(207, 2, '棋牌室', '商务/休闲/棋牌室', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(208, 2, '电影院', '商务/休闲/电影院', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(209, 2, '溜冰场', '商务/休闲/溜冰场', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(210, 2, '台球室', '商务/休闲/台球室', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(290, 2, '其他', '商务/休闲/其他', 0, 2, UTC_TIMESTAMP());
    
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(301, 3, '服饰鞋包', '商务/便利店/服饰鞋包', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(302, 3, '生活家居', '商务/便利店/生活家居', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(303, 3, '数码家电', '商务/便利店/数码家电', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(304, 3, '食品饮料', '商务/便利店/食品饮料', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(305, 3, '母婴用品', '商务/便利店/母婴用品', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(306, 3, '个护化妆', '商务/便利店/个护化妆', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(307, 3, '珠宝饰品', '商务/便利店/珠宝饰品', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(308, 3, '钟表眼镜', '商务/便利店/钟表眼镜', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(309, 3, '影音书刊', '商务/便利店/影音书刊', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(310, 3, '水果生鲜', '商务/便利店/水果生鲜', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(390, 3, '其他', '商务/便利店/其他', 0, 2, UTC_TIMESTAMP());
    
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(501, 5, '快递', '商务/配送/快递', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(502, 5, '送水', '商务/配送/送水', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(503, 5, '送牛奶', '商务/配送/送牛奶', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(504, 5, '送煤气', '商务/配送/送煤气', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(590, 5, '其它', '商务/配送/其它', 0, 2, UTC_TIMESTAMP());

INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(701, 7, '音乐', '商务/教育/音乐', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(702, 7, '美术', '商务/教育/美术', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(703, 7, '体育', '商务/教育/体育', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(704, 7, '棋艺', '商务/教育/棋艺', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(705, 7, '珠心算', '商务/教育/珠心算', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(706, 7, '舞蹈', '商务/教育/舞蹈', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(707, 7, '中小学课外辅导', '商务/教育/中小学课外辅导', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(708, 7, '亲子教育', '商务/教育/亲子教育', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(790, 7, '其他', '商务/教育/其他', 0, 2, UTC_TIMESTAMP());

INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(1001, 10, '保姆', '商务/家政/保姆', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(1002, 10, '月嫂', '商务/家政/月嫂', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(1003, 10, '钟点工', '商务/家政/钟点工', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(1004, 10, '搬家', '商务/家政/搬家', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(1005, 10, '洗衣店', '商务/家政/洗衣店', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(1006, 10, '配送', '商务/家政/配送', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(1007, 10, '裁缝', '商务/家政/裁缝', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(1090, 10, '其他', '商务/家政/其他', 0, 2, UTC_TIMESTAMP());

INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(1101, 11, '房屋', '商务/维修/房屋', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(1102, 11, '家电', '商务/维修/家电', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(1103, 11, '电脑', '商务/维修/电脑', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(1104, 11, '开锁', '商务/维修/开锁', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(1105, 11, '家具', '商务/维修/家具', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(1106, 11, '空调', '商务/维修/空调', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(1190, 11, '其他', '商务/维修/其他', 0, 2, UTC_TIMESTAMP());

INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(1201, 12, '手机', '商务/二手与换物/手机', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(1202, 12, '笔记本', '商务/二手与换物/笔记本', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(1203, 12, '平板电脑', '商务/二手与换物/平板电脑', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(1204, 12, '电脑与配件', '商务/二手与换物/电脑与配件', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(1205, 12, '数码产品', '商务/二手与换物/数码产品', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(1206, 12, '家电', '商务/二手与换物/家电', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(1207, 12, '家居', '商务/二手与换物/家居', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(1208, 12, '母婴玩具', '商务/二手与换物/母婴玩具', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(1209, 12, '服装箱包', '商务/二手与换物/服装箱包', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(1210, 12, '美容保健', '商务/二手与换物/美容保健', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(1211, 12, '艺术收藏', '商务/二手与换物/艺术收藏', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(1212, 12, '图书音像', '商务/二手与换物/图书音像', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(1213, 12, '文体户外', '商务/二手与换物/文体户外', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(1214, 12, '办公设备', '商务/二手与换物/办公设备', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(1215, 12, '摩托车', '商务/二手与换物/摩托车', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(1216, 12, '自行车', '商务/二手与换物/自行车', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(1217, 12, '电动车', '商务/二手与换物/电动车', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(1218, 12, '网游与虚拟', '商务/二手与换物/网游与虚拟', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(1219, 12, '其他', '商务/二手与换物/其他', 0, 2, UTC_TIMESTAMP());

INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(1401, 14, '门票', '商务/旅游/门票', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(1402, 14, '周边游', '商务/旅游/周边游', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(1403, 14, '国内游', '商务/旅游/国内游', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(1404, 14, '国外游', '商务/旅游/国外游', 0, 2, UTC_TIMESTAMP());

#
# Common interest categories
#
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(2000, 10001, NULL, '其它', '兴趣／其它', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(2001, 10001, 51, '亲子与教育', '兴趣／亲子与教育', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(2002, 10001, 52, '运动与音乐', '兴趣／运动与音乐', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(2003, 10001, 53, '美食与厨艺', '兴趣／美食与厨艺', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(2004, 10001, 54, '美容化妆', '兴趣／美容化妆', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(2005, 10001, 55, '家庭装饰', '兴趣／家庭装饰', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(2006, 10001, 56, '名牌汇', '兴趣／名牌汇', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(2007, 10001, 57, '宠物会', '兴趣／宠物会', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(2008, 10001, 58, '旅游与摄影', '兴趣／旅游与摄影', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(2009, 10001, 59, '拼车', '兴趣／拼车', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(2010, 10001, NULL, '老乡', '兴趣／老乡', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`,`name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(2011, 10001, NULL, '同事', '兴趣／同事', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`,`name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(2012, 10001, NULL, '同学', '兴趣／同学', 0, 2, UTC_TIMESTAMP());

#
# Application tool categories 
#
# Compatibility note
#   category based change from 0 to 3000
#
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`,`name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(3041, 10002, 4, '找车', '应用/找车', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`,`name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(3042, 10002, 4, '找乘客', '应用/找乘客', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`,`name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(3071, 10002, 7, '找老师', '应用/找老师', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`,`name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(3072, 10002, 7, '找学生', '应用/找学生', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`,`name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(3091, 10002, 9, '公告', '应用/公告', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`,`name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(3092, 10002, 9, '物业建议', '应用/物业建议', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`,`name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(3093, 10002, 9, '业主求助', '应用/业主求助', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`,`name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(3094, 10002, 9, '业主报修', '应用/业主报修', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`,`name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(3101, 10002, 10, '找家政', '应用/找家政', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`,`name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(3102, 10002, 10, '家政推荐', '应用/家政推荐', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`,`name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(3111, 10002, 11, '报修', '应用/报修', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`,`name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(3112, 10002, 11, '修理推荐', '应用/修理推荐', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`,`name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(3121, 10002, 12, '转让', '应用/转让', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`,`name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(3122, 10002, 12, '求购', '应用/求购', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`,`name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(3131, 10002, 13, '出租', '应用/出租', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`,`name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(3132, 10002, 13, '租房', '应用/租房', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`,`name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(3133, 10002, 13, '买房', '应用/买房', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`,`name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(3134, 10002, 13, '卖房', '应用/卖房', 0, 2, UTC_TIMESTAMP());

INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'forum', 1, 'zh_CN', '此帖已删除');	
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'forum', 2, 'zh_CN', '此回复已删除');	
	
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'group.notification', 1, 'zh_CN', '有人申请加入圈（圈不需要审批），通知申请者成功加入圈', '您已加入圈“${groupName}”。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'group.notification', 2, 'zh_CN', '有人申请加入圈（圈不需要审批），通知圈管理员有新成员加入', '${userName}已加入圈“${groupName}”。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'group.notification', 3, 'zh_CN', '有人申请加入圈（圈需要审批），通知申请者等待审核', '您已成功提交加入圈“${groupName}”的申请，请耐心等待审核通过。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'group.notification', 4, 'zh_CN', '有人申请加入圈（圈需要审批），通知圈管理员审核', '${userName}正在申请加入圈“${groupName}”，您同意此申请吗？');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'group.notification', 5, 'zh_CN', '有人申请加入圈（圈需要审批），圈管理员审核通过，通知申请者', '管理员${operatorName}已通过您加入圈“${groupName}”的申请，您可以在圈内聊天、分享了！！');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'group.notification', 6, 'zh_CN', '有人申请加入圈（圈需要审批），圈管理员审核通过，通知审核人', '您已通过${userName}加入圈“${groupName}”的申请。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'group.notification', 7, 'zh_CN', '有人申请加入圈（圈需要审批），圈管理员审核通过，通知其它人', '管理员${operatorName}已通过${userName}加入圈“${groupName}”的申请。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'group.notification', 8, 'zh_CN', '有人申请加入圈（圈需要审批），圈管理员拒绝，通知申请者', '管理员${operatorName}已拒绝您加入圈“${groupName}”的申请。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'group.notification', 9, 'zh_CN', '有人申请加入圈（圈需要审批），圈管理员拒绝，通知审核人', '您已拒绝${userName}加入圈“${groupName}”的申请。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'group.notification', 10, 'zh_CN', '有人申请加入圈（圈需要审批），圈管理员拒绝，通知其它管理员', '管理员${operatorName}已拒绝${userName}加入圈“${groupName}”的申请。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'group.notification', 11, 'zh_CN', '邀请别人加入圈（不需要同意），通知被邀请人', '${operatorName}邀请您加入了圈“${groupName}”，您可以在圈内留便条或者发帖了！');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'group.notification', 12, 'zh_CN', '邀请别人加入圈（不需要同意），通知操作者', '您邀请${userName}加入了圈“${groupName}”，请耐心等待对方回复。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'group.notification', 13, 'zh_CN', '邀请别人加入圈（不需要同意），通知其它人', '${operatorName}邀请${userName}加入了圈“${groupName}”。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'group.notification', 14, 'zh_CN', '邀请别人加入圈（需要同意），通知邀请人', '您邀请${userName}加入圈“${groupName}”，请耐心等待对方回复。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'group.notification', 15, 'zh_CN', '邀请别人加入圈（需要同意），通知被邀请人', '${operatorName}邀请您加入圈“${groupName}”，您同意加入吗？');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'group.notification', 16, 'zh_CN', '邀请别人加入圈（需要同意），通知管理员', '${operatorName}正在邀请${userName}加入圈“${groupName}”。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'group.notification', 17, 'zh_CN', '被邀请人同意加入圈，通知邀请人', '${userName}同意您的邀请，已加入圈“${groupName}”。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'group.notification', 18, 'zh_CN', '被邀请人同意加入圈，通知被邀请人', '您已加入圈“${groupName}”，可以在圈内聊天、分享了！');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'group.notification', 19, 'zh_CN', '被邀请人同意加入圈，通知其它人', '${userName}已接受${operatorName}的邀请，加入了圈“${groupName}”。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'group.notification', 20, 'zh_CN', '被邀请人拒绝加入圈，通知邀请人', '${userName}已拒绝加入圈“${groupName}”的邀请。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'group.notification', 21, 'zh_CN', '被邀请人拒绝加入圈，通知被邀请人', '您已拒绝加入圈“${groupName}”的邀请。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'group.notification', 22, 'zh_CN', '被邀请人拒绝加入圈，通知管理员', '${userName}已拒绝${operatorName}加入圈“${GROUP_NAME}”的邀请。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'group.notification', 23, 'zh_CN', '圈里的人主动退出圈，通知退出人', '您已退出圈“${groupName}”。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'group.notification', 24, 'zh_CN', '圈里的人主动退出圈，通知其它人', '${userName}已退出圈“${groupName}”。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'group.notification', 25, 'zh_CN', '圈里的人被踢出圈，通知退出人', '您已被${operatorName}请出了圈“${groupName}”。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'group.notification', 26, 'zh_CN', '圈里的人被踢出圈，通知操作者', '您已将${userName}请出圈“${groupName}”。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'group.notification', 27, 'zh_CN', '圈里的人被踢出圈，通知其它人', '${operatorName}已将${userName}请出了圈“${groupName}”。');

INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'group.admin.notification', 1, 'zh_CN', '有人申请成为圈管理员，通知申请人', '您正在申请成为圈“${groupName}”的管理员，请耐心等待审核通过。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'group.admin.notification', 2, 'zh_CN', '有人申请成为圈管理员，通知审核人', '${userName}正在申请成为圈“${groupName}”的管理员，您同意此申请吗？');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'group.admin.notification', 3, 'zh_CN', '申请成为圈管理员的请求被审核通过，通知申请人', '您已成为圈“${groupName}”的管理员。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'group.admin.notification', 4, 'zh_CN', '申请成为圈管理员的请求被审核通过，通知审核人', '您已通过${userName}成为圈“${groupName}”管理员的申请。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'group.admin.notification', 5, 'zh_CN', '申请成为圈管理员的请求被审核通过，通知其它管理员', '管理员${operatorName}已通过${userName}成为圈“${groupName}”管理员的申请。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'group.admin.notification', 6, 'zh_CN', '申请成为圈管理员的请求被拒绝通过，通知申请人', '管理员${operatorName}已拒绝您成为圈“${groupName}”管理员的申请。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'group.admin.notification', 7, 'zh_CN', '申请成为圈管理员的请求被拒绝通过，通知审核人', '您已拒绝${userName}成为圈“${groupName}”管理员的申请。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'group.admin.notification', 8, 'zh_CN', '申请成为圈管理员的请求被拒绝通过，通知其它管理员', '管理员${operatorName}已拒绝${userName}成为圈“${groupName}”的管理员的申请。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'group.admin.notification', 9, 'zh_CN', '邀请别人成为圈管理员，通知邀请人', '您正邀请${userName}成为圈“${groupName}”管理员，请耐心等待对方同意。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'group.admin.notification', 10, 'zh_CN', '邀请别人成为圈管理员，通知被邀请人', '${operatorName}邀请您成为圈“${groupName}”的管理员，您同意此邀请吗？');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'group.admin.notification', 11, 'zh_CN', '邀请别人成为圈管理员，被邀请人同意时，通知被邀请人', '您已接受${operatorName}的邀请，现成为圈“${groupName}”的管理员。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'group.admin.notification', 12, 'zh_CN', '邀请别人成为圈管理员，被邀请人同意时，通知邀请人', '${userName}已接受您的邀请，现成为圈“${groupName}”的管理员。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'group.admin.notification', 13, 'zh_CN', '邀请别人成为圈管理员，被邀请人同意时，通知其它人', '${userName}已接受${operatorName}的邀请，现成为圈“${groupName}”的管理员。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'group.admin.notification', 14, 'zh_CN', '邀请别人成为圈管理员，被邀请人拒绝时，通知被邀请人', '您已拒绝担任圈“${groupName}”的管理员。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'group.admin.notification', 15, 'zh_CN', '邀请别人成为圈管理员，被邀请人拒绝时，通知邀请人', '${userName}已拒绝担任圈“${groupName}”的管理员。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'group.admin.notification', 16, 'zh_CN', '邀请别人成为圈管理员，被邀请人拒绝时，通知其它人', '${userName}已拒绝担任圈“${groupName}”的管理员。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'group.admin.notification', 17, 'zh_CN', '圈管理员主动辞去管理员身份，通知操作人', '您已辞去圈“${groupName}”的管理员身份。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'group.admin.notification', 18, 'zh_CN', '圈管理员主动辞去管理员身份，通知其它管理员', '${userName}已辞去圈“${groupName}”的管理员身份。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'group.admin.notification', 19, 'zh_CN', '某管理员被收回管理员权限，通知操作者', '您已解除${userName}在圈“${groupName}”的管理员身份。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'group.admin.notification', 20, 'zh_CN', '某管理员被收回管理员权限，通知被收回权限的人', '${operatorName}已解除您在圈“${groupName}”的管理员身份。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'group.admin.notification', 21, 'zh_CN', '某管理员被收回管理员权限，通知其它人', '${operatorName}已解除${userName}在圈“${groupName}”的管理员身份。');

INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'family.notification', 1, 'zh_CN', '有人填写地址（地址匹配或不匹配），通知申请人等待审核', '您已提交地址${address}，请耐心等待审核通过。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'family.notification', 2, 'zh_CN', '有人填写地址（地址匹配），通知家人审核', '您的家人${userName}已提交地址审核申请，请及时进行审核。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'family.notification', 3, 'zh_CN', '填写的地址被管理员审核通过，通知申请人', '您提交的地址${address}已被审核通过，请享受您的社区新生活。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'family.notification', 4, 'zh_CN', '填写的地址被管理员审核通过，通知家庭其它成员', '您的家人${userName}已通过地址审核。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'family.notification', 5, 'zh_CN', '填写的地址被管理员纠正通过，通知申请人', '您提交的地址已由${orginalAddress}纠正为${address}，请享受您的社区新生活。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'family.notification', 6, 'zh_CN', '填写的地址被管理员纠正通过，通知家庭其它成员', '您的家人${userName}已通过地址审核。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'family.notification', 7, 'zh_CN', '填写的地址被家人审核通过，通知申请人', '您提交的地址${address}已被审核通过，请享受您的社区新生活。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'family.notification', 8, 'zh_CN', '填写的地址被家人审核通过，通知操作人', '您已通过家人${userName}的地址审核申请。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'family.notification', 9, 'zh_CN', '填写的地址被家人审核通过，通知家庭其它成员', '${operatorName}已通过${userName}的地址审核申请。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'family.notification', 10, 'zh_CN', '填写的地址被管理员拒绝，通知申请人', '您提交的地址${address}有误，未通过审核。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'family.notification', 11, 'zh_CN', '填写的地址被管理员拒绝，通知家庭其它成员', '您的家人${userName}提交的地址${address}有误，未通过审核。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'family.notification', 12, 'zh_CN', '填写的地址被家人拒绝，通知申请人', '你提交的地址${address}审核申请已被拒绝。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'family.notification', 13, 'zh_CN', '填写的地址被家人拒绝，通知操作人', '您已拒绝通过${userName}提交的地址审核申请。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'family.notification', 14, 'zh_CN', '填写的地址被家人拒绝，通知家庭其它成员', '家人${operatorName}已拒绝通过${userName}提交的地址审核申请。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'family.notification', 15, 'zh_CN', '某家人主动退出家庭，通知操作人', '您已退出家庭${address}。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'family.notification', 16, 'zh_CN', '某家人主动退出家庭，通知家庭其它成员', '${userName}已退出家庭${address}。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'family.notification', 17, 'zh_CN', '某家人被踢出家庭，通知被踢人', '${operatorName}已将您移出家庭${address}。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'family.notification', 18, 'zh_CN', '某家人被踢出家庭，通知操作人', '您已将${userName}移出家庭${address}。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'family.notification', 19, 'zh_CN', '某家人被踢出家庭，通知家庭其它成员', '${operatorName}已将${userName}移出家庭${address}。');

INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'user.notification', 1, 'zh_CN', '新用户注册', '小左等您好久啦，已经为您准备好了精彩的社区生活…');

INSERT INTO `eh_configurations` (`name`, `value`, `description`) VALUES ('etag.timeout', '300', 'the timeout for etag');
	
	
    
SET foreign_key_checks = 1;
