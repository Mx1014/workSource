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
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(1, 0, 0, '帖子', '帖子', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(2, 0, 0, '兴趣', '兴趣', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(3, 0, 0, '商家和服务', '商家和服务', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(4, 0, 0, '活动', '活动', 0, 2, UTC_TIMESTAMP());

#
# Business categories
#
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(1001, 1, 0, '普通', '帖子/普通', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(1002, 1, 0, '紧急通知', '帖子/紧急通知', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(1003, 1, 0, '公告', '帖子/公告', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(1004, 1, 0, '报修', '帖子/报修', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(1005, 1, 0, '咨询与求助', '帖子/咨询与求助', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(1006, 1, 0, '投诉与建议', '帖子/投诉与建议', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(1007, 1, 0, '二手和租售', '帖子/二手和租售', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(1008, 1, 0, '免费物品', '帖子/免费物品', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(1009, 1, 0, '失物招领', '帖子/失物招领', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(1010, 1, 0, '活动', '帖子/活动', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(1011, 1, 0, '投票', '帖子/投票', 0, 2, UTC_TIMESTAMP());	
	
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(2001, 2, 0, '亲子与教育', '兴趣/亲子与教育', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(2002, 2, 0, '运动与音乐', '兴趣/运动与音乐', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(2003, 2, 0, '美食与厨艺', '兴趣/美食与厨艺', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(2004, 2, 0, '美容化妆', '兴趣/美容化妆', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(2005, 2, 0, '家庭装饰', '兴趣/家庭装饰', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(2006, 2, 0, '名牌汇', '兴趣/名牌汇', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(2007, 2, 0, '宠物会', '兴趣/宠物会', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(2008, 2, 0, '旅游摄影', '兴趣/旅游摄影', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(2009, 2, 0, '拼车', '兴趣/拼车', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(2010, 2, 0, '老乡群', '兴趣/老乡群', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(2011, 2, 0, '同事群', '兴趣/同事群', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(2012, 2, 0, '同学群', '兴趣/同学群', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(2013, 2, 0, '其他', '兴趣/其他', 1, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(2014, 2001, 0, '亲子', '兴趣/亲子与教育/亲子', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(2015, 2001, 0, '教育', '兴趣/亲子与教育/教育', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(2016, 2002, 0, '运动', '兴趣/运动与音乐/运动', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(2017, 2002, 0, '音乐', '兴趣/运动与音乐/音乐', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(2018, 2008, 0, '旅游', '兴趣/旅游摄影/旅游', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(2019, 2008, 0, '摄影', '兴趣/旅游摄影/摄影', 0, 2, UTC_TIMESTAMP());
	
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(3001, 3, 0, '周边商铺', '商家和服务/周边商铺', 1, 2, UTC_TIMESTAMP());	
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(3002, 3, 0, '邻家小店', '商家和服务/邻家小店', 2, 2, UTC_TIMESTAMP());	
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(3003, 3, 0, '上门服务', '商家和服务/上门服务', 3, 2, UTC_TIMESTAMP());	
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(3004, 3, 0, '其他', '商家和服务/其他', 4, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(3005, 3001, 0, '周边商铺-便利店', '商家和服务/周边商铺/便利店', 1, 2, UTC_TIMESTAMP());	
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(3006, 3001, 0, '周边商铺-水果店', '商家和服务/周边商铺/水果店', 1, 2, UTC_TIMESTAMP());	
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(3007, 3001, 0, '周边商铺-餐饮店', '商家和服务/周边商铺/餐饮店', 1, 2, UTC_TIMESTAMP());	
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(3008, 3001, 0, '周边商铺-烟酒店', '商家和服务/周边商铺/烟酒店', 1, 2, UTC_TIMESTAMP());	
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(3009, 3001, 0, '周边商铺-生鲜店', '商家和服务/周边商铺/生鲜店', 1, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(3010, 3002, 0, '邻家小店', '商家和服务/邻家小店/邻家小店', 2, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(3011, 3003, 0, '上门服务-开锁疏通', '商家和服务/上门服务/管道疏通', 3, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(3012, 3003, 0, '上门服务-家电维修', '商家和服务/上门服务/家电维修', 3, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(3013, 3003, 0, '上门服务-家政服务', '商家和服务/上门服务/家政服务', 3, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(3014, 3003, 0, '上门服务-家电清洁', '商家和服务/上门服务/家电清洁', 3, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(3015, 3003, 0, '上门服务-宠物美容', '商家和服务/上门服务/宠物美容', 3, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(3016, 3004, 0, '快递查询', '商家和服务/其他/快递查询', 4, 2, UTC_TIMESTAMP());
	
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(4001, 4, 0, '亲子与教育', '活动/亲子与教育', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(4002, 4, 0, '运动与音乐', '活动/运动与音乐', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(4003, 4, 0, '宠物会', '活动/宠物会', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(4004, 4, 0, '美食与厨艺', '活动/美食与厨艺', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(4005, 4, 0, '美容化妆', '活动/美容化妆', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(4006, 4, 0, '家庭装饰', '活动/家庭装饰', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(4007, 4, 0, '其他', '活动/其他', 1, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(4008, 4001, 0, '亲子', '活动/亲子与教育/亲子', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(4009, 4001, 0, '教育', '活动/亲子与教育/教育', 0, 2, UTC_TIMESTAMP());	
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(4010, 4002, 0, '运动', '活动/运动与音乐/运动', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(4011, 4002, 0, '音乐', '活动/运动与音乐/音乐', 0, 2, UTC_TIMESTAMP());
  

INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'forum', '1', 'zh_CN', '此帖已删除');	
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'forum', '2', 'zh_CN', '此回复已删除');	

INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'general', '200', 'zh_CN', '您已操作成功');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'general', '201', 'zh_CN', '下拉加载更多');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'general', '500', 'zh_CN', '呃，好像哪里出错了');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'general', '501', 'zh_CN', '数据加载失败，请稍后重试');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'general', '502', 'zh_CN', '呃，好像哪里出错了');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'general', '503', 'zh_CN', '呃，好像哪里出错了');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'general', '504', 'zh_CN', '呃，好像哪里出错了');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'general', '505', 'zh_CN', '呃，好像哪里出错了');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'general', '506', 'zh_CN', '呃，好像哪里出错了');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'general', '507', 'zh_CN', '呃，好像哪里出错了');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'activity', '10000', 'zh_CN', '呃，好像哪里出错了');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'activity', '10001', 'zh_CN', '呃，好像哪里出错了');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'activity', '10002', 'zh_CN', '签到无法取消');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'activity', '10003', 'zh_CN', '请输入正确的电话号码');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'activity', '10004', 'zh_CN', '家庭不存在');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'activity', '10005', 'zh_CN', '用户不存在');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'activity', '10006', 'zh_CN', '用户没有参加活动');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'address', '10001', 'zh_CN', '地址不存在');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'banner', '10001', 'zh_CN', '呃，好像哪里出错了');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'banner', '10002', 'zh_CN', '呃，好像哪里出错了');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'banner', '10003', 'zh_CN', '广告激活数量超过最大值啦!');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'business', '10001', 'zh_CN', '商家不存在');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'community', '10001', 'zh_CN', '小区不存在');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'community', '10002', 'zh_CN', '呃，好像哪里出错了');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'community', '10003', 'zh_CN', '呃，好像哪里出错了');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'contentserver', '10000', 'zh_CN', '呃，好像哪里出错了');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'contentserver', '10001', 'zh_CN', '呃，好像哪里出错了');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'contentserver', '10002', 'zh_CN', '呃，好像哪里出错了');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'contentserver', '10003', 'zh_CN', '呃，好像哪里出错了');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'contentserver', '10004', 'zh_CN', '呃，好像哪里出错了');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'contentserver', '10005', 'zh_CN', '呃，好像哪里出错了');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'family', '10001', 'zh_CN', '家庭不存在');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'family', '10002', 'zh_CN', '用户未加入家庭');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'family', '10003', 'zh_CN', '呃，好像哪里出错了');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'family', '10004', 'zh_CN', '无法删除自己');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'family', '10005', 'zh_CN', '无法拒绝自己');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'family', '10006', 'zh_CN', '用户已加入家庭');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'forum', '10001', 'zh_CN', '论坛不存在');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'forum', '10002', 'zh_CN', '可见区域不存在');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'forum', '10003', 'zh_CN', '小区不存在');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'forum', '10004', 'zh_CN', '帖子不存在');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'forum', '10005', 'zh_CN', '片区不存在');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'forum', '10006', 'zh_CN', '帖子已删除');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'forum', '10011', 'zh_CN', '无法分配任务');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'forum', '10012', 'zh_CN', '任务已分配');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'group', '10001', 'zh_CN', '圈子不存在');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'group', '10002', 'zh_CN', '用户已加入圈子');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'group', '10003', 'zh_CN', '用户待审核');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'group', '10004', 'zh_CN', '圈子成员不存在');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'group', '10005', 'zh_CN', '圈子成员状态异常');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'group', '10006', 'zh_CN', '被邀请的用户未加入圈子');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'group', '10007', 'zh_CN', '呃，好像哪里出错了');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'group', '10008', 'zh_CN', '呃，好像哪里出错了');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'group', '10009', 'zh_CN', '圈子创建者无法退出');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'group', '10010', 'zh_CN', '圈子创建者不能取消管理员角色');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'group', '10011', 'zh_CN', '圈子创建者不能取消管理员角色');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'group', '10012', 'zh_CN', '不存在申请纪录');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'group', '10013', 'zh_CN', '用户申请状态异常');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'group', '10014', 'zh_CN', '用户查询失败');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'group', '10015', 'zh_CN', '家庭不存在');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'group', '10016', 'zh_CN', '家庭成员不存在');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'group', '10017', 'zh_CN', '圈子成员状态异常');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'group', '10018', 'zh_CN', '用户未加入圈子');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'launchpad', '10001', 'zh_CN', '呃，好像哪里出错了');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'launchpad', '10002', 'zh_CN', '呃，好像哪里出错了');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'oauth2', '1', 'zh_CN', '呃，好像哪里出错了');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'oauth2', '2', 'zh_CN', '呃，好像哪里出错了');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'oauth2', '3', 'zh_CN', '呃，好像哪里出错了');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'oauth2', '4', 'zh_CN', '呃，好像哪里出错了');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'oauth2', '5', 'zh_CN', '呃，好像哪里出错了');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'oauth2', '6', 'zh_CN', '呃，好像哪里出错了');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'oauth2', '7', 'zh_CN', '呃，好像哪里出错了');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'oauth2', '8', 'zh_CN', '呃，好像哪里出错了');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'oauth2', '9', 'zh_CN', '呃，好像哪里出错了');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'oauth2', '10', 'zh_CN', '呃，好像哪里出错了');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'pm', '10001', 'zh_CN', '家庭不存在');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'pm', '10002', 'zh_CN', '地址不存在');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'pm', '10003', 'zh_CN', '账单不存在');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'pm', '11001', 'zh_CN', '帖子不存在');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'pm', '12002', 'zh_CN', '评论不存在');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'pm', '12003', 'zh_CN', '任务不存在');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'pm', '12004', 'zh_CN', '呃，好像哪里出错了');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'poll', '10000', 'zh_CN', '呃，好像哪里出错了');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'poll', '10001', 'zh_CN', '投票帖子无效');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'poll', '10002', 'zh_CN', '投票选项不能为空');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'poll', '10003', 'zh_CN', '找不到匹配的选项');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'poll', '10004', 'zh_CN', '不能重复投票');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'qrcode', '10001', 'zh_CN', '动作类型无效');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'qrcode', '10002', 'zh_CN', '二维码已实效');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'region', '10001', 'zh_CN', '地区不存在');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'user', '10000', 'zh_CN', '呃，好像哪里出错了');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'user', '10001', 'zh_CN', '呃，好像哪里出错了');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'user', '10002', 'zh_CN', '旧密码不正确');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'user', '10003', 'zh_CN', '呃，好像哪里出错了');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'user', '10004', 'zh_CN', '验证码无效');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'user', '10005', 'zh_CN', '用户名或密码不正确');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'user', '10006', 'zh_CN', '用户不存在');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'user', '10007', 'zh_CN', '密码不正确');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'user', '10008', 'zh_CN', '呃，好像哪里出错了');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'user', '10009', 'zh_CN', '呃，好像哪里出错了');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'user', '10010', 'zh_CN', '用户未激活');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'user', '10011', 'zh_CN', '号码已存在');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'user', '10012', 'zh_CN', '名字已存在');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'user', '10013', 'zh_CN', '用户还未设置密码');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'user', '10014', 'zh_CN', '邀请码无效');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'user', '10015', 'zh_CN', '邀请码生成失败');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'user', '10016', 'zh_CN', '呃，好像哪里出错了');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'user', '10017', 'zh_CN', '用户权限不足');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'user', '401', 'zh_CN', '用户未授权');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'version', '1', 'zh_CN', '呃，好像哪里出错了');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'version', '2', 'zh_CN', '版本内容不存在');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'version', '3', 'zh_CN', '版本内容不存在');
INSERT INTO eh_locale_strings(scope,code,locale,text) VALUES('activity','1','zh_CN','我已报名!');
INSERT INTO eh_locale_strings(scope,code,locale,text) VALUES('activity','2','zh_CN','我已签到!');
INSERT INTO eh_locale_strings(scope,code,locale,text) VALUES('activity','3','zh_CN','回复 ${username}：您的报名已被确认');
INSERT INTO eh_locale_strings(scope,code,locale,text) VALUES('activity','4','zh_CN','我已取消报名!');
INSERT INTO eh_locale_strings(scope,code,locale,text) VALUES('activity','5','zh_CN','回复 ${username}：您的报名已被拒绝，原因：${reason}');

	
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
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'group.admin.notification', 22, 'zh_CN', '邀请别人成为圈管理员（不需要同意），通知被邀请人', '${operatorName}已经邀请您成为圈“${groupName}”的管理员了。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'group.admin.notification', 23, 'zh_CN', '邀请别人成为圈管理员（不需要同意），通知邀请人', '您已邀请${userName}成为圈“${groupName}”的管理员了。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'group.admin.notification', 24, 'zh_CN', '邀请别人成为圈管理员（不需要同意），通知其它人', '${operatorName}已经邀请${userName}成为圈“${groupName}”的管理员了。');

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


INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'community.notification', 1, 'zh_CN', '有人填加小区（小区地址不存在），通知申请人等待审核', '您已经提交小区${communityName}，请耐心等待管理员的审核。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'community.notification', 2, 'zh_CN', '管理员通过用户填写的小区，用户收到的通知', '管理员通过了您填写的小区${communityName}，您现在可以填写地址啦。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'community.notification', 3, 'zh_CN', '管理员驳回用户填写的小区，用户收到的通知', '管理员驳回了您填写的小区${communityName}，请确认填写信息的正确性。');


INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'user.notification', 1, 'zh_CN', '新用户注册', '小左等您好久啦，已经为您准备好了精彩的社区生活…');


INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES ('organization.notification', '1', 'zh_CN', '给用户发通知：同意用户加入组织', '管理员${memberName}同意了您加入${orgName}的申请。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES ('organization.notification', '2', 'zh_CN', '给其他管理员发通知：同意用户加入组织','管理员${memberName}同意了${userName}加入${orgName}的申请。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES ('organization.notification', '3', 'zh_CN', '给用户发通知：拒绝用户加入组织', '管理员${memberName}拒绝了您加入${orgName}的申请。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES ('organization.notification', '4', 'zh_CN', '给其他管理员发通知：拒绝用户加入组织','管理员${memberName}拒绝了${userName}加入${orgName}的申请。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES ('organization.notification', '5', 'zh_CN', '在请求服务帖发评论:分配请求服务帖任务给处理员', '该请求已安排${memberName}来处理。电话是${memberContactToken}，业主可以电话直接联系。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES ('organization.notification', '6', 'zh_CN', '给被分配人员发短信:分配请求服务帖任务给处理员', '业主${phone}发布了新的${topicType}帖，您已被分配处理该业主的需求，请尽快联系该业主。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES ('organization.notification', '7', 'zh_CN', '给被分配人员发短信:分配请求服务帖任务给处理员,用户求助由管理员代发', '${orgName}管理员给您分配了${topicType}帖的任务，管理员电话${phone},请尽快与管理员联系处理问题。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES ('organization.notification', '8', 'zh_CN', '通知其他管理员：删除组织成员','${orgName}管理员${memberName}删除了${userName}成员。');


INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'messaging', '1', 'zh_CN', '你有新消息');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'messaging', '2', 'zh_CN', '你有新的语音消息');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'messaging', '3', 'zh_CN', '你有新的图片消息');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'messaging', '4', 'zh_CN', '你有新的视频消息');


