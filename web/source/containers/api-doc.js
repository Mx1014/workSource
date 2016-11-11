import React, { PropTypes, Component} from 'react'

import jsonStringify from 'json-pretty'

import {registerComponent, getComponentState, WidgetComponent} from 'widget-redux-util/redux-enhancer'
import Sandbox from './sandbox'

import {getServiceRoot} from '../actions'

class ApiDoc extends WidgetComponent {

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

    render() {
        let {responseSchema, javadocUrl, apiUri} = this.props;

        if (process.env.NODE_ENV === 'production') {
            javadocUrl = getServiceRoot() + javadocUrl;
        }

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
