c=(d)=>{
    console.log(d);
}
seeonline=()=>{
    console.log(io.engine.clientsCount);
}

const {MongoClient} = require("mongodb");
const io = require("socket.io").listen(3000);
const mqtt = require("mqtt");





var mqttBaglan=(sunucu,s)=>{
    var client = mqtt.connect(sunucu, {clientId: 23423});

        client.on("connect", ()=>{
            console.log(sunucu+ " MQTT sunucusuna yapılan bağlantı başarılı.");

            client.subscribe("LattLang", err=>{
                if (err){
                    console.log(err);
                    console.log("Subscribe işlemi gerçekleştirilemiyor");
                    return false
                }else{
                    client.publish("LattLang", "Hello");
                }
            });

            client.on("message", function(topic, message){

                /*
                console.log("#############################");
                console.log("Topic: "+topic);
                console.log("Message: "+message.toString());
                console.log("#############################");
                */


                if(message.toString().split(",")[2] && message.toString().split(",")[2].length>0){
                    s.emit("yeniKoordinat", {
                        id: message.toString().split(",")[2],
                        latitude: message.toString().split(",")[0],
                        longitude: message.toString().split(",")[1],
                        title: "Başlık",
                        description: "Açıklama"
                    });
                }

            });
        })


    


}


/*

class mqtt_sinifi {
    constructor(sunucu){
        this.client = mqtt.connect(sunucu, {clientId: 23423});
    }

    islemler(s){
        this.client.on("connect", ()=>{
            console.log("MQTT Bağlantı başarılı.");

            this.client.subscribe("LattLang", err=>{
                if (err){
                    console.log(err);
                    console.log("Subscribe işlemi gerçekleştirilemiyor");
                    return false
                }else{
                    this.client.publish("LattLang", "Hello");
                }
            });

            this.client.on("message", function(topic, message){

                
                console.log("#############################");
                console.log("Topic: "+topic);
                console.log("Message: "+message.toString());
                console.log("#############################");
                


                if(message.toString().split(",")[2] && message.toString().split(",")[2].length>0){
                    s.emit("yeniKoordinat", {
                        id: message.toString().split(",")[2],
                        latitude: message.toString().split(",")[0],
                        longitude: message.toString().split(",")[1],
                        title: "Başlık",
                        description: "Açıklama"
                    });
                }

            });
        })


    }


}

*/


var region = {

    latitude: 38,
    longitude: 35.4826,
    latitudeDelta: 0.0922,
    longitudeDelta: 0.0421  
}





MongoClient.connect("mongodb://localhost", {useNewUrlParser: true, useUnifiedTopology:true}, (err, mongoclient)=>{
    if (err) throw err

    var vt = mongoclient.db("aractakip");

    


    io.on("connection", (s)=>{



        console.log(s.id+" bağlandı");

        s.on("disconnect", ()=>{
            c(s.id+" uygulamadan ayrıldı");
        });

        s.on("giris", (data)=>{
            console.log(data);
            if (data.kullanici && data.parola){
                
                vt.collection("giris").findOne({kullanici: data.kullanici.toLowerCase(), parola: data.parola.toLowerCase()}, (err, result)=>{
                    if(result){
                        console.log("Giriş başarılı");
                        console.log(result);
                        mqttBaglan(result.sunucu, s);
                        
                        s.emit("giris", {
                            durum: 1,
                            mesaj: "Giriş Başarılı",
                            sunucu: result.sunucu
                        })
                    }
                    else{
                        
                        s.emit("giris", {
                            durum: 0,
                            mesaj: "Giriş Başarısız"
                        })
                    }
                });
            }           
        });


        seeonline();
    
    });




});