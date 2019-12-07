package com.srnjak.testing.json;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;

/**
 * Generators for mock data.
 */
public class Generators {

    /**
     * Generates a simple {@link JsonObject} instance.
     * @return The generated {@link JsonObject} instance.
     */
    public static JsonObject generateSimpleObject() {
        return Json.createObjectBuilder()
                .add("abc", "xxx")
                .add("def", "yyy")
                .build();
    }

    /**
     * Generates a {@link JsonArray} instance containing objects.
     * @return The generated {@link JsonArray} instance.
     */
    public static JsonArray generateJsonArrayOfObjects() {
        return Json.createArrayBuilder()
                .add(Json.createObjectBuilder().add("x", 1).add("y", 2).build())
                .add(Json.createObjectBuilder().add("x", 3).add("z", 4).build())
                .add(Json.createArrayBuilder().add("ab").add("cd").build())
                .build();
    }

    /**
     * Generates a {@link JsonArray} instance containing scalars.
     * @return The generated {@link JsonArray} instance.
     */
    public static JsonArray generateJsonArrayOfScalars() {
        return Json.createArrayBuilder()
                .add("one")
                .add(2)
                .add(true)
                .build();
    }

    /**
     * Generates a {@link JsonArray} instance containing arrays.
     * @return The generated {@link JsonArray} instance.
     */
    public static JsonArray generateJsonArrayOfArrays() {
        return Json.createArrayBuilder()
                .add(generateJsonArrayOfScalars())
                .add(generateJsonArrayOfScalars())
                .add(generateJsonArrayOfScalars())
                .build();
    }


    /**
     * Generates a complex {@link JsonObject} instance.
     * @return The generated {@link JsonObject} instance
     */
    public static JsonObject generateComplexObject() {
        return Json.createObjectBuilder()
                .add("first", "first")
                .add("second", 2)
                .add("third", false)
                .add("four", generateSimpleObject())
                .add("five", generateJsonArrayOfObjects())
                .build();
    }

    /**
     * Generates a complex {@link JsonObject} instance which is same
     * as {@link Generators#generateComplexObject()}, but shuffled.
     *
     * @return The generated {@link JsonObject} instance
     */
    public static JsonObject generateComplexObjectShuffled() {
        return Json.createObjectBuilder()
                .add("third", false)
                .add("second", 2)
                .add("five", generateJsonArrayOfObjects())
                .add("four", generateSimpleObject())
                .add("first", "first")
                .build();
    }
}
