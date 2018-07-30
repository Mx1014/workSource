-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: OPERATION
-- DESCRIPTION: 此SECTION放升级相关的操作要求，如调接口、查询数据确认、修改配置文件、更新特殊程序等
-- AUTHOR: 杨崇鑫  20180720
-- REMARK: content图片程序升级，① 从本版中的content二进制更新到正式环境中  ② 把allowOriginToGet = * 加到 config.ini 配置文件中的 system 区域下

-- --------------------- SECTION END ---------------------------------------------------------


-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境
-- AUTHOR: 杨崇鑫
-- REMARK: 根据域空间判断是否展示能耗数据，测试环境初始化鼎峰汇支持展示能耗数据
INSERT INTO `eh_service_module_functions`(`id`, `module_id`, `privilege_id`, `explain`) VALUES (101, 20400, 0, '账单显示用量');

-- --------------------- SECTION END ---------------------------------------------------------


-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: zuolin-base
-- DESCRIPTION: 此SECTION只在左邻基线（非独立署部）执行的脚本
-- AUTHOR: 杨崇鑫
-- REMARK: 需根据域空间配置是否显示，初始化时配置好，默认不展示
SET @id = ifnull((SELECT MAX(id) FROM `eh_service_module_exclude_functions`),0);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 1000000 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999999 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999996 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999994 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999993 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999992 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999990 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999991 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999989 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999988 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999986 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999985 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999983 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999982 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999981 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999980 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999978 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999977 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 1 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999976 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999975 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999974 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999973 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999972 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999971 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999970 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999969 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999968 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999967 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999965 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999963 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999962 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999961 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999960 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999959 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999958 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999957 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999956 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999955 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999954 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999953 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999952 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999951 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999950 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999948 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999947 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999946 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999945 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999944 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999943 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999942 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999941 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999940 , NULL, 20400, 101);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999939 , NULL, 20400, 101);
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
-- AUTHOR: 杨崇鑫
-- REMARK: 需根据域空间配置是否显示，初始化时配置好，默认不展示
SET @id = ifnull((SELECT MAX(id) FROM `eh_service_module_exclude_functions`),0);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999984 , NULL, 20400, 101);
-- --------------------- SECTION END ---------------------------------------------------------

-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: guangda
-- DESCRIPTION: 此SECTION只在光大-999979执行的脚本
-- AUTHOR: 杨崇鑫
-- REMARK: 需根据域空间配置是否显示，初始化时配置好，默认不展示
SET @id = ifnull((SELECT MAX(id) FROM `eh_service_module_exclude_functions`),0);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999979 , NULL, 20400, 101);
-- --------------------- SECTION END ---------------------------------------------------------

-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: szbay
-- DESCRIPTION: 此SECTION只在深圳湾-999966执行的脚本
-- AUTHOR: 杨崇鑫
-- REMARK: 需根据域空间配置是否显示，初始化时配置好，默认不展示
SET @id = ifnull((SELECT MAX(id) FROM `eh_service_module_exclude_functions`),0);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999966 , NULL, 20400, 101);
-- --------------------- SECTION END ---------------------------------------------------------

-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: chuangyechang
-- DESCRIPTION: 此SECTION只在成都创业场-999964执行的脚本
-- AUTHOR: 杨崇鑫
-- REMARK: 需根据域空间配置是否显示，初始化时配置好，默认不展示
SET @id = ifnull((SELECT MAX(id) FROM `eh_service_module_exclude_functions`),0);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999964 , NULL, 20400, 101);
-- --------------------- SECTION END ---------------------------------------------------------

-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: anbang
-- DESCRIPTION: 此SECTION只在安邦物业-999949执行的脚本
-- AUTHOR: 杨崇鑫
-- REMARK: 需根据域空间配置是否显示，初始化时配置好，默认不展示
SET @id = ifnull((SELECT MAX(id) FROM `eh_service_module_exclude_functions`),0);
INSERT INTO `eh_service_module_exclude_functions`(`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id := @id + 1, 999949 , NULL, 20400, 101);
-- --------------------- SECTION END ---------------------------------------------------------


