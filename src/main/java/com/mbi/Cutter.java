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
        for (String field : fields) {
            json.remove(field);
        }

        return json;
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
            for (String field : fields) {
                jsonObject.remove(field);
            }

            result.put(jsonObject);
        }

        return result;
    }
}
