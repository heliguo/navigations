import React, {useEffect, useRef} from 'react';
import {findNodeHandle, UIManager} from 'react-native';
import {HomePageManager} from './HomePageManager';

//调用 HomePageManger 中的receiveCommand方法 通过 create
const createFragment = (viewId, bundle) =>
    UIManager.dispatchViewManagerCommand(
        viewId,
        UIManager.HomePageManager.Commands.create.toString(), // we are calling the 'create' command
        [viewId, bundle],
    );

export const HomePageFragment = ({style, bundle}) => {
    const ref = useRef(null);
    useEffect(() => {
        const viewId = findNodeHandle(ref.current);
        createFragment(viewId, bundle);
    }, []);
    return (
        <HomePageManager
            bundle={bundle}
            style={{
                height: style && style.height !== undefined ? style.height : '100%',
                width: style && style.width !== undefined ? style.width : '100%',
            }}
            ref={ref}
        />
    );
};

