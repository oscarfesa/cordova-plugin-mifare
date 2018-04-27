package com.paradigma.cordova.mifare;

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;

import com.rfid.reader.Reader;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.concurrent.*;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class MifarePlugin extends CordovaPlugin {

    public Reader reader = null;
    private final ExecutorService readUIDExecutor;
    private static final String TAG = "MifarePlugin";
    private static final Integer TIMEOUT_SECONDS_DEFAULT = 10;
    private static final String DEVICE = "/dev/ttyS3"; // UART serial port

    public MifarePlugin(){
        super();
        readUIDExecutor= Executors.newSingleThreadExecutor();
    }
    
    @Override
    public boolean execute(String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
        // Configure timeout
        final Integer timeout = args.length() != 0 ? args.getJSONObject(0).getInt("timeout") : TIMEOUT_SECONDS_DEFAULT;
        
        if (action.equals("readUID")) {
            cordova.getThreadPool().execute(new Runnable() {
                public void run() {
                    
                    Future<String> future = readUIDExecutor.submit(new Callable() {
                        
                        public String call() throws Exception {
                            String uid = getUID();
                            PluginResult result = new PluginResult(PluginResult.Status.OK, uid);
                            result.setKeepCallback(true);
                            callbackContext.sendPluginResult(result);      
                            return uid ;
                        }
                    });
                    try {
                        Log.d(TAG, "Start looking for Mifare Card...");
                        future.get(timeout, TimeUnit.SECONDS); 
                    } catch (TimeoutException e) {
                        Log.d(TAG, "Mifare timeout");
                        PluginResult result = new PluginResult(PluginResult.Status.ERROR, "Mifare timeout");
                        callbackContext.sendPluginResult(result);
                    } catch (Exception e) {
                        Log.d(TAG, "Mifare error: " + e.getMessage());
                        PluginResult result = new PluginResult(PluginResult.Status.ERROR, "Mifare error");
                        callbackContext.sendPluginResult(result);
                    } finally {
                        readUIDExecutor.shutdownNow();
                    }
                }
            });
            return true;
        } else {
            
            return false;

        }
    }

    private Reader getReader() throws SecurityException, IOException, InvalidParameterException {
        if (reader == null) {
            reader = Reader.getInstance(DEVICE, 9600);
        }

        return reader;
    }

    private String getUID() throws IOException,SecurityException,InvalidParameterException{
        byte[] uid = new byte[32];
        byte[] uidLen = new byte[1];
        byte[] errCode = new byte[1];

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
    }
}