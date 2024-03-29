# Srnjak JSON test

Java utility for writing JSON tests. It supports standard json representations from `jakarta.json` package as well as string representation or any combinations between them.

Supports testing framework [JUnit 5](https://junit.org/junit5/).

## Build
Use maven for build the source:

    mvn clean install
    
## Usage
Released version of library is available at Maven Central. 
Include it into your project's `pom.xml` as a test dependency using the following coordinates (do not forget to specify the desired version):

    <dependency>
        <groupId>com.srnjak</groupId>
        <artifactId>srnjak-testing-json</artifactId>
        <version>2.0.0</version>
        <scope>test</scope>
    </dependency>
     
Call desired method from `com.srnjak.testing.json.AssertJson` class. It supports the following asserts:
    
    assertEquals(expected, actual)
    assertNotEquals(unexpected, actual)
    assertContains(expectedElement, actualArray)
    assertNotContains(unexpectedElement, actualArray)
    assertContainsAll(expectedElements, actualArray)
    assertContainsAny(expectedElements, actualArray)
    assertContainsNone(unexpectedElements, actualArray)
    assertContainsProperty(expectedValue, path, actual)
    assertNotContainsProperty(unexpectedValue, path, actual)
    
### Test example

    @Test
    public void jsonTest() {
        
        JsonObject expected = Json.createObjectBuilder()
            .add("name", "John Doe")
            .add("age", 30)
            .build();
            
        JsonObject actual = getActualJson(); // get it from service under test
        
        AssertJson.assertEquals(expected, actual);
    }
