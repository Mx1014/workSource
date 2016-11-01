import React, { PropTypes, Component} from 'react'
import ReactDOM from 'react-dom'

import Header from '../components/header'

import layoutStyles from '../shared/style/layout.css'
import styles from '../shared/style/app.css'

export default class Home extends Component {
    render() {
        let {children} = this.props;

        return (
            <table className={layoutStyles.layoutTable}><tbody>
            <tr style={{height: '40px'}}><td>
                <Header />
            </td></tr>
            <tr><td>
                {children}
            </td></tr>
            </tbody></table>
        );
    }
}
