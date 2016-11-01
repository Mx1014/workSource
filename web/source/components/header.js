import React, { PropTypes, Component} from 'react'

import styles from '../shared/style/app.css'

export default class Header extends Component {
    render() {
        return (
            <div className={styles.header}>Core-Server API portal</div>
        )
    }
}
