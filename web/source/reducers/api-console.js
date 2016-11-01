import {CONSOLE_APPEND, CONSOLE_CLEAR, API_SUCCESS, API_FAILURE} from '../actions'

export default function apiNavigationReducer(state = {consoleItems: []}, action) {
    switch(action.type) {
        case CONSOLE_APPEND:
            return {consoleItems: [...state.consoleItems, action.text]};

        case CONSOLE_CLEAR:
            return {consoleItems: []};

        case API_SUCCESS:
            return {consoleItems: [...state.consoleItems, '<= ' + JSON.stringify(action.response)]};

        case API_FAILURE:
            return {consoleItems: [...state.consoleItems, '<= ' + JSON.stringify(action.error)]};
    }

    return state;
}
