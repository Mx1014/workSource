import React, { PropTypes, Component} from 'react'

import styles from '../shared/style/app.css'

export default class InputForm extends Component {

    constructor(props) {
        super(props)
    }

    render() {
        let {title, items} = this.props;

        return (
            <div>
                <form ref={(domForm)=>this.domForm = domForm}>
                <table className={styles.inputForm}>
                    <tbody>
                        <tr>
                            <td colSpan="2" className={styles.inputFullCol} style={{padding: '4px 8px', backgroundColor: 'lightgray'}}>{title}</td>
                        </tr>
                        {items.map((item, index)=>this.renderRow(item, index))}
                        <tr>
                            <td colSpan="2" className={styles.inputFullCol} style={{padding: '8px 8px'}}>
                                <button onClick={(e)=>this.onSubmit(e)}>Submit</button>
                            </td>
                        </tr>
                    </tbody>
                </table>
                </form>
            </div>
        )
    }

    onSubmit(e) {
        e.preventDefault()
        e.stopPropagation()

        let {items, onFormSubmit = null} = this.props;
        let data = {}

        for(let item of items) {
            if(item.typeName == 'MultipartFile[]' || item.typeName == 'MultipartFile') {
                if (!!this.domForm.elements[item.paramName].value) {
                    let formElement = this.domForm.elements[item.paramName];
                    data[item.paramName] = formElement.files[0];
                }
            } else {
                if (!!this.domForm.elements[item.paramName].value)
                    data[item.paramName] = this.domForm.elements[item.paramName].value;
                else
                    data[item.paramName] = '';
            }
        }

        if(onFormSubmit)
            onFormSubmit(this.props.title, data);
    }

    renderRow(item, index) {
        let className

        if(index % 2 == 0)
            className = styles.inputRowEven
        else
            className = styles.inputRowOdd

        let labelStyle = {}
        if(item.required)
            labelStyle = {fontWeight: 'bold'}

        if(item.typeName == 'MultipartFile[]' || item.typeName == 'MultipartFile') {
            return (
                <tr className={className} key={index}>
                    <td className={styles.inputLabelCol} style={labelStyle}>{item.paramName}:</td>
                    <td className={styles.inputValueCol}><input type="file" name={item.paramName}
                                                                style={{width: '100%'}}></input></td>
                </tr>
            )
        } else {
            return (
                <tr className={className} key={index}>
                    <td className={styles.inputLabelCol} style={labelStyle}>{item.paramName}:</td>
                    <td className={styles.inputValueCol}><input type="text" name={item.paramName}
                                                                style={{width: '100%'}}></input></td>
                </tr>
            )
        }
    }
}
