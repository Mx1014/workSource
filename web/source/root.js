// React
import React, {Component} from 'react'
import {render} from 'react-dom'

// React router
import {Router, Route, IndexRedirect} from 'react-router'
import {routerReducer} from 'react-router-redux'

import Home from './containers/home/home'
import Sandbox from './containers/sandbox/sandbox'
import ApiPanel from './containers/sandbox/api-panel'

import HelpDesk from './containers/helpdesk/help-desk'
import ServerConsolePanel from './containers/helpdesk/server-console-panel'

export default class Root extends Component {
    render() {
        return (
            <Router history={this.props.history}>
                <Route path="/" component={Home}>
                    <IndexRedirect to="/sandbox"/>
                    <Route path="/sandbox" component={Sandbox}/>
                    <Route path="/helpdesk" component={HelpDesk}>
                        <IndexRedirect to="/helpdesk/console"/>
                        <Route path="/helpdesk/console" component={ServerConsolePanel}/>
                    </Route>
                </Route>
            </Router>
        );
    }
}
