
INSERT INTO `eh_configurations`(`name`, `value`, `description`) VALUES ('biz.op.serverUrl', 'https://biz.zuolin.com/zl-ec', '弹窗的电商服务器地址');
INSERT INTO `eh_configurations`(`name`, `value`, `description`) VALUES ('biz.op.appKey', '39628d1c-0646-4ff6-9691-2c327b03f9c4', '弹窗的appKey');
INSERT INTO `eh_configurations`(`name`, `value`, `description`) VALUES ('biz.op.secretKey', 'PSsIB9nZm3ENS3stei8oAvGa2afRW7wT+UxBn9li4C7JCfjCtHYJY6x76XDtUCUcXOUhkPYK9V/5r03pD2rquQ==', '弹窗的secretKey');

--海岸物业初始数据
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES 
('94', 'haian.siyuan', 'http://183.62.221.2:8082/NetApp/SYS86Service.asmx/GetSYS86Service', 'the url of siyuan wuye', '0', NULL);
INSERT INTO `eh_pmsy_communities` (`id`, `namespace_id`, `community_id`, `community_token`, `contact`, `bill_tip`) VALUES 
('1', '0', '24210090697427178', '00100120131200000003', '联系电话', '提示信息');
INSERT INTO `eh_pmsy_communities` (`id`, `namespace_id`, `community_id`, `community_token`, `contact`, `bill_tip`) VALUES 
('2', '0', '24210090697427178', '00100120131200000005', '联系电话', '提示信息');
INSERT INTO `eh_pmsy_communities` (`id`, `namespace_id`, `community_id`, `community_token`, `contact`, `bill_tip`) VALUES 
('3', '0', '24210090697427178', '00100120131200000007', '联系电话', '提示信息');
INSERT INTO `eh_pmsy_communities` (`id`, `namespace_id`, `community_id`, `community_token`, `contact`, `bill_tip`) VALUES 
('4', '0', '24210090697427178', '00100120131200000009', '联系电话', '提示信息');
INSERT INTO `eh_pmsy_communities` (`id`, `namespace_id`, `community_id`, `community_token`, `contact`, `bill_tip`) VALUES 
('5', '0', '24210090697427178', '00100120131200000011', '联系电话', '提示信息');
INSERT INTO `eh_pmsy_communities` (`id`, `namespace_id`, `community_id`, `community_token`, `contact`, `bill_tip`) VALUES 
('6', '0', '24210090697427178', '00100120131200000013', '联系电话', '提示信息');
INSERT INTO `eh_pmsy_communities` (`id`, `namespace_id`, `community_id`, `community_token`, `contact`, `bill_tip`) VALUES 
('7', '0', '24210090697427178', '00100120131200000015', '联系电话', '提示信息');
INSERT INTO `eh_pmsy_communities` (`id`, `namespace_id`, `community_id`, `community_token`, `contact`, `bill_tip`) VALUES 
('8', '0', '24210090697427178', '00100120131200000017', '联系电话', '提示信息');