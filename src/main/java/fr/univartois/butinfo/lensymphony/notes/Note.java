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
 * The Note interface defines the contract for musical notes, including methods to get
 * their frequency and duration (w.r.t. a given tempo).
 *
 * @author Romain Wallon
 *
 * @version 0.1.0
 */
public interface Note {

    /**
     * Gives the frequency of this note, as a frequency in Hertz (Hz).
     *
     * @return The frequency of this note.
     */
    double getFrequency();

    /**
     * Gives the duration of this note in milliseconds, given a tempo in beats per
     * minute (BPM).
     *
     * @param tempo The tempo in beats per minute (BPM).
     *
     * @return The duration of this note, in milliseconds.
     */
    int getDuration(int tempo);

}
