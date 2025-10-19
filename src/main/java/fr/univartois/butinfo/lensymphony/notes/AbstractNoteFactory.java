/**
 * LenSymphony - A simple music synthesizer library developed in Lens, France.
 * Copyright (c) 2025 Romain Wallon - Université d'Artois.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE
 * USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package fr.univartois.butinfo.lensymphony.notes;

import java.util.List;

/**
 * The AbstractNoteFactory interface defines a factory for creating musical notes.
 *
 * @author Romain Wallon
 *
 * @version 0.1.0
 */
public interface AbstractNoteFactory {

    /**
     * Creates a rest with the given value.
     *
     * @param value The value of the rest.
     *
     * @return The created rest.
     */
    Note createRest(NoteValue value);

    /**
     * Creates a note with the given pitch and value.
     *
     * @param pitch The pitch of the note.
     * @param value The value of the note.
     *
     * @return The created note.
     */
    Note createNote(NotePitch pitch, NoteValue value);

    /**
     * Creates a dotted note from the given existing note.
     *
     * @param note The existing note on which to add a dot.
     *
     * @return The created dotted note.
     */
    Note createDottedNote(Note note);

    /**
     * Creates a note representing a fermata on the given note.
     *
     * @param note The note on which to add a fermata.
     *
     * @return The created note with a fermata.
     */
    Note createFermataOn(Note note);

    /**
     * Creates a note representing the tie of the given notes.
     *
     * @param notes The notes to tie together.
     *
     * @return The created tied note.
     */
    default Note createTiedNotes(Note... notes) {
        return createTiedNotes(List.of(notes));
    }

    /**
     * Creates a note representing the tie of the given notes.
     *
     * @param notes The notes to tie together.
     *
     * @return The created tied note.
     */
    Note createTiedNotes(List<Note> notes);

}
