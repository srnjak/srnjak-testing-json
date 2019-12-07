package com.srnjak.testing.json;

import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import javax.json.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.srnjak.testing.json.AssertJson.FailureType.*;
import static org.junit.jupiter.api.Assertions.*;

class AssertJsonTest {

    @Test
    public void equality_WhenEqual() {

        // object
        passEquals(
                Json.createObjectBuilder().build(),
                Json.createObjectBuilder().build());
        passEquals(
                Generators.generateComplexObject(),
                Generators.generateComplexObject());
        passEquals(
                Generators.generateComplexObject(),
                Generators.generateComplexObjectShuffled());

        failNotEquals(
                Json.createObjectBuilder().build(),
                Json.createObjectBuilder().build());
        failNotEquals(
                Generators.generateComplexObject(),
                Generators.generateComplexObject());
        failNotEquals(
                Generators.generateComplexObject(),
                Generators.generateComplexObjectShuffled());

        // array
        passEquals(
                Json.createArrayBuilder().build(),
                Json.createArrayBuilder().build());
        passEquals(
                Generators.generateJsonArrayOfObjects(),
                Generators.generateJsonArrayOfObjects());

        failNotEquals(
                Json.createArrayBuilder().build(),
                Json.createArrayBuilder().build());
        failNotEquals(
                Generators.generateJsonArrayOfObjects(),
                Generators.generateJsonArrayOfObjects());
    }

    @Test
    public void equality_WhenNull() {
        passEquals(null, null);
        failEquals(Json.createObjectBuilder().build(), null, null, NULL);
        failEquals(Json.createArrayBuilder().build(), null, null, NULL);
        failEquals(null, Json.createObjectBuilder().build(), null, NOT_NULL);
        failEquals(null, Json.createArrayBuilder().build(), null, NOT_NULL);

        failNotEquals(null, null);
        passNotEquals(Json.createObjectBuilder().build(), null);
        passNotEquals(Json.createArrayBuilder().build(), null);
        passNotEquals(null, Json.createObjectBuilder().build());
        passNotEquals(null, Json.createArrayBuilder().build());
    }

    @Test
    public void equality_WhenPropertyMissing() {

        // a simple object
        JsonObject expected = Json.createObjectBuilder()
                .add("test", "xxx")
                .build();
        JsonObject actual = Json.createObjectBuilder().build();

        // an object with a nested object
        JsonObject expectedObject = Json.createObjectBuilder()
                .add("nested", expected)
                .build();
        JsonObject actualObject = Json.createObjectBuilder()
                .add("nested", actual)
                .build();

        // an array with an entry
        JsonArray expectedArray = Json.createArrayBuilder()
                .add(expected)
                .build();
        JsonArray actualArray = Json.createArrayBuilder()
                .add(actual)
                .build();

        // an object with a nested array
        JsonObject expectedObjectA = Json.createObjectBuilder()
                .add("nestedArray", expectedArray)
                .build();
        JsonObject actualObjectA = Json.createObjectBuilder()
                .add("nestedArray", actualArray)
                .build();

        // an array of arrays
        JsonArray expectedArrayA = Json.createArrayBuilder()
                .add(expectedArray)
                .build();
        JsonArray actualArrayA = Json.createArrayBuilder()
                .add(actualArray)
                .build();

        failEquals(expected, actual, "/test", MISSING);
        failEquals(expectedObject, actualObject, "/nested/test", MISSING);
        failEquals(expectedArray, actualArray, "/0/test", MISSING);
        failEquals(
                expectedObjectA, actualObjectA, "/nestedArray/0/test", MISSING);
        failEquals(expectedArrayA, actualArrayA, "/0/0/test", MISSING);

        passNotEquals(expected, actual);
        passNotEquals(expectedObject, actualObject);
        passNotEquals(expectedArray, actualArray);
        passNotEquals(expectedObjectA, actualObjectA);
        passNotEquals(expectedArrayA, actualArrayA);
    }

