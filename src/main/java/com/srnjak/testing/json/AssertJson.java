package com.srnjak.testing.json;

import org.opentest4j.AssertionFailedError;

import javax.json.*;
import java.io.StringReader;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.srnjak.testing.json.AssertJson.FailureType.*;

public class AssertJson {

    enum FailureType {
        NULL("Json structure is null."),
        NOT_NULL("Json structure is not null."),
        UNEXPECTED("Unexpected: %s"),
        MISSING("Missing: %s"),
        INVALID("Invalid: %s"),
        UNEXPECTED_EQUAL("Expected not equal but was: %s"),
        NOT_CONTAINING("The expected element: %s is not part of: %s"),
        CONTAINING("The element: %s is part of: %s, but is not expected."),
        NOT_CONTAINING_ALL("Expected elements %s are missing in %s"),
        NOT_CONTAINING_ANY("Not found any of elements %s in the given %s"),
        CONTAINING_SOME("Found %s in the given %s, but none of them expected."),
        UNEXPECTED_PROPERTY(
                "The property %s was found on path %s, but is not expected.");

        String message;

        FailureType(String message) {
            this.message = message;
        }

        public String message(Object... params) {

            String[] strParams = Arrays.stream(params)
                    .map(String::valueOf)
                    .toArray(String[]::new);
            
            @SuppressWarnings("RedundantCast")
            String format = String.format(message, (Object[]) strParams);
            return format;
        }
    }

    /**
     * Verifies if an actual {@link JsonStructure} is semantically
     * equal to an expected one.
     *
     * @param expected The expected {@link JsonStructure}
     * @param actual The actual {@link JsonStructure}
     */
    public static void assertEquals(
            JsonStructure expected, JsonStructure actual) {

        if (expected == actual) {
            return;
        }

        if (actual == null) {
            failNull(expected);
        }

        if (expected == null) {
            failNotNull(actual);
        }

        Json.createDiff(expected, actual)
                .toJsonArray().stream()
                .map(JsonValue::asJsonObject)
                .forEach(failEquals(expected, actual));
    }

    /**
     * Verifies if an actual json string is semantically equal
     * to an expected {@link JsonStructure}.
     *
     * @param expected The expected {@link JsonStructure}
     * @param actual The actual json string
     */
    public static void assertEquals(String expected, String actual) {
        assertEquals(parse(expected), parse(actual));
    }

    /**
     * Verifies if an actual {@link JsonStructure} is semantically equal
     * to an expected json string.
     *
     * @param expected The expected {@link JsonStructure}
     * @param actual The actual json string
     */
    public static void assertEquals(JsonStructure expected, String actual) {
        assertEquals(expected, parse(actual));
    }

    /**
     * Verifies if an actual {@link JsonStructure} is semantically equal
     * to an expected json string.
     *
     * @param expected The expected json string
     * @param actual The actual {@link JsonStructure}
     */
    public static void assertEquals(String expected, JsonStructure actual) {
        assertEquals(parse(expected), actual);
    }

    /**
     * Verifies if an actual {@link JsonStructure} is semantically
     * not equal to an unexpected one.
     *
     * @param unexpected The unexpected {@link JsonStructure}
     * @param actual The actual {@link JsonStructure}
     */
    public static void assertNotEquals(
            JsonStructure unexpected, JsonStructure actual) {

        if (unexpected == actual) {
            failUnexpectedEqual(actual);
        }

        if (unexpected == null || actual == null) {
            return;
        }

        boolean equals = equals(unexpected, actual);

        if (equals) {
            failUnexpectedEqual(actual);
        }
    }

    /**
     * Verifies if an actual json string is semantically not equal
     * to an unexpected {@link JsonStructure}.
     *
     * @param unexpected The unexpected {@link JsonStructure}
     * @param actual The actual json string
     */
    public static void assertNotEquals(String unexpected, String actual) {
        assertNotEquals(parse(unexpected), parse(actual));
    }

    /**
     * Verifies if an actual {@link JsonStructure} is semantically not equal
     * to an unexpected json string.
     *
     * @param unexpected The unexpected {@link JsonStructure}
     * @param actual The actual json string
     */
    public static void assertNotEquals(
            JsonStructure unexpected, String actual) {
        assertNotEquals(unexpected, parse(actual));
    }

    /**
     * Verifies if an actual {@link JsonStructure} is semantically not equal
     * to an unexpected json string.
     *
     * @param unexpected The unexpected json string
     * @param actual The actual {@link JsonStructure}
     */
    public static void assertNotEquals(
            String unexpected, JsonStructure actual) {
        assertNotEquals(parse(unexpected), actual);
    }

