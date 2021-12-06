import {requireNativeComponent} from "react-native";
import React from "react";
import {RecyclerViewProps} from "./RecyclerViewProps";


export const RecyclerViewFooter: React.FC<RecyclerViewProps> =
    ({
         style,
         renderItem
     }) => {
        return (
            <NativeRecyclerFooterItem {...style}>
                {renderItem}
            </NativeRecyclerFooterItem>

        );

    };

const NativeRecyclerFooterItem = requireNativeComponent('RecyclerViewFooterView');
