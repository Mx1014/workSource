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
import {applyMiddleware, combineReducers, compose} from 'redux'
import thunk from 'redux-thunk'

import {routerReducer} from 'react-router-redux'
import {browserHistory} from 'react-router'
import {syncHistoryWithStore, routerMiddleware} from 'react-router-redux'

import {bindLocaleLoader} from 'widget-common-util/locale'
import {createStore, getInitState, getReducers, registerComponentReducer, apiDispatcherMiddleware} from 'widget-redux-util/redux-enhancer'
import {actionInterceptor} from 'widget-redux-util/action-interceptor'

import {loadLocaleResource} from './locale/resource-loader'

import createLogger from 'redux-logger'
import DevTools from './dev-tools'

import Root from './root'

//
// Create store and launch (Standard)
//
bindLocaleLoader(loadLocaleResource)
registerComponentReducer('routing', routerReducer);

export const store = createStore(
    getReducers(),
    getInitState(),
    compose(
        applyMiddleware(
            thunk,
            actionInterceptor.getMiddleware(),
            apiDispatcherMiddleware,
            routerMiddleware(browserHistory),        // allow navigation through React action
            createLogger()
        ),
        DevTools.instrument()
    )
);

export const history = syncHistoryWithStore(browserHistory, store);
history.listen(location=> { console.log('go to location ' + location.pathname); });

/*
render(
    <div>
        <Root store={store} history={history} />
        <DevTools store={store} />
    </div>,
    document.getElementById('root')
);
*/

render(
    <div style={{width: '100%', height: '100%'}}>
        <Root store={store} history={history} />
    </div>,
    document.getElementById('root')
);

