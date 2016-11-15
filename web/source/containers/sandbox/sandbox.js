import React, { PropTypes, Component} from 'react'

import {registerComponent, getComponentState, WidgetComponent} from 'widget-redux-util/redux-enhancer'

import {SplitPane, Pane} from 'widget-splitter'

import ApiList from '../../components/apilist'
import ApiPanel from './api-panel'

import layoutStyles from '../../shared/style/layout.css'

import {fetchApiList, appendToConsole, setSandboxCurrentApi, setSandboxInitialized, setApiFilter} from '../../actions'
import {API_LIST_SUCCESS, SANDBOX_SET_CURRENT, SANDBOX_SET_INITIALIZED, SANDBOX_SET_APIFILTER} from '../../actions'

class Sandbox extends WidgetComponent {

    //
    // Component state structure design and state -> props mapping
    //
    static statePath = "Sandbox";
    static getInstanceInitState() {
        return ({
            currentApi: null,
            apiFilter: null,
            apis: [],
            initialized: false
        });
    }

    static mapStateToProps(state, ownProps) {
        let instanceState = getComponentState(state, Sandbox, undefined, false);
        let prefix = instanceState.apiFilter;

        let apis = instanceState.apis;
        if(!!prefix)
            apis = apis.filter((item) => item.uri.startsWith(prefix));

        return ({
            apis: apis,
            initialized: instanceState.initialized,
            apiFilter: instanceState.apiFilter
        });
    }

    //
    // Component state reducing
    //
    static componentReducer(state, action) {
        switch(action.type) {
            case API_LIST_SUCCESS:
                return {...state, apis: action.response.response};

            case SANDBOX_SET_CURRENT: {
                let item = state.apis.find((e) => { return e.uri == action.uri});

                return {...state, currentApi: item};
            }

            case SANDBOX_SET_INITIALIZED: {
                return {...state, initialized: true};
            }

            case SANDBOX_SET_APIFILTER: {
                return {...state, apiFilter: action.filter}
            }
        }
        return state;
    }

    //
    // Component rendering and behaviour
    //
    render() {
        let {apis = [], apiFilter} = this.props;

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
        if(!this.props.initialized) {
            this.dispatch(appendToConsole('Core-Server API portal started at ' + new Date().toLocaleString()));
            this.dispatch(setSandboxInitialized());
        }

        let {apiFilter} = this.props;
        if(!!apiFilter)
            this.domInput.value = apiFilter;
    }

    onInputChange(val) {
        this.dispatch(setApiFilter(val))
    }

    onItemClick(item) {
        this.dispatch(setSandboxCurrentApi(item.uri));
    }
}

export default registerComponent(Sandbox, true);
