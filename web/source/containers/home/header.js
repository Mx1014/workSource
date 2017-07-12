import React, { PropTypes, Component} from 'react'

import {registerComponent, getComponentState, WidgetComponent} from '../../../local/node_modules/widget-redux-util/redux-enhancer'

import {replace} from 'react-router-redux'

import styles from '../../shared/style/app.css'

export default class Header extends WidgetComponent {
    render() {
        return (
            <div className={styles.header}>
                Core-Server API portal
                <a href="#" onClick={(e)=>this.onHelpDeskClick(e)}>
                    Help Desk
                </a>
                <a href="#" onClick={(e)=>this.onSandboxClick(e)}>
                    API Sandbox
                </a>
            </div>
        )
    }

    onHelpDeskClick(e) {
        this.dispatch(replace('/helpdesk'));
    }

    onSandboxClick(e) {
        this.dispatch(replace('/sandbox'));
    }
}
