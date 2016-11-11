import React, {Component} from 'react'
import {serverSentEventConnect} from 'react-server-sent-event-container'

import {dispatch, WidgetComponent} from 'widget-redux-util/redux-enhancer'
import {actionInterceptor} from 'widget-redux-util/action-interceptor'
import {utf8ArrayToStr} from 'widget-common-util/misc'

import Base64 from 'base64-js'

import {getServiceRoot} from '../actions'

import styles from '../shared/style/app.css'

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

        if(!!eventSource)
            eventSource.close();
    }

    onEventAction(action) {
        if(action.type == 'SSE.data') {
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
        }
    }

    onStopSse(e, eventSource) {
        e.preventDefault();
        e.stopPropagation();

        eventSource.close();
    }
}

const onOpen = (props, source) => {
    console.log('open');
};

const onMessage = (event, props, source) => {
    console.log('onMessage');

    if(!!event && !!event.data)
        dispatch({type: 'SSE.data', data: event.data});
};

const onError = (event, props, source) => {
    console.log('error');
    console.log(event);
    source.close();
}

const eventObj = {
    "SSE.Console": (event, props, source) => {
        if(!!event && !!event.data)
            dispatch({type: 'SSE.data', data: event.data});
    }
}

export default serverSentEventConnect(getServiceRoot() + '/admin/sseConsole',
    false, onOpen, onMessage, onError, eventObj)(ServerConsole);
