-- 增加资源预订到期文案 by st.zheng
set @eh_locale_templates_id = (select max(id) from eh_locale_templates);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@eh_locale_templates_id:=@eh_locale_templates_id+1, 'rental.notification', '10', 'zh_CN', '资源到期给资源负责人推送提醒(半天/小时)', '温馨提醒：${resourceName}资源的使用将在15分钟后结束，使用客户${requestorName}(${requestorPhone}),请进行确认', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@eh_locale_templates_id:=@eh_locale_templates_id+1, 'rental.notification', '11', 'zh_CN', '资源到期给资源负责人推送提醒(天/月)', '温馨提醒：${resourceName}资源的使用将在今日结束，使用客户${requestorName}(${requestorPhone}),请进行确认', '0');

set @eh_locale_strings_id = (select max(id) from eh_locale_strings);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@eh_locale_strings_id:=@eh_locale_strings_id+1, 'rental.flow', 'authKey', 'zh_CN', '门禁二维码');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@eh_locale_strings_id:=@eh_locale_strings_id+1, 'rental.flow', 'authValue', 'zh_CN', '在二维码有效期内前往门禁功能查看二维码');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@eh_locale_strings_id:=@eh_locale_strings_id+1, 'rental.flow', 'authTime', 'zh_CN', '二维码有效期');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@eh_locale_strings_id:=@eh_locale_strings_id+1, 'rental.flow', 'packageName', 'zh_CN', '使用套餐');

-- 修正消息模板 by lei.lv
update eh_locale_templates set text = '${userName}(${userToken})${description}申请加入公司“${enterpriseName}”，是否同意？' where scope = 'enterprise.notification' and code = 7;
update eh_locale_templates set text = '${userName}(${userToken})已加入公司“${enterpriseName}”。' where scope = 'enterprise.notification' and code = 2;
update eh_locale_templates set text = '${userName}(${userToken})已离开公司“${enterpriseName}”。' where scope = 'enterprise.notification' and code = 5;

-- merge from customer20171108 add by xiongying
INSERT INTO `eh_var_field_groups` (`id`, `module_name`, `parent_id`, `path`, `title`, `name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`) 
VALUES (27, 'enterprise_customer', '0', '/27', '客户合同', '', '0', NULL, '2', '1', NOW());

