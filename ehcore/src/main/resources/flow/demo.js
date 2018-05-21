var configService = require("configService");
var apiService = require("apiService");
var fetch = require("fetch");
var log = require("log");

var app = {};

// 导出配置，用于管理员自定义配置
app.config = {
    vendor: {
        description: "供应商名字",
        default: "通联支付",
        validator: function (value) {
            return value.indexOf("支付") !== -1;
        }
    },
    vendorURL: {
        description: "供应商服务器地址",
        default: "http://zzz.com"
    }
};

// 入口函数，工作流后台启用脚本后，一旦满足触发条件，则会调用此函数，主要业务逻辑所在
app.main = function (ctx) {
    // 提供的API用于获取管理员在后台配置的属性值
    var config = configService(ctx.flow.id, ctx.flow.flowVersion);

    // 通过 apiService 获取 api 提供对象
    var flowService = apiService.get("flowService");
    var moduleService = apiService.get("moduleService");

    var vendor = config.getString("vendor");
    var vendorURL = config.getObject("vendorURL");
    var vendorKey = config.getInt("vendorKey");

    fetch("http://www.baidu.com", {
        param: {
            a: 1,
            b: 2,
            c: "ccccc",
            d: {
                d1:"d1"
            },
            e: [1,2,3]
        },
        headers: {
            "Content-Type": "application/x-www-form-urlencoded",
            "xxx-header": "xxxqqq"
        }
    }, function (result) {
        log.info(new java.lang.String(result.getBody()));
    }, function (reason) {
        log.warn(reason, "aaaa", "bbbb");
    });

    log.debug(vendor);
    log.debug(vendorURL);
    log.debug(vendorKey);

    flowService.testDummyCall();

    return vendorURL;
};

module.exports = app;