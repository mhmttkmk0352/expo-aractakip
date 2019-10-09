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
        this.client = mqtt.connect("mqtt://185.122.200.117", {});
    }

    islemler(){
        this.client.on("connect", ()=>{
            console.log("Bağlantı başarılı.");

            this.client.subscribe("456456", err=>{
                if (err){
                    console.log(err);
                    console.log("Subscriibe işlemi gerçekleştirilemiyor");
                    return false
                }else{
                    this.client.publish("456456", "nbr");
                }
            });

            this.client.on("message", function(topic, message){
                console.log("#############################");
                console.log("Topic: "+topic);
                console.log("Message: "+message.toString());
                console.log("#############################");
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




var mq = new mqtt_sinifi;
mq.islemler();