import React, {Component} from 'react';
import PropsText from './PropsText';

export default class HomePager extends Component {

    render() {
        let params = {name: 'li si', age: 18, sex: 'man'};
        return <PropsText name={params.name} sex={params.sex} age={params.age}/>;
    }
};
