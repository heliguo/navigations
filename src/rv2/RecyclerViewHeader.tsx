import React from "react";
import {RecyclerViewProps} from "./RecyclerViewProps";
import {requireNativeComponent} from "react-native";

export const RecyclerViewHeader: React.FC<RecyclerViewProps> =
    ({
         style,
         renderItem
     }) => {
        return (
            <NativeRecyclerHeaderItem {...style}>
                {renderItem}
            </NativeRecyclerHeaderItem>

        );

    };

const NativeRecyclerHeaderItem = requireNativeComponent('RecyclerViewHeaderView');