    /**
     * Verifies if an actual {@link JsonArray} contains an expected
     * {@link JsonValue} element.
     *
     * @param expectedElement The expected {@link JsonValue} element
     * @param actualArray The actual {@link JsonArray}
     */
    public static void assertContains(
            JsonValue expectedElement, JsonArray actualArray) {

        if (!contains(expectedElement, actualArray)) {
            failNotContaining(expectedElement, actualArray);
        }
    }

    /**
     * Verifies if an actual json array string contains an expected
     * json element.
     *
     * @param expectedElement The expected json element
     * @param actualArray The actual json array string
     */
    public static void assertContains(
            String expectedElement, String actualArray) {
        assertContains(parseValue(expectedElement), parseArray(actualArray));
    }

    /**
     * Verifies if an actual {@link JsonArray} string contains an expected
     * json element.
     *
     * @param expectedElement The expected json element
     * @param actualArray The actual {@link JsonArray} string
     */
    public static void assertContains(
            String expectedElement, JsonArray actualArray) {
        assertContains(parseValue(expectedElement), actualArray);
    }

    /**
     * Verifies if an actual json array string contains an expected
     * {@link JsonValue} element.
     *
     * @param expectedElement The expected {@link JsonValue} element
     * @param actualArray The actual json array string
     */
    public static void assertContains(
            JsonValue expectedElement, String actualArray) {
        assertContains(expectedElement, parseArray(actualArray));
    }

    /**
     * Verifies if an actual {@link JsonArray} does not contain an unexpected
     * {@link JsonValue} element.
     *
     * @param unexpectedElement The unexpected {@link JsonValue} element
     * @param actualArray The actual {@link JsonArray}
     */
    public static void assertNotContains(
            JsonValue unexpectedElement, JsonArray actualArray) {

        if (contains(unexpectedElement, actualArray)) {
            failContaining(unexpectedElement, actualArray);
        }
    }

    /**
     * Verifies if an actual json array string does not contain an unexpected
     * json element.
     *
     * @param unexpectedElement The unexpected json element
     * @param actualArray The actual json array string
     */
    public static void assertNotContains(
            String unexpectedElement, String actualArray) {
        assertNotContains(parseValue(unexpectedElement), parseArray(actualArray));
    }

    /**
     * Verifies if an actual {@link JsonArray} does not contain an unexpected
     * json element.
     *
     * @param unexpectedElement The unexpected json element
     * @param actualArray The actual {@link JsonArray}
     */
    public static void assertNotContains(
            String unexpectedElement, JsonArray actualArray) {
        assertNotContains(parseValue(unexpectedElement), actualArray);
    }

    /**
     * Verifies if an actual json array string does not contain an unexpected
     * {@link JsonValue} element.
     *
     * @param unexpectedElement The unexpected {@link JsonValue} element
     * @param actualArray The actual json array string
     */
    public static void assertNotContains(
            JsonValue unexpectedElement, String actualArray) {
        assertNotContains(unexpectedElement, parseArray(actualArray));
    }

    /**
     * Verifies if an actual {@link JsonArray} contains all elements
     * from {@link JsonArray} of expected elements.
     *
     * @param expectedElements The {@link JsonArray} of expected elements
     * @param actualArray The actual {@link JsonArray}
     */
    public static void assertContainsAll(
            JsonArray expectedElements, JsonArray actualArray) {

        Optional.ofNullable(expectedElements)
                .ifPresent(e -> {
                    List<JsonValue> missingList = e.stream()
                                .filter(exp -> !contains(exp, actualArray))
                                .collect(Collectors.toList());

                    if (!missingList.isEmpty()) {
                        failNotContainingAll(missingList, actualArray);
                    }
                });
    }

    /**
     * Verifies if an actual json array string contains all elements
     * from json array string of expected elements.
     *
     * @param expectedElements The json array string of expected elements
     * @param actualArray The actual json array string
     */
    public static void assertContainsAll(
            String expectedElements, String actualArray) {
        assertContainsAll(
                parseArray(expectedElements), parseArray(actualArray));
    }

    /**
     * Verifies if an actual {@link JsonArray} contains all elements
     * from json array string of expected elements.
     *
     * @param expectedElements The json array string of expected elements
     * @param actualArray The actual {@link JsonArray}
     */
    public static void assertContainsAll(
            String expectedElements, JsonArray actualArray) {
        assertContainsAll(parseArray(expectedElements), actualArray);
    }

    /**
     * Verifies if an actual json array string contain all elements
     * from {@link JsonArray} of expected elements.
     *
     * @param expectedElements The {@link JsonArray} of expected elements
     * @param actualArray The actual json array string
     */
    public static void assertContainsAll(
            JsonArray expectedElements, String actualArray) {
        assertContainsAll(expectedElements, parseArray(actualArray));
    }

