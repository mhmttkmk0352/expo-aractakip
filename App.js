
import {createAppContainer} from 'react-navigation';
import {createStackNavigator} from 'react-navigation-stack';

import HomeScreen from './src/pages/homescreen.js';
import MapScreen from './src/pages/mapscreen.js';


/*
Source:
https://forums.expo.io/t/openstreetmap-and-here-maps-with-expo/11505
*/

/*
var tileUrl = "https://2.base.maps.cit.api.here.com/maptile/2.1/"
		+ "maptile/newest/normal.day/{z}/{x}/{y}/256/png8"
		+ "?app_id=NQAkFAx4PPaqQcdOumcdj5piB8Q1dp5fB25DpkKF&app_code=ZoFeuKy52BCGoHB7OjD6G3M16VNHvcTZqW4As7UI";

    */

 


const MainNavigator = createStackNavigator({

  Home: {screen: HomeScreen},
  Map: {screen: MapScreen},
});

const App = createAppContainer(MainNavigator);



export default App;