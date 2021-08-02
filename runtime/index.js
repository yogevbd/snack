import 'expo-dev-client';

import 'react-native-gesture-handler';
import { registerRootComponent } from 'expo';

import App from './App';

// registerRootComponent calls AppRegistry.registerComponent('main', () => App);
// It also ensures that whether you load the app in Expo Go or in a native build,
// the environment is set up appropriately
// registerRootComponent(App);

import { NativeModules } from 'react-native';
const { DevSettings } = NativeModules;
import { Navigation } from "react-native-navigation";

Navigation.registerComponent("test", () => App);

Navigation.events().registerAppLaunchedListener(() => {
    setRoot();
})

const setRoot = () => {
    Navigation.setRoot({
        root: {
          stack: {
              children: [{
                component: {
                    name: "test",
                  },
              }]
          }
        },
      });    
}