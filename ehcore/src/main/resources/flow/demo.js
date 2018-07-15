/**
 * 引入依赖
 */
var configService = require("configService");
var apiService = require("apiService");
var log = require("log");
var fetch = require("fetch");

// 常用库
var md5 = require("lib/md5");
var base64 = require("lib/base64");

var App = {};

/**
 * 导出配置，用于管理员自定义配置
 */
App.config = {
    vendor: {
        description: "供应商名字",
        default: "通联支付"
    },
    vendorCount: {
        description: "供应商数量",
        default: "2"
    },
    vendorURL: {
        description: "供应商服务器地址",
        default: "http://10.1.110.33:8888/",
        // 校验用户输入内容, 如果校验通过, 返回 true, 否则返回提示信息
        validator: function (value) {
            var pass = value.indexOf("http") !== -1;
            return pass ? pass : "请输入正确的URL";
        }
    }
};

/**
 * 通过 http 请求回调到这里
 */
App.mapping = {
    base64: function (requestParamMap, requestBody) {
        var b = base64.encode(requestParamMap.toString());
        log.info("base64.encode() = ", b);
        return b;
    },
    md5: function (requestParamMap, requestBody) {
        var hash = md5.create();
        hash.update(requestParamMap.toString());
        var hex = hash.hex();
        log.info("hash.hex() = ", hex);
        return hex;
    }
};

/**
 * 入口函数，工作流后台启用脚本后，一旦满足触发条件，则会调用此函数，主要业务逻辑所在
 */
App.main = function (ctx) {
    // 提供的API用于获取管理员在后台配置的属性值
    var config = configService(ctx.flowActionType, ctx.flowActionId);

    var vendor = config.getString("vendor") || "通联支付";
    var vendorCount = config.getInt("vendorCount") || 2;
    var vendorURL = config.getString("vendorURL") || "http://10.1.110.33:8888/";

    log.debug("vendor = ", vendor);
    log.debug("vendorCount = ", vendorCount);
    log.debug("vendorURL = ", vendorURL);

    // 通过 apiService 获取 api 提供对象
    var flowService = apiService.get("flowService");
    var moduleService = apiService.get("moduleService");
    // moduleService.testCall();

    var flowGraph = flowService.getFlowGraph(ctx.flow.flowMainId, ctx.flow.flowVersion);
    if (flowGraph != null) {
        log.debug("flowGraph =", flowGraph);
        log.debug("flowGraph.flow =", flowGraph.flow);
        log.debug("flowGraph.flow.flowMainId = ", flowGraph.flow.flowMainId);
    }

    var cctx = flowService.getFlowCaseState(ctx);
    if (cctx != null) {
        log.debug("flowCaseState = ", cctx);
        log.debug("flowCaseState.operator = ", cctx.operator);
        log.debug("flowCaseState.operator.nickName = ", cctx.operator.nickName);
    }

    var url = vendorURL;
    url += "?scriptMainId=";
    url += ctx.action.scriptMainId;
    url += "&scriptVersion=";
    url += ctx.action.scriptVersion;

    // 发送 http 请求
    fetch(url, function (result) {
        log.info(new java.lang.String(result.getBody()))
    }, function (reason) {
        log.error(reason)
    });
};

module.exports = App;