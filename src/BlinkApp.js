import React, {Component} from 'react';
import {View} from 'react-native';
import Blink from './Blink';


export default class BlinkApp extends Component {
    render() {
        return (
            <View>
                <Blink text='I love to blink'/>
                <Blink style={{color: 'red'}} text='一闪一闪亮晶晶'/>
            </View>
        );
    }
}
