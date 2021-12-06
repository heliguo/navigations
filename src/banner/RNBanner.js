import React, {Component} from 'react';
import PropTypes from 'prop-types';
import {DeviceEventEmitter, requireNativeComponent, StyleSheet, View} from 'react-native';

export default class RNBanner extends Component {

    static propTypes = {
        autoLoop: PropTypes.bool,//是否自动轮播，不传默认为true
        ...View.propTypes,
        renderItem: PropTypes.func,
        renderNumber: PropTypes.number.isRequired,
        bannerDataSource: PropTypes.array.isRequired,
        onItemClick: PropTypes.func,//点击事件
        loopTime: PropTypes.number,//轮播时间间隔，毫秒
        scrollTime: PropTypes.number,//图片滑动时间间隔，毫秒（滑动快慢）
        currentItem: PropTypes.number,//指定当前item，int
        bannerRound: PropTypes.number,//圆角
    };

    static defaultProps = {
        bannerDataSource: [],
    };

    constructor(props) {
        super(props);
        this._onItemClick = this._onItemClick.bind(this);
    }

    componentDidMount(): void {
        this.clickListener = DeviceEventEmitter.addListener('itemClick', (position) => {
            this._onItemClick(position);
        });
    }

    componentWillUnmount(): void {
        DeviceEventEmitter.removeListener(this.clickListener);

    }


    render() {
        const {
            autoLoop,
            bannerDataSource,
            renderNumber,
            loopTime,
            scrollTime,
            currentItem,
            renderItem,
            bannerRound,
            ...rest
        } = this.props;

        let body = [];//item集合
        if (renderNumber > 0) {
            for (let i = 0; i < renderNumber; i++) {
                body.push(
                    <View
                        style={styles.absolute}
                        renderItem={renderItem}
                    />,
                );
            }
        }
        return (
            <NativeBanner
                autoLoop={autoLoop}
                bannerDataSource={bannerDataSource}
                loopTime={loopTime}
                currentItem={currentItem}
                scrollTime={scrollTime}
                bannerRound={bannerRound}
                {...rest}>
                {body}
            </NativeBanner>
        );
    }


    _onItemClick(position) {
        if (this.props.onItemClick) {
            this.props.onItemClick(position);
        }
    };

}

const nativeOnlyProps = {
    nativeOnly: {
        onItemClick: true,
    },
};

const styles = StyleSheet.create({
    absolute: {
        position: 'absolute',
        top: 0,
        left: 0,
        right: 0,
    },
});

const NativeBanner = requireNativeComponent('RNBannerManager', RNBanner, nativeOnlyProps);

module.exports = RNBanner;

