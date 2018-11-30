-- AUTHOR: 张智伟
-- REMARK: issue-42126
INSERT INTO eh_locale_strings(scope,code,locale,text)
SELECT r.scope,r.code,r.locale,r.text FROM(
SELECT 'meetingMessage' AS scope,100008 AS code,'zh_CN' AS locale,'您已不是参会人' AS text UNION ALL
SELECT 'meetingMessage' AS scope,100009 AS code,'zh_CN' AS locale,'您已不是会务人' AS text UNION ALL
SELECT 'meetingMessage' AS scope,100010 AS code,'zh_CN' AS locale,'您已被指定为会务人' AS text
)r LEFT JOIN eh_locale_strings s ON r.scope=s.scope AND r.code=s.code AND r.locale=s.locale
WHERE s.id IS NULL;