set @field_id = (SELECT MAX(id) from eh_var_fields);
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) 
VALUES ((@field_id := @field_id + 1), 'enterprise_customer', 'month', '年月', 'Long', '9', '/9', '0', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"datetime\", \"length\": 32}');

-- bug 16915 【客户管理-动态字段配置】人才团队tab页个人证书、主要职业经历建议为多行文本（优化）
update eh_var_fields set field_param = '{"fieldParamType": "multiText", "length": 2048}' where name = 'personalCertificate';
update eh_var_fields set field_param = '{"fieldParamType": "multiText", "length": 2048}' where name = 'careerExperience';


-- 张江高科基础数据
-- 楼栋篇
update eh_buildings set namespace_building_type = 'shenzhou', namespace_building_token = '7AAA3813-6775-4928-84BE-2D26199D1F02' where namespace_id = 999971 and name = '张东路1387号27幢';
update eh_buildings set namespace_building_type = 'shenzhou', namespace_building_token = 'C2B0007F-2AA0-4DD9-83DE-3638CD312D52' where namespace_id = 999971 and name = '张东路1387号11幢';
update eh_buildings set namespace_building_type = 'shenzhou', namespace_building_token = '67BA3829-D743-413D-A1E9-4DA284261BAF' where namespace_id = 999971 and name = '张东路1387号33幢';
update eh_buildings set namespace_building_type = 'shenzhou', namespace_building_token = 'BF296B93-9502-466D-BB7E-5EF6FE143C9A' where namespace_id = 999971 and name = '张东路1387号2幢';
update eh_buildings set namespace_building_type = 'shenzhou', namespace_building_token = '1FF16DA3-D16B-404A-8375-6B8277152C2B' where namespace_id = 999971 and name = '张东路1387号1幢';
update eh_buildings set namespace_building_type = 'shenzhou', namespace_building_token = 'DCDF67BC-AA70-449A-9B6C-6DD2B36A6D51' where namespace_id = 999971 and name = '张东路1387号7幢';
update eh_buildings set namespace_building_type = 'shenzhou', namespace_building_token = 'A028AEEA-86B2-4B08-8626-763ED23EE80B' where namespace_id = 999971 and name = '张东路1387号34幢';
update eh_buildings set namespace_building_type = 'shenzhou', namespace_building_token = 'AACBE13F-ABD7-4925-9EAC-879EA9350EEA' where namespace_id = 999971 and name = '张东路1387号50幢';
update eh_buildings set namespace_building_type = 'shenzhou', namespace_building_token = 'FAF3E20B-8BF2-46B0-AB6B-950BD86E512E' where namespace_id = 999971 and name = '张东路1387号32幢';
update eh_buildings set namespace_building_type = 'shenzhou', namespace_building_token = 'C9E184F9-C6E8-4730-B1CD-98506F6EA639' where namespace_id = 999971 and name = '张东路1387号46幢';
update eh_buildings set namespace_building_type = 'shenzhou', namespace_building_token = '65C841AF-2447-417B-B7A0-9A66BA279AEF' where namespace_id = 999971 and name = '张东路1387号45幢';
update eh_buildings set namespace_building_type = 'shenzhou', namespace_building_token = 'F1D01B3A-540D-420C-8D21-A5B2118B2DF2' where namespace_id = 999971 and name = '张东路1387号49幢';
update eh_buildings set namespace_building_type = 'shenzhou', namespace_building_token = '03F886C7-B79C-42A9-B4B2-A7213C58B67F' where namespace_id = 999971 and name = '张东路1387号51幢';
update eh_buildings set namespace_building_type = 'shenzhou', namespace_building_token = 'B206C68F-15EC-467B-9BE8-A27FDC470B44' where namespace_id = 999971 and name = '张东路1387号47幢';
update eh_buildings set namespace_building_type = 'shenzhou', namespace_building_token = 'D536A442-BACA-43D5-9BFC-B3595E1A4EB2' where namespace_id = 999971 and name = '张东路1387号39幢';
update eh_buildings set namespace_building_type = 'shenzhou', namespace_building_token = '0F03B8A0-8A08-4EF2-9FF6-C9806A6834A8' where namespace_id = 999971 and name = '张东路1387号43幢';
update eh_buildings set namespace_building_type = 'shenzhou', namespace_building_token = 'CBE3E817-8556-4C33-B280-F06C0DDCFE3B' where namespace_id = 999971 and name = '张东路1387号10幢';
update eh_buildings set namespace_building_type = 'shenzhou', namespace_building_token = '768A176B-8906-47D5-B261-F4A3676E2B25' where namespace_id = 999971 and name = '张东路1387号48幢';
update eh_buildings set namespace_building_type = 'shenzhou', namespace_building_token = '6ED12712-4262-45ED-9E50-A330353808BC' where namespace_id = 999971 and name = '560号1幢-地下';
update eh_buildings set namespace_building_type = 'shenzhou', namespace_building_token = '45CAF993-6C60-4237-85E7-B17C88125A34' where namespace_id = 999971 and name = '松涛路560号1幢';
update eh_buildings set namespace_building_type = 'shenzhou', namespace_building_token = '3C816724-EB3C-4AA8-BE3B-A79EFF5A1E51' where namespace_id = 999971 and name = '289号1幢-地下';
update eh_buildings set namespace_building_type = 'shenzhou', namespace_building_token = '379E1E3E-6999-40AD-90E8-9655159D5627' where namespace_id = 999971 and name = '春晓路289号1幢';
update eh_buildings set namespace_building_type = 'shenzhou', namespace_building_token = 'AA236B5E-E252-4DD9-ABEA-A8A31A835457' where namespace_id = 999971 and name = '张东路1387号创新之家E幢';
update eh_buildings set namespace_building_type = 'shenzhou', namespace_building_token = 'DD53037E-82FF-40FA-9C1A-D93F1D39C5DD' where namespace_id = 999971 and name = '张东路1387号21幢地下（社区中心）';
update eh_buildings set namespace_building_type = 'shenzhou', namespace_building_token = 'ACC94EC2-62C7-4383-B14D-E2FFA7202459' where namespace_id = 999971 and name = '张东路1387号社区中心';
update eh_buildings set namespace_building_type = 'shenzhou', namespace_building_token = '20CAA987-50A5-4205-B71A-776AAA174737' where namespace_id = 999971 and name = '创新之家中厅';
update eh_buildings set namespace_building_type = 'shenzhou', namespace_building_token = '8F81E55D-5D32-4FAA-9EAD-2E031BFD5161' where namespace_id = 999971 and name = '张东路1387号创新之家A幢';
update eh_buildings set namespace_building_type = 'shenzhou', namespace_building_token = '46511A55-1CD0-4AFB-BBD6-2A5418393CBA' where namespace_id = 999971 and name = '张东路1387号创新之家CD幢';
update eh_buildings set namespace_building_type = 'shenzhou', namespace_building_token = 'B6F94B3A-184A-4171-98AC-F24DD1D4BABE' where namespace_id = 999971 and name = '创新之家食堂';
update eh_buildings set namespace_building_type = 'shenzhou', namespace_building_token = '07BD2F30-25F7-4FC4-AEE9-E8FFA4D3B238' where namespace_id = 999971 and name = '张东路1387号创新之家B幢';
update eh_buildings set namespace_building_type = 'shenzhou', namespace_building_token = '1C319258-EDB1-4CCF-9EB8-EE24D84CBAD9' where namespace_id = 999971 and name = '张东路1388号12幢';
update eh_buildings set namespace_building_type = 'shenzhou', namespace_building_token = '2C45A2F3-D1D3-44F2-A32E-F1305603ED2E' where namespace_id = 999971 and name = '张东路1388号28幢地下1层';
update eh_buildings set namespace_building_type = 'shenzhou', namespace_building_token = 'E8E65161-7361-42B6-A332-F6241E46DF02' where namespace_id = 999971 and name = '张东路1388号1幢';
update eh_buildings set namespace_building_type = 'shenzhou', namespace_building_token = 'FB5C632A-DD5C-4C94-97C6-158B56764F44' where namespace_id = 999971 and name = '56幢';
update eh_buildings set namespace_building_type = 'shenzhou', namespace_building_token = '4CAEA658-E22C-4BD5-B216-5084E7BD64A0' where namespace_id = 999971 and name = '58幢';
update eh_buildings set namespace_building_type = 'shenzhou', namespace_building_token = '103CD51A-B147-40D9-B358-39695F700306' where namespace_id = 999971 and name = '55幢';
update eh_buildings set namespace_building_type = 'shenzhou', namespace_building_token = 'F769CD5D-86F3-4D5C-83A3-42F62AC85D87' where namespace_id = 999971 and name = '张东路1388号22、23幢';
update eh_buildings set namespace_building_type = 'shenzhou', namespace_building_token = 'DB832699-8467-406E-9A42-85B550CE1CEF' where namespace_id = 999971 and name = '57幢';
update eh_buildings set namespace_building_type = 'shenzhou', namespace_building_token = 'C5BA07D1-0573-4F69-BC5C-94646E42827E' where namespace_id = 999971 and name = '53幢';
update eh_buildings set namespace_building_type = 'shenzhou', namespace_building_token = '73388C88-6CEA-48C0-B8B4-763E7586EFBE' where namespace_id = 999971 and name = '张东路1388号2幢';
update eh_buildings set namespace_building_type = 'shenzhou', namespace_building_token = '9CBEE67F-4E8F-453E-9EB8-E338590C66F8' where namespace_id = 999971 and name = '张东路1388号28幢02';
update eh_buildings set namespace_building_type = 'shenzhou', namespace_building_token = '03024249-B3AA-4B9C-AA86-D268B47663B9' where namespace_id = 999971 and name = '54幢';
update eh_buildings set namespace_building_type = 'shenzhou', namespace_building_token = '6BA9BAE7-83F3-4869-A814-9C751A72394D' where namespace_id = 999971 and name = '张东路1388号21幢';
update eh_buildings set namespace_building_type = 'shenzhou', namespace_building_token = '1D4BD124-12AB-468D-8C80-A893A24B264A' where namespace_id = 999971 and name = '张东路1388号25幢';

-- 门牌篇
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = 'B647286C-275D-42E3-A21E-440FEB980F5C' where namespace_id = 999971 and building_name = '张东路1387号10幢' and apartment_name = '102-1层';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = 'F31D98B9-3111-4C0A-9914-8D6EEE2B697D' where namespace_id = 999971 and building_name = '张东路1387号10幢' and apartment_name = '101-1-2层';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '703C5D16-C52F-4346-B881-921665C43429' where namespace_id = 999971 and building_name = '张东路1387号10幢' and apartment_name = '101-3层';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '14F2E4DF-B17B-4A89-84B0-977D39CA5455' where namespace_id = 999971 and building_name = '张东路1387号10幢' and apartment_name = '102-2-3层';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '5CCA2EE5-0876-4668-93E2-294D63BEE953' where namespace_id = 999971 and building_name = '张东路1387号11幢' and apartment_name = '102';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = 'DA9F2796-A249-41CC-9AAC-FB1F7AF0F38D' where namespace_id = 999971 and building_name = '张东路1387号11幢' and apartment_name = '101';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '47E77307-CCBE-4BAD-BB6E-2031E900A86C' where namespace_id = 999971 and building_name = '张东路1387号1幢' and apartment_name = '102';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = 'CD38C267-24D5-4310-9E42-C90C7A2D0999' where namespace_id = 999971 and building_name = '张东路1387号1幢' and apartment_name = '101';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '65DA37AC-6F03-4A22-8476-0B1BBF026E23' where namespace_id = 999971 and building_name = '张东路1387号27幢' and apartment_name = '101-1-2层';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '7AF06CDF-CF48-4AC9-BF4B-1AFA9FE0CB6E' where namespace_id = 999971 and building_name = '张东路1387号27幢' and apartment_name = '101-3-4层';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = 'D060635C-666F-42E3-BB26-2105E34680E0' where namespace_id = 999971 and building_name = '张东路1387号2幢' and apartment_name = '102';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = 'A47B8D6F-786A-4F52-90EC-BE42037C28B3' where namespace_id = 999971 and building_name = '张东路1387号2幢' and apartment_name = '101';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '7DA1004A-D0FE-42E3-B58F-B00D1F4C8D21' where namespace_id = 999971 and building_name = '张东路1387号32幢' and apartment_name = '101';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = 'C51000C8-1BF0-4F4B-8DB0-30BA7820360A' where namespace_id = 999971 and building_name = '张东路1387号33幢' and apartment_name = '102-3层';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = 'EC8CB732-A7DB-41E3-BDE3-44D85FE358D4' where namespace_id = 999971 and building_name = '张东路1387号33幢' and apartment_name = '102-1层';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '550BFCB0-F901-421A-BD87-97218C273CB3' where namespace_id = 999971 and building_name = '张东路1387号33幢' and apartment_name = '102-2层';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = 'E7BB3B98-396B-4DD3-8EC7-D5623A676CA2' where namespace_id = 999971 and building_name = '张东路1387号33幢' and apartment_name = '101';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '825681B6-1367-421E-ACBA-E61211E3F670' where namespace_id = 999971 and building_name = '张东路1387号33幢' and apartment_name = '102-4层';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = 'D64D2BA6-44B8-4A1B-9785-3FDD47E0B454' where namespace_id = 999971 and building_name = '张东路1387号34幢' and apartment_name = '102';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = 'E9EAE9D7-E879-41E6-AF59-E4EF8A56D3CE' where namespace_id = 999971 and building_name = '张东路1387号34幢' and apartment_name = '101';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = 'B447C76B-29AC-47CB-911B-F162E6215F1F' where namespace_id = 999971 and building_name = '张东路1387号39幢' and apartment_name = '101';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '4EF3583A-307F-494A-8AC9-5E49249D02C0' where namespace_id = 999971 and building_name = '张东路1387号43幢' and apartment_name = '101';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = 'BBCE2372-8071-4D21-B7B3-AE89D086D8C3' where namespace_id = 999971 and building_name = '张东路1387号45幢' and apartment_name = '101';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = 'D8C7210E-E984-4380-B3BB-06F18CB51507' where namespace_id = 999971 and building_name = '张东路1387号46幢' and apartment_name = '101';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = 'DC009DB9-0612-4AC9-BDC7-0AC0D2B1F529' where namespace_id = 999971 and building_name = '张东路1387号47幢' and apartment_name = '101';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = 'B52B44D2-A872-4104-961F-4A8EE3BD692A' where namespace_id = 999971 and building_name = '张东路1387号48幢' and apartment_name = '101';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '96F2BEB6-5B84-414E-933A-D13B31698498' where namespace_id = 999971 and building_name = '张东路1387号49幢' and apartment_name = '101';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = 'E50EB39C-EA49-446E-AFE7-F90437CF55A3' where namespace_id = 999971 and building_name = '张东路1387号50幢' and apartment_name = '101';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = 'A56B7AD5-A3A1-431B-A3EF-66C4666689CB' where namespace_id = 999971 and building_name = '张东路1387号51幢' and apartment_name = '101';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = 'CA7F4E26-D367-4906-A7A6-B9604EFAC39E' where namespace_id = 999971 and building_name = '张东路1387号51幢' and apartment_name = '102';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '94F62932-346B-4354-982E-AD0B3D16B1FF' where namespace_id = 999971 and building_name = '张东路1387号7幢' and apartment_name = '101';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '28378133-5487-4092-BFB7-17C99A52580B' where namespace_id = 999971 and building_name = '289号1幢-地下' and apartment_name = '-1层车库';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = 'CEE65622-9988-4C7E-B5B9-26E1308E5203' where namespace_id = 999971 and building_name = '289号1幢-地下' and apartment_name = '-1层泵房';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '0CAD56D4-E73A-4C5B-8B08-AE090F7E82FC' where namespace_id = 999971 and building_name = '289号1幢-地下' and apartment_name = '-1层-食堂';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '91F78880-1A85-44E2-BBB1-10FE6D1D1C48' where namespace_id = 999971 and building_name = '560号1幢-地下' and apartment_name = '-1层';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '282B01CB-4B8D-49C3-8F25-227A8E1FF056' where namespace_id = 999971 and building_name = '560号1幢-地下' and apartment_name = '机房';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '31C3E3C4-62DE-4958-8ED9-235539FCAE75' where namespace_id = 999971 and building_name = '560号1幢-地下' and apartment_name = '机房（移动）';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = 'F5DDFF48-8379-462A-99CF-39A3DE7A27D7' where namespace_id = 999971 and building_name = '560号1幢-地下' and apartment_name = '地下1层人防';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '22339BF6-4FF0-4719-9F5D-9DB952D13B53' where namespace_id = 999971 and building_name = '560号1幢-地下' and apartment_name = '-1';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = 'B0FE0949-937E-403D-A2E0-18F66CC701AB' where namespace_id = 999971 and building_name = '春晓路289号1幢' and apartment_name = '1502';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '3BD0202C-8BD1-4C54-AF90-2E20BF27FADA' where namespace_id = 999971 and building_name = '春晓路289号1幢' and apartment_name = '1602';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '10630918-CC9C-4326-8B54-2FDD8E76DE56' where namespace_id = 999971 and building_name = '春晓路289号1幢' and apartment_name = '1501';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '4F239A80-0A4F-48A8-BD90-3886CCAE0336' where namespace_id = 999971 and building_name = '春晓路289号1幢' and apartment_name = '802';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '3CEC33C6-8A8C-4033-8AA7-4E53BF3193D3' where namespace_id = 999971 and building_name = '春晓路289号1幢' and apartment_name = '1301';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = 'BC5C530B-7C15-48F1-94C9-598B0B7DE4C2' where namespace_id = 999971 and building_name = '春晓路289号1幢' and apartment_name = '1802-A';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '68B857D9-0244-45CD-9368-74D0B3D1DC5F' where namespace_id = 999971 and building_name = '春晓路289号1幢' and apartment_name = '701';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = 'C9AEB01F-0004-4518-BA9E-7D79A358F52B' where namespace_id = 999971 and building_name = '春晓路289号1幢' and apartment_name = '1802-B';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = 'A87EBF81-92B8-4069-96E9-8938455E0795' where namespace_id = 999971 and building_name = '春晓路289号1幢' and apartment_name = '201';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '6AABAE06-6D80-47CF-9F08-AB7E4CF1DF9A' where namespace_id = 999971 and building_name = '春晓路289号1幢' and apartment_name = '702';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = 'CCA7F2B7-C36B-44BA-BE9F-C487D0A02AEA' where namespace_id = 999971 and building_name = '春晓路289号1幢' and apartment_name = '1402';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '55E19272-4377-46C7-BB98-CC4DB027C651' where namespace_id = 999971 and building_name = '春晓路289号1幢' and apartment_name = '1201';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = 'DAB7870A-E7DD-4E84-B3F5-F6D9CF3E1444' where namespace_id = 999971 and building_name = '春晓路289号1幢' and apartment_name = '1801';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '5F8CC445-2008-409B-8473-0426627F32C1' where namespace_id = 999971 and building_name = '松涛路560号1幢' and apartment_name = '1层甲';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '213096E3-1EB1-408D-9688-0E9ECBD4E6CD' where namespace_id = 999971 and building_name = '松涛路560号1幢' and apartment_name = '1层甲-04';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '364AB4AC-DA41-40BD-BFB2-45C326DE8772' where namespace_id = 999971 and building_name = '松涛路560号1幢' and apartment_name = '1层甲-01';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '21134045-11C5-4989-A957-A6EDDDF583A9' where namespace_id = 999971 and building_name = '张东路1387号21幢地下（社区中心）' and apartment_name = '基站（彭茂）';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = 'FD8982B1-2C90-404E-A1A1-ACB0B4E7A94E' where namespace_id = 999971 and building_name = '张东路1387号21幢地下（社区中心）' and apartment_name = '基站';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = 'AFBA8884-D318-48A5-80F3-B5405A1DD6FC' where namespace_id = 999971 and building_name = '张东路1387号21幢地下（社区中心）' and apartment_name = '配套';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '2DD6A7FB-0821-4F0F-9742-D55485553A7A' where namespace_id = 999971 and building_name = '张东路1387号21幢地下（社区中心）' and apartment_name = '餐厅';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = 'ECB1067C-A92A-4C19-B01F-01CA544EF73A' where namespace_id = 999971 and building_name = '张东路1387号创新之家A幢' and apartment_name = '2层A215';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '8051916C-1EBF-47A4-8D05-056E4E1AD76D' where namespace_id = 999971 and building_name = '张东路1387号创新之家A幢' and apartment_name = '3层A310';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = 'E51E1F2E-EE09-4583-A155-2282BA32443C' where namespace_id = 999971 and building_name = '张东路1387号创新之家A幢' and apartment_name = '3层A304';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '10174F65-E97D-4B6F-A4C2-29B6A028CB0B' where namespace_id = 999971 and building_name = '张东路1387号创新之家A幢' and apartment_name = '2层A212';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = 'BEF3E7CA-563F-4E32-B29C-2C2A23621FA0' where namespace_id = 999971 and building_name = '张东路1387号创新之家A幢' and apartment_name = '3层A307';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = 'FB4B5CA1-2860-46C0-8297-2C398DE0A8FE' where namespace_id = 999971 and building_name = '张东路1387号创新之家A幢' and apartment_name = '2层A202';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '66DFA5E3-9E8A-475F-BE2C-2E03F5F10E73' where namespace_id = 999971 and building_name = '张东路1387号创新之家A幢' and apartment_name = '1层A102';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = 'AE132E14-2BE3-4CB6-874B-2F4FFC8CB528' where namespace_id = 999971 and building_name = '张东路1387号创新之家A幢' and apartment_name = '3层A301';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '08DA734B-6854-4259-8FED-31BF913BB6A5' where namespace_id = 999971 and building_name = '张东路1387号创新之家A幢' and apartment_name = '2层A203';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '0E5BCE17-E35A-4404-A961-39CB19CBB95E' where namespace_id = 999971 and building_name = '张东路1387号创新之家A幢' and apartment_name = '2层A213';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '058A1FD8-A18D-420E-978A-44ECF57D6E78' where namespace_id = 999971 and building_name = '张东路1387号创新之家A幢' and apartment_name = '3层A303';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '9D2B3CE9-38A5-4643-878E-4E7A2C540BFF' where namespace_id = 999971 and building_name = '张东路1387号创新之家A幢' and apartment_name = '1层A106';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = 'B11604EF-6A54-4317-8468-5075447EB64D' where namespace_id = 999971 and building_name = '张东路1387号创新之家A幢' and apartment_name = '2层A205';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '8AFB95A0-6C18-4537-88AC-5E32A4D996E5' where namespace_id = 999971 and building_name = '张东路1387号创新之家A幢' and apartment_name = '1层A104';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = 'B0E695CE-7B80-4E63-88CF-6226373C5D64' where namespace_id = 999971 and building_name = '张东路1387号创新之家A幢' and apartment_name = '2层A207';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '90768F15-77C7-4D4C-B0A2-63F677E1DB7E' where namespace_id = 999971 and building_name = '张东路1387号创新之家A幢' and apartment_name = '3层A305';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '116D1EA4-00EE-4C42-84D0-71D9BB2DD1B6' where namespace_id = 999971 and building_name = '张东路1387号创新之家A幢' and apartment_name = '2层A209';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = 'CD9EF8AE-4E95-4334-90C8-744CA38B4269' where namespace_id = 999971 and building_name = '张东路1387号创新之家A幢' and apartment_name = '1层A105';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = 'D23677A1-03BE-408C-B340-7638B2D33C4F' where namespace_id = 999971 and building_name = '张东路1387号创新之家A幢' and apartment_name = '1层A101';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '14308762-A255-4A83-AE09-A27444378317' where namespace_id = 999971 and building_name = '张东路1387号创新之家A幢' and apartment_name = '2层A206';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '3A788484-72B4-49B6-8DEB-A743ECE52033' where namespace_id = 999971 and building_name = '张东路1387号创新之家A幢' and apartment_name = '1层A107';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = 'CDE0172B-B368-48B5-AE53-A9B3C290CE4D' where namespace_id = 999971 and building_name = '张东路1387号创新之家A幢' and apartment_name = '3层A306';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '8425960A-A602-4FDC-82F6-AAE1153D03D3' where namespace_id = 999971 and building_name = '张东路1387号创新之家A幢' and apartment_name = '3层A309';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '3A0CBA65-EDC5-476D-AA80-AFF132BA1444' where namespace_id = 999971 and building_name = '张东路1387号创新之家A幢' and apartment_name = '2层A201';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '6E2859B8-9FD5-4D31-88EA-BB02D14D80BC' where namespace_id = 999971 and building_name = '张东路1387号创新之家A幢' and apartment_name = '2层A214';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '24CEA152-6C3A-4699-B5D2-BF8E40C2C059' where namespace_id = 999971 and building_name = '张东路1387号创新之家A幢' and apartment_name = '2层A204';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = 'D90F6DF1-B527-4F70-A8E9-D1A4A739039F' where namespace_id = 999971 and building_name = '张东路1387号创新之家A幢' and apartment_name = '2层A210';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = 'D0C09B06-61C5-4FB0-B012-D6D2A5FCD551' where namespace_id = 999971 and building_name = '张东路1387号创新之家A幢' and apartment_name = '1层A103';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '9D5D86B6-9A53-4DB4-A418-EBE9AA1049A2' where namespace_id = 999971 and building_name = '张东路1387号创新之家A幢' and apartment_name = '2层A211';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = 'D03D3A8B-269B-4DDD-9533-F1974097F2E1' where namespace_id = 999971 and building_name = '张东路1387号创新之家A幢' and apartment_name = '2层A208';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '094514D9-84F7-44C9-95F3-F8EDF204EF4B' where namespace_id = 999971 and building_name = '张东路1387号创新之家A幢' and apartment_name = '3层A308';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = 'CDE629F8-04DB-47A1-A614-FDEF7E355659' where namespace_id = 999971 and building_name = '张东路1387号创新之家A幢' and apartment_name = '3层A302';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '087BCC1A-DDB9-48C2-B4C5-00FF53D94658' where namespace_id = 999971 and building_name = '张东路1387号创新之家B幢' and apartment_name = '2层B（201-207）';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '1CD93966-39C8-413B-A49A-30488524138E' where namespace_id = 999971 and building_name = '张东路1387号创新之家B幢' and apartment_name = '1层B107+108';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '0D52CE14-0168-4696-86C2-6E9D2CC7EF9C' where namespace_id = 999971 and building_name = '张东路1387号创新之家B幢' and apartment_name = '1层B（103-105）';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '2CBB2767-9E2F-4314-9935-6FEB6AE7F731' where namespace_id = 999971 and building_name = '张东路1387号创新之家B幢' and apartment_name = '1层B101';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '5162026D-9A30-4A76-8362-DA4610BA530F' where namespace_id = 999971 and building_name = '张东路1387号创新之家B幢' and apartment_name = '3层B（301-305）';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '7974CC73-166C-4007-9090-DBE8EF63F622' where namespace_id = 999971 and building_name = '张东路1387号创新之家B幢' and apartment_name = '1层B102';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = 'FDE29BD1-6531-41E0-979E-F61941C1E195' where namespace_id = 999971 and building_name = '张东路1387号创新之家B幢' and apartment_name = '1层B106+109';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = 'A8621C3C-2567-4FFC-A4BB-0A567C42267E' where namespace_id = 999971 and building_name = '张东路1387号创新之家CD幢' and apartment_name = '2层207';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '35789C47-E524-4AF9-ABC4-247D2A998646' where namespace_id = 999971 and building_name = '张东路1387号创新之家CD幢' and apartment_name = '2层213';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '679DD69E-26F1-40D3-BC04-45CF786CEBA4' where namespace_id = 999971 and building_name = '张东路1387号创新之家CD幢' and apartment_name = '2层212';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = 'E576E854-B621-4C04-8CEE-48C0423F126B' where namespace_id = 999971 and building_name = '张东路1387号创新之家CD幢' and apartment_name = '2层211';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = 'A7A85EF5-C52E-4A96-AC6F-497F9280E350' where namespace_id = 999971 and building_name = '张东路1387号创新之家CD幢' and apartment_name = '3层D（301、304-307）';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '2335BD69-6D9D-44DC-9A1E-7602B41869AE' where namespace_id = 999971 and building_name = '张东路1387号创新之家CD幢' and apartment_name = '2层206';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '5808FE8A-7785-4AD3-B91F-788742DF6D78' where namespace_id = 999971 and building_name = '张东路1387号创新之家CD幢' and apartment_name = '2层214';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '7E9A3B01-BE78-4B24-B964-7D036DD12E06' where namespace_id = 999971 and building_name = '张东路1387号创新之家CD幢' and apartment_name = '2层208';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = 'DAE5A086-1E44-424D-9086-80208757240C' where namespace_id = 999971 and building_name = '张东路1387号创新之家CD幢' and apartment_name = '1层接待中心';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = 'E7DDC801-A2A7-4648-A457-873672763B86' where namespace_id = 999971 and building_name = '张东路1387号创新之家CD幢' and apartment_name = '3层D（302+303）';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '1608F13E-7516-48FC-9C7A-8AB7F41BD302' where namespace_id = 999971 and building_name = '张东路1387号创新之家CD幢' and apartment_name = '1层展厅';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = 'CC9C9DE9-12C7-4D93-A425-8FF63FB12E54' where namespace_id = 999971 and building_name = '张东路1387号创新之家CD幢' and apartment_name = '2层209';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = 'E5AC2051-BF1F-4089-A4EA-9022C942CB33' where namespace_id = 999971 and building_name = '张东路1387号创新之家CD幢' and apartment_name = '2层201';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = 'DD47339E-67E6-4B92-8D43-9BFCD82BE80E' where namespace_id = 999971 and building_name = '张东路1387号创新之家CD幢' and apartment_name = '2层203';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '11B7AEBB-4F21-40BF-8AAA-9CC11DEF834D' where namespace_id = 999971 and building_name = '张东路1387号创新之家CD幢' and apartment_name = '2层210';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '788FD410-D2E9-46F5-B157-A31EC3DFC328' where namespace_id = 999971 and building_name = '张东路1387号创新之家CD幢' and apartment_name = '2层204';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = 'A84CAC22-CE52-491B-9E64-C49E82537C75' where namespace_id = 999971 and building_name = '张东路1387号创新之家CD幢' and apartment_name = '2层205';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = 'F4256D91-94CA-44F4-A2CA-D5BB8E44762D' where namespace_id = 999971 and building_name = '张东路1387号创新之家CD幢' and apartment_name = '2层多功能厅';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '9CB56D5A-44DE-4205-996C-E37E9404676A' where namespace_id = 999971 and building_name = '张东路1387号创新之家CD幢' and apartment_name = '3层C（301-309）';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '45904437-3268-439A-AC27-EDEC96ABB91A' where namespace_id = 999971 and building_name = '张东路1387号创新之家CD幢' and apartment_name = '2层202';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '16B146FB-62C8-4466-B1E6-FBE9791CDE22' where namespace_id = 999971 and building_name = '张东路1387号创新之家CD幢' and apartment_name = '2层215+216+217+218';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '82BCFDEC-468A-46CA-A8D5-1881E46D3D0D' where namespace_id = 999971 and building_name = '张东路1387号创新之家E幢' and apartment_name = '1层E107';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '9D105212-C0C7-4A5D-9881-5B3C27AEBDBC' where namespace_id = 999971 and building_name = '张东路1387号创新之家E幢' and apartment_name = '2层E201';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '0A39646B-8799-4245-87EA-5EAE15784976' where namespace_id = 999971 and building_name = '张东路1387号创新之家E幢' and apartment_name = '1层E（101、104、106）';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '5E6B4DAD-401A-4ECC-BF53-69D18821466A' where namespace_id = 999971 and building_name = '张东路1387号创新之家E幢' and apartment_name = '3层E（308）';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '784121B8-EC00-4CF6-9F9E-7B8C894DCD79' where namespace_id = 999971 and building_name = '张东路1387号创新之家E幢' and apartment_name = '3层E（301-307）';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '32EB6B83-4EF2-4E0B-A3C7-809FB5F5C629' where namespace_id = 999971 and building_name = '张东路1387号创新之家E幢' and apartment_name = '1层E（102+103）';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = 'D8B045C1-8FBE-44AD-98ED-89193EB85F88' where namespace_id = 999971 and building_name = '张东路1387号创新之家E幢' and apartment_name = '2层E（208、210、212）';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '5D276FF8-74CE-4B67-8327-A7F4239C5383' where namespace_id = 999971 and building_name = '张东路1387号创新之家E幢' and apartment_name = '1层E（105+108）';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = 'B0CDB4A2-7112-40DC-B7AE-DEBAABD1162D' where namespace_id = 999971 and building_name = '张东路1387号创新之家E幢' and apartment_name = '2层E205';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '29FE11A3-C82C-44E8-BCEF-F7A23E622233' where namespace_id = 999971 and building_name = '张东路1387号创新之家E幢' and apartment_name = '2层E（202-204）';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = 'D6BADEE7-8D42-42A3-93AF-020B96794E3A' where namespace_id = 999971 and building_name = '张东路1387号社区中心' and apartment_name = '一层商业-104';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = 'E9278BBA-C0D7-498E-A551-06BA1107D36C' where namespace_id = 999971 and building_name = '张东路1387号社区中心' and apartment_name = '一层配电间';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = 'BA9FF5B4-CD4E-4CAC-B45C-06CE6D720BA0' where namespace_id = 999971 and building_name = '张东路1387号社区中心' and apartment_name = '一层商业-101';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '14D40E61-71CF-4BFB-AF79-0DF3A2D99EA9' where namespace_id = 999971 and building_name = '张东路1387号社区中心' and apartment_name = '一层商业-102-1';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = 'B77C0892-DFDE-43B9-8304-2F001756EAA0' where namespace_id = 999971 and building_name = '张东路1387号社区中心' and apartment_name = '一层商业-102';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = 'B47A4CC3-A75A-4461-889B-3010D96073EE' where namespace_id = 999971 and building_name = '张东路1387号社区中心' and apartment_name = '一层商业-103-1';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = 'C70E9453-2648-44B1-8870-339C65FFAA5A' where namespace_id = 999971 and building_name = '张东路1387号社区中心' and apartment_name = '一层电信机房';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '5764D725-2081-440F-A989-582F8977FA51' where namespace_id = 999971 and building_name = '张东路1387号社区中心' and apartment_name = '一层商业-105';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '4C1B844A-01FC-426C-8A2B-91ECFFE3F72B' where namespace_id = 999971 and building_name = '张东路1387号社区中心' and apartment_name = '一层商业-106-108';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '3C566FD9-8331-46EE-8473-AD97D76980BA' where namespace_id = 999971 and building_name = '张东路1387号社区中心' and apartment_name = '一层商业-104-1';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = 'DCD3A022-292F-4C0E-80C1-B4F7D1F10667' where namespace_id = 999971 and building_name = '张东路1387号社区中心' and apartment_name = '一层商业-103';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '5557B361-28AB-4808-8DB9-F7075FCBF0D7' where namespace_id = 999971 and building_name = '张东路1387号社区中心' and apartment_name = '一层有线电视机房';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '0B976305-08A7-4A7B-A6AD-4C70D890754C' where namespace_id = 999971 and building_name = '张东路1388号12幢' and apartment_name = '101';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = 'D13BAB60-8EAE-401D-98C1-CFE532A832F3' where namespace_id = 999971 and building_name = '张东路1388号12幢' and apartment_name = '102';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '882B5391-9DF5-4524-821A-40A73C818EEF' where namespace_id = 999971 and building_name = '张东路1388号1幢' and apartment_name = '1层';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = 'C99B06E4-EA7C-4FCD-90B8-64AEFCE20189' where namespace_id = 999971 and building_name = '张东路1388号1幢' and apartment_name = '3层';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = 'D645E96D-C9BF-47E0-A673-E8780AFA6F6A' where namespace_id = 999971 and building_name = '张东路1388号1幢' and apartment_name = '2层';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '3870445F-3EFB-4CFD-989D-1986177D1FF8' where namespace_id = 999971 and building_name = '张东路1388号21幢' and apartment_name = '301';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = 'FFC8D997-17C8-4251-A33D-294194B7E1EE' where namespace_id = 999971 and building_name = '张东路1388号21幢' and apartment_name = '202';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '874B6E9E-A3AA-40E3-96A8-31D00621E802' where namespace_id = 999971 and building_name = '张东路1388号21幢' and apartment_name = '102';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = 'CCBC5986-5010-4E51-91E6-4E5C9A8AE734' where namespace_id = 999971 and building_name = '张东路1388号21幢' and apartment_name = '302';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '1EFAF5C1-56CE-4987-96C0-5DC70F256611' where namespace_id = 999971 and building_name = '张东路1388号21幢' and apartment_name = '101';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = 'FB60503C-3EEC-4538-B952-D59586825077' where namespace_id = 999971 and building_name = '张东路1388号21幢' and apartment_name = '201';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '6F464136-1F51-483A-B73E-3D7F2E5D3487' where namespace_id = 999971 and building_name = '张东路1388号22、23幢' and apartment_name = '101-102';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '3C66CFAF-F45C-413F-9A7F-406085537D46' where namespace_id = 999971 and building_name = '张东路1388号25幢' and apartment_name = '102';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '2952CA6D-0BEF-4234-A222-7583224895E0' where namespace_id = 999971 and building_name = '张东路1388号25幢' and apartment_name = '101';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '12CEB41C-5597-4AF8-AEA5-D138DDD3C776' where namespace_id = 999971 and building_name = '张东路1388号25幢' and apartment_name = '三鑫';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '63523601-84A2-4495-A9F3-0D4C095772E2' where namespace_id = 999971 and building_name = '张东路1388号28幢02' and apartment_name = '102';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = 'CBC14A04-CDD3-4ECA-8987-2395F7108A7B' where namespace_id = 999971 and building_name = '张东路1388号28幢地下1层' and apartment_name = '基站（联通）';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '721AD57B-86E2-43BC-8A2E-7AF0820F6BF6' where namespace_id = 999971 and building_name = '张东路1388号28幢地下1层' and apartment_name = '基站';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = 'FA20DA58-3CEB-4448-BE74-51E6098EEF40' where namespace_id = 999971 and building_name = '张东路1388号2幢' and apartment_name = '302';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = 'A3A02F00-7D79-4716-8193-580FFAD924A6' where namespace_id = 999971 and building_name = '张东路1388号2幢' and apartment_name = '102';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = 'EC224A24-C819-4AB8-A5EB-8A69F1858DC9' where namespace_id = 999971 and building_name = '张东路1388号2幢' and apartment_name = '101';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = 'D027F8E4-0E8C-406B-B077-A609A7B83C9F' where namespace_id = 999971 and building_name = '张东路1388号2幢' and apartment_name = '202';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '8D9E03E2-0E05-4C25-AD4C-B8467BF34639' where namespace_id = 999971 and building_name = '张东路1388号2幢' and apartment_name = '201';
update eh_addresses set namespace_address_type = 'shenzhou', namespace_address_token = '0D874418-2127-416C-BCB6-CE0F3CF06511' where namespace_id = 999971 and building_name = '张东路1388号2幢' and apartment_name = '301';

-- added by R 11/16/2017
-- 更新导入错误提示语
update eh_locale_strings set text = '岗位不存在' where scope = 'archives' and `code` = 100009;
-- added by R 11/16/2017 17:36
-- 导入错误提示信息
SET @string_id = (SELECT MAX(id) FROM `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@string_id := @string_id +1, 'archives', '100010', 'zh_CN', '英文名格式错误');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@string_id := @string_id +1, 'archives', '100011', 'zh_CN', '邮箱格式错误');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@string_id := @string_id +1, 'archives', '100012', 'zh_CN', '短号格式错误');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@string_id := @string_id +1, 'archives', '100013', 'zh_CN', '日期格式错误');
	
-- 表计管理的menu换链接，fix bug 18412 by xiongying20171117
update eh_web_menus set data_type = 'react:/energy-management/table-list' where id = 49110;

delete from `eh_var_field_scopes`  WHERE `module_name` = 'contract' AND `field_id` = (select id from `eh_var_fields` WHERE `module_name` = 'contract' AND `display_name` = '租赁总额');
delete from `eh_var_fields` WHERE `module_name` = 'contract' AND `display_name` = '租赁总额';

-- 删除用户行为统计错误数据 add by xq.tian  2017/11/17
DELETE FROM eh_stat_event_statistics WHERE event_name='launchpad_on_launch_pad_item_click';

-- 删除不需要的菜单 delete by R 2017/11/20
-- 菜单
-- 1.修改原先组织架构菜单的react
UPDATE `eh_web_menus` SET `data_type`='react:/address-book/address-list' WHERE id = 50100;
UPDATE `eh_web_menus` SET `data_type`='react:/address-book/address-list' WHERE id = 501000;
-- 2.删除原有的菜单id
DELETE FROM `eh_web_menus` where `id` IN (501100,502000,502100,502200);
DELETE FROM `eh_web_menus` where `id` IN (50110,50200,50210,50220);