    /**
     * Verifies if an actual {@link JsonArray} contains any of
     * expected elements.
     *
     * @param expectedElements The {@link JsonArray} of expected elements
     * @param actualArray The actual {@link JsonArray}
     */
    public static void assertContainsAny(
            JsonArray expectedElements, JsonArray actualArray) {

        boolean containsAny = Optional.ofNullable(expectedElements)
                .filter(e -> !e.isEmpty())
                .map(e -> e.stream()
                        .anyMatch(exp -> contains(exp, actualArray)))
                .orElse(true);

        if (!containsAny) {
            failNotContainingAny(expectedElements, actualArray);
        }
    }

    /**
     * Verifies if an actual json array string contains any of
     * expected elements.
     *
     * @param expectedElements The json array string of expected elements
     * @param actualArray The actual json array string
     */
    public static void assertContainsAny(
            String expectedElements, String actualArray) {
        assertContainsAny(
                parseArray(expectedElements), parseArray(actualArray));
    }

    /**
     * Verifies if an actual {@link JsonArray} contains any of
     * expected elements.
     *
     * @param expectedElements The actual json array of expected elements
     * @param actualArray The actual {@link JsonArray}
     */
    public static void assertContainsAny(
            String expectedElements, JsonArray actualArray) {
        assertContainsAny(parseArray(expectedElements), actualArray);
    }

    /**
     * Verifies if an actual actual json array contains any of
     * expected elements.
     *
     * @param expectedElements The {@link JsonArray} of expected elements
     * @param actualArray The actual actual json array
     */
    public static void assertContainsAny(
            JsonArray expectedElements, String actualArray) {
        assertContainsAny(expectedElements, parseArray(actualArray));
    }

    /**
     * Verifies if an actual {@link JsonArray} contains none of
     * unexpected elements.
     *
     * @param unexpectedElements The {@link JsonArray} of unexpected elements
     * @param actualArray The actual {@link JsonArray}
     */
    public static void assertContainsNone(
            JsonArray unexpectedElements, JsonArray actualArray) {

        Optional.ofNullable(unexpectedElements)
                .filter(e -> !e.isEmpty())
                .map(e -> e.stream()
                        .filter(exp -> contains(exp, actualArray))
                        .collect(Collectors.toList()))
                .filter(l -> !l.isEmpty())
                .ifPresent(l -> {
                    failContainingSome(l, actualArray);
                });
    }

    /**
     * Verifies if an actual json array string contains none of
     * unexpected elements.
     *
     * @param unexpectedElements The json array string of unexpected elements
     * @param actualArray The actual json array string
     */
    public static void assertContainsNone(
            String unexpectedElements, String actualArray) {
        assertContainsNone(
                parseArray(unexpectedElements), parseArray(actualArray));
    }

    /**
     * Verifies if an actual {@link JsonArray} contains none of
     * unexpected elements.
     *
     * @param unexpectedElements The json array string of unexpected elements
     * @param actualArray The actual {@link JsonArray}
     */
    public static void assertContainsNone(
            String unexpectedElements, JsonArray actualArray) {
        assertContainsNone(parseArray(unexpectedElements), actualArray);
    }

    /**
     * Verifies if an actual json array string contains none of
     * unexpected elements.
     *
     * @param unexpectedElements The {@link JsonArray} of unexpected elements
     * @param actualArray The actual json array string
     */
    public static void assertContainsNone(
            JsonArray unexpectedElements, String actualArray) {
        assertContainsNone(unexpectedElements, parseArray(actualArray));
    }

    /**
     * Verifies if an actual {@link JsonStructure} contains expected
     * {@link JsonValue} on specified path.
     *
     * @param expectedValue The expected {@link JsonValue}
     * @param path The path
     * @param actual The actual {@link JsonStructure}
     *
     * @throws NullPointerException If path is null
     */
    public static void assertContainsProperty(
            JsonValue expectedValue, String path, JsonStructure actual)
            throws NullPointerException {

        JsonValue expectedV = Optional.ofNullable(expectedValue)
                .orElse(JsonValue.NULL);

        JsonPointer pointer = Optional.of(path)
                .map(Json::createPointer)
                .get();

        Optional.ofNullable(actual)
                .filter(pointer::containsValue)
                .map(pointer::getValue)
                .ifPresentOrElse(
                        v -> Optional.of(v)
                                .filter(v1 -> !equals(expectedV, v1))
                                .ifPresent(v1 -> failInvalid(
                                        path, expectedV, v1)),
                        () -> failMissing(path));
    }

