import {CALL_API} from 'widget-redux-util/redux-enhancer'

export const API_LIST_REQUEST = "Api.LIST_REQUST"
export const API_LIST_SUCCESS = "Api.LIST_SUCCESS"
export const API_LIST_FAILURE = "Api.LIST_FAILURE"

export const SANDBOX_SET_CURRENT = "SandBox.SET_CURRENT"
export const SANDBOX_SET_INITIALIZED = "SandBox.SET_INITIALIZED"
export const SANDBOX_SET_APIFILTER = "SandBox.SET_APIFILTER"

export const CONSOLE_APPEND = "Console.APPEND"
export const CONSOLE_CLEAR = "Console.CLEAR"

export const API_REQUEST = "Api.REQUST"
export const API_SUCCESS = "Api.SUCCESS"
export const API_FAILURE = "Api.FAILURE"

export const SERVER_CONSOLE_PAUSE = "ServerConsole.PAUSE";
export const SERVER_CONSOLE_RESUME = "ServerConsole.RESUME";
export const SERVER_CONSOLE_CONNECT = "ServerConsole.CONNECT";
export const SERVER_CONSOLE_DISCONNECT = "ServerConsole.DISCONNECT";
export const SERVER_CONSOLE_CLEAR = "ServerConsole.CLEAR";

//
// Service root
//
var SERVICE_ROOT;
if (process.env.NODE_ENV === 'production') {
    let url = document.location.toString();
    SERVICE_ROOT = url;
    SERVICE_ROOT = SERVICE_ROOT.substring(0, SERVICE_ROOT.lastIndexOf('/'));
} else {
    SERVICE_ROOT = "http://localhost:5000/evh"
}

export function getServiceRoot() {
    return SERVICE_ROOT;
}

//
// API call actions
//
export function fetchApiList() {
    return {
        [CALL_API]: {
            types: [ API_LIST_REQUEST, API_LIST_SUCCESS, API_LIST_FAILURE ],
            service: SERVICE_ROOT,
            endpoint: '/discover'
        }
    }
}

export function apiAction(uri, commandObject, headers, callContext) {
    if(!!headers) {
        return {
            [CALL_API]: {
                types: [ API_REQUEST, API_SUCCESS, API_FAILURE ],
                service: SERVICE_ROOT,
                endpoint: uri,
                headers,
                commandObject
            },

            callContext
        }
    } else {
        return {
            [CALL_API]: {
                types: [ API_REQUEST, API_SUCCESS, API_FAILURE ],
                service: SERVICE_ROOT,
                endpoint: uri,
                commandObject
            },

            callContext
        }
    }
}

//
// API sandbox actions/events
//
export function setSandboxCurrentApi(uri) {
    return {
        type: SANDBOX_SET_CURRENT,
        uri
    }
}

export function setSandboxInitialized() {
    return {
        type: SANDBOX_SET_INITIALIZED
    }
}

export function setApiFilter(filter) {
    return {
        type: SANDBOX_SET_APIFILTER,
        filter
    }
}

//
// API console actions/events
//
export function appendToConsole(text) {
    return {
        type: CONSOLE_APPEND,
        text
    }
}

export function clearConsole() {
    return {
        type: CONSOLE_CLEAR
    }
}

//
// Server console actions/events
//
export function pauseServerConsole() {
    return {
        type: SERVER_CONSOLE_PAUSE
    }
}

export function resumeServerConsole() {
    return {
        type: SERVER_CONSOLE_RESUME
    }
}

export function connectServerConsole() {
    return {
        type: SERVER_CONSOLE_CONNECT
    }
}

export function disconnectServerConsole() {
    return {
        type: SERVER_CONSOLE_DISCONNECT
    }
}

export function clearServerConsole() {
    return {
        type: SERVER_CONSOLE_CLEAR
    }
}
