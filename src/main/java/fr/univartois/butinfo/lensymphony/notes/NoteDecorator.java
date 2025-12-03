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
 * An abstract decorator for {@link Note} that wraps another note and delegates
 * all operations to it by default.
 * <p>
 * Subclasses can override specific methods to add or modify behavior.
 *
 * @author Rabhi Nessim
 *
 */
public abstract class NoteDecorator implements Note {


    protected final Note note;

    /**
     * Creates a new note decorator that wraps the given note.
     *
     * @param note The note to decorate (must not be {@code null}).
     *
     * @throws NullPointerException If {@code note} is {@code null}.
     */
    protected NoteDecorator(Note note) {
        if (note == null) {
            throw new NullPointerException("Note is null"); //if note is null throw exception
        }

        this.note = note;
    }

    /**
     * Returns the frequency of the decorated note, in Hertz (Hz).
     * <p>
     * This default implementation delegates to the wrapped note.
     *
     * @return The frequency of the decorated note (Hz).
     */
    @Override
    public double getFrequency() {
        return note.getFrequency();
    }

    /**
     * Returns the duration of the decorated note, in milliseconds, for the given tempo.
     * <p>
     * This default implementation delegates to the wrapped note.
     *
     * @param tempo The tempo in beats per minute (BPM).
     *
     * @return The duration of the decorated note in milliseconds.
     */
    @Override
    public int getDuration(int tempo) {
        return note.getDuration(tempo);
    }
}
