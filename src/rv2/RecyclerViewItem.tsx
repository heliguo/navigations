import {DataSource} from "./DataSource";
import React from 'react';
import {requireNativeComponent, StyleProp, ViewStyle} from 'react-native';

interface RecyclerViewItemProps {
    style?: StyleProp<ViewStyle>;
    shouldUpdate: boolean;
    itemIndex: number;
    dataSource: DataSource;
    renderItem: React.FunctionComponent;
}

export const RecyclerViewItem: React.FC<RecyclerViewItemProps> =
    ({
         style,
         itemIndex,
         dataSource,
         renderItem
     }) => {
        let element = renderItem(dataSource.get(itemIndex), itemIndex);
        return (
            <NativeRecyclerViewItem {...style}>
                {element}
            </NativeRecyclerViewItem>

        );

    };

const NativeRecyclerViewItem = requireNativeComponent('RecyclerViewItemView');