INSERT INTO `eh_apps` (`id`,`creator_uid`,`app_key`,`secret_key`,`name`,`description`,`status`,`create_time`) VALUES (100,NULL,'b86ddb3b-ac77-4a65-ae03-7e8482a3db70','2-0cDFNOq-zPzYGtdS8xxqnkR8PRgNhpHcWoku6Ob49NdBw8D9-Q72MLsCidI43IKhP1D_43ujSFbatGPWuVBQ','SystemExtension',NULL,1,'2015-08-24 14:14:16');
INSERT INTO `eh_apps` (`id`,`creator_uid`,`app_key`,`secret_key`,`name`,`description`,`status`,`create_time`) VALUES (101,1,'d80e06ca-3766-11e5-b18f-b083fe4e159f','g1JOZUM3BYzWpZD5Q7p3z+i/z0nj2TcokTFx2ic53FCMRIKbMhSUCi7fSu9ZklFCZ9tlj68unxur9qmOji4tNg==','test','dianshang test',1,'2015-08-24 14:17:53');
INSERT INTO `eh_apps` (`id`,`creator_uid`,`app_key`,`secret_key`,`name`,`description`,`status`,`create_time`) VALUES (102,1,'93e8275c-31e2-11e5-b7ad-b083fe4e159f','2nDpmzJj63Un0GzXyeZKUKlVSOKzNHv4FidFL9uCpNaLq6rqE0VAOv3uPaR0jWIRMNqedgci3vzLPAkaX1jg6Q==','door','door open',1,'2015-09-01 14:39:18');
INSERT INTO `eh_apps` (`id`,`creator_uid`,`app_key`,`secret_key`,`name`,`description`,`status`,`create_time`) VALUES (103,1,'cePPQVM-I4cN73W_ZQZ6VkISDgHNAvUBZPjH2J','Jm38pWr5XEvdXnHmNuBb4PbzaP-sIpEZvcmvxC9S4ypDVRql-KUbN0Dq_Djv-GxZ','common-pay','',1,'2015-09-01 17:43:53');

