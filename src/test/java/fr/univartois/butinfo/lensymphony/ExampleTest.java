package fr.univartois.butinfo.lensymphony;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Example class.
 */
class ExampleTest {

    @Test
    void testConstructorThrowsAssertionError() {
        // This test covers the private constructor's assertion error
        InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> {
            java.lang.reflect.Constructor<?> constructor = Example.class.getDeclaredConstructor();
            constructor.setAccessible(true); // Allow access to private constructor
            constructor.newInstance();
        });

        // Check that the *cause* of the reflection exception is the expected error
        assertInstanceOf(AssertionError.class, exception.getCause());
        assertEquals("No Example instances for you!", exception.getCause().getMessage());
    }

    // Note: We don't test main() for Example.java because it's just an example
    // that tries to play sound, which is bad for unit tests.
}