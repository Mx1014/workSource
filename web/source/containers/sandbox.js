import React, { PropTypes, Component} from 'react'
import {connect} from 'react-redux'

import {SplitPane, Pane} from 'widget-splitter'
import {dispatch} from 'widget-redux-util/redux-enhancer'

import {ApiList} from '../components/apilist'
import {fetchApiList, loadNavigationData, initConsole, setSandboxCurrentApi, setApiFilter} from '../actions'
import ApiPanel from './api-panel'

import layoutStyles from '../shared/style/layout.css'

class Sandbox extends Component {
    render() {
        let {apis} = this.props;

        return (
            <SplitPane>
                <Pane style={{width: '350px'}}>
                    <table className={layoutStyles.layoutTable}><tbody>
                    <tr style={{height: '40px', backgroundColor: '#f1f1f1', padding: '4px 8px'}}><td>
                        <div style={{padding: '4px 32px 4px 16px'}}>
                            <input type="text"
                                   ref={(domInput) => this.domInput = domInput}
                                   onChange={(e) => this.onInputChange(e)}
                                   style={{width: '100%'}}
                                   placeholder={"Type API prefix to narrow down the list below"}
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
        this.props.loadNavigationData();
        this.props.initConsole();
    }

    onInputChange(e) {
        dispatch(setApiFilter(this.domInput.value))
    }

    onItemClick(item) {
        dispatch(setSandboxCurrentApi(item.uri));
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
})

export default Sandbox = connect(
    mapStateToProps,
    {
        loadNavigationData,
        initConsole
    }
)(Sandbox);
