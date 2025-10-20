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

import java.util.Objects;

/**
 * A concrete implementation of {@link Note} representing a pitched musical note.
 * <p>
 * This note is defined by:
 * <ul>
 *   <li>a {@link NotePitch} providing its frequency (in Hz), and</li>
 *   <li>a {@link NoteValue} providing its duration with respect to a tempo (in BPM).</li>
 * </ul>
 * The class is immutable: both components are set at construction time and never change.
 * <p>
 * The frequency is delegated to {@link NotePitch#frequency()}, while the duration in
 * milliseconds for a given tempo is delegated to {@link NoteValue#duration(int)}.
 */
public final class PitchedNote implements Note {

    /**
     * The pitch (frequency provider) of this note.
     */
    private final NotePitch pitch;

    /**
     * The rhythmic value (duration provider) of this note.
     */
    private final NoteValue value;

    /**
     * Creates a new pitched note with the given pitch and value.
     *
     * @param pitch The pitch of the note (must not be {@code null}).
     * @param value The rhythmic value of the note (must not be {@code null}).
     *
     * @throws NullPointerException If either {@code pitch} or {@code value} is {@code null}.
     */
    public PitchedNote(NotePitch pitch, NoteValue value) {
        this.pitch = Objects.requireNonNull(pitch, "pitch");
        this.value = Objects.requireNonNull(value, "value");
    }

    /**
     * Returns the frequency of this note, in Hertz (Hz).
     * <p>
     * This method delegates to {@link NotePitch#frequency()}.
     *
     * @return The frequency of this note (Hz).
     */
    @Override
    public double getFrequency() {
        return pitch.frequency();
    }

    /**
     * Returns the duration of this note, in milliseconds, for the given tempo.
     * <p>
     * This method delegates to {@link NoteValue#duration(int)}.
     *
     * @param tempo The tempo in beats per minute (BPM).
     *
     * @return The duration of this note in milliseconds at the given {@code tempo}.
     */
    @Override
    public int getDuration(int tempo) {
        return value.duration(tempo);
    }

    /**
     * Gives access to this note's pitch component.
     *
     * @return The {@link NotePitch} of this note.
     */
    public NotePitch pitch() {
        return pitch;
    }

    /**
     * Gives access to this note's value component.
     *
     * @return The {@link NoteValue} of this note.
     */
    public NoteValue value() {
        return value;
    }

    /**
     * Returns a human-readable representation of this note, including its pitch and value.
     *
     * @return A string representation of this note.
     */
    @Override
    public String toString() {
        return "PitchedNote[pitch=" + pitch + ", value=" + value + "]";
    }

    /**
     * Compares this note to another object for logical equality.
     * <p>
     * Two {@code PitchedNote} instances are considered equal if and only if they
     * have the same {@link #pitch} and the same {@link #value}.
     *
     * @param o The object to compare with.
     *
     * @return {@code true} if {@code o} is a {@code PitchedNote} with identical pitch
     *         and value; {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PitchedNote that)) return false;
        return pitch.equals(that.pitch) && value.equals(that.value);
    }

    /**
     * Returns a hash code consistent with {@link #equals(Object)}, based on
     * this note's pitch and value.
     *
     * @return The hash code of this note.
     */
    @Override
    public int hashCode() {
        return 31 * pitch.hashCode() + value.hashCode();
    }

}