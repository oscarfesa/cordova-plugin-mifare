"use strict";

var mifare ={
    readUID: function(onSuccess, onError, args){
        cordova.exec(
            onSuccess,
            onError,
            "MifarePlugin", 
            "readUID", 
            [args]
        );
    }
}

window.mifare=mifare;