/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 */

import React, {Component} from 'react';
import {Image, StyleSheet, Text, View} from 'react-native';

import RNRecyclerView from './rv/RNRecyclerView';
import DataSource from './rv/DataSource';

let _gCounter = 1;

function newItem() {
    return {
        id: _gCounter++,
        counter: 0,
    };
}

export default class RNRecyclerViewTest extends Component {
    constructor(props) {
        super(props);
        const data = Array(100).fill().map((e, i) => newItem());

        this.state = {
            dataSource: new DataSource(data, (item, index) => item.id),
            inverted: false,
        };
    }

    render() {
        const {dataSource, inverted} = this.state;

        return (
            <View style={styles.container}>
                <RNRecyclerView
                    layoutManager={[102, 2]}
                    ref={(component) => this._recycler = component}
                    style={{flex: 1}}
                    dataSource={dataSource}
                    renderItem={this.renderItem}
                    // windowSize={20}
                    // initialScrollIndex={0}
                    // inverted={inverted}
                    // ListHeaderComponent={(
                    //     <View style={{paddingTop: 15, backgroundColor: '#eee'}}/>
                    // )}
                    // ListFooterComponent={(
                    //     <View style={{paddingTop: 15, backgroundColor: '#aaa'}}/>
                    // )}
                    // ListEmptyComponent={(
                    //     <View style={{borderColor: '#e7e7e7', borderWidth: 1, margin: 10, padding: 20}}>
                    //         <Text style={{fontSize: 15}}>No results.</Text>
                    //     </View>
                    // )}
                    // ItemSeparatorComponent={(
                    //     <View style={{
                    //         borderBottomWidth: 1,
                    //         borderColor: '#e7e7e7',
                    //         marginHorizontal: 5,
                    //         marginVertical: 5,
                    //     }}/>
                    // )}
                />
            </View>
        );
    }

    renderItem = ({item, index}) => {
        return (
            <Item
                item={item}
                index={index}
                onRemove={() => this.remove(index)}
                onAddAbove={() => this.addAbove(index)}
                onMoveUp={() => this.moveUp(index)}
                onMoveDown={() => this.moveDown(index)}
                onAddBelow={() => this.addBelow(index)}
                onIncrementCounter={() => this.incrementCounter(index)}
                dataSource={this.state.dataSource}/>
        );
    };

    remove(index) {
        this.state.dataSource.splice(index, 1);
    }

    addAbove(index) {
        this.state.dataSource.splice(index, 0, newItem());
    }

    addBelow(index) {
        const {dataSource} = this.state;
        if (index === dataSource.size() - 1 && this._recycler) {
            this._recycler.scrollToIndex({
                animated: true,
                index: dataSource.size(),
                velocity: 120,
            });
        }

        this.state.dataSource.splice(index + 1, 0, newItem());
    }

    incrementCounter(index) {
        var item = this.state.dataSource.get(index);
        item.counter++;
        this.state.dataSource.set(index, item);
    }

    moveUp(index) {
        this.state.dataSource.moveUp(index);
    }

    moveDown(index) {
        this.state.dataSource.moveDown(index);
    }

    addToTop(size) {
        const currCount = this.state.dataSource.size();
        const newItems = Array(size).fill().map((e, i) => newItem());
        this.state.dataSource.splice(0, 0, ...newItems);
    }

    addToBottom(size) {
        const currCount = this.state.dataSource.size();
        const newItems = Array(size).fill().map((e, i) => newItem());
        this.state.dataSource.splice(currCount, 0, ...newItems);
    }
}

class Item extends Component {
    render() {
        const {item, index, onRemove, onAddAbove, onAddBelow, onMoveUp, onMoveDown, onIncrementCounter, dataSource} = this.props;
        const {id, counter} = item;
        const imageWidth = 70;
        const imageHeight = 70 + (index % 5 === 2 ? 20 : 0);

        return (
            // <TouchableNativeFeedback
            //     onPress={onIncrementCounter}>
            <View style={{flexDirection: 'row', alignItems: 'center', marginHorizontal: 5, marginVertical: 5}}>
                <Image
                    source={{uri: 'http://loremflickr.com/320/240?t=' + (id % 9)}}
                    style={{
                        width: imageWidth,
                        height: imageHeight,
                        marginRight: 10,
                    }}/>
                <View style={{flex: 1}}>
                    <Text style={{
                        fontSize: 16,
                        color: 'black',
                    }}>Item #{id}</Text>
                    <Text style={{
                        fontSize: 14,
                        color: 'black',
                    }}>total size:{dataSource.size()}</Text>
                    <Text style={{
                        fontSize: 13,
                        color: '#888',
                    }}>count {counter ?
                        <Text style={{fontWeight: 'bold', color: 'black'}}>{counter}</Text>
                        : null}</Text>
                </View>
                <View style={{
                    borderBottomWidth: 1,
                    borderColor: '#e7e7e7',
                    marginHorizontal: 5,
                    marginVertical: 5,
                }}/>
            </View>
            // </TouchableNativeFeedback>
        );
    }
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#F5FCFF',
    },
});
