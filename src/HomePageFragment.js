import React, {useEffect, useRef, useState} from 'react';
import {findNodeHandle, requireNativeComponent, UIManager, View} from 'react-native';
//调用 HomePageManger 中的receiveCommand方法 通过 create
const createFragment = (viewId, bundle) =>
    UIManager.dispatchViewManagerCommand(
        viewId,
        UIManager.HomePageManager.Commands.create.toString(), // we are calling the 'create' command
        [viewId, bundle],
    );

export const HomePageFragment = ({style: style, bundle}) => {
    // 声明一个叫 "count" 的 state 变量
    const [count, setCount] = useState(1);
    const ref = useRef(null);
    // Similar to componentDidMount and componentDidUpdate:
    useEffect(() => {
        const viewId = findNodeHandle(ref.current);
        createFragment(viewId, bundle);
    }, []);
    return (
        <HomePageManager
            // bundle={bundle}
            style={{
                height: style && style.height !== undefined ? style.height : '100%',
                width: style && style.width !== undefined ? style.width : '100%',
            }}
            ref={ref}
        />
    );
};

const HomePageManager = requireNativeComponent(
    'HomePageManager', {
        propTypes: {
            // bundle: PropTypes.string,
            ...View.propTypes,
        },
    },
);

