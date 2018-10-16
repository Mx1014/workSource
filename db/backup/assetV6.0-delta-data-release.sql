-- AUTHOR: 丁建民
-- REMARK: #31812   保存用户当前所在场景

SET @id = IFNULL((SELECT MAX(`id`) FROM `eh_app_urls`),0);
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`) VALUES (@id:=@id+1, '11', '左邻', '2', 'http://a.app.qq.com/o/simple.jsp?pkgname=com.yjtc.everhomes', 'cs://1/image/aW1hZ2UvTVRwbU5qQXhOVFJtWW1FNU5UazNObUkyTldFeU5HWTFOekJpTWpWaU5XUTNNUQ', '移动平台聚合服务，助力园区效能提升');

-- END
