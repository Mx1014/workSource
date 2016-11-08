import React, { PropTypes, Component} from 'react'
import {connect} from 'react-redux'

import {SplitPane, Pane} from 'widget-splitter'

import ApiList from '../components/apilist'
import ApiPanel from './api-panel'

import layoutStyles from '../shared/style/layout.css'

import {fetchApiList, appendToConsole, setSandboxCurrentApi, setApiFilter} from '../actions'

class Sandbox extends Component {
    render() {
        let {apis, onInputChange, onItemClick} = this.props;

        return (
            <SplitPane>
                <Pane style={{width: '350px'}}>
                    <table className={layoutStyles.layoutTable}><tbody>
                    <tr style={{height: '40px', backgroundColor: '#f1f1f1', padding: '4px 8px'}}><td>
                        <div style={{padding: '4px 32px 4px 16px'}}>
                            <input type="text"
                                   ref={(domInput) => this.domInput = domInput}
                                   onChange={(e) => onInputChange(this.domInput.value)}
                                   style={{width: '100%'}}
                                   placeholder={"Type prefix to narrow down the list below"}
                                >
                            </input>
                        </div>
                    </td></tr>

                    <tr><td>
                        <ApiList items={apis} onItemClick={(item) => onItemClick(item)} />
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
        let {onSandboxInit} = this.props;

        onSandboxInit();
    }
}

function filterApiList(state, prefix) {
    if(!!prefix) {
        return state.apiNavigation.apis.filter((item) => item.uri.startsWith(prefix));
    }
    return state.apiNavigation.apis;
}

const mapStateToProps = (state, ownProps) => ({
    apis: filterApiList(state, state.apiNavigation.apiFilter)
});

const mapDispatchToProps = (dispatch) => ({
    onSandboxInit: ()=> {
        dispatch(fetchApiList());
        dispatch(appendToConsole('Core-Server API portal started at ' + new Date().toLocaleString()));
    },

    onItemClick: (item) => {
        dispatch(setSandboxCurrentApi(item.uri));
    },

    onInputChange: (val)=> {
        dispatch(setApiFilter(val))
    }
});

export default connect(
    mapStateToProps,
    mapDispatchToProps
)(Sandbox);
