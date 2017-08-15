-- 更改群聊名称可为空  edit by yanjun 20170724
ALTER TABLE eh_groups MODIFY `name` VARCHAR(128) DEFAULT NULL;