-- by dengs,司印服务器ip地址 20170615
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('print.siyin.serverUrl', 'http://siyin.zuolin.com:8119', '司印服务器ip地址', '0', NULL);
-- by dengs,默认打印教程、默认复印/扫描教程、默认价格
INSERT INTO `ehcore`.`eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('print', 'print_course_list', 'zh_CN', '在电脑上打开需要打印的文档，点击打印，\n 设置好打印参数，点击确定，电脑上出现二维码 | 打开APP，点击右上角扫一扫按钮，或者进入云打印界面，\n 点击右上角扫一扫按钮，扫描电脑出现的二维码 | APP上出现提示，询问是否立即打印，点击立即打印，\n 打印任务将发送给打印机，等待打印完成即可去打印机上\n 取回打印资料（为了确保打印的资料不被他人取走，您可以\n 走到打印机前再点击“立即打印”） | 打印完成后，APP端生成一个待付款的订单，您可以立即\n 支付，也可以等待下次打印时再进行支付（注意每次打印\n 必须保证上次的订单已付款，否则无法进行打印）\n ');
INSERT INTO `ehcore`.`eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('print', 'scan_copy_course_list', 'zh_CN', '准备好需要复印/扫描的资料，前往打印机处 |  打开APP，进入云打印界面，点击右上角扫一扫按钮，\n扫描打印机上的二维码，填写扫描件接受邮箱 |按照打印机上的指引进行操作，点击扫描/复印，进行\n扫描/复印参数设置，设置好参数进行对应的操作即可 |扫描/复印完成后，点击打印机屏幕右上角的登出按钮\n结束任务（或者直接离开，系统自动超时结束任务）|APP端生成待付款的订单，您可以立即支付，\n也可以等待下次扫描/复印时再进行支付\n（注意每次扫描/复印前必须保证上次的订单已付款，否则无法进行扫描/复印）');
INSERT INTO `ehcore`.`eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('print', 'print_default_price', 'zh_CN', '0.1');
INSERT INTO `ehcore`.`eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('print', 'inform_print_url', 'zh_CN', 'http://core.zuolin.com/evh/siyinprint/informPrint?identifierToken=');
