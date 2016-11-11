import React, { PropTypes, Component} from 'react'
import {connect} from 'react-redux'

import jsonStringify from 'json-pretty'

class ApiDoc extends Component {
    render() {
        let {responseSchema, javadocUrl, apiUri} = this.props;

        if (process.env.NODE_ENV === 'production') {
            let url = document.location.toString();
            let SERVICE_ROOT = url;
            SERVICE_ROOT = SERVICE_ROOT.substring(0, SERVICE_ROOT.lastIndexOf('/'));

            javadocUrl = SERVICE_ROOT + javadocUrl;
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

function getResponseSchema(state) {
    let navigationState = state.apiNavigation;

    if(!!navigationState.currentApi)
        return navigationState.currentApi.returnTemplate;

    return null;
}

function getDocUrl(state) {
    let navigationState = state.apiNavigation;

    if(!!navigationState.currentApi)
        return navigationState.currentApi.javadocUrl;

    return null;
}

function getUri(state) {
    let navigationState = state.apiNavigation;

    if(!!navigationState.currentApi)
        return navigationState.currentApi.uri;

    return null;
}

const mapStateToProps = (state, ownProps) => {
    return ({
        responseSchema: getResponseSchema(state),
        javadocUrl: getDocUrl(state),
        apiUri: getUri(state)
    });
}

export default connect(mapStateToProps)(ApiDoc);
