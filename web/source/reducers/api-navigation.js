import {API_LIST_SUCCESS, SANDBOX_SET_CURRENT, SANDBOX_SET_APIFILTER} from '../actions'
import {setSandboxCurrentApi} from '../actions'

function getInitState() {
    return ({
        currentApi: null,
        apiFilter: null,
        apis: []
    });
}

export default function apiNavigationReducer(state = getInitState(), action) {
    switch(action.type) {
        case API_LIST_SUCCESS:
            return {...state, apis: action.response.response};

        case SANDBOX_SET_CURRENT: {
            let item = state.apis.find((e) => { return e.uri == action.uri});

            return {...state, currentApi: item};
        }

        case SANDBOX_SET_APIFILTER: {
            return {...state, apiFilter: action.filter}
        }
    }
    return state;
}
