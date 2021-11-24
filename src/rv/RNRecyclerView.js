import React, {PureComponent} from 'react';
import ReactNative, {requireNativeComponent, StyleSheet, UIManager, View} from 'react-native';
import PropTypes from 'prop-types';
import DataSource from './DataSource';
import RecyclerViewItem from './RcyclerViewItem';


class RNRecyclerView extends PureComponent {
    static propTypes = {
        layoutManager:PropTypes.array.isRequired,
        ...View.propTypes,
        renderItem: PropTypes.func,
        dataSource: PropTypes.instanceOf(DataSource),
        windowSize: PropTypes.number,
        initialListSize: PropTypes.number,
        initialScrollIndex: PropTypes.number,
        initialScrollOffset: PropTypes.number,
        initialScrollPosition: PropTypes.number,
        inverted: PropTypes.bool,
        itemAnimatorEnabled: PropTypes.bool,
        ListHeaderComponent: PropTypes.element,//头
        ListFooterComponent: PropTypes.element,//尾
        ListEmptyComponent: PropTypes.element,//空
        ItemSeparatorComponent: PropTypes.element,//分割
        onVisibleItemsChange: PropTypes.func,
        refreshControl: PropTypes.object,
    };

    static defaultProps = {
        dataSource: new DataSource([], (item, i) => i),
        initialListSize: 10,
        windowSize: 30,
        inverted: false,
        itemAnimatorEnabled: true,
    };


    _dataSourceListener = {
        onUnshift: () => {
            this._notifyItemRangeInserted(0, 1);
            this._shouldUpdateAll = true;
        },

        onPush: () => {
            const {dataSource} = this.props;
            this._notifyItemRangeInserted(dataSource.size(), 1);
            this._shouldUpdateAll = true;
        },

        onMoveUp: (position) => {
            this._notifyItemMoved(position, position - 1);
            this._shouldUpdateAll = true;
        },

        onMoveDown: (position) => {
            this._notifyItemMoved(position, position + 1);
            this._shouldUpdateAll = true;
        },

        onSplice: (start, deleteCount, ...items) => {
            if (deleteCount > 0) {
                this._notifyItemRangeRemoved(start, deleteCount);
            }
            if (items.length > 0) {
                this._notifyItemRangeInserted(start, items.length);
            }
            this._shouldUpdateAll = true;
        },

        onSet: (index, item) => {
            this._shouldUpdateKeys.push(this.props.dataSource.getKey(item, index));
            this.forceUpdate();
        },

        onSetDirty: () => {
            this._shouldUpdateAll = true;
            this.forceUpdate();
        }
    };

    constructor(props) {
        super(props);
        const {
            dataSource,//数据
            initialListSize,//初始化大小
            initialScrollIndex//定位index
        } = this.props;

        dataSource._addListener(this._dataSourceListener);

        let visibleRange = initialScrollIndex >= 0 ?
            [initialScrollIndex, initialScrollIndex + initialListSize]
            : [0, initialListSize];

        this.state = {
            firstVisibleIndex: visibleRange[0],
            lastVisibleIndex: visibleRange[1],
            itemCount: dataSource.size()
        };

        this._shouldUpdateAll = true;
        this._shouldUpdateKeys = [];
    }

    componentWillMount() {
    }

    componentWillUnmount() {
        const {dataSource} = this.props;
        this.mounted = false;
        if (dataSource) {
            dataSource._removeListener(this._dataSourceListener);
        }
    }

    componentDidMount() {
        const {initialScrollIndex, initialScrollOffset, initialScrollPosition} = this.props;
        this.mounted = true;
        if (initialScrollIndex) {
            setTimeout(() => {
                this.scrollToIndex({
                    animated: false,
                    index: initialScrollIndex,
                    viewPosition: initialScrollPosition,
                    viewOffset: initialScrollOffset
                });
            }, 0);
        }

        this._shouldUpdateAll = false;
        this._shouldUpdateKeys = [];
    }