    /**
     * Verifies if an actual json string contains expected
     * json value on specified path.
     *
     * @param expectedValue The expected json value
     * @param path The path
     * @param actual The actual json string
     *
     * @throws NullPointerException If path is null
     */
    public static void assertContainsProperty(
            String expectedValue, String path, String actual)
            throws NullPointerException {
        assertContainsProperty(parseValue(expectedValue), path, parse(actual));
    }

    /**
     * Verifies if an actual {@link JsonStructure} contains expected
     * json value on specified path.
     *
     * @param expectedValue The expected json value
     * @param path The path
     * @param actual The actual {@link JsonStructure}
     *
     * @throws NullPointerException If path is null
     */
    public static void assertContainsProperty(
            String expectedValue, String path, JsonStructure actual)
            throws NullPointerException {
        assertContainsProperty(parseValue(expectedValue), path, actual);
    }

    /**
     * Verifies if an actual json string contains expected
     * {@link JsonValue} on specified path.
     *
     * @param expectedValue The expected {@link JsonValue}
     * @param path The path
     * @param actual The actual json string
     *
     * @throws NullPointerException If path is null
     */
    public static void assertContainsProperty(
            JsonValue expectedValue, String path, String actual)
            throws NullPointerException {
        assertContainsProperty(expectedValue, path, parse(actual));
    }

    /**
     * Verifies if an actual {@link JsonStructure} does not contains unexpected
     * {@link JsonValue} on specified path.
     *
     * @param unexpectedValue The unexpected {@link JsonValue}
     * @param path The path
     * @param actual The actual {@link JsonStructure}
     *
     * @throws NullPointerException If path is null
     */
    public static void assertNotContainsProperty(
            JsonValue unexpectedValue, String path, JsonStructure actual)
            throws NullPointerException {

        JsonValue unexpectedV = Optional.ofNullable(unexpectedValue)
                .orElse(JsonValue.NULL);

        JsonPointer pointer = Optional.of(path)
                .map(Json::createPointer)
                .get();

        Optional.ofNullable(actual)
                .filter(pointer::containsValue)
                .map(pointer::getValue)
                .flatMap(v -> Optional.of(v)
                        .filter(v1 -> equals(unexpectedV, v1)))
                .ifPresent(v1 -> failUnexpectedProperty(v1, path));
    }

    /**
     * Verifies if an actual json string does not contains unexpected
     * json value on specified path.
     *
     * @param unexpectedValue The unexpected json value
     * @param path The path
     * @param actual The actual json string
     *
     * @throws NullPointerException If path is null
     */
    public static void assertNotContainsProperty(
            String unexpectedValue, String path, String actual)
            throws NullPointerException {
        assertNotContainsProperty(
                parseValue(unexpectedValue), path, parse(actual));
    }

    /**
     * Verifies if an actual {@link JsonStructure} does not contains unexpected
     * json value on specified path.
     *
     * @param unexpectedValue The unexpected json value
     * @param path The path
     * @param actual The actual {@link JsonStructure}
     *
     * @throws NullPointerException If path is null
     */
    public static void assertNotContainsProperty(
            String unexpectedValue, String path, JsonStructure actual)
            throws NullPointerException {
        assertNotContainsProperty(
                parseValue(unexpectedValue), path, actual);
    }

    /**
     * Verifies if an actual json string does not contains unexpected
     * {@link JsonValue} on specified path.
     *
     * @param unexpectedValue The unexpected {@link JsonValue}
     * @param path The path
     * @param actual The actual json string
     *
     * @throws NullPointerException If path is null
     */
    public static void assertNotContainsProperty(
            JsonValue unexpectedValue, String path, String actual)
            throws NullPointerException {
        assertNotContainsProperty(
                unexpectedValue, path, parse(actual));
    }

    /**
     * Whether a {@link JsonArray} contains a specified
     * {@link JsonValue} element.
     *
     * @param element The specified {@link JsonValue} element
     * @param array The {@link JsonArray}
     *
     * @return {@code true} if contains
     */
    private static boolean contains(JsonValue element, JsonArray array) {
        return Optional.ofNullable(array)
                .filter(a -> !a.isEmpty())
                .map(a -> {
                    JsonValue el = Optional.ofNullable(element)
                            .orElse(JsonValue.NULL);

                    return a.stream().anyMatch(v -> equals(el, v));
                })
                .orElse(false);
    }

