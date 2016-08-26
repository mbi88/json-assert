package com.mbi;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by mbi on 8/18/16.
 */
class Cutter {

    static JSONObject cutFields(JSONObject json, String... fields) {
        for (String field : fields) {
            json.remove(field);
        }

        return json;
    }

    static JSONArray cutFields(JSONArray json, String[] fields) {
        JSONArray result = new JSONArray();
        for (int i = 0; i < json.length(); i++) {
            JSONObject jsonObject = new JSONObject(json.get(i).toString());
            for (String f : fields) {
                jsonObject.remove(f);
            }
            result.put(jsonObject);
        }

        return result;
    }
}
