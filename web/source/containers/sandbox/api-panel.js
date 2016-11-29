import React, { PropTypes, Component} from 'react'

import {registerComponent, getComponentState, WidgetComponent} from 'widget-redux-util/redux-enhancer'

import {SplitPane, Pane} from 'widget-splitter'
import {Tab} from 'widget-tab'

import Sandbox from './sandbox'
import ApiResult from './api-console'
import ApiDoc from './api-doc'
import InputForm from '../../components/input'

import {apiAction, appendToConsole} from '../../actions'

class ApiPanel extends WidgetComponent {

    //
    // Component state structure design and state -> props mapping
    //
    static statePath = "ApiPanel";
    static mapStateToProps(state, ownProps) {
        let navigationState = getComponentState(state, "Sandbox", null, false);

        let currentApi = null;
        if(!!navigationState)
            currentApi = navigationState.currentApi;

        return ({
            responseSchema: !!currentApi ? currentApi.returnTemplate : null,
            javadocUrl: !!currentApi ? currentApi.javadocUrl : null,
            apiUri: !!currentApi ? currentApi.uri : null,
            currentApi
        });
    }

    //
    // Component rendering and behaviour
    //
    render() {
        let {currentApi} = this.props;

        let items = [
            {description: 'API Console', component: <ApiResult/>},
            {description: 'API Documentation', component: <ApiDoc/>},
        ];

        if(currentApi) {
            return (
                <div style={{height: "100%", width: "100%"}}>
                    <SplitPane splitDirection="vertical">
                        <Pane style={{height: '300px', overflow: 'scroll'}}>
                            <InputForm title={currentApi.uri}
                                       items={currentApi.params}
                                       onFormSubmit={(uri, data)=>this.onFormSubmit(uri, data)} />
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

    onFormSubmit(uri, data) {
        this.dispatch(appendToConsole('=> ' + uri + ' ' + JSON.stringify(data)))
        this.dispatch(apiAction(uri, data))
    }
}

export default registerComponent(ApiPanel, true);
