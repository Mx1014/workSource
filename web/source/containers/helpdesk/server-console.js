import React, {Component} from 'react'

import {dispatch, getComponentState, WidgetComponent} from 'widget-redux-util/redux-enhancer'

import {utf8ArrayToStr} from 'widget-common-util/misc'
import {serverSentEventConnect} from 'react-server-sent-event-container'
import Base64 from 'base64-js'

import {getServiceRoot, connectServerConsole, disconnectServerConsole, SERVER_CONSOLE_CLEAR} from '../../actions'
import ServerConsolePanel from './server-console-panel'

import styles from '../../shared/style/app.css'

class ServerConsole extends WidgetComponent {
    render() {
        let {message, eventSource} = this.props;

        return (
            <div className={styles.serverConsoleBox}>
                <textarea
                    ref={(domResultPanel) => this.domResultPanel = domResultPanel}
                    wrap="soft"
                    readOnly="readonly"
                    style={{width: '100%', height: '100%', border: '0px'}}
                    >
                </textarea>
            </div>
        )
    }

    componentWillUnmount() {
        super.componentWillUnmount();

        if(!!this.props.eventSource)
            this.props.eventSource.close();
    }

    onEventAction(action) {
        if(action.type == 'SSE.data') {
            let panelState = getComponentState(null, ServerConsolePanel, undefined, false);
            if(panelState.paused)
                return;

            let message = utf8ArrayToStr(Base64.toByteArray(action.data));

            if(!!this.domResultPanel) {
                if(!!this.domResultPanel.value) {
                    if(this.domResultPanel.value.length < 65536)
                        this.domResultPanel.value += message;
                    else
                        this.domResultPanel.value = message;
                }
                else
                    this.domResultPanel.value = message;

                this.domResultPanel.scrollTop = this.domResultPanel.scrollHeight - this.domResultPanel.clientHeight;
            }
        } else if(action.type == SERVER_CONSOLE_CLEAR) {
            if(!!this.domResultPanel)
                this.domResultPanel.value = '';
        }
    }

    onStopSse(e, eventSource) {
        e.preventDefault();
        e.stopPropagation();

        eventSource.close();
    }
}

const onOpen = (props, source) => {
    dispatch(connectServerConsole());
};

const onMessage = (event, props, source) => {
    if(!!event && !!event.data)
        dispatch({type: 'SSE.data', data: event.data});
};

const onError = (event, props, source) => {
    source.close();
    dispatch(disconnectServerConsole());
}

const eventObj = {
    "SSE.Console": (event, props, source) => {
        if(!!event && !!event.data)
            dispatch({type: 'SSE.data', data: event.data});
    }
}

export default serverSentEventConnect(getServiceRoot() + '/admin/sseConsole',
    false, onOpen, onMessage, onError, eventObj)(ServerConsole);
