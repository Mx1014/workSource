var configService = require("configService");
var apiService = require("apiService");
var log = require("log");

var app = {};

// 导出配置，用于管理员自定义配置
app.config = {
    vendor: {
        description: "供应商名字",
        default: "通联支付"
    },
    vendorCount: {
        description: "供应商数量",
        default: "2",
        validator: function (value) {
            try {
                parseInt(value);
                return true;
            } catch (e) {
                return false;
            }
        }
    },
    vendorKey: {
        description: "供应商服务器地址",
        default: "http://zzz.com",
        validator: function (value) {
            return value.indexOf("http") !== -1;
        }
    }
};

// 入口函数，工作流后台启用脚本后，一旦满足触发条件，则会调用此函数，主要业务逻辑所在
app.main = function (ctx) {
    // 提供的API用于获取管理员在后台配置的属性值
    var config = configService(ctx.flowActionType, ctx.flowActionId);

    // 通过 apiService 获取 api 提供对象
    var flowService = apiService.get("flowService");
    var moduleService = apiService.get("moduleService");

    var vendor = config.getString("vendor");
    var vendorCount = config.getInt("vendorCount");
    var vendorKey = config.getString("vendorKey");

    log.debug("vendor = ", vendor);
    log.debug("vendorCount = ", vendorCount);
    log.debug("vendorKey = ", vendorKey);

    var flowGraph = flowService.getFlowGraph(ctx.flow.flowMainId, ctx.flow.flowVersion);
    log.debug("flowGraph =", flowGraph);
    log.debug("flowGraph.flow.flowMainId = ", flowGraph.flow.flowMainId);
    log.debug("flowGraph.flow =", flowGraph.flow);

    var cctx = flowService.getFlowCaseState(ctx);
    log.debug("FlowCaseState = ", cctx);
    log.debug("FlowCaseState.operator = ", cctx.operator);
    log.debug("FlowCaseState.operator.nickName = ", cctx.operator.nickName);

    log.debug("js obj = ", {a:1, b:2});
    log.debug("js arr = ", [1,2,3]);

    flowService.testDummyCall();
};

module.exports = app;