    componentWillReceiveProps(nextProps) {
        const {dataSource} = this.props;
        if (nextProps.dataSource !== dataSource) {
            dataSource._removeListener(this._dataSourceListener);
            nextProps.dataSource._addListener(this._dataSourceListener);
            this._notifyDataSetChanged(nextProps.dataSource.size());
        }
    }

    componentDidUpdate(prevProps, prevState) {
        this._shouldUpdateAll = false;
        this._shouldUpdateKeys = [];
    }

    render() {
        const {
            layoutManager,
            dataSource,
            renderItem,
            ListHeaderComponent,
            ListFooterComponent,
            ListEmptyComponent,
            ItemSeparatorComponent,
            inverted,
            refreshControl,
            ...rest
        } = this.props;

        const itemCount = dataSource.size();
        const end = itemCount - 1;
        let stateItemCount = this.state.itemCount;

        let body = [];//item集合
        let itemRangeToRender = this._calcItemRangeToRender(this.state.firstVisibleIndex, this.state.lastVisibleIndex);

        if (ListHeaderComponent) {
            var headerElement = React.isValidElement(ListHeaderComponent)
                ? ListHeaderComponent
                : <ListHeaderComponent/>;
        }

        if (ListFooterComponent) {
            var footerElement = React.isValidElement(ListFooterComponent)
                ? ListFooterComponent
                : <ListFooterComponent/>;
        }

        if (ItemSeparatorComponent) {
            var separatorElement = React.isValidElement(ItemSeparatorComponent)
                ? ItemSeparatorComponent
                : <ItemSeparatorComponent/>;
        }

        if (itemCount > 0) {
            for (let i = itemRangeToRender[0]; i < itemRangeToRender[1]; i++) {
                let item = dataSource.get(i);
                let itemKey = dataSource.getKey(item, i);
                let shouldUpdate = this._needsItemUpdate(itemKey);
                let isFirst = i === 0;
                let isLast = i === end;
                let header = inverted ? (isLast && footerElement) : (isFirst && headerElement);
                let footer = inverted ? (isFirst && headerElement) : (isLast && footerElement);
                let separator = inverted ? (!isFirst && separatorElement) : (!isLast && separatorElement);
                body.push(
                    <RecyclerViewItem
                        key={itemKey}
                        style={styles.absolute}
                        itemIndex={i}
                        shouldUpdate={shouldUpdate}
                        dataSource={dataSource}
                        renderItem={renderItem}
                        header={header}
                        separator={separator}
                        footer={footer}/>
                );
            }
        } else if (ListEmptyComponent) {
            const emptyElement = React.isValidElement(ListEmptyComponent)
                ? ListEmptyComponent
                : <ListEmptyComponent/>;

            body.push(
                <RecyclerViewItem
                    style={styles.absolute}
                    key="$empty"
                    itemIndex={0}
                    shouldUpdate={true}
                    dataSource={dataSource}
                    renderItem={() => emptyElement}
                    header={headerElement}
                    footer={footerElement}/>
            );

            stateItemCount = 1;
        }

        return (
            <NativeRecyclerView
                layoutManager={layoutManager}
                {...rest}
                itemCount={stateItemCount}
                onVisibleItemsChange={this._handleVisibleItemsChange}
                inverted={inverted}>
                {body}
            </NativeRecyclerView>
        );
    }

    setLocalState = (state, callback) => {
        if (this.mounted) {
            this.setState(state, callback);
        }
    };

    scrollToEnd({animated = true, velocity} = {}) {
        this.scrollToIndex({
            index: this.props.dataSource.size() - 1,
            animated,
            velocity
        });
    }

