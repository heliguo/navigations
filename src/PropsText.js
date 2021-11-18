import PropTypes from 'prop-types';
import React, {Component} from 'react';
import {Text} from 'react-native';

export default class PropsText extends Component {
    static defaultProps = {age: 22, sex: 'man'};
    //约束的关键就是这里在定义属性的时候指定属性的类型，类似安卓private String name;
    static propTypes = {
        name: PropTypes.string.isRequired,//必须参数
        age: PropTypes.number,
        sex: PropTypes.string,
    };

    render() {
        //在这里我们使用props中的name属性
        return <Text>{this.props.name + ' age：' + this.props.age + ' sex: ' + this.props.sex}</Text>;
    }
}
