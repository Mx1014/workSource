-- AUTHOR: 杨崇鑫  20180913
-- REMARK: 物业缴费V6.8（海岸馨服务项目对接） ：release：正式环境，beta：测试环境，以此来判断是否需要为海岸造测试数据
update `eh_configurations` set value = 'beta' WHERE `name`='pay.v2.asset.haian_environment';