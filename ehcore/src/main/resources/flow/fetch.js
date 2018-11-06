var template = Java.type("com.everhomes.oauth2client.handler.RestCallTemplate");

function prepend(prefix, name, separator) {
    if (prefix) {
        if (separator) {
            return prefix + "." + name;
        }
        return prefix + name;
    }
    return name;
}

function each(obj, func) {
    for (var key in obj) {
        if (obj.hasOwnProperty(key) === true) {
            func(obj[key], key);
        }
    }
}

function flatten(prefix, obj, map) {
    if (obj || obj === 0) {
        if (Object.prototype.toString.call(obj) === '[object Array]') {
            for (var i = 0; i < obj.length; i++) {
                var item = obj[i];
                flatten(prepend(prefix, "[" + i + "]", false), item, map);
            }
        } else if (Object.prototype.toString.call(obj) === '[object Object]') {
            if (obj['__type__'] === 'map') {
                each(obj, function (propertyObject, propertyName) {
                    if (propertyName !== '__type__') {
                        flatten(prepend(prefix, "[" + propertyName + "]", false), propertyObject, map);
                    }
                });
            } else {
                each(obj, function (propertyObject, propertyName) {
                    flatten(prepend(prefix, propertyName, true), propertyObject, map);
                });
            }
        }
        else {
            map[prefix] = obj;
        }
    }
}

function toFlattenMap(obj) {
    var map = {};
    flatten(null, obj, map);
    return map;
}

function ContentType(headers) {
    var contentType;
    if (headers && headers["Content-Type"]) {
        contentType = headers["Content-Type"];
    } else {
        contentType = "application/x-www-form-urlencoded";
    }
    this.contentType = contentType;
}

ContentType.prototype = {
    getBody: function(param) {
        if (!param) {
            return "";
        }
        if (this.contentType.indexOf("application/x-www-form-urlencoded") !== -1) {
            var flattenMap = toFlattenMap(param);
            return template.queryStringBuilder().vars(flattenMap).build("");
        }
        else if (this.contentType.indexOf("application/json") !== -1) {
            if (typeof param === 'string') {
                return param;
            }
            return JSON.stringify(param);
        }
        else {
            return param.toString();
        }
    }
};

function RequestMethod(method) {
    if (method) {
        this.method = method;
    } else {
        this.method = "GET";
    }
}

function fetch(url, data, success, error) {
    // 不传data参数的时候
    if (typeof data === "function") {
        error = success;
        success = data;
    }

    var contentType = new ContentType(data.headers);
    var body = contentType.getBody(data.param);
    var requestMethod = new RequestMethod(data.method);

    try {
        var response = template
            .url(url)
            .body(body)
            .headers(data.headers ? data.headers : {})
            .method(requestMethod.method)
            .byteDataExecute();

        if (response.getStatusCode().value() < 400) {
            if (success) {
                success(response);
            }
        } else {
            if (error) {
                error(response);
            }
        }
    } catch (e) {
        if (error) {
            error(e);
        }
    }
}

module.exports = fetch;