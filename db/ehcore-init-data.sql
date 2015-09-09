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
    VALUES(3001, 3, 0, '教育', '商家和服务/教育', 0, 2, UTC_TIMESTAMP());	
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(3002, 3, 0, '家政与维修', '商家和服务/家政与维修', 0, 2, UTC_TIMESTAMP());	
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(3003, 3, 0, '休闲与运动', '商家和服务/休闲与运动', 0, 2, UTC_TIMESTAMP());	
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(3004, 3, 0, '便利店与超市', '商家和服务/便利店与超市', 0, 2, UTC_TIMESTAMP());	
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(3005, 3, 0, '水果店', '商家和服务/水果店', 0, 2, UTC_TIMESTAMP());	
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(3006, 3, 0, '餐饮', '商家和服务/餐饮', 0, 2, UTC_TIMESTAMP());	
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(3007, 3, 0, '宠物', '商家和服务/宠物', 0, 2, UTC_TIMESTAMP());	
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(3008, 3, 0, '公共服务', '商家和服务/公共服务', 0, 2, UTC_TIMESTAMP());	
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(3009, 3, 0, '其他', '商家和服务/其他', 1, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(3010, 3002, 0, '家政', '商家和服务/家政与维修/家政', 0, 2, UTC_TIMESTAMP());	
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(3011, 3002, 0, '维修', '商家和服务/家政与维修/维修', 0, 2, UTC_TIMESTAMP());	
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(3012, 3003, 0, '休闲', '商家和服务/休闲与运动/休闲', 0, 2, UTC_TIMESTAMP());	
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(3013, 3003, 0, '运动', '商家和服务/休闲与运动/运动', 0, 2, UTC_TIMESTAMP());	
	
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
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'forum', '10006', 'zh_CN', '片区所在小区不存在');
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

INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'messaging', '1', 'zh_CN', '你有新消息');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'messaging', '2', 'zh_CN', '你有新的语音消息');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'messaging', '3', 'zh_CN', '你有新的图片消息');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'messaging', '4', 'zh_CN', '你有新的视频消息');


INSERT INTO `eh_apps` (`id`,`creator_uid`,`app_key`,`secret_key`,`name`,`description`,`status`,`create_time`) VALUES (100,NULL,'b86ddb3b-ac77-4a65-ae03-7e8482a3db70','2-0cDFNOq-zPzYGtdS8xxqnkR8PRgNhpHcWoku6Ob49NdBw8D9-Q72MLsCidI43IKhP1D_43ujSFbatGPWuVBQ','SystemExtension',NULL,1,'2015-08-24 14:14:16');
INSERT INTO `eh_apps` (`id`,`creator_uid`,`app_key`,`secret_key`,`name`,`description`,`status`,`create_time`) VALUES (101,1,'d80e06ca-3766-11e5-b18f-b083fe4e159f','g1JOZUM3BYzWpZD5Q7p3z+i/z0nj2TcokTFx2ic53FCMRIKbMhSUCi7fSu9ZklFCZ9tlj68unxur9qmOji4tNg==','test','dianshang test',1,'2015-08-24 14:17:53');
INSERT INTO `eh_apps` (`id`,`creator_uid`,`app_key`,`secret_key`,`name`,`description`,`status`,`create_time`) VALUES (102,1,'93e8275c-31e2-11e5-b7ad-b083fe4e159f','2nDpmzJj63Un0GzXyeZKUKlVSOKzNHv4FidFL9uCpNaLq6rqE0VAOv3uPaR0jWIRMNqedgci3vzLPAkaX1jg6Q==','door','door open',1,'2015-09-01 14:39:18');
INSERT INTO `eh_apps` (`id`,`creator_uid`,`app_key`,`secret_key`,`name`,`description`,`status`,`create_time`) VALUES (103,1,'cePPQVM-I4cN73W_ZQZ6VkISDgHNAvUBZPjH2J','Jm38pWr5XEvdXnHmNuBb4PbzaP-sIpEZvcmvxC9S4ypDVRql-KUbN0Dq_Djv-GxZ','common-pay','',1,'2015-09-01 17:43:53');
# INSERT INTO `eh_apps` (`id`,`creator_uid`,`app_key`,`secret_key`,`name`,`description`,`status`,`create_time`) VALUES (104,1,'93e8275c-31e2-11e5-b7ad-b083fe4e159f','2nDpmzJj63Un0GzXyeZKUKlVSOKzNHv4FidFL9uCpNaLq6rqE0VAOv3uPaR0jWIRMNqedgci3vzLPAkaX1jg6Q==','app','app',1,'2015-09-01 14:39:18');

SET foreign_key_checks = 1;
