package com.srnjak.testing.json;

import jakarta.json.JsonObject;
import jakarta.json.JsonPatch;

public class DiffParser {

    public static final String OPERATION = "op";

    public static final String PATH = "path";

    public static final String VALUE = "value";

    public static JsonPatch.Operation getOperation(JsonObject diff) {
        return JsonPatch.Operation.fromOperationName(diff.getString(OPERATION));
    }

    public static String getPath(JsonObject diff) {
        return diff.getString(PATH);
    }

    public static String getValue(JsonObject diff) {
        return diff.getString(VALUE);
    }
}
