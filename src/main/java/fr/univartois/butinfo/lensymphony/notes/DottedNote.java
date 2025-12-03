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

/**
 * A decorator for {@link Note} that represents a dotted note.
 * <p>
 * A dotted note has its duration increased by 50% (multiplied by 1.5).
 *
 * @author Rabhi Nessim
 *
 * @version 0.1.0
 */
public class DottedNote extends NoteDecorator {

    /**
     * Creates a new dotted note that decorates the given note.
     *
     * @param note The note to decorate (must not be {@code null}).
     */
    public DottedNote(Note note) {
        super(note);
    }

    /**
     * Returns the duration of the dotted note, in milliseconds, for the given tempo.
     * <p>
     * A dotted note's duration is 1.5 times the original note.
     *
     * @param tempo The tempo in beats per minute (BPM).
     *
     * @return The duration in milliseconds.
     */
    @Override
    public int getDuration(int tempo) {
        return (int) (super.getDuration(tempo) * 1.5); // Dotted note duration is 1.5 times the original note duration
    }
}