    @Test
    public void equality_WhenPropertyUnexpected() {

        // a simple object
        JsonObject expected = Json.createObjectBuilder().build();
        JsonObject actual = Json.createObjectBuilder()
                .add("test", "xxx")
                .build();

        // an object with a nested object
        JsonObject expectedObject = Json.createObjectBuilder()
                .add("nested", expected)
                .build();
        JsonObject actualObject = Json.createObjectBuilder()
                .add("nested", actual)
                .build();

        // an array with an entry
        JsonArray expectedArray = Json.createArrayBuilder()
                .add(expected)
                .build();
        JsonArray actualArray = Json.createArrayBuilder()
                .add(actual)
                .build();

        // an object with a nested array
        JsonObject expectedObjectA = Json.createObjectBuilder()
                .add("nestedArray", expectedArray)
                .build();
        JsonObject actualObjectA = Json.createObjectBuilder()
                .add("nestedArray", actualArray)
                .build();

        // an array of arrays
        JsonArray expectedArrayA = Json.createArrayBuilder()
                .add(expectedArray)
                .build();
        JsonArray actualArrayA = Json.createArrayBuilder()
                .add(actualArray)
                .build();

        failEquals(expected, actual, "/test", UNEXPECTED);
        failEquals(expectedObject, actualObject, "/nested/test", UNEXPECTED);
        failEquals(expectedArray, actualArray, "/0/test", UNEXPECTED);
        failEquals(expectedObjectA, actualObjectA,
                "/nestedArray/0/test", UNEXPECTED);
        failEquals(expectedArrayA, actualArrayA, "/0/0/test", UNEXPECTED);

        passNotEquals(expected, actual);
        passNotEquals(expectedObject, actualObject);
        passNotEquals(expectedArray, actualArray);
        passNotEquals(expectedObjectA, actualObjectA);
        passNotEquals(expectedArrayA, actualArrayA);
    }

    @Test
    public void equality_WhenPropertyInvalid() {

        // expected and actual values
        String expectedValue = "xxx";
        String actualValue = "yyy";

        // a simple object
        JsonObject expected = Json.createObjectBuilder()
                .add("test", expectedValue)
                .build();
        JsonObject actual = Json.createObjectBuilder()
                .add("test", actualValue)
                .build();

        // an object with a nested object
        JsonObject expectedObject = Json.createObjectBuilder()
                .add("nested", expected)
                .build();
        JsonObject actualObject = Json.createObjectBuilder()
                .add("nested", actual)
                .build();

        // an array with an entry
        JsonArray expectedArray = Json.createArrayBuilder()
                .add(expected)
                .build();
        JsonArray actualArray = Json.createArrayBuilder()
                .add(actual)
                .build();

        // an object with a nested array
        JsonObject expectedObjectA = Json.createObjectBuilder()
                .add("nestedArray", expectedArray)
                .build();
        JsonObject actualObjectA = Json.createObjectBuilder()
                .add("nestedArray", actualArray)
                .build();

        // an array of arrays
        JsonArray expectedArrayA = Json.createArrayBuilder()
                .add(expectedArray)
                .build();
        JsonArray actualArrayA = Json.createArrayBuilder()
                .add(actualArray)
                .build();

        failEquals(
                expected, actual,
                expectedValue, actualValue,
                "/test",
                INVALID);
        failEquals(
                expectedObject, actualObject,
                expectedValue, actualValue,
                "/nested/test",
                INVALID);
        failEquals(
                expectedArray, actualArray,
                expectedValue, actualValue,
                "/0/test",
                INVALID);
        failEquals(
                expectedObjectA, actualObjectA,
                expectedValue, actualValue,
                "/nestedArray/0/test",
                INVALID);
        failEquals(
                expectedArrayA, actualArrayA,
                expectedValue, actualValue,
                "/0/0/test",
                INVALID);

        passNotEquals(expected, actual);
        passNotEquals(expectedObject, actualObject);
        passNotEquals(expectedArray, actualArray);
        passNotEquals(expectedObjectA, actualObjectA);
        passNotEquals(expectedArrayA, actualArrayA);
    }

    @Test
    public void equality_WhenArrayEntryMissing() {

        // an array entry
        JsonObject expectedEntry =
                Json.createObjectBuilder(Generators.generateSimpleObject())
                        .add("test", "xxx")
                        .build();

        // a simple array
        JsonArray expected = Json.createArrayBuilder()
                .add(expectedEntry)
                .build();
        JsonArray actual = Json.createArrayBuilder().build();

        // an object with a nested array
        JsonObject expectedObject = Json.createObjectBuilder()
                .add("nestedArray", expected)
                .build();
        JsonObject actualObject = Json.createObjectBuilder()
                .add("nestedArray", actual)
                .build();

        // an array of arrays
        JsonArray expectedArray = Json.createArrayBuilder()
                .add(expected)
                .build();
        JsonArray actualArray = Json.createArrayBuilder()
                .add(actual)
                .build();

        failEquals(expected, actual, "/0", MISSING);
        failEquals(expectedObject, actualObject, "/nestedArray/0", MISSING);
        failEquals(expectedArray, actualArray, "/0/0", MISSING);

        passNotEquals(expected, actual);
        passNotEquals(expectedObject, actualObject);
        passNotEquals(expectedArray, actualArray);
    }

