/**
 * 引入依赖
 */
var configService = require("configService");
var apiService = require("apiService");
var log = require("log");
var fetch = require("fetch");

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
    vendorKey: {
        description: "供应商服务器地址",
        default: "http://zzz.com",
        validator: function (value) {
            return value.indexOf("http") !== -1;
        }
    }
};

/**
 * 入口函数，工作流后台启用脚本后，一旦满足触发条件，则会调用此函数，主要业务逻辑所在
 */
App.main = function (ctx) {
    // 提供的API用于获取管理员在后台配置的属性值
    var config = configService(ctx.flowActionType, ctx.flowActionId);

    var vendor = config.getString("vendor");
    var vendorCount = config.getInt("vendorCount");
    var vendorKey = config.getString("vendorKey");

    log.debug("vendor = ", vendor);
    log.debug("vendorCount = ", vendorCount);
    log.debug("vendorKey = ", vendorKey);

    // 通过 apiService 获取 api 提供对象
    var flowService = apiService.get("flowService");
    var moduleService = apiService.get("moduleService");
    // moduleService.testCall();

    var flowGraph = flowService.getFlowGraph(ctx.flow.flowMainId, ctx.flow.flowVersion);
    log.debug("flowGraph =", flowGraph);
    log.debug("flowGraph.flow =", flowGraph.flow);
    log.debug("flowGraph.flow.flowMainId = ", flowGraph.flow.flowMainId);

    var cctx = flowService.getFlowCaseState(ctx);
    log.debug("flowCaseState = ", cctx);
    log.debug("flowCaseState.operator = ", cctx.operator);
    log.debug("flowCaseState.operator.nickName = ", cctx.operator.nickName);

    // 发送 http 请求
    fetch("https://www.zuolin.com", {
        method: "GET",
        param: {a: 1, b: "bbb"}
    }, function (result) {
        log.info(new java.lang.String(result.getBody()))
    }, function (reason) {
        log.error(reason.getStatusCode().value())
    });
};

module.exports = App;