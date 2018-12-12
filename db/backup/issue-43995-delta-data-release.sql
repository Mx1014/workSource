-- AUTHOR:  谢旭双
-- REMARK: 修改会议预定消息内容
UPDATE eh_locale_strings SET text = REPLACE(text,'您','你') WHERE scope='meetingErrorCode';
UPDATE eh_locale_templates SET text = REPLACE(text,'您','你'),description=REPLACE(description,'您','你')  WHERE scope='meetingMessage';
