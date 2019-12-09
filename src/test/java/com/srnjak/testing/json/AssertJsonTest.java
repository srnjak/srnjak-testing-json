package com.srnjak.testing.json;

import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import javax.json.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

        // contains
        failContains(null, null);
        failContains(null, arrayEmpty);
        passContains(null, arrayNull);
        failContains(null, arrayScalars);
        failContains(null, arrayObjects);
        failContains(null, arrayArrays);

        failContains(JsonValue.NULL, null);
        failContains(JsonValue.NULL, arrayEmpty);
        passContains(JsonValue.NULL, arrayNull);
        failContains(JsonValue.NULL, arrayScalars);
        failContains(JsonValue.NULL, arrayObjects);
        failContains(JsonValue.NULL, arrayArrays);

        // does not contain
        passNotContains(null, null);
        passNotContains(null, arrayEmpty);
        failNotContains(null, arrayNull);
        passNotContains(null, arrayScalars);
        passNotContains(null, arrayObjects);
        passNotContains(null, arrayArrays);

        passNotContains(JsonValue.NULL, null);
        passNotContains(JsonValue.NULL, arrayEmpty);
        failNotContains(JsonValue.NULL, arrayNull);
        passNotContains(JsonValue.NULL, arrayScalars);
        passNotContains(JsonValue.NULL, arrayObjects);
        passNotContains(JsonValue.NULL, arrayArrays);
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

        // contains
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

        // does not contain
        passNotContains(elementStr, null);
        passNotContains(elementInt, null);
        passNotContains(elementBoolean, null);

        passNotContains(elementStr, arrayEmpty);
        passNotContains(elementInt, arrayEmpty);
        passNotContains(elementBoolean, arrayEmpty);

        failNotContains(elementStr, arrayScalars);
        failNotContains(elementInt, arrayScalars);
        failNotContains(elementBoolean, arrayScalars);

        passNotContains(notElementStr, arrayScalars);
        passNotContains(notElementInt, arrayScalars);
        passNotContains(notElementBoolean, arrayScalars);
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

        // contains
        failContains(element, null);
        failContains(element, arrayEmpty);
        passContains(element, arrayObjects);
        failContains(notElement, arrayObjects);

        // does not contain
        passNotContains(element, null);
        passNotContains(element, arrayEmpty);
        failNotContains(element, arrayObjects);
        passNotContains(notElement, arrayObjects);
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

        // contains
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

        // does not contain
        passNotContains(elementArrayScalars, null);
        passNotContains(elementArrayObjects, null);

        passNotContains(elementArrayScalars, arrayEmpty);
        passNotContains(elementArrayObjects, arrayEmpty);

        failNotContains(elementArrayScalars, arrayArraysScalars);
        passNotContains(notElementArrayScalars, arrayArraysScalars);
        passNotContains(elementArrayObjects, arrayArraysScalars);

        failNotContains(elementArrayObjects, arrayArraysObjects);
        passNotContains(notElementArrayObjects, arrayArraysObjects);
        passNotContains(elementArrayScalars, arrayArraysObjects);
    }

    @Test
    public void containingSet_WhenNull() {

        JsonArray arrayEmpty = Json.createArrayBuilder().build();
        JsonArray arrayNull =
                Json.createArrayBuilder().add(JsonValue.NULL).build();
        JsonArray arrayScalars = Generators.generateJsonArrayOfScalars();
        JsonArray arrayObjects = Generators.generateJsonArrayOfObjects();
        JsonArray arrayArrays = Generators.generateJsonArrayOfArrays();

        // contains all
        passContainsAll(null, null);
        passContainsAll(null, arrayEmpty);
        passContainsAll(null, arrayNull);
        passContainsAll(null, arrayScalars);
        passContainsAll(null, arrayObjects);
        passContainsAll(null, arrayArrays);

        // contains any
        passContainsAny(null, null);
        passContainsAny(null, arrayEmpty);
        passContainsAny(null, arrayNull);
        passContainsAny(null, arrayScalars);
        passContainsAny(null, arrayObjects);
        passContainsAny(null, arrayArrays);

        // contains none
        passContainsNone(null, null);
        passContainsNone(null, arrayEmpty);
        passContainsNone(null, arrayNull);
        passContainsNone(null, arrayScalars);
        passContainsNone(null, arrayObjects);
        passContainsNone(null, arrayArrays);
    }

    @Test
    public void containingSet_WhenEmpty() {

        JsonArray arrayEmpty = Json.createArrayBuilder().build();
        JsonArray arrayNull =
                Json.createArrayBuilder().add(JsonValue.NULL).build();
        JsonArray arrayScalars = Generators.generateJsonArrayOfScalars();
        JsonArray arrayObjects = Generators.generateJsonArrayOfObjects();
        JsonArray arrayArrays = Generators.generateJsonArrayOfArrays();

        // contains all
        passContainsAll(arrayEmpty, null);
        passContainsAll(arrayEmpty, arrayEmpty);
        passContainsAll(arrayEmpty, arrayNull);
        passContainsAll(arrayEmpty, arrayScalars);
        passContainsAll(arrayEmpty, arrayObjects);
        passContainsAll(arrayEmpty, arrayArrays);

        // contains any
        passContainsAny(arrayEmpty, null);
        passContainsAny(arrayEmpty, arrayEmpty);
        passContainsAny(arrayEmpty, arrayNull);
        passContainsAny(arrayEmpty, arrayScalars);
        passContainsAny(arrayEmpty, arrayObjects);
        passContainsAny(arrayEmpty, arrayArrays);

        // contains none
        passContainsNone(arrayEmpty, null);
        passContainsNone(arrayEmpty, arrayEmpty);
        passContainsNone(arrayEmpty, arrayNull);
        passContainsNone(arrayEmpty, arrayScalars);
        passContainsNone(arrayEmpty, arrayObjects);
        passContainsNone(arrayEmpty, arrayArrays);
    }

    @Test
    public void containingSet_WhenNotEmpty() {

        JsonObject obj = Json.createObjectBuilder().add("a", "x").build();
        JsonArray arr = Json.createArrayBuilder().add("el").build();

        JsonArray elements = Json.createArrayBuilder()
                .add(JsonValue.NULL)
                .add(true)
                .add(2)
                .add("aa")
                .add(obj)
                .add(arr)
                .build();

        JsonArray arrayEmpty = Json.createArrayBuilder().build();
        JsonArray arraySame = Json.createArrayBuilder(elements).build();
        JsonArray arrayLess =
                Json.createArrayBuilder(elements).remove(0).build();
        JsonArray arrayLess1 =
                Json.createArrayBuilder(elements).remove(1).build();
        JsonArray arrayLess2 =
                Json.createArrayBuilder(elements).remove(2).build();
        JsonArray arrayLess3 =
                Json.createArrayBuilder(elements).remove(3).build();
        JsonArray arrayLess4 =
                Json.createArrayBuilder(elements).remove(4).build();
        JsonArray arrayLess5 =
                Json.createArrayBuilder(elements).remove(5).build();
        JsonArray arrayMore =
                Json.createArrayBuilder(elements).add("xxx").build();
        JsonArray arrayDifferent = Json.createArrayBuilder()
                .add(false)
                .add(-1)
                .add("notHere")
                .build();

        // contains all
        failContainsAll(elements, null, new ArrayList<>(elements));
        failContainsAll(elements, arrayEmpty, new ArrayList<>(elements));
        passContainsAll(elements, arraySame);
        failContainsAll(elements, arrayLess,
                Stream.of(JsonValue.NULL).collect(Collectors.toList()));
        failContainsAll(elements, arrayLess1,
                Stream.of(JsonValue.TRUE).collect(Collectors.toList()));
        failContainsAll(elements, arrayLess2,
                Stream.of(Json.createValue(2)).collect(Collectors.toList()));
        failContainsAll(elements, arrayLess3,
                Stream.of(Json.createValue("aa")).collect(Collectors.toList()));
        failContainsAll(elements, arrayLess4,
                Stream.of(obj).collect(Collectors.toList()));
        failContainsAll(elements, arrayLess5,
                Stream.of(arr).collect(Collectors.toList()));
        passContainsAll(elements, arrayMore);
        failContainsAll(elements, arrayDifferent, new ArrayList<>(elements));

        // contains any
        failContainsAny(elements, null);
        failContainsAny(elements, arrayEmpty);
        passContainsAny(elements, arraySame);
        passContainsAny(elements, arrayLess);
        passContainsAny(elements, arrayLess1);
        passContainsAny(elements, arrayLess2);
        passContainsAny(elements, arrayLess3);
        passContainsAny(elements, arrayLess4);
        passContainsAny(elements, arrayLess5);
        passContainsAny(elements, arrayMore);
        failContainsAny(elements, arrayDifferent);

        // contains none
        passContainsNone(elements, null);
        passContainsNone(elements, arrayEmpty);
        failContainsNone(elements, arraySame, new ArrayList<>(elements));
        failContainsNone(elements, arrayLess,
                Stream.of(
                        JsonValue.TRUE,
                        Json.createValue(2),
                        Json.createValue("aa"),
                        obj,
                        arr)
                        .collect(Collectors.toList()));
        failContainsNone(elements, arrayLess1,
                Stream.of(
                        JsonValue.NULL,
                        Json.createValue(2),
                        Json.createValue("aa"),
                        obj,
                        arr)
                        .collect(Collectors.toList()));
        failContainsNone(elements, arrayLess2,
                Stream.of(
                        JsonValue.NULL,
                        JsonValue.TRUE,
                        Json.createValue("aa"),
                        obj,
                        arr)
                        .collect(Collectors.toList()));
        failContainsNone(elements, arrayLess3,
                Stream.of(
                        JsonValue.NULL,
                        JsonValue.TRUE,
                        Json.createValue(2),
                        obj,
                        arr)
                        .collect(Collectors.toList()));
        failContainsNone(elements, arrayLess4,
                Stream.of(
                        JsonValue.NULL,
                        JsonValue.TRUE,
                        Json.createValue(2),
                        Json.createValue("aa"),
                        arr)
                        .collect(Collectors.toList()));
        failContainsNone(elements, arrayLess5,
                Stream.of(
                        JsonValue.NULL,
                        JsonValue.TRUE,
                        Json.createValue(2),
                        Json.createValue("aa"),
                        obj)
                        .collect(Collectors.toList()));
        failContainsNone(elements, arrayMore, new ArrayList<>(elements));
        passContainsNone(elements, arrayDifferent);
    }

    @Test
    public void containingProperty_WhenNull() {

        JsonObject objectEmbedded = Json.createObjectBuilder()
                .add("one", 1)
                .add("two", 2)
                .add("three", 3)
                .build();
        JsonArray arrayEmbedded = Json.createArrayBuilder()
                .add(1).add(2).add(3).build();

        JsonObject objectEmpty = Json.createObjectBuilder().build();
        JsonObject objectNull = Json.createObjectBuilder()
                .add("a", JsonValue.NULL)
                .build();
        JsonObject object1 = Json.createObjectBuilder()
                .add("a", 1)
                .add("b", "x")
                .add("c", true)
                .add("d", objectEmbedded)
                .add("e", arrayEmbedded)
                .build();

        JsonArray arrayEmpty = Json.createArrayBuilder().build();
        JsonArray arrayNull = Json.createArrayBuilder()
                .add(JsonValue.NULL)
                .build();
        JsonArray arrayObjects = Json.createArrayBuilder()
                .add(objectEmbedded)
                .build();
        JsonArray arrayObjectNull = Json.createArrayBuilder()
                .add(objectNull)
                .build();

        failContainsProperty(null, "/a", null, null, MISSING);
        failContainsProperty(null, "/a", objectEmpty, null, MISSING);
        passContainsProperty(null, "/a", objectNull);
        failContainsProperty(null, "/a", object1, 1, INVALID);

        failContainsProperty(null, "/0", arrayEmpty, null, MISSING);
        passContainsProperty(null, "/0", arrayNull);
        failContainsProperty(null, "/0", arrayObjects, objectEmbedded, INVALID);
        passContainsProperty(null, "/0/a", arrayObjectNull);
    }

    @Test
    public void containingProperty_WhenScalar() {

        JsonValue expected = Json.createValue(1);
        JsonValue unexpected = Json.createValue(2);

        JsonObject objectEmbedded = Json.createObjectBuilder()
                .add("one", expected)
                .add("two", unexpected)
                .build();
        JsonArray arrayEmbedded = Json.createArrayBuilder()
                .add(1).add(2).add(3).build();

        JsonObject objectEmpty = Json.createObjectBuilder().build();
        JsonObject object1 = Json.createObjectBuilder()
                .add("a", expected)
                .add("b", unexpected)
                .add("c", objectEmbedded)
                .add("d", arrayEmbedded)
                .build();

        JsonArray arrayEmpty = Json.createArrayBuilder().build();
        JsonArray arrayScalar = Json.createArrayBuilder()
                .add(expected)
                .add(unexpected)
                .build();
        JsonArray arrayObjects = Json.createArrayBuilder()
                .add(objectEmbedded)
                .build();

        failContainsProperty(expected, "/a", null, null, MISSING);
        failContainsProperty(expected, "/a", objectEmpty, null, MISSING);
        passContainsProperty(expected, "/a", object1);
        failContainsProperty(expected, "/b", object1, unexpected, INVALID);
        passContainsProperty(expected, "/c/one", object1);
        failContainsProperty(expected, "/c/two", object1, unexpected, INVALID);
        failContainsProperty(expected, "/c/xxx", object1, null, MISSING);

        failContainsProperty(expected, "/0", arrayEmpty, null, MISSING);
        passContainsProperty(expected, "/0", arrayScalar);
        failContainsProperty(expected, "/1", arrayScalar, unexpected, INVALID);
        passContainsProperty(expected, "/0/one", arrayObjects);
        failContainsProperty(
                expected, "/0/two", arrayObjects, unexpected, INVALID);
    }

    @Test
    public void containingProperty_WhenObject() {

        JsonValue expected = Json.createObjectBuilder()
                .add("x", 1)
                .add("y", 2)
                .build();
        JsonValue unexpected = Json.createObjectBuilder()
                .add("x", 1)
                .add("y", -1)
                .build();

        JsonObject objectEmbedded = Json.createObjectBuilder()
                .add("one", expected)
                .add("two", unexpected)
                .build();
        JsonArray arrayEmbedded = Json.createArrayBuilder()
                .add(1).add(2).add(3).build();

        JsonObject objectEmpty = Json.createObjectBuilder().build();
        JsonObject object1 = Json.createObjectBuilder()
                .add("a", expected)
                .add("b", unexpected)
                .add("c", objectEmbedded)
                .add("d", arrayEmbedded)
                .build();

        JsonArray arrayEmpty = Json.createArrayBuilder().build();
        JsonArray arrayNotEmpty = Json.createArrayBuilder()
                .add(expected)
                .add(unexpected)
                .add(objectEmbedded)
                .build();

        failContainsProperty(expected, "/a", null, null, MISSING);
        failContainsProperty(expected, "/a", objectEmpty, null, MISSING);
        passContainsProperty(expected, "/a", object1);
        failContainsProperty(expected, "/b", object1, unexpected, INVALID);
        passContainsProperty(expected, "/c/one", object1);
        failContainsProperty(expected, "/c/two", object1, unexpected, INVALID);
        failContainsProperty(expected, "/c/xxx", object1, null, MISSING);

        failContainsProperty(expected, "/0", arrayEmpty, null, MISSING);
        passContainsProperty(expected, "/0", arrayNotEmpty);
        failContainsProperty(expected, "/1", arrayNotEmpty, unexpected, INVALID);
        passContainsProperty(expected, "/2/one", arrayNotEmpty);
        failContainsProperty(
                expected, "/2/two", arrayNotEmpty, unexpected, INVALID);
    }

    @Test
    public void containingProperty_WhenArray() {
        JsonArray expected = Json.createArrayBuilder()
                .add("x").add("y").build();
        JsonArray unexpected = Json.createArrayBuilder()
                .add("x").add("z").build();

        JsonObject objectEmbedded = Json.createObjectBuilder()
                .add("one", expected)
                .add("two", unexpected)
                .build();
        JsonArray arrayEmbedded = Json.createArrayBuilder()
                .add(1).add(2).add(3).build();

        JsonObject objectEmpty = Json.createObjectBuilder().build();
        JsonObject object1 = Json.createObjectBuilder()
                .add("a", expected)
                .add("b", unexpected)
                .add("c", objectEmbedded)
                .add("d", arrayEmbedded)
                .build();

        JsonArray arrayEmpty = Json.createArrayBuilder().build();
        JsonArray arrayNotEmpty = Json.createArrayBuilder()
                .add(expected)
                .add(unexpected)
                .add(objectEmbedded)
                .build();

        failContainsProperty(expected, "/a", null, null, MISSING);
        failContainsProperty(expected, "/a", objectEmpty, null, MISSING);
        passContainsProperty(expected, "/a", object1);
        failContainsProperty(expected, "/b", object1, unexpected, INVALID);
        passContainsProperty(expected, "/c/one", object1);
        failContainsProperty(expected, "/c/two", object1, unexpected, INVALID);
        failContainsProperty(expected, "/c/xxx", object1, null, MISSING);

        failContainsProperty(expected, "/0", arrayEmpty, null, MISSING);
        passContainsProperty(expected, "/0", arrayNotEmpty);
        failContainsProperty(
                expected, "/1", arrayNotEmpty, unexpected, INVALID);
        passContainsProperty(expected, "/2/one", arrayNotEmpty);
        failContainsProperty(
                expected, "/2/two", arrayNotEmpty, unexpected, INVALID);
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

    private void passNotContains(
            JsonValue expectedElement, JsonArray actualArray) {
        AssertJson.assertNotContains(expectedElement, actualArray);
        AssertJson.assertNotContains(
                stringOf(expectedElement), stringOf(actualArray));
        AssertJson.assertNotContains(stringOf(expectedElement), actualArray);
        AssertJson.assertNotContains(expectedElement, stringOf(actualArray));
    }

    private void passContainsAll(
            JsonArray expectedElements, JsonArray actualArray) {
        AssertJson.assertContainsAll(expectedElements, actualArray);
        AssertJson.assertContainsAll(
                stringOf(expectedElements), stringOf(actualArray));
        AssertJson.assertContainsAll(stringOf(expectedElements), actualArray);
        AssertJson.assertContainsAll(expectedElements, stringOf(actualArray));
    }

    private void passContainsAny(
            JsonArray expectedElements, JsonArray actualArray) {
        AssertJson.assertContainsAny(expectedElements, actualArray);
        AssertJson.assertContainsAny(
                stringOf(expectedElements), stringOf(actualArray));
        AssertJson.assertContainsAny(stringOf(expectedElements), actualArray);
        AssertJson.assertContainsAny(expectedElements, stringOf(actualArray));
    }

    private void passContainsNone(
            JsonArray unexpectedElements, JsonArray actualArray) {
        AssertJson.assertContainsNone(unexpectedElements, actualArray);
        AssertJson.assertContainsNone(
                stringOf(unexpectedElements), stringOf(actualArray));
        AssertJson.assertContainsNone(
                stringOf(unexpectedElements), actualArray);
        AssertJson.assertContainsNone(
                unexpectedElements, stringOf(actualArray));
    }

    private void passContainsProperty(
            JsonValue expected, String path, JsonStructure actual) {
        AssertJson.assertContainsProperty(expected, path, actual);
        AssertJson.assertContainsProperty(
                stringOf(expected), path, stringOf(actual));
        AssertJson.assertContainsProperty(stringOf(expected), path, actual);
        AssertJson.assertContainsProperty(expected, path, stringOf(actual));
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
                NOT_NULL,
                () -> failEquals(
                        expected,
                        actual,
                        NOT_NULL.message(),
                        null,
                        actual,
                        true));

        runnableMap.put(
                UNEXPECTED,
                () -> failEquals(
                        expected,
                        actual,
                        UNEXPECTED.message(path),
                        null,
                        null,
                        false));

        runnableMap.put(
                MISSING,
                () -> failEquals(
                        expected,
                        actual,
                        MISSING.message(path),
                        null,
                        null,
                        false));

        runnableMap.get(type).run();
    }

    @SuppressWarnings("SameParameterValue")
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

    private void failNotContains(
            JsonValue unexpectedElement, JsonArray actualArray) {

        AssertionFailedError e1 = assertThrows(
                AssertionFailedError.class,
                () -> AssertJson.assertNotContains(
                        unexpectedElement, actualArray));

        AssertionFailedError e2 = assertThrows(
                AssertionFailedError.class,
                () -> AssertJson.assertNotContains(
                        stringOf(unexpectedElement), stringOf(actualArray)));

        AssertionFailedError e3 = assertThrows(
                AssertionFailedError.class,
                () -> AssertJson.assertNotContains(
                        stringOf(unexpectedElement), actualArray));

        AssertionFailedError e4 = assertThrows(
                AssertionFailedError.class,
                () -> AssertJson.assertNotContains(
                        unexpectedElement, stringOf(actualArray)));

        String expectedMessage =
                CONTAINING.message(unexpectedElement, actualArray);

        verifyFailure(e1, expectedMessage);
        verifyFailure(e2, expectedMessage);
        verifyFailure(e3, expectedMessage);
        verifyFailure(e4, expectedMessage);
    }

    private void failContainsAll(
            JsonArray expectedElements,
            JsonArray actualArray,
            List<JsonValue> missingElements) {

        AssertionFailedError e1 = assertThrows(
                AssertionFailedError.class,
                () -> AssertJson.assertContainsAll(
                        expectedElements, actualArray));

        AssertionFailedError e2 = assertThrows(
                AssertionFailedError.class,
                () -> AssertJson.assertContainsAll(
                        stringOf(expectedElements), stringOf(actualArray)));

        AssertionFailedError e3 = assertThrows(
                AssertionFailedError.class,
                () -> AssertJson.assertContainsAll(
                        stringOf(expectedElements), actualArray));

        AssertionFailedError e4 = assertThrows(
                AssertionFailedError.class,
                () -> AssertJson.assertContainsAll(
                        expectedElements, stringOf(actualArray)));

        String expectedMessage =
                NOT_CONTAINING_ALL.message(missingElements, actualArray);

        verifyFailure(e1, expectedMessage);
        verifyFailure(e2, expectedMessage);
        verifyFailure(e3, expectedMessage);
        verifyFailure(e4, expectedMessage);
    }

    private void failContainsAny(
            JsonArray expectedElements,
            JsonArray actualArray) {

        AssertionFailedError e1 = assertThrows(
                AssertionFailedError.class,
                () -> AssertJson.assertContainsAny(
                        expectedElements, actualArray));

        AssertionFailedError e2 = assertThrows(
                AssertionFailedError.class,
                () -> AssertJson.assertContainsAny(
                        stringOf(expectedElements), stringOf(actualArray)));

        AssertionFailedError e3 = assertThrows(
                AssertionFailedError.class,
                () -> AssertJson.assertContainsAny(
                        stringOf(expectedElements), actualArray));

        AssertionFailedError e4 = assertThrows(
                AssertionFailedError.class,
                () -> AssertJson.assertContainsAny(
                        expectedElements, stringOf(actualArray)));

        String expectedMessage =
                NOT_CONTAINING_ANY.message(expectedElements, actualArray);

        verifyFailure(e1, expectedMessage);
        verifyFailure(e2, expectedMessage);
        verifyFailure(e3, expectedMessage);
        verifyFailure(e4, expectedMessage);
    }

    private void failContainsNone(
            JsonArray unexpectedElements,
            JsonArray actualArray,
            List<JsonValue> containingElements) {

        AssertionFailedError e1 = assertThrows(
                AssertionFailedError.class,
                () -> AssertJson.assertContainsNone(
                        unexpectedElements, actualArray));

        AssertionFailedError e2 = assertThrows(
                AssertionFailedError.class,
                () -> AssertJson.assertContainsNone(
                        stringOf(unexpectedElements), stringOf(actualArray)));

        AssertionFailedError e3 = assertThrows(
                AssertionFailedError.class,
                () -> AssertJson.assertContainsNone(
                        stringOf(unexpectedElements), actualArray));

        AssertionFailedError e4 = assertThrows(
                AssertionFailedError.class,
                () -> AssertJson.assertContainsNone(
                        unexpectedElements, stringOf(actualArray)));

        String expectedMessage =
                CONTAINING_SOME.message(containingElements, actualArray);

        verifyFailure(e1, expectedMessage);
        verifyFailure(e2, expectedMessage);
        verifyFailure(e3, expectedMessage);
        verifyFailure(e4, expectedMessage);
    }

    private void failContainsProperty(
            JsonValue expectedValue,
            String path,
            JsonStructure actual,
            Object actualValue,
            AssertJson.FailureType type) {

        AssertionFailedError e1 = assertThrows(
                AssertionFailedError.class,
                () -> AssertJson.assertContainsProperty(
                        expectedValue, path, actual));

        AssertionFailedError e2 = assertThrows(
                AssertionFailedError.class,
                () -> AssertJson.assertContainsProperty(
                        stringOf(expectedValue), path, stringOf(actual)));

        AssertionFailedError e3 = assertThrows(
                AssertionFailedError.class,
                () -> AssertJson.assertContainsProperty(
                        stringOf(expectedValue), path, actual));

        AssertionFailedError e4 = assertThrows(
                AssertionFailedError.class,
                () -> AssertJson.assertContainsProperty(
                        expectedValue, path, stringOf(actual)));

        String expectedMessage = type.message(path);

        if (INVALID.equals(type)) {
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

    private void verifyFailure(
            AssertionFailedError e,
            String expectedMessage,
            Object expectedValue,
            Object actualValue) {

        System.out.println(e.getMessage());
        System.out.println("\texpected:" +
                e.getExpected().getStringRepresentation());
        System.out.println("\tactual:" +
                e.getActual().getStringRepresentation());

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