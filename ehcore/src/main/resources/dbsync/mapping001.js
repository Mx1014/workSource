var App = require("app");
var dsl = require("dsl");
var DSL = dsl.DSL;
var Tables = dsl.TABLES;

tables = ["eh_user_identifiers", "eh_users"];
map = {
    "eh_user_identifiers": {
        "fields": ["id", "owner_uid", "identifier_type", "identifier_token", "claim_status"],
        "primary": "id",
        "belong": [
            {
            "table": "eh_users",
            "fieldA": "owner_uid",
            "fieldB": "id",
            "join": "INNER_JOIN"
            }
        ],
        "hasMany": [],
        "hasOne": []
    },
    "eh_users": {
        "fields": ["id", "uuid", "account_name", "nick_name"],
        "primary": "id"
    }
};
query = {
    "getByUserId": [{
        "tableName": "eh_user_identifiers",
        "conditions": [" eh_users.id = $userId ", " eh_user_identifiers.claim_status = $claimStatus "],
        "defaults": {"claimStatus": 3}
        }
    ],
    "getByPhone": [
        {
            "tableName": "eh_users",
            "rawSql": "select * from eh_users join eh_user_identifiers on eh_users.id=eh_user_identifiers.owner_uid where eh_user_identifiers.identifier_token = $phone",
            "defaults": {"regCityId": 10002}
        }
    ],
    "findByAccountName": [{
        "queryFunc": function(input) {
            var o = {};
            if(input && input.length > 0) {
                o = JSON.parse(input);
            }
            records = DSL.using(dsl.readOnly(Tables.EhUsers)).select(dsl.fields("eh_users.id", "eh_users.uuid", "eh_users.account_name"))
                .where("eh_users.account_name like ?", o.accountName + "%").from(Tables.EH_USERS).limit(10).fetch();
            return dsl.parseRecords(records);
        }
    }]
};

var mapping = {};
mapping.mappingStart = function(apps, appName, mapName) {
    var app = apps[appName];
    if(!app) {
        app = new App(appName)
        apps[appName] = app;
    }

    app.maps[mapName] = map;
    app.queries[mapName] = query;

    var graphName = appName + "$" + mapName;
    graph = nashornObjs.getGraph(graphName);
    if(!graph) {
        var obj = {};
        obj["appName"] = appName;
        obj["mapName"] = mapName;
        obj["mapping"] = map;
        obj["tables"] = tables;
        graph = nashornObjs.createGraph(JSON.stringify(obj));

        nashornObjs.createQueryBase(appName, mapName, JSON.stringify(query));
    }
}

module.exports = mapping;
