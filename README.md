# Cordova Plugin for Paradigma Cards (Mifare TypeA)

Cordova Plugin that integrates NFC reader from Paradigma Tablets.
This Plugin lets you read an UID from a Mifare Card (TypeA). 
In case the plugin cannot read a card for a number of seconds a timeout error is returned.
You can configure this timeout. 

## Installation

To add this plugin to your Cordova Project just type on the root path of your project.

```bash
$ cordova plugin add https://github.com/oscarfesa/cordova-plugin-mifare.git
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

### Error results
In case an error ocurred a JSON result will be returned with an error code and an error message.

```javascript
{'code': int, 'message': string}
```

Example:
```javascript
{'code': 1, 'message': 'Mifare timeout (20 seconds)'}
```

Error list:
- 1: timeout error. 
- 2: generic error.
