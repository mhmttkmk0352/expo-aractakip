import React, { Component } from 'react';
import { Text, View, Platform } from 'react-native';
import MapView, {Marker, PROVIDER_DEFAULT} from 'react-native-maps';

const io = require("socket.io-client");
var s = io.connect("http://192.168.1.118:3000");

var markers_genel = [];




export default class App extends Component {
  constructor(props){
    super(props)
    this.state = {
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
      markers_genel[990] = {
        key:990,
        koor: {
          latitude: 28.9999,
          longitude: 25.9999
        },
        title:'Deneme',
        description: "Açıklama",
        adsoyad:'Mehmet TOKMAK',
        plaka: '38 AB 993'
      } 
      markers_genel[991] = {
        key:991,
        koor: {
          latitude: 27.9989,
          longitude: 20.9989
        },
        title:'Deneme2',
        description: "Açıklama2",
        adsoyad:'Mehmet TOKMAK2',
        plaka: '38 AB 9932'
      } 

      markers_genel[992] = {
        key:992,
        koor: {
          latitude: 27.9989,
          longitude: 30.9989
        },
        title:'Deneme2',
        description: "Açıklama2",
        adsoyad:'Mehmet TOKMAK2',
        plaka: '38 AB 9932'
      } 

      markers_genel[993] = {
        key:993,
        koor: {
          latitude: 20.9989,
          longitude: 20.9989
        },
        title:'Deneme2',
        description: "Açıklama2",
        adsoyad:'Mehmet TOKMAK2',
        plaka: '38 AB 9932'
      } 




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
    s.on("ilkbaglanti", ilkbaglanti);
    s.on("connect", baglandiginda)
    

    
    console.log(Platform.OS);
  }

  render() {
    return (
      <View style={{flex:1, justifyContent:'center'}}>
        <View style={{flex:1, justifyContent: 'center'}}>
          <Text style={{textAlign: 'center', color: "red"}}>
            {this.state.sid}
          </Text>
        </View>


        <View style={{flex:9, justifyContent: "center"}}>
        <MapView
          provider={PROVIDER_DEFAULT}
          style={{flex:98}}
          initialRegion={{
            latitude: this.state.sabit_latitude,
            longitude: this.state.longitude,
            latitudeDelta: 50,
            longitudeDelta: 50

          }}>

          {
           
              Object.keys(this.state.markers).map((v,k)=>
                (
                
                <Marker key={this.state.markers[v].key}
                  coordinate={{
                    latitude: this.state.markers[v].koor.latitude,
                    longitude: this.state.markers[v].koor.longitude
                  }}
                  title={this.state.markers[v].adsoyad}
                  description={this.state.markers[v].plaka}
                  image={require("./src/img/car.png")}
                  >
                </Marker>
                  
                )
              )        

          }
          </MapView>
        </View>


      </View>
    );
  }
}