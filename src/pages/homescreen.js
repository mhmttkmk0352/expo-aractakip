import React, { Component } from 'react';
import { View, Button, TextInput } from 'react-native';




class HomeScreen extends Component {
    render(){
      
      const {navigate, push} = this.props.navigation;
      return(
        <View style={{flex: 1, flexDirection: 'column',  justifyContent: "center", position: 'relative', padding:30}}>
            
        
            <TextInput
              style={{borderColor: "#ccc", borderWidth: 1, height:40, borderRadius: 5, textAlign: 'center', marginBottom: 5}}
              placeholder={"Kullanıcı adı"}
            />           
            <TextInput
              style={{borderColor: "#ccc", borderWidth: 1, height:40, borderRadius: 5, textAlign: 'center', marginBottom: 10}}
              placeholder={'Parola'}
            />           
            <Button title={"GİRİŞ yap"}
              style={{marginTop: 10}}
              onPress={()=>navigate("Map")}
            />
       
        </View>
      );
    }
}

export default HomeScreen;