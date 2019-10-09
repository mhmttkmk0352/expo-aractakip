import React, { Component } from 'react';
import { Text, View, Platform, Button } from 'react-native';
import MapView, {Marker, UrlTile } from 'react-native-maps';



const io = require("socket.io-client");
var s = io.connect("http://192.168.1.118:3000");

/*
import {createAppContainer} from 'react-navigation';
import {createStackNavigator} from 'react-navigation-stack';

const MainNavigator = createStackNavigator({
  Home: {screen: App},
  Profile: {screen: HaritaEkrani},
});

const App2 = createAppContainer(MainNavigator);


console.log(App2);
*/


var markers_genel = {};

var region = {
  latitude: 38.7204,
  longitude: 35.4825,
  latitudeDelta: 0.05,
  longitudeDelta: 0.05
};

/*
Source:
https://forums.expo.io/t/openstreetmap-and-here-maps-with-expo/11505
*/

/*
var tileUrl = "https://2.base.maps.cit.api.here.com/maptile/2.1/"
		+ "maptile/newest/normal.day/{z}/{x}/{y}/256/png8"
		+ "?app_id=NQAkFAx4PPaqQcdOumcdj5piB8Q1dp5fB25DpkKF&app_code=ZoFeuKy52BCGoHB7OjD6G3M16VNHvcTZqW4As7UI";

    */

 

  
class App extends Component {
    render(){
     
      return(
        <View style={{flex: 1, justifyContent: "center"}}>
          <Button
            title={"Giriş yap"}
            onPress={()=>
              {
                var har = new HaritaEkrani;
                har;
              }
            }
          />
       
        </View>
      );
    }
}

  
class HaritaEkrani extends Component {
  constructor(props){
    super(props)
    this.state = {
      UrlTile: 'http://c.tile.openstreetmap.org/{z}/{x}/{y}.png',
      sabit_latitude: 38.7205,
      sabit_longtitute: 35.4826,
      latitude: 38.7205,
      longitude: 35.4826,
      latitude2: 38.7205,
      longitude2: 35.4826,
      sid: "#Yok",
      markers: {}             
    }

    
  }

  

  componentDidMount(){
    ilkbaglanti = (data)=>{




/*
      markers_genel.push(
  
      );
      markers_genel.push(
        {
          key:999,
          koor: {
            latitude: 18.9999,
            longitude: 15.9999
          },
          title:'Deneme',
          description: "Açıklama"
        }   
      );
*/

      this.setState({markers: markers_genel});
 
    }
    baglandiginda =()=>{
      this.setState({sid: s.id});
    }
    genelMarkersDegis =()=>{
      this.setState({markers: markers_genel});
     
    }
    yeniKoordinat=(data)=>{

      if (markers_genel){
        if(!markers_genel[data.id]){
          markers_genel[data.id] = {
            key: "",
            koor: {
              latitude: "",
              longitude: ""
            },
            title: "",
            description: ""
          }
        }
        
          markers_genel[data.id].key = data.id;
          markers_genel[data.id].koor.latitude = parseFloat(data.latitude);
          markers_genel[data.id].koor.longitude = parseFloat(data.longitude);
          markers_genel[data.id].title = "Başlık";
          markers_genel[data.id].description = "Açıklama";          
      

         //console.log(markers_genel);
        
         this.setState({markers: markers_genel});

  
      }


    }
    s.on("ilkbaglanti", ilkbaglanti);
    s.on("yeniKoordinat", yeniKoordinat);
    s.on("connect", baglandiginda)
    

    
   
  }

  render() {
    return (
      <View style={{}}>
  
        <MapView style={{height: "105%"}} initialRegion={region} provider={null} 
			      mapType={Platform.OS == "android" ? "none" : "standard"}>

          <UrlTile
            urlTemplate={this.state.UrlTile}
          />
      {
        Object.keys(this.state.markers).map((v,k)=>(

          <Marker
          key={this.state.markers[v].key}
          coordinate={{
            latitude: this.state.markers[v].koor.latitude,
            longitude: this.state.markers[v].koor.longitude
          }}
          title={this.state.markers[v].title}
          description={this.state.markers[v].description}
          image={require("./src/img/car.png")}
          ></Marker>
        ))
      }



	      </MapView>
  


      </View>
    );
  }
}


export default App;