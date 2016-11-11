var dsl = require("dsl");
var DSL = dsl.DSL;
var Tables = dsl.TABLES;

var Apps = {};

function doResp(jreq, obj) {
    jreq.resp(JSON.stringify(obj))
}

function httpProcess(id) {
    jreq = nashornObjs.get(id);
    var app = Apps[jreq.getAppName()];
    if(!app) {
        print("app not found");
        jreq.resp("app not found");
        return;
    }
    var query = app.queries[jreq.getMapName()];
    if(!query) {
        print("query not found");
        jreq.resp("query not found");
        return;
    }
    var queryItem = query[jreq.getQuery()];
    if(queryItem.length == 1 && queryItem[0]["rawSql"]) {
        queryItem = queryItem[0];
        rawSql = queryItem["rawSql"];
        print(rawSql);
        def = "";
        if(queryItem["defaults"]) {
            def = JSON.stringify(queryItem["defaults"]);
        }
        rlt = nashornObjs.makeRawQuery(rawSql, def, jreq.getBody());
        print("rlt=" + rlt);
        jreq.resp(rlt);
    } if(queryItem.length == 1 && queryItem[0]["queryFunc"]) {
        print("dsl Query...");
        queryItem = queryItem[0];
        rlt = queryItem.queryFunc(jreq.getBody());
        jreq.resp(rlt);
    } else {
        print("makeQuery...");
        rlt = nashornObjs.makeQuery(jreq.getAppName(), jreq.getMapName(), jreq.getQuery(), jreq.getBody())
        print("rlt=" + rlt);
        //TODO more convert
        jreq.resp(rlt);
    }
}

function mappingInit(id) {
    jmap = nashornObjs.get(id);
    print("initial:" + jmap.getName());
    //use prefix to identify that it's a mapping resource
    var mapName = "app$" + jmap.getAppName() + "$" + jmap.getName();
    var imp = require(mapName);
    imp.mappingStart(Apps, jmap.getAppName(), jmap.getName());
}

function testDSL() {
    print(Tables.EhUsers.getName());
    print(dsl.getNextSequence(Tables.EhUsers));
    o = {"accountName": "1"};
    records = DSL.using(dsl.readOnly(Tables.EhUsers)).select(dsl.fields("eh_users.id", "eh_users.uuid", "eh_users.account_name"))
        .where("eh_users.account_name like ?", o.accountName + "%").from(Tables.EH_USERS).limit(10).fetch();
    print(dsl.parseRecords(records));
}

