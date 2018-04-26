var config = require("config");
// var validator = require("validator");
// var DSL = require("dsl");

var app = {};
app.config = {
    vendor: {
        desc: "供应商名字",
        default: "通联支付",
        validator: function (value) {
            return value.indexOf("支付") !== -1;
        }
    },
    vendorURL: {
        desc: "供应商服务器地址",
        default: "http://zzz.com",
        validator: ""
    }
};

app.main = function () {
    // tables = nashornObjs.getTables();
    // for(var i = 0; i < tables.size(); i++) {
    //     var table = tables.get(i);
    //     var dtName = table.getName();
    //     clsName = toClsName(dtName);
    //     //print(clsName);
    //     dsl.TABLES[clsName] = table;
    //     dsl.TABLES[dtName.toUpperCase()] = dtName;
    //     dsl.POJOS[clsName] = Java.type("com.everhomes.server.schema.tables.pojos." + clsName);
    // }
    var vendor = config.String("vendor") || "通联支付";
    var vendorURL = config.Object("vendorURL") || "http://zzz.com";
    var vendorKey = config.Int("vendorKey") || 1;
    return vendorURL;
};

module.exports = app;