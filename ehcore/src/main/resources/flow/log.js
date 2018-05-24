var log = {};

log.info = function () {
    nashornObjs.log("info", argArr(arguments));
};

log.error = function () {
    nashornObjs.log("error", argArr(arguments));
};

log.debug = function () {
    nashornObjs.log("debug", argArr(arguments));
};

log.warn = function () {
    nashornObjs.log("warn", argArr(arguments));
};

function argArr(argObj) {
    var a = [];
    for(var i = 0; i < argObj.length; i++){
        if (typeof argObj[i] === "object") {
            a.push(JSON.stringify(argObj[i]));
        } else {
            a.push(argObj[i]);
        }
    }
    return a;
}

module.exports = log;