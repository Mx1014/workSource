import React, { PropTypes, Component} from 'react'
import {connect} from 'react-redux'

import {SplitPane, Pane} from 'widget-splitter'
import {Accordion} from 'widget-accordion'

import {fetchApiList, loadNavigationData, initConsole} from '../actions'
import ApiPanel from './api-panel'

class Sandbox extends Component {
    render() {
        let {navigationItems} = this.props;

        return (
            <SplitPane>
                <Pane style={{width: '300px'}}>
                    <Accordion items={navigationItems} sectionCaptionStyle={{background: 'linear-gradient(white, lightgray)'}}/>
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
}

const mapStateToProps = (state, ownProps) => ({
    navigationItems: state.apiNavigation.navItems
})

export default Sandbox = connect(
    mapStateToProps,
    {
        loadNavigationData,
        initConsole,
    }
)(Sandbox);
