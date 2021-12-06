import {requireNativeComponent} from "react-native";
import React from "react";
import {RecyclerViewProps} from "./RecyclerViewProps";


export const RecyclerViewEmpty: React.FC<RecyclerViewProps> =
    ({
         style,
         renderItem
     }) => {
        return (
            <NativeRecyclerEmptyItem {...style}>
                {renderItem}
            </NativeRecyclerEmptyItem>

        );

    };

const NativeRecyclerEmptyItem = requireNativeComponent('RecyclerViewEmptyView');
