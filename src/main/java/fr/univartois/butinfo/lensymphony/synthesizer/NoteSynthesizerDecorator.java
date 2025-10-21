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

package fr.univartois.butinfo.lensymphony.synthesizer;

import fr.univartois.butinfo.lensymphony.notes.Note;

/**
 * An abstract base decorator for {@link NoteSynthesizer} that wraps another
 * synthesizer and delegates all operations to it by default.
 * <p>
 * Subclasses can override specific methods to add or modify synthesis behavior.
 * This class follows the Decorator design pattern.
 *
 * @author Rabhi Nessim
 *
 * @version 0.1.0
 */
public abstract class NoteSynthesizerDecorator implements NoteSynthesizer {

    /**
     * The decorated (wrapped) synthesizer to which operations are delegated.
     */
    protected final NoteSynthesizer synthesizer;

    /**
     * Creates a new synthesizer decorator that wraps the given synthesizer.
     *
     * @param synthesizer The synthesizer to decorate (must not be {@code null}).
     *
     * @throws NullPointerException If {@code synthesizer} is {@code null}.
     */
    protected NoteSynthesizerDecorator(NoteSynthesizer synthesizer) {
        if (synthesizer == null) {
            throw new NullPointerException("synthesizer");
        }
        this.synthesizer = synthesizer;
    }

    /**
     * Synthesizes the given note by delegating to the wrapped synthesizer.
     * <p>
     * This default implementation delegates to the wrapped synthesizer.
     * Subclasses may override to modify the synthesis process.
     *
     * @param note   The note to synthesize.
     * @param tempo  The tempo in beats per minute (BPM).
     * @param volume The volume level for the note (0.0 to 1.0).
     *
     * @return An array of audio samples representing the synthesized note.
     */
    @Override
    public double[] synthesize(Note note, int tempo, double volume) {
        return synthesizer.synthesize(note, tempo, volume);
    }
}