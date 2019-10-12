
import React, { Component } from 'react';
import { Text, View, Button, TextInput, StyleSheet, Platform, AsyncStorage, ScrollView } from 'react-native';
import MapView, {Marker, UrlTile } from 'react-native-maps';
import {createAppContainer} from 'react-navigation';
import {createStackNavigator} from 'react-navigation-stack';
import init from 'react_native_mqtt';
import { TouchableOpacity } from 'react-native-gesture-handler';


const fetch_url = "http://192.168.1.118/dashboard/expo/arac-takipX/php/aractakipkontrol.php?";
var f_url = "";
var g_subscribe = "";

init({
  size: 10000,
  storageBackend: AsyncStorage,
  defaultExpires: 1000 * 3600 * 24,
  enableCache: true,
  reconnect: true,
  sync : {
  }
});

const io = require("socket.io-client")
var s = io.connect("http://192.168.1.118:3000");







const styles = StyleSheet.create({
  loginTextInput: {
    borderColor: "#ccc", 
    borderWidth: 1, 
    height:40, 
    borderRadius: 10, 
    textAlign: 'center', 
    marginBottom: 5
  },
  girisBtn:{
    borderRadius: 20,
    backgroundColor: 'orange',
    color: 'red'
  },
  HomeScreenMainView: {
    flex: 1,  justifyContent: "center", padding:50
  },
  loginContainer: {
    flex: 1,
    backgroundColor: '#F5FCFF',
    paddingVertical: 40
  },
  loginHeadBackground: {
    backgroundColor: '#4894ec',
    width: '100%',
    height: 800,
    paddingVertical: 100
  
  },
  loginLogoTitle: {
    fontSize:40,
    color: '#f2f2f2',
    textAlign: 'center',
    fontWeight: 'bold',
    paddingTop: 10
  },
  loginLogoDescription: {
    textAlign: 'center',
    color: '#f2f2f2'
  },
  loginArea: {
    marginHorizontal: 40,
    marginVertical: 40,
    backgroundColor: '#fff',
    padding: 20,
    borderRadius:10,
  },
  loginError: {
    fontSize:10,
    color: 'red',
    textAlign: 'center',
    paddingVertical: 3
  }
});
  
/*
###############################
#           HOMESCREEN        #
###############################
*/



class LoginScreen extends Component {
  constructor(props){
    super(props)
    this.state = {
      kullanici: '',
      parola: '',
      f_url: '',
      girisBasarisiz: ''
    }
  }

    

    girisBtn=()=>{/*
      s.emit("giris", {
        kullanici: this.state.kullanici,
        parola: this.state.parola
      }); 
      */
  
     
      f_url = fetch_url+"kullanici="+this.state.kullanici.toLowerCase()+"&parola="+this.state.parola.toLowerCase();
      
      fetch(f_url).
      then((res)=>res.json()).
      then(res=>{
        console.log(res);
        if (res && res.durum == 1){
          console.log("Giriş başarılı");
          console.log(res);
          if (res.subscribe){
            g_subscribe = res.subscribe;
            this.setState({girisBasarisiz: ''});
            this.props.navigation.navigate("Map");
          }
          else{
            this.setState({girisBasarisiz:" Başarısız giriş denemesi"})
            setTimeout(()=>{
              this.setState({girisBasarisiz: ''});
            },2000);
          }
  
        }
        else{
          this.setState({girisBasarisiz:" Başarısız giriş denemesi"})
          setTimeout(()=>{
            this.setState({girisBasarisiz: ''});
          },2000);          
        }
      });
      
    }    
  

  render(){
    return (
      <View styles={styles.loginContainer}>
        <View style={styles.loginHeadBackground}>
          <View>
            <Text style={styles.loginLogoTitle}>ArgeRF</Text>
            <Text style={styles.loginLogoDescription}>Araç Takip Sistemi</Text>
          </View>
          <ScrollView>
            <View style={styles.loginArea}>

            <TextInput
                    style={styles.loginTextInput}
                    placeholder={"Kullanıcı adı"}
                    onChangeText={kullanici=>
                      {
                      this.setState({kullanici}); 
                      /*
                      s.emit("giris", {
                        kullanici: kullanici,
                        parola: this.state.parola
                      });
                      */
                      }
                    }
                  />           
                  <TextInput
                    style={styles.loginTextInput}
                    placeholder={'Parola'}
                    secureTextEntry={true}
                    onChangeText={parola=>
                      {
                        this.setState({parola});
                      /*
                        s.emit("giris", {
                          kullanici: this.state.kullanici,
                          parola: parola
                        });     
                      */
                      }
                    }
                    
                  />

    
                  <Text style={styles.loginError}>{this.state.girisBasarisiz}</Text>           
                  <Button title={"GİRİŞ YAP"} 
                    color={"tomato"} 
                    onPress={this.girisBtn}
                    />

            </View>
          </ScrollView>

        </View>
      </View>
    );
  }

  


}



