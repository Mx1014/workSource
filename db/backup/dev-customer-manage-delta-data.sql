-- 在业主类型表中添加数据      2016/09/02 by xq.tian
INSERT INTO `eh_organization_owner_type` (`id`, `namespace_id`, `name`, `display_name`, `status`) VALUES ('1', '0', 'owner', '业主', '1');
INSERT INTO `eh_organization_owner_type` (`id`, `namespace_id`, `name`, `display_name`, `status`) VALUES ('2', '0', 'tenement', '租客', '1');
INSERT INTO `eh_organization_owner_type` (`id`, `namespace_id`, `name`, `display_name`, `status`) VALUES ('3', '0', 'relative', '亲属', '1');
INSERT INTO `eh_organization_owner_type` (`id`, `namespace_id`, `name`, `display_name`, `status`) VALUES ('4', '0', 'friend', '朋友', '1');
INSERT INTO `eh_organization_owner_type` (`id`, `namespace_id`, `name`, `display_name`, `status`) VALUES ('5', '0', 'nurse', '保姆', '1');
INSERT INTO `eh_organization_owner_type` (`id`, `namespace_id`, `name`, `display_name`, `status`) VALUES ('6', '0', 'agency', '地产中介', '1');
INSERT INTO `eh_organization_owner_type` (`id`, `namespace_id`, `name`, `display_name`, `status`) VALUES ('7', '0', 'readyauth', '待审核', '1');
INSERT INTO `eh_organization_owner_type` (`id`, `namespace_id`, `name`, `display_name`, `status`) VALUES ('8', '0', 'other', '其他', '1');

-- 插入模板       2016/09/02 by xq.tian
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('orgOwnerBehavior', 'immigration', 'zh_CN', '迁入');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('orgOwnerBehavior', 'emigration', 'zh_CN', '迁出');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('orgOwnerAddressAuthType', '2', 'zh_CN', '无效');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('orgOwnerAddressAuthType', '0', 'zh_CN', '未认证');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('orgOwnerAddressAuthType', '1', 'zh_CN', '认证');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('orgOwnerAddressLivingStatus', '0', 'zh_CN', '否');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('orgOwnerAddressLivingStatus', '1', 'zh_CN', '是');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('ownerCarPrimaryFlag', '0', 'zh_CN', '否');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('ownerCarPrimaryFlag', '1', 'zh_CN', '是');



-- 添加模板    2016/09/02 by xq.tian
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('pm', '14001', 'zh_CN', '业主已存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('pm', '14002', 'zh_CN', '业主不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('pm', '14003', 'zh_CN', '客户手机号码重复');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('pm', '15001', 'zh_CN', '导入失败');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('pm', '16001', 'zh_CN', '车辆已存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('pm', '16002', 'zh_CN', '车辆不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('pm', '16003', 'zh_CN', '车牌号码重复');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('pm', '16004', 'zh_CN', '用户已经在车辆的使用者列表中');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('pm', '17001', 'zh_CN', '用户已经在楼栋门牌中');






INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('parkingType', '2', 'zh_CN', '月卡');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('parkingType', '1', 'zh_CN', '临时');

INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('user', '1', 'zh_CN', '男');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('user', '2', 'zh_CN', '女');