    @Test
    public void equality_WhenArrayEntryUnexpected() {

        // an array entry
        JsonObject expectedEntry =
                Json.createObjectBuilder(Generators.generateSimpleObject())
                        .add("test", "xxx")
                        .build();
        JsonObject unexpectedEntry =
                Json.createObjectBuilder(Generators.generateSimpleObject())
                        .add("unexp", "yyy")
                        .build();

        // a simple array
        JsonArray expected = Json.createArrayBuilder()
                .add(expectedEntry)
                .build();
        JsonArray actual = Json.createArrayBuilder()
                .add(unexpectedEntry)
                .add(expectedEntry)
                .build();

        // an object with a nested array
        JsonObject expectedObject = Json.createObjectBuilder()
                .add("nestedArray", expected)
                .build();
        JsonObject actualObject = Json.createObjectBuilder()
                .add("nestedArray", actual)
                .build();

        // an array of arrays
        JsonArray expectedArray = Json.createArrayBuilder()
                .add(expected)
                .build();
        JsonArray actualArray = Json.createArrayBuilder()
                .add(actual)
                .build();

        failEquals(expected, actual, "/0", UNEXPECTED);
        failEquals(expectedObject, actualObject, "/nestedArray/0", UNEXPECTED);
        failEquals(expectedArray, actualArray, "/0/0", UNEXPECTED);

        passNotEquals(expected, actual);
        passNotEquals(expectedObject, actualObject);
        passNotEquals(expectedArray, actualArray);
    }

    @Test
    public void containing_WithinArray_Null() {

        JsonArray arrayEmpty = Json.createArrayBuilder().build();
        JsonArray arrayNull =
                Json.createArrayBuilder().add(JsonValue.NULL).build();
        JsonArray arrayScalars = Generators.generateJsonArrayOfScalars();
        JsonArray arrayObjects = Generators.generateJsonArrayOfObjects();
        JsonArray arrayArrays = Generators.generateJsonArrayOfArrays();

        failContains(JsonValue.NULL, null);
        failContains(null, null);

        failContains(JsonValue.NULL, arrayEmpty);
        failContains(null, arrayEmpty);

        passContains(JsonValue.NULL, arrayNull);
        passContains(null, arrayNull);

        failContains(JsonValue.NULL, arrayScalars);
        failContains(null, arrayScalars);

        failContains(JsonValue.NULL, arrayObjects);
        failContains(null, arrayObjects);

        failContains(JsonValue.NULL, arrayArrays);
        failContains(null, arrayArrays);
    }

    @Test
    public void containing_WithinArray_Scalar() {
        JsonValue elementStr = Json.createValue("one");
        JsonValue elementInt = Json.createValue(2);
        JsonValue elementBoolean = JsonValue.TRUE;

        JsonValue notElementStr = Json.createValue("zero");
        JsonValue notElementInt = Json.createValue(-1);
        JsonValue notElementBoolean = JsonValue.FALSE;

        JsonArray arrayEmpty = Json.createArrayBuilder().build();

        JsonArray arrayScalars = Json.createArrayBuilder()
                .add(elementStr)
                .add(elementInt)
                .add(true)
                .build();

        failContains(elementStr, null);
        failContains(elementInt, null);
        failContains(elementBoolean, null);

        failContains(elementStr, arrayEmpty);
        failContains(elementInt, arrayEmpty);
        failContains(elementBoolean, arrayEmpty);

        passContains(elementStr, arrayScalars);
        passContains(elementInt, arrayScalars);
        passContains(elementBoolean, arrayScalars);

        failContains(notElementStr, arrayScalars);
        failContains(notElementInt, arrayScalars);
        failContains(notElementBoolean, arrayScalars);
    }

    @Test
    public void containing_WithinArray_Object() {

        JsonObject element = Json.createObjectBuilder()
                .add("a", "one")
                .add("b", 2)
                .add("c", true)
                .build();

        JsonObject notElement = Json.createObjectBuilder()
                .add("a", "one")
                .add("b", 2)
                .add("c", false)
                .build();

        JsonArray arrayEmpty = Json.createArrayBuilder().build();

        JsonArray arrayObjects = Json.createArrayBuilder()
                .add(Json.createObjectBuilder().add("x", 1).add("y", 2).build())
                .add(element)
                .add(Json.createArrayBuilder().add("ab").add("cd").build())
                .build();

        failContains(element, null);
        failContains(element, arrayEmpty);
        passContains(element, arrayObjects);
        failContains(notElement, arrayObjects);
    }

