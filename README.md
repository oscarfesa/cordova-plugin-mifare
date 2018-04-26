# Cordova Plugin for Paradigma Cards (Mifare TypeA)

Cordova Plugin that integrates NFC reader from Paradigma Tablets.
This Pluging tries to read an UID from a Mifare Card. If a card is read the plugin returns the UID. In case the plugin cannot read a card for a number of seconds a timeout error is returned.
You can configure this timeout. 

## Installation

To add this plugin to your Cordova Project remember you need to have a Paradigma Gitlab account. Then just type on the root path of your project.

```bash
$ cordova plugin add git+ssh://git@git.paradigmadigital.com/iot/paradigma-spaces/cordova-plugin-mifare.git
```

## Usage

In your Cordova Project you can use it as follows:
```javascript
    var onSuccess = function(data){
        console.log("UID read from card: " + data );
    };
    var onError = function(data){
        console.log("Cannot read card: " + data);
    };

    var uid = mifare.readUID(onSuccess, onError, {'timeout': 20});
```

You can configure your timeout (in seconds) using arguments as before. By default a 10 seconds timeout is configured. 
