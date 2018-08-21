-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: OPERATION
-- DESCRIPTION: 此SECTION放升级相关的操作要求，如调接口、查询数据确认、修改配置文件、更新特殊程序等
-- AUTHOR: 杨崇鑫  20180720
-- REMARK: content图片程序升级，① 从本版中的content二进制更新到正式环境中  ② 把allowOriginToGet = * 加到 config.ini 配置文件中的 system 区域下

-- AUTHOR: ryan  20180807
-- REMARK: 执行 /archives/cleanRedundantArchivesDetails 接口(可能速度有点慢，但可重复执行)

-- AUTHOR: jiarui  20180807
-- REMARK: 执行search 下脚本 enter_meter.sh
-- 执行 /energy/syncEnergyMeterIndex 接口(可能速度有点慢，但可重复执行)

-- --------------------- SECTION END ---------------------------------------------------------


-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境
-- AUTHOR: 黄良铭
-- REMARK: 积分所需配置表
SET @b_id = (SELECT IFNULL(MAX(id),1) FROM eh_configurations);

INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`)
 VALUES(@b_id:= @b_id +1,'server.point.url','http://test105.zuolin.com','the point url','0',NULL,NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`)
 VALUES(@b_id:= @b_id +1,'server.point.secretKey','OMtTBDhmVQSIP6oJBZ+mw+9i8+wnS1WAwsEVRoFvGXfNmCokOamwScJLdilQ3CuCXYb5J7HK+aua8sifKcEsiQ==','the point secretKey','0',NULL,NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`)
 VALUES(@b_id:= @b_id +1,'server.point.appKey','476fba87-dd1b-4ab9-ad6a-ca598a889c91','the point appKey','0',NULL,NULL);

-- --------------------- SECTION END ---------------------------------------------------------

-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: dev
-- DESCRIPTION: 此SECTION只在开发库中执行的脚本
-- AUTHOR:
-- REMARK:

-- --------------------- SECTION END ---------------------------------------------------------

-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: zijing
-- DESCRIPTION: 此SECTION只在清华信息港(紫荆)-999984执行的脚本

-- --------------------- SECTION END ---------------------------------------------------------

-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: guangda
-- DESCRIPTION: 此SECTION只在光大-999979执行的脚本

-- --------------------- SECTION END ---------------------------------------------------------

-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: szbay
-- DESCRIPTION: 此SECTION只在深圳湾-999966执行的脚本

-- --------------------- SECTION END ---------------------------------------------------------

-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: chuangyechang
-- DESCRIPTION: 此SECTION只在成都创业场-999964执行的脚本

-- --------------------- SECTION END ---------------------------------------------------------

-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: anbang
-- DESCRIPTION: 此SECTION只在安邦物业-999949执行的脚本

-- --------------------- SECTION END ---------------------------------------------------------


