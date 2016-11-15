import React, {PropTypes, Component} from 'react'
import ReactDOM from 'react-dom'

import styles from '../shared/style/app.css'

const PAGE_SIZE = 20;
const RENDER_INTERVAL = 50;
export default class ApiList extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        this.clearAsyncRendering();
        this.startAsyncRendering();

        return (
            <div className={styles.navBox}>
                <ul ref={(domListElement) => this.domListElement = domListElement}>
                </ul>
            </div>
        )
    }

    clearAsyncRendering() {
        if(!!this.asyncRenderingTimer)
            clearInterval(this.asyncRenderingTimer);

        this.asyncRenderingTimer = null;
        this.asyncRenderingPos = 0;
    }

    startAsyncRendering() {
        if(!!this.domListElement) {
            if(!!this.lastRenderedCount) {
                if (this.lastRenderedCount == this.props.items.length)
                    return;
            }

            while (this.domListElement.firstChild)
                this.domListElement.removeChild(this.domListElement.firstChild);
        }

        this.asyncRenderingTimer = setInterval(()=> {
            let {items = [], onItemClick} = this.props;
            let itemsToRender = items.slice(this.asyncRenderingPos, this.asyncRenderingPos + PAGE_SIZE);
            if(itemsToRender.length > 0) {
                for(let i = 0; i < itemsToRender.length; i++) {
                    let elem = document.createElement('ul');

                    let item = itemsToRender[i];
                    let index = this.asyncRenderingPos + i;

                    ReactDOM.render(
                        <li onClick={(e) => {onItemClick(item);}}
                            key={index}
                            data-itemid={index}>
                            <a href="#">{item.uri}</a>
                        </li>, elem);
                    this.domListElement.appendChild(elem.firstChild);
                }

                this.asyncRenderingPos += itemsToRender.length;
            } else {
                this.lastRenderedCount = items.length;
                this.clearAsyncRendering();
            }
        }, RENDER_INTERVAL);
    }
}
