import { combineReducers } from 'redux'
import { routerReducer as routing } from 'react-router-redux'

import apiNavigation from './api-navigation'
import apiConsole from './api-console'

const rootReducer = combineReducers({
    routing,
    apiNavigation,
    apiConsole
});

export default rootReducer;
