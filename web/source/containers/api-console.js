import React, { PropTypes, Component} from 'react'

import {clearConsole} from '../actions'

import {registerComponent, getComponentState, WidgetComponent} from 'widget-redux-util/redux-enhancer'

import styles from '../shared/style/app.css'
import layoutStyles from '../shared/style/layout.css'

import {CONSOLE_APPEND, CONSOLE_CLEAR, API_SUCCESS, API_FAILURE} from '../actions'

class ApiConsole extends WidgetComponent {

    static getInstanceInitState() {
        return {
            consoleItems: []
        }
    }

    static componentReducer(state, action) {
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

    static mapStateToProps(state, ownProps) {
        let instanceState = getComponentState(state, ApiConsole, ownProps.instanceKey, false);

        return ({
            items: instanceState.consoleItems
        })
    }

    render() {
        let {items} = this.props;

        return (
            <table className={layoutStyles.layoutTable}><tbody>
            <tr style={{height: '20px'}}><td>
                <div>
                    <button onClick={(e)=>this.onClearConsoleClick()}>Clear</button>
                </div>
            </td></tr>
            <tr style={{height: '*%'}}><td>
                <div className={styles.consoleBox}>
                    {items.map((item, index)=>this.renderItem(item, index))}
                </div>
            </td></tr>
            </tbody></table>
        );
    }

    renderItem(item, index) {
        return <p key={index}>{item}</p>;
    }

    onClearConsoleClick() {
        this.dispatch(clearConsole());
    }
}

export default registerComponent(ApiConsole, true);
