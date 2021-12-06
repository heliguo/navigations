export default class PropsText extends Component<any, any, any> {
    static defaultProps: {
        age: number;
        sex: string;
    };
    static propTypes: {
        name: any;
        age: any;
        sex: any;
    };
    constructor(props: any);
    constructor(props: any, context: any);
}
import { Component } from "react";
