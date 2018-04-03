import React, {Component} from 'react'
import ReactDOM from 'react-dom'

import {registerComponent, getComponentState, WidgetComponent} from 'widget-redux-util/redux-enhancer'

import ServerConsole from './server-console'

import {clearServerConsole, pauseServerConsole, resumeServerConsole, connectServerConsole, disconnectServerConsole} from '../../actions'
import {SERVER_CONSOLE_PAUSE, SERVER_CONSOLE_RESUME, SERVER_CONSOLE_CONNECT, SERVER_CONSOLE_DISCONNECT} from '../../actions'

class ServerConsolePanel extends WidgetComponent {

    //
    // Component state structure design and state -> props mapping
    //
    static statePath = "ServerConsolePanel";
    static getInstanceInitState() {
        return {
            connected: true,
            paused: false
        }
    }

    static mapStateToProps(state, ownProps) {
        let instanceState = getComponentState(state, ServerConsolePanel, ownProps.instanceKey, false);

        return ({
            connected: instanceState.connected,
            paused: instanceState.paused
        })
    }

    //
    // Component state reducing
    //
    static componentReducer(state, action) {
        switch(action.type) {
            case SERVER_CONSOLE_PAUSE:
                return {...state, paused: true}

            case SERVER_CONSOLE_RESUME:
                return {...state, paused: false}

            case SERVER_CONSOLE_CONNECT:
                return {...state, connected: true}

            case SERVER_CONSOLE_DISCONNECT:
                return {...state, connected: false}
        }

        return state;
    }

    //
    // Component rendering and behaviour
    //
    render() {
        let {connected, paused} = this.props;

        let content = (
            <div style={{width: '100%', height: '100%', padding: '8px 0px'}}>
                Connection to server is not established yet. (Note: you need to be root to connect)
            </div>
        );

        if(connected) {
            content = <ServerConsole ref="console"/>
        } else {
            setTimeout(()=>this.unloadEventSource(), 0);
        }

        return (
            <table style={{height: '100%', width: '100%'}}><tbody>
                <tr style={{height: '30px', width: '100%'}}><td>
                    <button onClick={(e)=>this.onClearClick(e)}>
                        Clear
                    </button>
                    <button onClick={(e)=>this.onPauseClick(e)}>
                        {paused? 'Resume' : 'Pause'}
                    </button>
                    <button onClick={(e)=>this.onReconnectClick(e)}>
                        {connected? 'Disconnect' : 'Connect'}
                    </button>
                </td></tr>

                <tr style={{height: '*%', width: '100%'}}><td>
                    {content}
                </td></tr>
            </tbody></table>
        );
    }

    unloadEventSource() {
        if(!!this.refs.console) {
            let mountNode = ReactDOM.findDOMNode(this.refs.console);
            ReactDOM.unmountComponentAtNode(mountNode);
        }
    }

    onClearClick(e) {
        this.dispatch(clearServerConsole());
    }

    onPauseClick(e) {
        if(this.props.paused) {
            this.dispatch(resumeServerConsole());
        } else {
            this.dispatch(pauseServerConsole());
        }
    }

    onReconnectClick(e) {
        if(this.props.connected) {
            this.dispatch(disconnectServerConsole());
        } else {
            this.dispatch(connectServerConsole());
        }
    }
}

export default registerComponent(ServerConsolePanel, true);