    @Test
    public void containing_WithinArray_Array() {

        JsonArray elementArrayScalars = Json.createArrayBuilder()
                .add("one")
                .add(2)
                .add(true)
                .build();

        JsonArray notElementArrayScalars = Json.createArrayBuilder()
                .add("one")
                .add(2)
                .add(false)
                .build();

        JsonArray elementArrayObjects = Json.createArrayBuilder()
                .add(Json.createObjectBuilder()
                        .add("a", "x").add("b", "y").build())
                .add(Json.createObjectBuilder()
                        .add("a", "z").add("b", "w").build())
                .add(Json.createObjectBuilder()
                        .add("a", "p").add("b", "q").build())
                .build();

        JsonArray notElementArrayObjects = Json.createArrayBuilder()
                .add(Json.createObjectBuilder()
                        .add("a", "x").add("b", "y").build())
                .add(Json.createObjectBuilder()
                        .add("a", "z").add("b", "w").build())
                .add(Json.createObjectBuilder()
                        .add("a", "p").add("b", "not").build())
                .build();

        JsonArray arrayEmpty = Json.createArrayBuilder().build();

        JsonArray arrayArraysScalars = Json.createArrayBuilder()
                .add(elementArrayScalars)
                .build();

        JsonArray arrayArraysObjects = Json.createArrayBuilder()
                .add(elementArrayObjects)
                .build();

        failContains(elementArrayScalars, null);
        failContains(elementArrayObjects, null);

        failContains(elementArrayScalars, arrayEmpty);
        failContains(elementArrayObjects, arrayEmpty);

        passContains(elementArrayScalars, arrayArraysScalars);
        failContains(notElementArrayScalars, arrayArraysScalars);
        failContains(elementArrayObjects, arrayArraysScalars);

        passContains(elementArrayObjects, arrayArraysObjects);
        failContains(notElementArrayObjects, arrayArraysObjects);
        failContains(elementArrayScalars, arrayArraysObjects);
    }

    private void passEquals(JsonStructure expected, JsonStructure actual) {
        AssertJson.assertEquals(expected, actual);
        AssertJson.assertEquals(stringOf(expected), stringOf(actual));
        AssertJson.assertEquals(stringOf(expected), actual);
        AssertJson.assertEquals(expected, stringOf(actual));
    }

    private void passNotEquals(JsonStructure expected, JsonStructure actual) {
        AssertJson.assertNotEquals(expected, actual);
        AssertJson.assertNotEquals(stringOf(expected), stringOf(actual));
        AssertJson.assertNotEquals(stringOf(expected), actual);
        AssertJson.assertNotEquals(expected, stringOf(actual));
    }

    private void passContains(
            JsonValue expectedElement, JsonArray actualArray) {
        AssertJson.assertContains(expectedElement, actualArray);
        AssertJson.assertContains(
                stringOf(expectedElement), stringOf(actualArray));
        AssertJson.assertContains(stringOf(expectedElement), actualArray);
        AssertJson.assertContains(expectedElement, stringOf(actualArray));
    }

    private void failEquals(
            JsonStructure expected,
            JsonStructure actual,
            String path,
            AssertJson.FailureType type) {

        Map<AssertJson.FailureType, Runnable> runnableMap = new HashMap<>();
        runnableMap.put(
                NULL,
                () -> failEquals(
                        expected,
                        actual,
                        NULL.message(),
                        expected,
                        null,
                        true));

        runnableMap.put(
                AssertJson.FailureType.NOT_NULL,
                () -> failEquals(
                        expected,
                        actual,
                        AssertJson.FailureType.NOT_NULL.message(),
                        null,
                        actual,
                        true));

        runnableMap.put(
                AssertJson.FailureType.UNEXPECTED,
                () -> failEquals(
                        expected,
                        actual,
                        AssertJson.FailureType.UNEXPECTED.message(path),
                        null,
                        null,
                        false));

        runnableMap.put(
                AssertJson.FailureType.MISSING,
                () -> failEquals(
                        expected,
                        actual,
                        AssertJson.FailureType.MISSING.message(path),
                        null,
                        null,
                        false));

        runnableMap.get(type).run();
    }

    private void failEquals(
            JsonStructure expected,
            JsonStructure actual,
            Object expectedValue,
            Object actualValue,
            String path,
            AssertJson.FailureType type) {

        failEquals(
                expected,
                actual,
                type.message(path),
                expectedValue,
                actualValue,
                true);
    }

