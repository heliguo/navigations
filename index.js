import React from 'react';
import {Button, DeviceEventEmitter, NativeModules, StyleSheet, Text, View} from 'react-native';
import {Navigation} from 'react-native-navigation';
import {HomePageFragment} from './src/HomePageFragment';
import Icon from 'react-native-vector-icons/FontAwesome5';


const LoginScreen = () => {

    return (
        <View style={styles.root}>
            <Button
                uppercase={false}
                title='Login'
                color='#710ce3'
                onPress={() => {
                    Navigation.setRoot(mainRoot);

                    NativeModules.HomePageManager.testAndroidCallback('1', (result) => {
                        NativeModules.HomePageManager.show(result);
                    });

                    NativeModules.HomePageManager.testAndroidPromise('2').then((result) => {
                        NativeModules.HomePageManager.show(result);
                    });
                }}
            />
        </View>
    );
};

/**
 * 调用Android native 方法
 */
function _onPressListener() {
    NativeModules.HomePageManager.sendEvent();
}


const HomeScreen = () => {
    /**
     * 注册监听
     */
    DeviceEventEmitter.addListener('EventName', function (params) {
        console.log('params:  ' + params.toString());
        let rest = NativeModules.HomePageManager.MESSAGE;//获取native常量

        NativeModules.HomePageManager.show('native常量： ' + rest + '  map: ' + params['key']);
    });
    return (
        <View style={styles.root}>
            <Text>Hello React Native Navigation 👋</Text>

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

const HomeScreen2 = () => {
    return (
        <View style={styles.root}>

            <Text>HomeScreen2</Text>

            <Icon name={'rocket'} color={'#ff0000'} size={20}/>
        </View>
    );
};

const HomeScreen3 = () => {
    return (
        <View style={styles.root}>

            <Text>HomeScreen3</Text>

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

const SettingsScreen = () => {
    return (
        <HomePageFragment style={{
            width: '100%',
            height: '100%',
        }} bundle={'～～～初始化参数～～～'}/>

    );
};

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
                                    name: 'Home3',
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
        NativeModules.HomePageManager.log('Appear:     componentName:' + event.componentName + '  componentType:' + event.componentType +
            '  componentId:' + event.componentId +
            '  passProps:' + event.passProps.toString());
    },
);

Navigation.events().registerComponentDidDisappearListener((event) => {
        NativeModules.HomePageManager.log('DidAppear:     componentName:' + event.componentName + '  componentType:' + event.componentType +
            '  componentId:' + event.componentId);
    },
);

Navigation.events().registerBottomTabSelectedListener(({selectedTabIndex}) => {
    NativeModules.HomePageManager.log('BottomTabSelected:   selectedTabIndex:' + selectedTabIndex);
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
