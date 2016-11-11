import React, { PropTypes, Component} from 'react'

import {SplitPane, Pane} from 'widget-splitter'
import {registerComponent, getComponentState, WidgetComponent} from 'widget-redux-util/redux-enhancer'

import ApiList from '../components/apilist'
import ApiPanel from './api-panel'

import layoutStyles from '../shared/style/layout.css'

import {fetchApiList, appendToConsole, setSandboxCurrentApi, setApiFilter} from '../actions'
import {API_LIST_SUCCESS, SANDBOX_SET_CURRENT, SANDBOX_SET_APIFILTER} from '../actions'

class Sandbox extends WidgetComponent {

    static getInstanceInitState() {
        return ({
            currentApi: null,
            apiFilter: null,
            apis: []
        });
    }

    static componentReducer(state, action) {
        switch(action.type) {
            case API_LIST_SUCCESS:
                return {...state, apis: action.response.response};

            case SANDBOX_SET_CURRENT: {
                let item = state.apis.find((e) => { return e.uri == action.uri});

                return {...state, currentApi: item};
            }

            case SANDBOX_SET_APIFILTER: {
                return {...state, apiFilter: action.filter}
            }
        }
        return state;
    }

    static mapStateToProps(state, ownProps) {
        let instanceState = getComponentState(state, Sandbox, undefined, false);
        let prefix = instanceState.apiFilter;

        let apis = instanceState.apis;
        if(!!prefix)
            apis = apis.filter((item) => item.uri.startsWith(prefix));

        return ({apis});
    }

    render() {
        let {apis} = this.props;

        return (
            <SplitPane>
                <Pane style={{width: '24%'}}>
                    <table className={layoutStyles.layoutTable}><tbody>
                    <tr style={{height: '40px', backgroundColor: '#f1f1f1', padding: '4px 8px'}}><td>
                        <div style={{padding: '4px 32px 4px 16px'}}>
                            <input type="text"
                                   ref={(domInput) => this.domInput = domInput}
                                   onChange={(e) => this.onInputChange(this.domInput.value)}
                                   style={{width: '100%'}}
                                   placeholder={"Type prefix to narrow down the list below"}
                                >
                            </input>
                        </div>
                    </td></tr>

                    <tr><td>
                        <ApiList items={apis} onItemClick={(item) => this.onItemClick(item)} />
                    </td></tr>
                    </tbody></table>
                </Pane>
                <Pane>
                    <ApiPanel />
                </Pane>
            </SplitPane>
        );
    }

    componentDidMount() {
        super.componentDidMount();

        this.dispatch(fetchApiList());
        this.dispatch(appendToConsole('Core-Server API portal started at ' + new Date().toLocaleString()));
    }

    onInputChange(val) {
        this.dispatch(setApiFilter(val))
    }

    onItemClick(item) {
        this.dispatch(setSandboxCurrentApi(item.uri));
    }
}

export default registerComponent(Sandbox, true);
