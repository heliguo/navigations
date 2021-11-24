import React from 'react';
import {Button, DeviceEventEmitter, NativeModules, StyleSheet, Text, ToastAndroid, View} from 'react-native';
import {Navigation} from 'react-native-navigation';
import {HomePageFragment} from './src/HomePageFragment';
import Icon from 'react-native-vector-icons/FontAwesome5';
import PropsText from './src/PropsText';
import BlinkApp from './src/BlinkApp';
import NativeUIManager from 'react-native/Libraries/ReactNative/NativeUIManager';
import RNRecyclerViewTest from './src/RNRecyclerViewTest';


const LoginScreen = () => {

    return (
        <View style={styles.root}>
            <Button
                uppercase={false}
                title='Login'
                color='#710ce3'
                onPress={() => {
                    Navigation.setRoot(mainRoot);

                    NativeModules.CommonModule.testAndroidCallback('1', (result) => {
                        NativeModules.CommonModule.show(result);
                    });

                    NativeModules.CommonModule.testAndroidPromise('2').then((result) => {
                        NativeModules.CommonModule.show(result);
                    });
                }}
            />
        </View>
    );
};


const HomeScreen = () => {
    return (
        <View style={styles.root}>

            <Text style={{color: 'red'}}>Hello React Native Navigation 👋</Text>

            <Button
                uppercase={false}
                title='Get android native event'
                color='#710ce3'
                onPress={() => {
                    _onPressListener();
                }}/>

        </View>
    );
};

const testStyle = StyleSheet.create({
    baseText: {
        fontFamily: 'Cochin',
    },
    titleText: {
        fontSize: 20,
        fontWeight: 'bold',
        color: '#ff0000',
    },
});

const HomeScreen2 = () => {
    return (
        <View style={styles.root}>

            <BlinkApp/>

            <Button
                testId={'button'}
                title='随机删除一条数据'
                color='#710ce3'
                onPress={() => {
                    ToastAndroid.showWithGravity('你点击了', ToastAndroid.SHORT, ToastAndroid.CENTER);
                }}/>

            <Icon name={'rocket'} color={'#ff0000'} size={20}/>
        </View>
    );
};

const HomeScreen3 = () => {
    return (
        <View style={styles.root}>

            <PropsText name={'James'} age={18} sex={'man'}/>

            <Icon name={'rocket'} color={'#ff0000'} size={20}/>
        </View>
    );
};

HomeScreen.options = {
    topBar: {
        title: {
            text: '首页',
        },
    },
    bottomTab: {
        // icon: <Icon name={'home-outline'} color={'#E6E6E6'} size={20}/>,
        // selectedIcon: <Icon name={'home-outline'} color={'#FF3C51'} size={20}/>,
        text: '首页',
        textColor: '#E6E6E6',
        selectedTextColor: '#FF3C51',

    },
};

HomeScreen2.options = {
    topBar: {
        title: {
            text: '我的',
        },
    },
    bottomTab: {
        // icon: <Icon name={'heart'} color={'#E6E6E6'} size={20}/>,
        // selectedIcon: <Icon name={'heart-filled'} color={'#FF3C51'} size={20}/>,
        text: '我的',
        textColor: '#E6E6E6',
        selectedTextColor: '#FF3C51',
    },
};

const SettingsScreen = () => {

    const callback = (params: Number) => {
        ToastAndroid.showWithGravity('~~RN Toast~~你点击了第' + (params + 1) + '条', ToastAndroid.SHORT, ToastAndroid.CENTER);
    };
    DeviceEventEmitter.addListener('ItemClick', callback);

    return (
        <HomePageFragment bundle={'～～～初始化参数～～～'}/>
    );
};

HomeScreen3.options = {
    topBar: {
        title: {
            text: '设置',
        },
    },
    bottomTab: {
        // icon: <Icon name={'cog-outline'} color={'#E6E6E6'} size={20}/>,
        // selectedIcon: <Icon name={'cog-outline'} color={'#FF3C51'} size={20}/>,
        text: '设置',
        textColor: '#E6E6E6',
        selectedTextColor: '#FF3C51',
    },
};

/**
 * 调用Android native 方法
 */
function _onPressListener() {
    let rest = NativeModules.CommonModule['MESSAGE'];//获取native常量
    let rests = NativeUIManager.HomePageManager.test;//获取native常量
    NativeModules.CommonModule.show('~~~~android toast~~~~' + rest + rests);
}

SettingsScreen.options = {
    topBar: {
        title: {
            text: '原生Fragment',
        },
    },
    bottomTab: {
        text: 'Fragment',
        textColor: '#E6E6E6',
        selectedTextColor: '#FF3C51',
    },
};


Navigation.registerComponent('Login', () => LoginScreen);
Navigation.registerComponent('Home', () => HomeScreen);
Navigation.registerComponent('Settings', () => SettingsScreen);
Navigation.registerComponent('Home2', () => HomeScreen2);
Navigation.registerComponent('Home3', () => HomeScreen3);
Navigation.registerComponent('RV', () => RV);


const RV = () => {
    return (
        <RNRecyclerViewTest/>
    );
};
RV.options = {
    topBar: {
        title: {
            text: '原生RecyclerView',
        },
    },
    bottomTab: {
        text: '原生RecyclerView',
        textColor: '#E6E6E6',
        selectedTextColor: '#FF3C51',
    },
};

Navigation.setDefaultOptions({
    statusBar: {
        backgroundColor: '#4d089a',
    },
    topBar: {
        title: {
            color: 'white',
        },
        backButton: {
            color: 'white',
        },
        background: {
            color: '#4d089a',
        },
    },
    bottomTab: {
        fontSize: 14,
        selectedFontSize: 14,
    },
});

const mainRoot = {
    root: {
        bottomTabs: {
            children: [
                {
                    stack: {
                        children: [
                            {
                                component: {
                                    name: 'Home',
                                },
                            },
                        ],
                    },
                }, {
                    stack: {
                        children: [
                            {
                                component: {
                                    name: 'Home2',
                                },
                            },
                        ],
                    },
                }, {
                    stack: {
                        children: [
                            {
                                component: {
                                    name: 'RV',
                                },
                            },
                        ],
                    },
                },
                {
                    stack: {
                        children: [
                            {
                                component: {
                                    name: 'Settings',
                                },
                            },
                        ],
                    },
                },
            ],
        },
    },
};

Navigation.events().registerAppLaunchedListener(async () => {
    Navigation.setRoot(loginRoot);

});

Navigation.events().registerComponentWillAppearListener((event) => {
        NativeModules.CommonModule.log('Appear:     componentName:' + event.componentName + '  componentType:' + event.componentType +
            '  componentId:' + event.componentId +
            '  passProps:' + event.passProps.toString());
    },
);

Navigation.events().registerComponentDidDisappearListener((event) => {
        NativeModules.CommonModule.log('DidAppear:     componentName:' + event.componentName + '  componentType:' + event.componentType +
            '  componentId:' + event.componentId);
    },
);

Navigation.events().registerBottomTabSelectedListener(({selectedTabIndex}) => {
    NativeModules.CommonModule.log('BottomTabSelected:   selectedTabIndex:' + selectedTabIndex);
});


const loginRoot = {
    root: {
        component: {
            name: 'Login',
        },
    },
};

const styles = StyleSheet.create({
    root: {
        flex: 1,
        alignItems: 'center',
        justifyContent: 'center',
        backgroundColor: 'whitesmoke',
    },
});
