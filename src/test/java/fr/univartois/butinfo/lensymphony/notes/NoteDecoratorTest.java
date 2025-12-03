package fr.univartois.butinfo.lensymphony.notes;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the abstract NoteDecorator class.
 * We test its logic (like the constructor's null check)
 * through a concrete child, DottedNote.
 */
class NoteDecoratorTest {

    @Test
    void testDecoratorConstructorNullCheck() {
        // This test validates the branch: if (note == null) in NoteDecorator
        Exception exception = assertThrows(NullPointerException.class, () -> {
            new DottedNote(null); // Or new FermataNote(null)
        });

        String expectedMessage = "Note is null";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage, "Decorator constructor did not throw for null note.");
    }
}