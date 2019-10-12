<?php 
define("DB_HOST","localhost");
define("DB_USER", "root");
define("DB_PWD", "");
define("DB_NAME","aractakip");
define("charset", "utf8");




$kullanici = $_GET["kullanici"];
$parola = $_GET["parola"];


if ($kullanici && $parola){
    $m = mysqli_connect(DB_HOST, DB_USER, DB_PWD, DB_NAME);
    $q = mysqli_query($m, "select * from giris where kullanici='".$kullanici."' AND parola='".$parola."'");
    
    
    
    
    if ($q && $q->num_rows && $q->num_rows == 1){
        
        while($ac = mysqli_fetch_assoc($q)){
           echo json_encode(array("durum"=>1,"kullanici"=>$ac["kullanici"], "subscribe"=>$ac["subscribe"]), JSON_UNESCAPED_UNICODE);
       
        }
        

    }else{
        echo json_encode(array("durum"=>0), JSON_UNESCAPED_UNICODE);
   
        
    }
    
    
}



?>