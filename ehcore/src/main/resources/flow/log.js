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
    for(var i = 0; i < argObj.length; i++) {
        var el = argObj[i];
        if (el == null) {
            a.push("null");
        } else if (el === undefined) {
            a.push("undefined");
        } else if (typeof el === "object") {
            var ify = JSON.stringify(el);
            if (ify !== undefined && ify !== "undefined") {
                a.push(ify);
            } else {
                a.push(el.toString());
            }
        } else {
            a.push(el);
        }
    }
    return a;
}

module.exports = log;