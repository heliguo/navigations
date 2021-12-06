/**
 * the example of RNRecyclerView
 */
export default class RNBannerTest extends Component<any, any, any> {
    constructor(props: any);
    _banner: RNBanner;
    renderItem: ({ item, index }: {
        item: any;
        index: any;
    }) => JSX.Element;
    onItemClick(position: Number): void;
}
import { Component } from "react";
import RNBanner from "./banner/RNBanner";
