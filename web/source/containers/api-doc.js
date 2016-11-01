import React, { PropTypes, Component} from 'react'
import {connect} from 'react-redux'

class ApiDoc extends Component {
    render() {
        let {responseSchema, javadocUrl, apiUri} = this.props;

        if(responseSchema) {
            return (
                <div style={{padding: '2px 8px', whiteSpace: 'normal'}}>
                    <h4 style={{margin: '0px'}}>Response JSON schema</h4>

                    <p>{responseSchema}</p>
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

export default ApiDoc = connect(mapStateToProps)(ApiDoc);
