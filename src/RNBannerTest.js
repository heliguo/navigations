import React, {Component} from 'react';
import {Image, StyleSheet, ToastAndroid, View} from 'react-native';
import RNBanner from './banner/RNBanner';


const array = [
    {viewType: 1, imageUrl: 'https://img.zcool.cn/community/01639a56fb62ff6ac725794891960d.jpg'},
    {viewType: 1, imageUrl: 'https://img.zcool.cn/community/013de756fb63036ac7257948747896.jpg'},
    {viewType: 1, imageUrl: 'https://img.zcool.cn/community/01270156fb62fd6ac72579485aa893.jpg'},
    {viewType: 1, imageUrl: 'https://img.zcool.cn/community/01233056fb62fe32f875a9447400e1.jpg'},
    {viewType: 1, imageUrl: 'https://img.zcool.cn/community/016a2256fb63006ac7257948f83349.jpg'},
];


/**
 * the example of RNRecyclerView
 */
export default class RNBannerTest extends Component {
    constructor(props) {
        super(props);
        this.state = {
            bannerDataSource: [
                {viewType: 1, imageUrl: 'https://img.zcool.cn/community/01639a56fb62ff6ac725794891960d.jpg'},
                {viewType: 1, imageUrl: 'https://img.zcool.cn/community/013de756fb63036ac7257948747896.jpg'},
                {viewType: 1, imageUrl: 'https://img.zcool.cn/community/01270156fb62fd6ac72579485aa893.jpg'},
                {viewType: 1, imageUrl: 'https://img.zcool.cn/community/01233056fb62fe32f875a9447400e1.jpg'},
                {viewType: 1, imageUrl: 'https://img.zcool.cn/community/016a2256fb63006ac7257948f83349.jpg'},
            ],
        };
    }

    render() {
        const {bannerDataSource} = this.state;
        return (

            <View style={{alignItems: 'center', marginTop: 20}}>
                <RNBanner
                    // autoLoop={true}
                    onItemClick={this.onItemClick}
                    ref={(component) => this._banner = component}
                    style={{width: '90%', height: 200}}
                    renderNumber={0}
                    bannerRound={10}
                    bannerDataSource={bannerDataSource}
                    // renderItem={this.renderItem}
                />

            </View>

        );
    }

    renderItem = ({item, index}) => {
        return (
            <BannerItem
                item={item}
                index={index}
                bannerDataSource={this.state.bannerDataSource}/>
        );
    };

    onItemClick(position: Number) {
        ToastAndroid.showWithGravity('~~RN Toast~~你点击了第' + (position) + '条', ToastAndroid.SHORT, ToastAndroid.CENTER);
    }

}


class BannerItem extends Component {
    render() {
        const {item, index, bannerDataSource} = this.props;

        return (
            <View>
                <Image
                    source={{
                        uri: item.imageUrl,
                    }}

                />
            </View>
        );
    }
}


