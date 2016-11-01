/**
 *
 *  Standard React-Wdiget bootstrap main sequence
 *
 *  @Author Kelven Yang
 *
 */

// React
import React, {Component} from 'react'
import {render} from 'react-dom'

// Redux
import {applyMiddleware, combineReducers} from 'redux'
import thunk from 'redux-thunk'

import {browserHistory} from 'react-router'
import {syncHistoryWithStore, routerMiddleware, routerReducer} from 'react-router-redux'

import {bindLocaleLoader} from 'widget-common-util/locale'
import {createStore, apiDispatcherMiddleware} from 'widget-redux-util/redux-enhancer'
import {actionInterceptor} from 'widget-redux-util/action-interceptor'

import {loadLocaleResource} from './locale/resource-loader'

import Root from './root'
import reducers from './reducers'

//
// Create store and launch (Standard)
//
bindLocaleLoader(loadLocaleResource);

export const store = createStore(
    reducers,
    {},
    applyMiddleware(
        thunk,
        actionInterceptor.getMiddleware(),
        apiDispatcherMiddleware,
        routerMiddleware(browserHistory)        // allow navigation through React action
    )
);

export const history = syncHistoryWithStore(browserHistory, store);
history.listen(location=> { console.log('go to location ' + location.pathname); });

render(<Root store={store} history={history} />, document.getElementById('root'));
