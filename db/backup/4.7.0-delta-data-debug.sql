-- 短信黑名单  add by xq.tian  2017/07/04
SET @max_locale_id = (SELECT MAX(id) FROM `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
VALUES ((@max_locale_id := @max_locale_id + 1), 'user', '300004', 'zh_CN', '对不起，您的手机号在我们的黑名单列表');

-- added by wh  邮件用html格式
UPDATE eh_locale_templates SET TEXT = '<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"><html><head><meta http-equiv="Content-Type" content="text/html; charset=UTF-8"><title>${title}</title></head><body><p>尊敬的${nickName}：<br>
您好，感谢您使用${appName}，点击下面的链接进行邮箱验证：<br>
<a href="${verifyUrl}">点我验证</a> <br>
如果链接没有跳转，请直接复制链接地址到您的浏览器地址栏中访问。（30分钟内有效）
 <br>
此邮件为系统邮件，请勿直接回复。
<br>
如非本人操作，请忽略此邮件。
<br>
谢谢，${appName}</p></body></html>'
WHERE scope = 'verify.mail' AND CODE =1 ;