    scrollToIndex = ({animated = true, index, velocity, viewPosition, viewOffset}) => {
        index = Math.max(0, Math.min(index, this.props.dataSource.size() - 1));

        if (animated) {
            UIManager.dispatchViewManagerCommand(
                ReactNative.findNodeHandle(this),
                UIManager.AndroidRecyclerViewBackedScrollView.Commands.scrollToIndex,
                [animated, index, velocity, viewPosition, viewOffset],
            );
        } else {
            this.setLocalState({
                firstVisibleIndex: index,
                lastVisibleIndex: index + (this.state.lastVisibleIndex - this.state.firstVisibleIndex)
            }, () => {
                UIManager.dispatchViewManagerCommand(
                    ReactNative.findNodeHandle(this),
                    UIManager.AndroidRecyclerViewBackedScrollView.Commands.scrollToIndex,
                    [animated, index, velocity, viewPosition, viewOffset],
                );
            });
        }
    };

    _needsItemUpdate(itemKey) {
        return this._shouldUpdateAll || this._shouldUpdateKeys.includes(itemKey);
    }

    _handleVisibleItemsChange = ({nativeEvent}) => {
        var firstIndex = nativeEvent.firstIndex;
        var lastIndex = nativeEvent.lastIndex;

        this.setLocalState({
            firstVisibleIndex: firstIndex,
            lastVisibleIndex: lastIndex,
        });

        const {onVisibleItemsChange} = this.props;
        if (onVisibleItemsChange) {
            onVisibleItemsChange(nativeEvent);
        }
    };

    _calcItemRangeToRender(firstVisibleIndex, lastVisibleIndex) {
        const {dataSource, windowSize} = this.props;
        var count = dataSource.size();
        var from = Math.min(count, Math.max(0, firstVisibleIndex - windowSize));
        var to = Math.min(count, lastVisibleIndex + windowSize);
        return [from, to];
    }

    _notifyItemMoved(currentPosition, nextPosition) {
        UIManager.dispatchViewManagerCommand(
            ReactNative.findNodeHandle(this),
            UIManager.AndroidRecyclerViewBackedScrollView.Commands.notifyItemMoved,
            [currentPosition, nextPosition],
        );
        this.forceUpdate();
    }

    _notifyItemRangeInserted(position, count) {
        UIManager.dispatchViewManagerCommand(
            ReactNative.findNodeHandle(this),
            UIManager.AndroidRecyclerViewBackedScrollView.Commands.notifyItemRangeInserted,
            [position, count],
        );

        const {firstVisibleIndex, lastVisibleIndex, itemCount} = this.state;

        if (itemCount === 0) {
            this.setLocalState({
                itemCount: this.props.dataSource.size(),
                firstVisibleIndex: 0,
                lastVisibleIndex: this.props.initialListSize
            });
        } else {
            if (position <= firstVisibleIndex) {
                this.setLocalState({
                    firstVisibleIndex: this.state.firstVisibleIndex + count,
                    lastVisibleIndex: this.state.lastVisibleIndex + count,
                });
            } else {
                this.forceUpdate();
            }
        }
    }

    _notifyItemRangeRemoved(position, count) {
        UIManager.dispatchViewManagerCommand(
            ReactNative.findNodeHandle(this),
            UIManager.AndroidRecyclerViewBackedScrollView.Commands.notifyItemRangeRemoved,
            [position, count],
        );
        this.forceUpdate();
    }

    _notifyDataSetChanged(itemCount) {
        UIManager.dispatchViewManagerCommand(
            ReactNative.findNodeHandle(this),
            UIManager.AndroidRecyclerViewBackedScrollView.Commands.notifyDataSetChanged,
            [itemCount],
        );
        this.setLocalState({
            itemCount
        });
    }
}

const nativeOnlyProps = {
    nativeOnly: {
        onVisibleItemsChange: true,
        itemCount: true
    }
};

const styles = StyleSheet.create({
    absolute: {
        position: 'absolute',
        top: 0,
        left: 0,
        right: 0
    },
});

const NativeRecyclerView = requireNativeComponent('AndroidRecyclerViewBackedScrollView', RNRecyclerView, nativeOnlyProps);

module.exports = RNRecyclerView;
