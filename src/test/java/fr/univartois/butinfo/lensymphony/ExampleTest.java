package fr.univartois.butinfo.lensymphony;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the Example class.
 */
class ExampleTest {

    @Test
    void testConstructorThrowsException() {
        InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> {
            java.lang.reflect.Constructor<?> constructor = Example.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            constructor.newInstance();
        });

        // Verify the cause is an AssertionError
        assertInstanceOf(AssertionError.class, exception.getCause());
        assertEquals("No Example instances for you!", exception.getCause().getMessage());
    }
}