c=(d)=>{
    console.log(d);
}
seeonline=()=>{
    console.log(io.engine.clientsCount);
}

const io = require("socket.io").listen(3000);
const mqtt = require("mqtt");



class mqtt_sinifi {
    constructor(){
        this.client = mqtt.connect("mqtt://broker.mqttdashboard.com", {clientId: 23423});
    }

    islemler(s){
        this.client.on("connect", ()=>{
            console.log("Bağlantı başarılı.");

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

class socket_sinifi {
    
}



var region = {

    latitude: 38,
    longitude: 35.4826,
    latitudeDelta: 0.0922,
    longitudeDelta: 0.0421  
}

io.on("connection", (s)=>{

    var mq = new mqtt_sinifi;
    mq.islemler(s);

    console.log(s.id+" bağlandı");

    s.on("disconnect", ()=>{
        c(s.id+" uygulamadan ayrıldı");
    });
    seeonline();
 

    setInterval(function(){
        region.latitude = (Math.random()*55).toFixed(4);
        s.emit("ilkbaglanti",  region);
    }, 500);
});




