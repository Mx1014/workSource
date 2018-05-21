INSERT INTO eh_configurations (name, value, description, namespace_id, display_name)
VALUES ('flow.stepname.suspend_step', '暂缓', 'suspend_step', 0, NULL);

INSERT INTO eh_configurations (name, value, description, namespace_id, display_name)
VALUES ('flow.stepname.abort_suspend_step', '取消暂缓', 'abort_suspend_step', 0, NULL);

INSERT INTO ehcore.eh_locale_strings (scope, code, locale, text)
VALUES ('flow', 100020, 'zh_CN', '脚本执行超时，请重试');

INSERT INTO ehcore.eh_locale_strings (scope, code, locale, text)
VALUES ('flow', 100021, 'zh_CN', '脚本参数校验失败');

INSERT INTO ehcore.eh_locale_strings (scope, code, locale, text)
VALUES ('flow', 100022, 'zh_CN', '脚本编译失败');

INSERT INTO ehcore.eh_locale_strings (scope, code, locale, text)
VALUES ('flow', 100023, 'zh_CN', '脚本执行失败');