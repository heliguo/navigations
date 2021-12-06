export class RNRecyclerView extends PureComponent<any, any, any> {
    static propTypes: any;
    static defaultProps: {
        dataSource: DataSource;
        inverted: boolean;
    };
    constructor(props: any);
    _shouldUpdateAll: boolean;
    _shouldUpdateKeys: any[];
    UNSAFE_componentWillUnmount(): void;
    mounted: boolean;
    UNSAFE_componentDidMount(): void;
    UNSAFE_componentDidUpdate(prevProps: any, prevState: any): void;
    _dataSourceListener: {
        onUnshift: () => void;
        onPush: () => void;
        onMoveUp: (position: any) => void;
        onMoveDown: (position: any) => void;
        onSplice: (start: any, deleteCount: any, ...items: any[]) => void;
        onSet: (index: any, item: any) => void;
        onSetDirty: () => void;
    };
    _onBottom: () => void;
    _onLoadMore: () => void;
    _onTop: () => void;
    setLocalState: (state: any, callback: any) => void;
    scrollToTop({ animated, velocity }?: {
        animated?: boolean;
        velocity: any;
    }): void;
    scrollToBottom({ animated, velocity }?: {
        animated?: boolean;
        velocity: any;
    }): void;
    scrollToIndex: ({ animated, index, velocity, viewPosition, viewOffset }: {
        animated?: boolean;
        index: any;
        velocity: any;
        viewPosition: any;
        viewOffset: any;
    }) => void;
    _needsItemUpdate(itemKey: any): boolean;
    _notifyItemMoved(currentPosition: any, nextPosition: any): void;
    _notifyItemRangeInserted(position: any, count: any): void;
    _notifyItemRangeRemoved(position: any, count: any): void;
    _notifyDataSetChanged(itemCount: any): void;
    _setLayoutManager(data: any): void;
    _setReverse(inverted: any): void;
}
import { PureComponent } from "react";
import DataSource from "./DataSource";
