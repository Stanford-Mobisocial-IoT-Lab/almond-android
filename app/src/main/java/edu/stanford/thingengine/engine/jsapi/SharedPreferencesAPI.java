// This file is part of Almond
//
// Copyright 2016-2017 The Board of Trustees of the Leland Stanford Junior University
//
// See COPYING for details
//
package edu.stanford.thingengine.engine.jsapi;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

/**
 * Created by gcampagn on 10/26/15.
 */
public class SharedPreferencesAPI extends JavascriptAPI {
    private final Context context;
    public SharedPreferencesAPI(Context context) {
        super("SharedPreferences");

        this.context = context;

        this.registerSync("readSharedPref", new GenericCall() {
            @Override
            public Object run(Object... args) throws Exception {
                return readSharedPref((String)args[0]);
            }
        });
        this.registerSync("writeSharedPref", new GenericCall() {
            @Override
            public Object run(Object... args) throws Exception {
                writeSharedPref((String)args[0]);
                return null;
            }
        });
    }

    private String readSharedPref(String name) {
        return context.getSharedPreferences("thingengine", Context.MODE_PRIVATE).getString(name, null);
    }

    private void writeSharedPref(String writes) throws JSONException {
        SharedPreferences.Editor editor = context.getSharedPreferences("thingengine", Context.MODE_PRIVATE).edit();
        JSONArray parsedWrites = (JSONArray) new JSONTokener(writes).nextValue();
        for (int i = 0; i < parsedWrites.length(); i++) {
            JSONArray write = parsedWrites.getJSONArray(i);
            String name = write.getString(0);
            String value = write.getString(1);
            editor.putString(name, value);
        }
        editor.apply();
    }
}
