import React, { PropTypes, Component} from 'react'
import {connect} from 'react-redux'

import {clearConsole} from '../actions'

import styles from '../shared/style/app.css'
import layoutStyles from '../shared/style/layout.css'

class ApiConsole extends Component {
    render() {
        let {items, onClearClick} = this.props;

        return (
            <table className={layoutStyles.layoutTable}><tbody>
                <tr style={{height: '20px'}}><td>
                <div>
                    <button onClick={onClearClick}>Clear</button>
                </div>
                </td></tr>
                <tr style={{height: '*%'}}><td>
                <div className={styles.consoleBox}>
                    {items.map((item, index)=>this.renderItem(item, index))}
                </div>
                </td></tr>
            </tbody></table>
        );
    }

    renderItem(item, index) {
        return <p key={index}>{item}</p>;
    }
}

const mapStateToProps = (state, ownProps) => ({
    items: state.apiConsole.consoleItems
});

const mapDispatchToProps = (dispatch) => ({
    onClearClick: (e)=> {
        dispatch(clearConsole());
    }
});

export default ApiConsole = connect(
    mapStateToProps,
    mapDispatchToProps
)(ApiConsole);
