import React, { PropTypes, Component} from 'react'

import {SplitPane, Pane} from 'widget-splitter'
import {WidgetComponent} from 'widget-redux-util/redux-enhancer'

import styles from '../../shared/style/app.css'

export default class HelpDesk extends WidgetComponent {
    render() {
        let {children} = this.props;

        let items = [{description: 'Server Console'}];

        return (
            <SplitPane>
                <Pane style={{width: '24%'}}>
                    <div className={styles.navBox}>
                        <ul>
                            {items.map((item, index)=>this.renderMenuItem(item, index))}
                        </ul>
                    </div>
                </Pane>
                <Pane>
                    {children}
                </Pane>
            </SplitPane>
        );
    }

    renderMenuItem(item, index) {
        return (
            <li onClick={(e)=>this.onMenuItemClick(e, item)}
                key={index}>
                <a href="#">{item.description}</a>
            </li>
        );
    }

    onMenuItemClick(e, item) {
    }
}
