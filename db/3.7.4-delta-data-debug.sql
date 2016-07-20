delete from eh_launch_pad_items where target_type='biz' and scope_code = 0;

INSERT INTO `ehcore`.`eh_configurations` (`name`, `value`, `description`) VALUES ('business.url', 'https://biz-beta.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https://biz-beta.zuolin.com%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fstore%2Fdetails%2F14616591393750460212%3F_k%3Dzlbiz#sign_suffix', 'business url');

INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('group', '10030', 'zh_CN', '呃，好像哪里出错了');