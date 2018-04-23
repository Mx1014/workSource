var app = {};

var config = require("config");
var validator = require("validator");
var DSL = require("dsl");

config.register({
    "vendor": {
        "desc": "供应商名字",
        "default": "通联支付",
        "validator": function(value) {
            return value.indexOf("支付") != -1;
        }
    },
    "vendorURL": {
        "desc": "供应商服务器地址",
        "default": "http://zzz.com",
        "validator": validator.URL
    }
});

dsl.init = function() {
    tables = nashornObjs.getTables();
    for(var i = 0; i < tables.size(); i++) {
        var table = tables.get(i);
        var dtName = table.getName();
        clsName = toClsName(dtName);
        //print(clsName);
        dsl.TABLES[clsName] = table;
        dsl.TABLES[dtName.toUpperCase()] = dtName;
        dsl.POJOS[clsName] = Java.type("com.everhomes.server.schema.tables.pojos." + clsName);
    }
}

function toClsName(dtName) {
    var ss = dtName.split("_");
    var rlt = "";
    for(var i = 0; i < ss.length; i++) {
        rlt += ss[i].charAt(0).toUpperCase() + ss[i].slice(1);
    }
    return rlt;
}

dsl.readOnly = function(dt) {
    var dtName = dt.getName();
    var clsName = toClsName(dtName);
    return nashornObjs.readOnly(dsl.POJOS[clsName].class);
}

dsl.readWrite = function(dt) {
    var dtName = dt.getName();
    var clsName = toClsName(dtName);
    return nashornObjs.readWrite(dsl.POJOS[clsName].class);
}

dsl.getNextSequence = function(dt) {
    var dtName = dt.getName();
    var clsName = toClsName(dtName);
    print("dtName=" + clsName);
    print("dtClass=" + dsl.POJOS[clsName]);
    return nashornObjs.getNextSequence(dsl.POJOS[clsName].class);
}

dsl.parseRecords = function(rs) {
    return nashornObjs.parseRecords(rs);
}

dsl.fields = function() {
    var strs = new StringArray();
    if(arguments.length == 1 && arguments[0] instanceof Array) {
        var vs = arguments[0];
        for(var i = 0; i < vs.length; i++) {
            strs.add(vs[i]);
        }
    } else {
        for (var i = 0; i < arguments.length; i++) {
            strs.add(arguments[i]);
        }
    }

    return nashornObjs.fields(strs);
}

dsl.init();
module.exports = dsl;
