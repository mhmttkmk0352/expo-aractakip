import React, { Component } from 'react';
import { View, Platform } from 'react-native';
import MapView, {Marker, UrlTile } from 'react-native-maps';

const io = require("socket.io-client");
var s = io.connect("http://192.168.1.118:3000");

var markers_genel = {};

var region = {
  latitude: 38.7204,
  longitude: 35.4825,
  latitudeDelta: 0.05,
  longitudeDelta: 0.05
};



class MapScreen extends Component {
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
            image={require("./../img/car.png")}
            ></Marker>
          ))
        }
  
  
  
            </MapView>
    
  
  
        </View>
      );
    }
  }



  export default MapScreen;