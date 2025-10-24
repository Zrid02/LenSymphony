package fr.univartois.butinfo.lensymphony;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the LenSymphony main class.
 */
class LenSymphonyTest {

    @Test
    void testConstructorThrowsAssertionError() {
        // This test covers the private constructor's assertion error
        InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> {
            java.lang.reflect.Constructor<?> constructor = LenSymphony.class.getDeclaredConstructor();
            constructor.setAccessible(true); // Allow access to private constructor
            constructor.newInstance();
        });

        // Check that the *cause* of the reflection exception is the expected error
        assertInstanceOf(AssertionError.class, exception.getCause());
        assertEquals("No LenSymphony instances for you!", exception.getCause().getMessage());
    }

    @Test
    void testMainThrowsWhenMissingMusicXMLArgument() {
        // This test covers the branch: if (args.length != 1)
        // LenSymphony.main executes CommandLine, which sets isPlay() to true
        // and then throws IllegalArgumentException if no args are present.
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            LenSymphony.main(new String[0]); // Pass an empty array
        });
        assertEquals("MusicXML file is required as single argument", ex.getMessage());
    }
}