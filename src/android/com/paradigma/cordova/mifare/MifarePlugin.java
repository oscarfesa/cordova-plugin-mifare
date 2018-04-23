package com.paradigma.cordova.mifare;

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;

import com.rfid.reader.Reader;
import java.io.IOException;
import java.security.InvalidParameterException;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class MifarePlugin extends CordovaPlugin {

    public Reader reader = null;
    private static final String TAG = "MifarePlugin";
    private static final String DEVICE = "/dev/ttyS3"; // UART serial port

    
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

        if (action.equals("readUID")) {

            cordova.getThreadPool().execute(new Runnable() {
                public void run() {
                    String uid = getUID();
                    PluginResult result = new PluginResult(PluginResult.Status.OK, uid);
                    result.setKeepCallback(true);
                    callbackContext.sendPluginResult(result);       
                }
            });
            return true;
        } else {
            
            return false;

        }
    }

    private Reader getReader() throws SecurityException, IOException, InvalidParameterException {
        if (reader == null) {
			// Get reader from serial port
            reader = Reader.getInstance(DEVICE, 9600);
        }
        Log.d(TAG, "getReader() before return() ");

        return reader;
    }

    private String getUID(){
        byte[] uid = new byte[32];
        byte[] uidLen = new byte[1];
        byte[] errCode = new byte[1];
        try {
            // Read until get response.
            int result = getReader().Iso14443a_GetUid(uid, uidLen, errCode);
            while (result != 0){
                result = getReader().Iso14443a_GetUid(uid, uidLen, errCode);
            }

            if (result != 0) {
                return "GetUid Error, errCode=" + String.format("%02X", (byte) errCode[0]);
            } else {
                String strUid = "";
                for (int i = 0; i < uidLen[0]; i++) {
                    strUid += String.format("%02X ", uid[i]);
                }
                return strUid;
            }
        } catch (Exception e){
            return ("Cannot get reader");
        }
    }
}