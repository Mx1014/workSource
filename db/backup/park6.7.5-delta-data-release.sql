-- AUTHOR: 缪洲
-- REMARK: 增加用户自定义上传资料与默认车牌的默认值
UPDATE eh_parking_lots SET default_data = 'identity,driver,driving';
UPDATE eh_parking_lots SET default_plate = '粤,B';