    private static boolean equals(
            JsonValue jsonValue1,
            JsonValue jsonValue2) {

        if (jsonValue1 == jsonValue2) {
            return true;
        }

        if (jsonValue1 == null
                || jsonValue2 == null
                || !jsonValue1.getValueType().equals(
                jsonValue2.getValueType())) {
            return false;
        }

        if (jsonValue1.getValueType().equals(JsonValue.ValueType.OBJECT)
                || jsonValue1.getValueType().equals(
                JsonValue.ValueType.ARRAY)) {

            return Json.createDiff(
                    (JsonStructure) jsonValue1, (JsonStructure) jsonValue2)
                    .toJsonArray().stream()
                    .findAny()
                    .isEmpty();
        }

        return jsonValue1.equals(jsonValue2);
    }

    private static JsonStructure parse(String json) {
        if (json == null) {
            return null;
        }

        try(JsonReader jsonReader = Json.createReader(new StringReader(json))) {
            return jsonReader.read();
        }
    }

    private static JsonValue parseValue(String json) {
        if (json == null) {
            return JsonValue.NULL;
        }

        try(JsonReader jsonReader = Json.createReader(new StringReader(json))) {
            return jsonReader.readValue();
        }
    }

    private static JsonArray parseArray(String json) {
        if (json == null) {
            return null;
        }

        try(JsonReader jsonReader = Json.createReader(new StringReader(json))) {
            return jsonReader.readArray();
        }
    }

    private static Consumer<JsonObject> failEquals(
            JsonStructure expected, JsonStructure actual) {

        Map<JsonPatch.Operation, Consumer<JsonObject>> failureMap =
                new HashMap<>();
        failureMap.put(JsonPatch.Operation.REPLACE,
                d -> failInvalid(d, expected, actual));
        failureMap.put(JsonPatch.Operation.REMOVE, AssertJson::failMissing);
        failureMap.put(JsonPatch.Operation.ADD, AssertJson::failUnexpected);

        return d -> failureMap.getOrDefault(
                DiffParser.getOperation(d), AssertJson::failUnknown)
                .accept(d);
    }

    private static void failInvalid(
            String path, JsonValue expected, JsonValue actual) {
        throw new AssertionFailedError(
                INVALID.message(path),
                expected,
                actual);
    }

    private static void failInvalid(
            JsonObject diff, JsonStructure expected, JsonStructure actual) {

        JsonPointer pointer = Json.createPointer(DiffParser.getPath(diff));
        failInvalid(
                DiffParser.getPath(diff),
                pointer.getValue(expected),
                pointer.getValue(actual));
    }

    private static void failNull(JsonStructure expected) {
        throw new AssertionFailedError(
                NULL.message(), expected, null);
    }

    private static void failNotNull(JsonStructure actual) {
        throw new AssertionFailedError(NOT_NULL.message(), null, actual);
    }

    private static void failUnexpected(String path) {
        throw new AssertionFailedError(UNEXPECTED.message(path));
    }

    private static void failUnexpected(JsonObject diff) {
        failUnexpected(DiffParser.getPath(diff));
    }

    private static void failMissing(String path) {
        throw new AssertionFailedError(MISSING.message(path));
    }

    private static void failMissing(JsonObject diff) {
        failMissing(DiffParser.getPath(diff));
    }

    private static void failUnknown(JsonObject diff) {
        throw new AssertionFailedError(diff.toString());
    }

    private static void failUnexpectedEqual(JsonStructure actual) {
        throw new AssertionFailedError(UNEXPECTED_EQUAL.message(actual));
    }

    private static void failNotContaining(
            JsonValue expectedElement, JsonArray actualArray) {
        throw new AssertionFailedError(
                NOT_CONTAINING.message(expectedElement, actualArray));
    }

    private static void failContaining(
            JsonValue unexpectedElement, JsonArray actualArray) {
        throw new AssertionFailedError(
                CONTAINING.message(unexpectedElement, actualArray));
    }

    private static void failNotContainingAll(
            List<JsonValue> missingElements, JsonArray actualArray) {
        throw new AssertionFailedError(
                NOT_CONTAINING_ALL.message(missingElements, actualArray));
    }

    private static void failNotContainingAny(
            JsonArray expectedElements, JsonArray actualArray) {
        throw new AssertionFailedError(
                NOT_CONTAINING_ANY.message(expectedElements, actualArray));
    }

    private static void failContainingSome(
            List<JsonValue> containingElements, JsonArray actualArray) {
        throw new AssertionFailedError(
                CONTAINING_SOME.message(containingElements, actualArray));
    }

    private static void failUnexpectedProperty(
            JsonValue unexpectedValue, String path) {
        throw new AssertionFailedError(
                UNEXPECTED_PROPERTY.message(unexpectedValue, path));
    }
}
