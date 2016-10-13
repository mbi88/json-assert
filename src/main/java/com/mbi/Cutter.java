package com.mbi;

import org.json.JSONArray;
import org.json.JSONObject;

class Cutter {

    static JSONObject cutFields(JSONObject json, String... fields) {
        JSONObject newJson = new JSONObject();
        for (String key : json.keySet()) {
            for (String field : fields) {
                if (!key.equalsIgnoreCase(field))
                    newJson.put(key, json.get(key));
            }
        }

        return newJson;
    }

    static JSONArray cutFields(JSONArray json, String[] fields) {
        JSONArray result = new JSONArray();
        for (int i = 0; i < json.length(); i++) {
            JSONObject jsonObject = new JSONObject(json.get(i).toString());
            JSONObject newJson = new JSONObject();
            for (String key : jsonObject.keySet()) {
                for (String field : fields) {
                    if (!key.equalsIgnoreCase(field))
                        newJson.put(key, jsonObject.get(key));
                }
            }
            result.put(newJson);
        }

        return result;
    }
}
