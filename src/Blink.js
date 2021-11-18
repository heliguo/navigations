import {Text} from 'react-native';
import React, {Component} from 'react';

export default class Blink extends Component {
    //constructor 构造函数
    constructor(props) {
        super(props);  //执行父类的方法
        this.state = {showText: true};

        // 每1000毫秒对showText状态做一次取反操作
        setInterval(() => {
            this.setState(previousState => {
                return {showText: !previousState.showText};
            });
        }, 1000);
    }

    render() {
        let {style: style} = this.props;
        // 根据当前showText的值决定是否显示text内容
        let display = this.state.showText ? this.props.text : ' ';
        return (
            <Text style={{
                color: style && style.color !== undefined ? style.color : 'black'
            }}>{display}</Text>
        );
    }
}
