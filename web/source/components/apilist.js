import React, {PropTypes, Component} from 'react'

import styles from '../shared/style/app.css'

export default class ApiList extends Component {
    render() {
        let {items, onItemClick} = this.props;

        return (
            <div className={styles.navBox}>
                <ul>
                    {
                        items.map((item, index)=> {
                            return (
                                <li onClick={(e) => {onItemClick(item);}}
                                    key={index}
                                    data-itemid={index}>
                                    <a href="#">{item.uri}</a>
                                </li>
                            )
                        })
                    }
                </ul>
            </div>
        )
    }
}