    private void failEquals(
            JsonStructure expected,
            JsonStructure actual,
            String expectedMessage,
            Object expectedValue,
            Object actualValue,
            boolean expectedActualDefined) {

        AssertionFailedError e1 = assertThrows(
                AssertionFailedError.class,
                () -> AssertJson.assertEquals(expected, actual));

        AssertionFailedError e2 = assertThrows(
                AssertionFailedError.class,
                () -> AssertJson.assertEquals(
                        stringOf(expected), stringOf(actual)));

        AssertionFailedError e3 = assertThrows(
                AssertionFailedError.class,
                () -> AssertJson.assertEquals(stringOf(expected), actual));

        AssertionFailedError e4 = assertThrows(
                AssertionFailedError.class,
                () -> AssertJson.assertEquals(expected, stringOf(actual)));

        if (expectedActualDefined) {
            verifyFailure(e1, expectedMessage, expectedValue, actualValue);
            verifyFailure(e2, expectedMessage, expectedValue, actualValue);
            verifyFailure(e3, expectedMessage, expectedValue, actualValue);
            verifyFailure(e4, expectedMessage, expectedValue, actualValue);
        } else {
            verifyFailure(e1, expectedMessage);
            verifyFailure(e2, expectedMessage);
            verifyFailure(e3, expectedMessage);
            verifyFailure(e4, expectedMessage);
        }
    }

    private void failNotEquals(
            JsonStructure expected,
            JsonStructure actual) {

        AssertionFailedError e1 = assertThrows(
                AssertionFailedError.class,
                () -> AssertJson.assertNotEquals(expected, actual));

        AssertionFailedError e2 = assertThrows(
                AssertionFailedError.class,
                () -> AssertJson.assertNotEquals(
                        stringOf(expected), stringOf(actual)));

        AssertionFailedError e3 = assertThrows(
                AssertionFailedError.class,
                () -> AssertJson.assertNotEquals(stringOf(expected), actual));

        AssertionFailedError e4 = assertThrows(
                AssertionFailedError.class,
                () -> AssertJson.assertNotEquals(expected, stringOf(actual)));

        String expectedMessage = UNEXPECTED_EQUAL.message(actual);

        verifyFailure(e1, expectedMessage);
        verifyFailure(e2, expectedMessage);
        verifyFailure(e3, expectedMessage);
        verifyFailure(e4, expectedMessage);
    }

    private void failContains(
            JsonValue expectedElement, JsonArray actualArray) {

        AssertionFailedError e1 = assertThrows(
                AssertionFailedError.class,
                () -> AssertJson.assertContains(
                        expectedElement, actualArray));

        AssertionFailedError e2 = assertThrows(
                AssertionFailedError.class,
                () -> AssertJson.assertContains(
                        stringOf(expectedElement), stringOf(actualArray)));

        AssertionFailedError e3 = assertThrows(
                AssertionFailedError.class,
                () -> AssertJson.assertContains(
                        stringOf(expectedElement), actualArray));

        AssertionFailedError e4 = assertThrows(
                AssertionFailedError.class,
                () -> AssertJson.assertContains(
                        expectedElement, stringOf(actualArray)));

        String expectedMessage =
                NOT_CONTAINING.message(expectedElement, actualArray);

        verifyFailure(e1, expectedMessage);
        verifyFailure(e2, expectedMessage);
        verifyFailure(e3, expectedMessage);
        verifyFailure(e4, expectedMessage);
    }

    private void verifyFailure(
            AssertionFailedError e,
            String expectedMessage,
            Object expectedValue,
            Object actualValue) {

        System.out.println(e.getMessage());

        assertEquals(expectedMessage, e.getMessage());
        assertEquals(
                stringOfValue(expectedValue),
                e.getExpected().getStringRepresentation());
        assertEquals(
                stringOfValue(actualValue),
                e.getActual().getStringRepresentation());
    }

    private void verifyFailure(
            AssertionFailedError e,
            String expectedMessage) {

        System.out.println(e.getMessage());

        assertEquals(expectedMessage, e.getMessage());
        assertFalse(e.isExpectedDefined());
        assertFalse(e.isActualDefined());
    }

    private String stringOf(JsonValue jsonValue) {
        return Optional.ofNullable(jsonValue)
                .map(v -> Optional.of(v)
                        .filter(v1 -> v1.equals(JsonValue.NULL))
                        .map(v1 -> (String) null)
                        .orElse(v.toString()))
                .orElse(null);
    }

    private String stringOfValue(Object value) {
        return Optional.ofNullable(value)
                .filter(v -> v instanceof String)
                .map(v -> "\"" + v + "\"")
                .orElse(String.valueOf(value));
    }
}