-- eh_banners
INSERT INTO `eh_banners`(id,namespace_id,appId,banner_location,banner_group,scope_code,scope_id,`name`,vendor_tag,poster_path,start_time,end_time,`status`,`order`,creator_uid,create_time,delete_time,action_type,action_data, target_type)
VALUES (1, 0, 0, '/home', 'Default', 0, 0, 'zuolin', 'zuolin', 'cs://1/image/aW1hZ2UvTVRveFl6YzJOVEkwTVRsaFptVXdNbUUxTkdZMlpEWXpZbUV6WVRrMllqUTBOUQ', null, null, '2', '1', '0', '2015-06-30 16:01:45', null, '13', '{"url":"https://zuolin.com"}', '');

-- eh_launch_pad_items;
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag) VALUES (1, 0, 0, 0, 0, '/home', 'GovAgencies', 'PM', '物业', 'cs://1/image/aW1hZ2UvTVRwak5tWTNabUpsTVRNMk16VTNNek16T1dabVlqZzJPREl4TjJJNFpUVTVZdw', 1, 1, 2, '{"itemLocation":"/home/Pm","layoutName":"PmLayout","title":"物业","entityTag":"PM"}', 0, 0, 1, 1, '', 0,NULL);
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag) VALUES (2, 0, 0, 0, 0, '/home', 'GovAgencies', 'GARC', '业委会', 'cs://1/image/aW1hZ2UvTVRvM09HSXdObUkyTTJaaU1XTXhNak5oWmpJNFpURmpaREkyTXpNMVl6aGpZUQ', 1, 1, 2, '{"itemLocation":"/home/Garc","layoutName":"GarcLayout","title":"业委会","entityTag":"GARC"}', 0, 0, 1, 1, '', 0,NULL);
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag) VALUES (3, 0, 0, 0, 0, '/home', 'GovAgencies', 'GANC', '工作站', 'cs://1/image/aW1hZ2UvTVRveFl6Z3pOR1JsWm1SbE16RmlaRFkxWW1VMVpEUmxOR1E1T1RWaU16TmtZUQ', 1, 1, 2, '{"itemLocation":"/home/Ganc","layoutName":"GancLayout","title":"工作站","entityTag":"GANC"}', 0, 0, 1, 1, '', 0,NULL);
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag) VALUES (4, 0, 0, 0, 0, '/home', 'GovAgencies', 'GAPS', '派出所', 'cs://1/image/aW1hZ2UvTVRvME1XVmtOVFk0TVRrNU1qVm1NV015WkRkbU56ZGpOak0yTURVNE1qYzFZdw', 1, 1, 2, '{"itemLocation":"/home/Gaps","layoutName":"GapsLayout","title":"派出所","entityTag":"GAPS"}', 0, 0, 1, 1, '', 0,NULL);

INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag) VALUES (5, 0, 0, 0, 0, '/home/Pm', 'GaActions', 'ADVISE', '投诉建议', 'cs://1/image/aW1hZ2UvTVRwall6VTVOV1U0WkdRMk56aGxPRFV3TVRBMk5qVXlNVFkzWVRZek56aGpOdw', 1, 1, 19, '{"contentCategory":1006,"actionCategory":0,"forumId":1,"targetEntityTag":"PM","embedAppId":27,"visibleRegionType":0}', 0, 0, 1, 1, '', 0,NULL);
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag) VALUES (6, 0, 0, 0, 0, '/home/Pm', 'GaActions', 'HELP', '咨询求助', 'cs://1/image/aW1hZ2UvTVRvM016VXdabVE0WW1SaVpqazJaVFJpWWpVeVpHTTBNMlEwT0RsaVpqQTRZZw', 1, 1, 19, '{"contentCategory":1005,"actionCategory":0,"forumId":1,"targetEntityTag":"PM","embedAppId":27,"visibleRegionType":0}', 0, 0, 1, 1, '', 0,NULL);
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag) VALUES (7, 0, 0, 0, 0, '/home/Pm', 'GaActions', 'REPAIR', '报修', 'cs://1/image/aW1hZ2UvTVRveE1tWmtOMkprWXpaak5EWmpOV1JrTURkbU1EUTNNbUptTXpJek4yUmpaQQ', 1, 1, 19, '{"contentCategory":1004,"actionCategory":0,"forumId":1,"targetEntityTag":"PM","embedAppId":27,"visibleRegionType":0}', 0, 0, 1, 1, '', 0,NULL);
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag) VALUES (8, 0, 0, 0, 0, '/home/Pm', 'GaActions', 'PAYMENT', '缴费', 'cs://1/image/aW1hZ2UvTVRvMk5qRmhPV1EyWlRRNFl6TTJPVFF5WmpNMllqSmtPVFJrT1RSbE1EQm1PUQ', 1, 1, 2, '{"itemLocation":"/home/Pm/Payment","layoutName":"PaymentLayout","title":"缴费"}', 0, 0, 1, 1, '', 0,NULL);

INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag) VALUES (9, 0, 0, 0, 0, '/home/Garc', 'GaActions', 'ADVISE', '投诉建议', 'cs://1/image/aW1hZ2UvTVRwall6VTVOV1U0WkdRMk56aGxPRFV3TVRBMk5qVXlNVFkzWVRZek56aGpOdw', 1, 1, 19, '{"contentCategory":1006,"actionCategory":0,"forumId":1,"targetEntityTag":"GARC","embedAppId":27,"visibleRegionType":0}', 0, 0, 1, 1, '', 0,NULL);
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag) VALUES (10, 0, 0, 0, 0, '/home/Garc', 'GaActions', 'HELP', '咨询求助', 'cs://1/image/aW1hZ2UvTVRvM016VXdabVE0WW1SaVpqazJaVFJpWWpVeVpHTTBNMlEwT0RsaVpqQTRZZw', 1, 1, 19, '{"contentCategory":1005,"actionCategory":0,"forumId":1,"targetEntityTag":"GARC","embedAppId":27,"visibleRegionType":0}', 0, 0, 1, 1, '', 0,NULL);
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag) VALUES (11, 0, 0, 0, 0, '/home/Ganc', 'GaActions', 'ADVISE', '投诉建议', 'cs://1/image/aW1hZ2UvTVRwall6VTVOV1U0WkdRMk56aGxPRFV3TVRBMk5qVXlNVFkzWVRZek56aGpOdw', 1, 1, 19, '{"contentCategory":1006,"actionCategory":0,"forumId":1,"targetEntityTag":"GANC","embedAppId":27,"visibleRegionType":1}', 0, 0, 1, 1, '', 0,NULL);
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag) VALUES (12, 0, 0, 0, 0, '/home/Ganc', 'GaActions', 'HELP', '咨询求助', 'cs://1/image/aW1hZ2UvTVRvM016VXdabVE0WW1SaVpqazJaVFJpWWpVeVpHTTBNMlEwT0RsaVpqQTRZZw', 1, 1, 19, '{"contentCategory":1005,"actionCategory":0,"forumId":1,"targetEntityTag":"GANC","embedAppId":27,"visibleRegionType":1}', 0, 0, 1, 1, '', 0,NULL);
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag) VALUES (13, 0, 0, 0, 0, '/home/Gaps', 'GaActions', 'ADVISE', '投诉建议', 'cs://1/image/aW1hZ2UvTVRwall6VTVOV1U0WkdRMk56aGxPRFV3TVRBMk5qVXlNVFkzWVRZek56aGpOdw', 1, 1, 19, '{"contentCategory":1006,"actionCategory":0,"forumId":1,"targetEntityTag":"GAPS","embedAppId":27,"visibleRegionType":1}', 0, 0, 1, 1, '', 0,NULL);
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag) VALUES (14, 0, 0, 0, 0, '/home/Gaps', 'GaActions', 'HELP', '咨询求助', 'cs://1/image/aW1hZ2UvTVRvM016VXdabVE0WW1SaVpqazJaVFJpWWpVeVpHTTBNMlEwT0RsaVpqQTRZZw', 1, 1, 19, '{"contentCategory":1005,"actionCategory":0,"forumId":1,"targetEntityTag":"GAPS","embedAppId":27,"visibleRegionType":1}', 0, 0, 1, 1, '', 0,NULL);

INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag) VALUES (15, 0, 0, 0, 0, '/home/Pm', 'CallPhones', '咨询电话', '咨询电话', 'cs://1/image/aW1hZ2UvTVRwaE1HVmtNRFJrWW1ZeVkyWXdPR1JtT1RSbFkyRTNOMlEzT1dOa05tSXhOQQ', 1, 1, 17, '{"callPhones":""}', 0, 0, 1, 1, '', 0,NULL);

INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag) VALUES (16, 0, 0, 0, 0, '/home/Pm', 'GaPosts', 'ADVISE', '投诉建议', NULL, 1, 1, 15, '{"contentCategory":1006,"actionCategory":0,"forumId":1,"embedAppId":27,"entityTag":"PM"}', 0, 0, 1, 1, '', 0,NULL);
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag) VALUES (17, 0, 0, 0, 0, '/home/Pm', 'GaPosts', 'HELP', '咨询求助', NULL, 1, 1, 15, '{"contentCategory":1005,"actionCategory":0,"forumId":1,"embedAppId":27,"entityTag":"PM"}', 0, 0, 1, 1, '', 0,NULL);
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag) VALUES (18, 0, 0, 0, 0, '/home/Pm', 'GaPosts', 'REPAIR', '报修', NULL, 1, 1, 15, '{"contentCategory":1004,"actionCategory":0,"forumId":1,"embedAppId":27,"entityTag":"PM"}', 0, 0, 1, 1, '', 0,NULL);

INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag) VALUES (19, 0, 0, 0, 0, '/home/Garc', 'GaPosts', 'ADVISE', '投诉建议', NULL, 1, 1, 15, '{"contentCategory":1006,"actionCategory":0,"forumId":1,"embedAppId":27,"entityTag":"GARC"}', 0, 0, 1, 1, '', 0,NULL);
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag) VALUES (20, 0, 0, 0, 0, '/home/Garc', 'GaPosts', 'HELP', '咨询求助', NULL ,1, 1, 15, '{"contentCategory":1005,"actionCategory":0,"forumId":1,"embedAppId":27,"entityTag":"GARC"}', 0, 0, 1, 1, '', 0,NULL);
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag) VALUES (21, 0, 0, 0, 0, '/home/Ganc', 'GaPosts', 'ADVISE', '投诉建议', NULL, 1, 1, 15, '{"contentCategory":1006,"actionCategory":0,"forumId":1,"embedAppId":27,"entityTag":"GANC"}', 0, 0, 1, 1, '', 0,NULL);
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag) VALUES (22, 0, 0, 0, 0, '/home/Ganc', 'GaPosts', 'HELP', '咨询求助', NULL, 1, 1, 15, '{"contentCategory":1005,"actionCategory":0,"forumId":1,"embedAppId":27,"entityTag":"GANC"}', 0, 0, 1, 1, '', 0,NULL);
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag) VALUES (23, 0, 0, 0, 0, '/home/Gaps', 'GaPosts', 'ADVISE', '投诉建议',NULL, 1, 1, 15, '{"contentCategory":1006,"actionCategory":0,"forumId":1,"embedAppId":27,"entityTag":"GAPS"}', 0, 0, 1, 1, '', 0,NULL);
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag) VALUES (24, 0, 0, 0, 0, '/home/Gaps', 'GaPosts', 'HELP', '咨询求助', NULL, 1, 1, 15, '{"contentCategory":1005,"actionCategory":0,"forumId":1,"embedAppId":27,"entityTag":"GAPS"}', 0, 0, 1, 1, '', 0,NULL);

INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag) VALUES (25, 0, 0, 0, 0, '/home', 'Coupons', '优惠券', '优惠券', 'cs://1/image/aW1hZ2UvTVRvM1pUTTRNekF5TXpKaFlXVTRaalkyTWpkbVlURTJNall6WVdFeE16UXlPUQ', 3, 1, 14, '{"url":"http://biz.zuolin.com/zl-ec?hideNavigationBar=1&sourceUrl=http%3A%2F%2Fbiz.zuolin.com%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fstore%2Fdetails%2Fpromotions%2F"}', 1, 0, 1, 1, '', 0,NULL);
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag) VALUES (26, 0, 0, 0, 0, '/home', 'Coupons', '市场动态', '市场动态', 'cs://1/image/aW1hZ2UvTVRwa05HWmpOelpoT1RNMVpHTTRObVV3WkdNNVpqSmtOREl6WkRBM05tWTVZUQ', 5, 1, 14, '{"url":"https://zuolin.com/evh/user/appServiceAccess?appKey=bf925ea0-a5e0-11e4-a68c-00163e024632&type=simple&uri=http%3a%2f%2fm.ubive.com%2findex.php%3futm_source%3d297321423546#zlservice_redirect"}', 2, 0, 1, 1, '', 0,NULL);

INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag) VALUES (27, 0, 0, 0, 0, '/home/Pm/Payment', 'PayActions', '物业费', '物业费', 'cs://1/image/aW1hZ2UvTVRwa05EQTJOVGxqTkRJMk56UmtaVEpqTm1ZeFlqazRPR1pqTkRJM01UTmpNQQ', 1, 1, 22, '', 0, 0, 1, 1, '', 0,NULL);
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag) VALUES (28, 0, 0, 0, 0, '/home/Pm/Payment', 'PayActions', '水费', '水费', 'cs://1/image/aW1hZ2UvTVRvd01XTXhPRGd5TnpZek9HUTFObVE1Wldaa01qQTNORFF6WlRjeU9XTmhZUQ', 1, 1, 14, '{"url":"http://zuolin.com//mobile/static/coming_soon/index.html"}', 0, 0, 1, 1, '', 0,NULL);
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag) VALUES (29, 0, 0, 0, 0, '/home/Pm/Payment', 'PayActions', '电费', '电费', 'cs://1/image/aW1hZ2UvTVRvd09UazFOMlkzWkRObFl6RTJZakEzT1RobU5EY3lZamN6TXpRM01tUTVOUQ', 1, 1, 14, '{"url":"http://zuolin.com//mobile/static/coming_soon/index.html"}', 0, 0, 1, 1, '', 0,NULL);
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag) VALUES (30, 0, 0, 0, 0, '/home/Pm/Payment', 'PayActions', '燃气费', '燃气费', 'cs://1/image/aW1hZ2UvTVRvNFpqVXhNREl6TmpneE1XVmxZVGhrTnpJd09XVXlaakZqWlRJeE1XUTFNQQ', 1, 1, 14, '{"url":"http://zuolin.com//mobile/static/coming_soon/index.html"}', 0, 0, 1, 1, '', 0,NULL);
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag) VALUES (31, 0, 0, 0, 0, '/home/Pm/Payment', 'PayActions', '固话宽带', '固话宽带', 'cs://1/image/aW1hZ2UvTVRwa05UWmxNVGt4TURObE9EbG1Oamd3TVdFMllUSTVOV1kwTlRNMk9HWmtNUQ', 1, 1, 14, '{"url":"http://zuolin.com//mobile/static/coming_soon/index.html"}', 0, 0, 1, 1, '', 0,NULL);
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag) VALUES (32, 0, 0, 0, 0, '/home/Pm/Payment', 'PayActions', '有线电视', '有线电视', 'cs://1/image/aW1hZ2UvTVRwaU9EWTFObVJpTmpCallUSTFOams1T0RJNFpHWTJNV1E1T1dOaE1UWmtOdw', 1, 1, 14, '{"url":"http://zuolin.com//mobile/static/coming_soon/index.html"}', 0, 0, 1, 1, '', 0,NULL);
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag) VALUES (33, 0, 0, 0, 0, '/home', 'Bizs', '更多', '更多', 'cs://1/image/aW1hZ2UvTVRvNE1XRTNZVEUxT0RGaE1EQmpZakF6TmprNE1EZ3paR1k0TVRVMFptUTJZdw', 1, 1, 1,'{"itemLocation":"/home","itemGroup":"Bizs"}', 30, 0, 1, 1, '', 0,NULL);


INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag) VALUES (34, 0, 0, 1, 24210090697430067, '/home/Pm', 'GaActions', 'UNLOCK', '门禁', 'cs://1/image/aW1hZ2UvTVRveU5qSTVaVGxrWXpabE9EQm1aalF4TXpGaU9EWTJOMlUzTURRek1qa3dOZw', 1, 1, 21, '{"vender":1,"remote":0}', 0, 0, 1, 1, '', 0,NULL);
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag) VALUES (35, 0, 0, 1, 24210090697430529, '/home/Pm', 'GaActions', 'UNLOCK', '门禁', 'cs://1/image/aW1hZ2UvTVRveU5qSTVaVGxrWXpabE9EQm1aalF4TXpGaU9EWTJOMlUzTURRek1qa3dOZw', 1, 1, 21, '{"vender":1,"remote":0}', 0, 0, 1, 1, '', 0,NULL);
-- INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag) VALUES (36, 0, 0, 1, 24210090697427178, '/home/Pm', 'GaActions', 'UNLOCK', '门禁', 'cs://1/image/aW1hZ2UvTVRveU5qSTVaVGxrWXpabE9EQm1aalF4TXpGaU9EWTJOMlUzTURRek1qa3dOZw', 1, 1, 21, '{"vender":1,"remote":0}', 0, 0, 1, 1, '', 0,NULL);

-- INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag,target_type,target_id) VALUES (36,0, 0, 0, 0, '/home', 'Bizs', '云家政', '云家政', 'cs://1/image/aW1hZ2UvTVRwall6QTJaalUxT0dNeE16QmhPRGRrT0RZM1l6Y3pZVFJrTURWbE4yTmtaQQ', 1, 1, 14, '{"url":"http://koala.yunjiazheng.com/"}', 1, 0, 1, 1, '', 0,NULL,'biz',1);
-- INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag,target_type,target_id) VALUES (37,0, 0, 0, 0, '/home', 'Bizs', '生鲜商城', '生鲜商城', 'cs://1/image/aW1hZ2UvTVRvd09EUm1aRFEwWlRGa01UVm1PR0kwWkdRME1UTXpaV05sTUdSaU1tRm1ZUQ', 1, 1, 14, '{"url":"http://m.ubive.com/index.php"}', 2, 0, 1, 1, '', 0,NULL,'biz',2);

INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag) VALUES (38, 0, 0, 0, 0, '/home/Pm', 'GaPosts', 'NOTICE', '公告', NULL, 1, 1, 15, '{"contentCategory":1003,"actionCategory":0,"forumId":1,"embedAppId":0,"entityTag":"PM"}', 0, 0, 1, 1, '', 0,NULL);
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag) VALUES (39, 0, 0, 0, 0, '/home/Garc', 'GaPosts', 'NOTICE', '公告', NULL, 1, 1, 15, '{"contentCategory":1003,"actionCategory":0,"forumId":1,"embedAppId":0,"entityTag":"GARC"}', 0, 0, 1, 1, '', 0,NULL);
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag) VALUES (40, 0, 0, 0, 0, '/home/Ganc', 'GaPosts', 'NOTICE', '公告', NULL, 1, 1, 15, '{"contentCategory":1003,"actionCategory":0,"forumId":1,"embedAppId":0,"entityTag":"GANC"}', 0, 0, 1, 1, '', 0,NULL);
INSERT INTO `eh_launch_pad_items`(id,namespace_id,app_id,scope_code,scope_id,item_location,item_group,item_name,item_label,icon_uri,item_width,item_height,action_type,action_data,default_order,apply_policy,min_version,display_flag,display_layout,bgcolor,tag) VALUES (41, 0, 0, 0, 0, '/home/Gaps', 'GaPosts', 'NOTICE', '公告',NULL, 1, 1, 15, '{"contentCategory":1003,"actionCategory":0,"forumId":1,"embedAppId":0,"entityTag":"GAPS"}', 0, 0, 1, 1, '', 0,NULL);


