-- AUTHOR: 缪洲
-- REMARK: 停车缴费V6.7，增加用户须知与发票开启字段
UPDATE `ehcore`.`eh_parking_lots` SET  `config_json` = '{\"tempfeeFlag\":1,\"rateFlag\":1,\"lockCarFlag\":1,\"searchCarFlag\":0,\"currentInfoType\":2,\"invoiceFlag\":1,\"businessLicenseFlag\":0,\"vipParkingFlag\":1,\"monthRechargeFlag\":1,\"identityCardFlag\":0,\"monthCardFlag\":1,\"flowMode\":3,\"noticeFlag\":1}', `func_list` = '[\"vipParking\",\"tempfee\",\"monthRecharge\",\"freePlace\",\"invoiceApply\",\"userNotice\",\"lockCar\",\"searchCar\",\"monthCardApply\"]';


