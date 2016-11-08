// React
import React, {Component} from 'react'
import {render} from 'react-dom'

// React Redux
import {Provider} from 'react-redux'

// React router
import {Router, Route, IndexRedirect} from 'react-router'
import {routerReducer} from 'react-router-redux'

import Home from './containers/home'
import Sandbox from './containers/sandbox'
import ApiPanel from './containers/api-panel'

export default class Root extends Component {
    render() {
        return (
            <Provider store={this.props.store}>
                <Router history={this.props.history}>
                    <Route path="/" component={Home}>
                        <IndexRedirect to="/sandbox" />
                        <Route path="/sandbox" component={Sandbox} />
                    </Route>
                    <Route path="*/api" component={Home}>
                        <IndexRedirect to="sandbox" />
                        <Route path="sandbox" component={Sandbox} />
                    </Route>
                </Router>
            </Provider>
        );
    }
}
