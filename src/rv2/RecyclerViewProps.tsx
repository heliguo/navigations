import {StyleProp, ViewStyle} from "react-native";
import React from "react";

export interface RecyclerViewProps {
    style?: StyleProp<ViewStyle>;
    renderItem: React.FunctionComponent|React.Component;
}
