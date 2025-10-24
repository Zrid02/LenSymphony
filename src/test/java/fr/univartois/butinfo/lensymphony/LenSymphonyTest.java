package fr.univartois.butinfo.lensymphony;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour la classe LenSymphony.
 */
class LenSymphonyTest {

    @Test
    void testMainWithNoArguments() {
        String[] args = {};
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            LenSymphony.main(args);
        });
        assertEquals("MusicXML file is required as single argument", exception.getMessage());
    }

    @Test
    void testMainWithMultipleArguments() {
        String[] args = {"file1.xml", "file2.xml"};
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            LenSymphony.main(args);
        });
        assertEquals("MusicXML file is required as single argument", exception.getMessage());
    }

    @Test
    void testConstructorThrowsException() {
        InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> {
            java.lang.reflect.Constructor<?> constructor = LenSymphony.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            constructor.newInstance();
        });

        // Verify the cause is an AssertionError
        assertInstanceOf(AssertionError.class, exception.getCause());
        assertEquals("No LenSymphony instances for you!", exception.getCause().getMessage());
    }
}