--  eh_launch_pad_layouts;
INSERT INTO `eh_launch_pad_layouts`(id, namespace_id, name, layout_json, version_code, min_version_code, status, create_time) VALUES ('1', '0', 'ServiceMarketLayout', '{"versionCode":"2015072815","versionName":"3.0.0","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":1,"separatorFlag":0,"separatorHeight":0},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"GovAgencies"},"style":"Default","defaultOrder":2,"separatorFlag":1,"separatorHeight":21,"columnCount":4},{"groupName":"","widget":"Coupons","instanceConfig":{"itemGroup":"Coupons"},"style":"Default","defaultOrder":3,"separatorFlag":1,"separatorHeight":21},{"groupName":"商家服务","widget":"Navigator","instanceConfig":{"itemGroup":"Bizs"},"style":"Default","defaultOrder":5,"separatorFlag":0,"separatorHeight":0}]}', '2015082914', '2015061701', '2', '2015-06-24 16:09:30');
INSERT INTO `eh_launch_pad_layouts`(id, namespace_id, name, layout_json, version_code, min_version_code, status, create_time) VALUES ('2', '0', 'PmLayout', '{"versionCode":"2015072815","versionName":"3.0.0","displayName":"物业首页","layoutName":"PmLayout","groups":[{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"GaActions"},"style":"Default","defaultOrder":2,"separatorFlag":1,"separatorHeight":21,"columnCount":4},{"groupName":"","widget":"Posts","instanceConfig":{"itemGroup":"GaPosts"},"style":"Default","defaultOrder":3,"separatorFlag":0,"separatorHeight":0},{"groupName":"CallPhone","widget":"CallPhones","instanceConfig":{"itemGroup":"CallPhones","position":"bottom"},"style":"Default","defaultOrder":3,"separatorFlag":0,"separatorHeight":0}]}', '2015082914', '2015061701', '2', '2015-06-27 14:04:57');
INSERT INTO `eh_launch_pad_layouts`(id, namespace_id, name, layout_json, version_code, min_version_code, status, create_time) VALUES ('3', '0', 'GarcLayout', '{"versionCode":"2015072815","versionName":"3.0.0","layoutName":"GarcLayout","displayName":"业委会首页","groups":[{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"GaActions"},"style":"Default","defaultOrder":2,"separatorFlag":1,"separatorHeight":21},{"groupName":"","widget":"Posts","instanceConfig":{"itemGroup":"GaPosts"},"style":"Default","defaultOrder":3,"separatorFlag":0,"separatorHeight":0},{"groupName":"CallPhone","widget":"CallPhones","instanceConfig":{"itemGroup":"CallPhones","position":"bottom"},"style":"Default","defaultOrder":3,"separatorFlag":0,"separatorHeight":0}]}', '2015082914', '2015061701', '2', '2015-07-13 16:46:43');
INSERT INTO `eh_launch_pad_layouts`(id, namespace_id, name, layout_json, version_code, min_version_code, status, create_time) VALUES ('4', '0', 'GapsLayout', '{"versionCode":"2015072815","versionName":"3.0.0","layoutName":"GapsLayout","displayName":"派出所首页","groups":[{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"GaActions"},"style":"Default","defaultOrder":2,"separatorFlag":1,"separatorHeight":21},{"groupName":"","widget":"Posts","instanceConfig":{"itemGroup":"GaPosts"},"style":"Default","defaultOrder":3,"separatorFlag":0,"separatorHeight":0},{"groupName":"CallPhone","widget":"CallPhones","instanceConfig":{"itemGroup":"CallPhones","position":"bottom"},"style":"Default","defaultOrder":3,"separatorFlag":0,"separatorHeight":0}]}', '2015082914', '2015061701', '2', '2015-07-13 16:46:40');
INSERT INTO `eh_launch_pad_layouts`(id, namespace_id, name, layout_json, version_code, min_version_code, status, create_time) VALUES ('5', '0', 'GancLayout', '{"versionCode":"2015072815","versionName":"3.0.0","layoutName":"GancLayout","displayName":"工作站首页","groups":[{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"GaActions"},"style":"Default","defaultOrder":2,"separatorFlag":1,"separatorHeight":21},{"groupName":"","widget":"Posts","instanceConfig":{"itemGroup":"GaPosts"},"style":"Default","defaultOrder":3,"separatorFlag":0,"separatorHeight":0},{"groupName":"CallPhone","widget":"CallPhones","instanceConfig":{"itemGroup":"CallPhones","position":"bottom"},"style":"Default","defaultOrder":3,"separatorFlag":0,"separatorHeight":0}]}', '2015082914', '2015061701', '2', '2015-07-13 16:46:37');
INSERT INTO `eh_launch_pad_layouts`(id, namespace_id, name, layout_json, version_code, min_version_code, status, create_time) VALUES ('6', '0', 'PaymentLayout', '{"versionCode":"2015072815","versionName":"3.0.0","layoutName":"PaymentLayout","displayName":"缴费首页","groups":[{"groupName":"pay","widget":"Navigator","instanceConfig":{"itemGroup":"PayActions"},"style":"Light","defaultOrder":1,"separatorFlag":0,"separatorHeight":0,"columnCount":3}]}', '2015072202', '2015061701', '2', '2015-07-13 16:46:37');

SET foreign_key_checks = 1;
