"use strict";

var mifare ={
    readUID: function(onSuccess){
        cordova.exec(
            onSuccess,
            function (reason) {
                console.log( "onFailure Plugin " + reason);
            },
            "MifarePlugin", "readUID"
        );
    }
}

window.mifare=mifare;