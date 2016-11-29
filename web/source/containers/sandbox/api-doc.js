import React, { PropTypes, Component} from 'react'

import {registerComponent, getComponentState, WidgetComponent} from 'widget-redux-util/redux-enhancer'

import jsonStringify from 'json-pretty'

import Sandbox from './sandbox'

import {getServiceRoot} from '../../actions'

class ApiDoc extends WidgetComponent {

    //
    // Component state structure design and state -> props mapping
    //
    static statePath = "ApiDoc";
    static mapStateToProps(state, ownProps) {
        let navigationState = getComponentState(state, Sandbox, null, false);
        let currentApi = null;
        if(!!navigationState)
            currentApi = navigationState.currentApi;

        return ({
            responseSchema: !!currentApi ? currentApi.returnTemplate : null,
            javadocUrl: !!currentApi ? currentApi.javadocUrl : null,
            apiUri: !!currentApi ? currentApi.uri : null
        });
    }

    //
    // Component rendering and behaviour
    //
    render() {
        let {responseSchema, javadocUrl, apiUri} = this.props;

        // attach random info to disable browser caching mechanism
        javadocUrl = javadocUrl + '?r=' + Math.random();

        if(responseSchema) {
            let obj = responseSchema;
            try {
                obj = JSON.parse(responseSchema);
            } catch(e) {
                console.log(`parsing ${responseSchema} got error: ${e}`);
            }
            return (
                <div style={{padding: '2px 8px', whiteSpace: 'normal'}}>
                    <h4 style={{margin: '0px'}}>Response JSON schema</h4>

                    <pre>{jsonStringify(obj)}</pre>
                    <h4>JavaDoc</h4>
                    <a href={javadocUrl} target="blank">{apiUri} details</a>
                </div>
            );
        } else {
            return (
                <div style={{padding: '0px 8px'}}>
                    <p style={{margin: '0px'}}>Please select an API item</p>
                </div>
            );
        }
    }
}

export default registerComponent(ApiDoc, true);
