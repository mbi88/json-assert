package com.mbi;

import org.json.JSONArray;
import org.json.JSONObject;

class Cutter {

    /**
     * Method to return json object without passed fields
     *
     * @param json   json with redundant fields
     * @param fields redundant fields to be removed
     * @return result json without redundant fields
     */
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

    /**
     * Method to return json array without passed fields
     *
     * @param json   json with redundant fields
     * @param fields redundant fields to be removed
     * @return result json without redundant fields
     */
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
