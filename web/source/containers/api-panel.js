import React, { PropTypes, Component} from 'react'
import {connect} from 'react-redux'

import {SplitPane, Pane} from 'widget-splitter'
import {Tab} from 'widget-tab'

import ApiResult from './api-console'
import ApiDoc from './api-doc'
import InputForm from '../components/input'

import {apiAction, appendToConsole} from '../actions'

class ApiPanel extends Component {
    render() {
        let {currentApi, onFormSubmit} = this.props;

        let items = [
            {description: 'API Console', component: <ApiResult/>},
            {description: 'API Documentation', component: <ApiDoc/>},
        ];

        if(currentApi) {
            return (
                <div style={{height: "100%", width: "100%"}}>
                    <SplitPane splitDirection="vertical">
                        <Pane style={{height: '300px', overflow: 'scroll'}}>
                            <InputForm title={currentApi.uri} items={currentApi.params} onFormSubmit={onFormSubmit}/>
                        </Pane>
                        <Pane>
                            <Tab items={items}/>
                        </Pane>
                    </SplitPane>
                </div>
            );
        } else {
            return (
                <div style={{height: "100%", width: "100%"}}>
                    <SplitPane splitDirection="vertical" style={{height: "100%", width: "100%"}}>
                        <Pane style={{height: '300px'}}>
                            <p style={{padding: '0px 8px'}}>Please select an API at left panel to test it lively</p>
                        </Pane>
                        <Pane>
                            <Tab items={items}/>
                        </Pane>
                    </SplitPane>
                </div>
            );
        }
    }
}

const mapStateToProps = (state, ownProps) => ({
    currentApi: state.apiNavigation.currentApi
});

const mapDispatchToProps = (dispatch, ownProps) => ({
    onFormSubmit: (uri, data) => {
        dispatch(appendToConsole('=> ' + uri + ' ' + JSON.stringify(data)))
        dispatch(apiAction(uri, data))
    }
});

export default connect(
    mapStateToProps,
    mapDispatchToProps
)(ApiPanel);