class HomeScreen extends Component {
  constructor(props){
    super(props)
    this.state = {
      kullanici: '',
      parola: '',
      f_url: ''
    }
  }


  girisBtn=()=>{/*
    s.emit("giris", {
      kullanici: this.state.kullanici,
      parola: this.state.parola
    }); 
    */

   
    f_url = fetch_url+"kullanici="+this.state.kullanici.toLowerCase()+"&parola="+this.state.parola.toLowerCase();
    
    fetch(f_url).
    then((res)=>res.json()).
    then(res=>{
      console.log(res);
      if (res && res.durum == 1){
        console.log("Giriş başarılı");
        console.log(res);
        if (res.subscribe){
          g_subscribe = res.subscribe;
          this.props.navigation.navigate("Map");
        }
        else{
          alert("Giriş başarısız");
        }

      }
      else{
        alert("Giriş başarısız");
      }
    });
    
  }

  componentDidMount(){
    s.on("giris", data=>{
      console.log("##");

      if (data.durum == 1){
        console.log(data);
        g_subscribe = data.subscribe;
        console.log("g_subscribe: "+ g_subscribe); 
        
        
        this.props.navigation.navigate("Map");
      }

    });



  }
 

  render(){
    
    const {navigate} = this.props.navigation;
    return(
      <View style={styles.HomeScreenMainView}>
 
          <Text style={{textAlign: 'center', fontSize:20}}>ArgeRF</Text>
  
          <Text style={{textAlign: 'center', fontSize:20}}>Araç Takip Sistemi</Text>
          

                  <TextInput
                    style={styles.loginTextInput}
                    placeholder={"Kullanıcı adı"}
                    onChangeText={kullanici=>
                      {
                      this.setState({kullanici}); 
                      /*
                      s.emit("giris", {
                        kullanici: kullanici,
                        parola: this.state.parola
                      });
                      */
                      }
                    }
                  />           
                  <TextInput
                    style={styles.loginTextInput}
                    placeholder={'Parola'}
                    secureTextEntry={true}
                    onChangeText={parola=>
                      {
                        this.setState({parola});
                      /*
                        s.emit("giris", {
                          kullanici: this.state.kullanici,
                          parola: parola
                        });     
                      */
                      }
                    }
                    
                  />           
                  <Button title={"GİRİŞ yap"}
                    onPress={this.girisBtn}
                    
                  />
            
      
      </View>
    );
  }
}

/*
###############################
#           MAPSCREEN         #
###############################
*/

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
        subscribe: "LattLang",
        UrlTile: 'http://c.tile.openstreetmap.org/{z}/{x}/{y}.png',
        sid: "#Yok",
        markers: {}             
      }
  
      
    }
  
    
  
    componentDidMount(){

      onMessageArrived=(message)=>{

        var id = message._getPayloadString().split(",")[2];
        var latitude = parseFloat( message._getPayloadString().split(",")[0] );
        var longitude = parseFloat( message._getPayloadString().split(",")[1] );
        

        if (id && latitude && longitude){
          if (markers_genel){
            if(!markers_genel[id]){
              markers_genel[id] = {
                key: "",
                koor: {
                  latitude: "",
                  longitude: ""
                },
                title: "",
                description: ""
              }
            }
              markers_genel[id].key = id;
              markers_genel[id].koor.latitude = parseFloat(latitude);
              markers_genel[id].koor.longitude = parseFloat(longitude);
              markers_genel[id].title = "Başlık";
              markers_genel[id].description = "Açıklama";          
              this.setState({markers: markers_genel});
          }
        }
      }

      mqttBalandiginda=()=>{
        mqttcli.onMessageArrived = onMessageArrived;
        mqttcli.subscribe(g_subscribe);
        mqttcli.publish(g_subscribe, "Hello");
        console.log(mqttcli._getClientId());
      }



      const mqttcli  = new Paho.MQTT.Client("broker.mqttdashboard.com", 8000, "eeee");
      mqttcli.connect({onSuccess: mqttBalandiginda});


      ilkbaglanti = (data)=>{
        this.setState({markers: markers_genel});
   
      }
      baglandiginda =()=>{
        this.setState({sid: s.id});
      }
      genelMarkersDegis =()=>{
        this.setState({markers: markers_genel});
       
      }


      s.on("ilkbaglanti", ilkbaglanti);

      s.on("connect", baglandiginda);
      
  
      
     
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
















const MainNavigator = createStackNavigator({

  Home: {screen: HomeScreen},
  Map: {screen: MapScreen},
  Login: {screen: LoginScreen}
}
,{
    initialRouteName: "Login", 
    headerMode: 'none'
});

const App = createAppContainer(MainNavigator);



export default App;