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

import java.io.ByteArrayOutputStream;

import fr.univartois.butinfo.lensymphony.notes.Note;

/**
 * The SimpleMusicSynthesizer allows to synthesize a sequence of notes into an audio
 * stream.
 * The audio stream can be played or saved to a WAV file.
 *
 * @author Romain Wallon
 *
 * @version 0.1.0
 */
public final class SimpleMusicSynthesizer implements IMusicSynthesizer {

    /**
     * The default volume level for the notes.
     */
    private static final double DEFAULT_VOLUME = 0.5;

    /**
     * The tempo of the music in beats per minute (BPM).
     */
    private int tempo;

    /**
     * The sequence of notes to play in the audio stream.
     */
    private Iterable<Note> notes;

    /**
     * The note synthesizer used to generate the audio samples for each note.
     */
    private NoteSynthesizer synthesizer;

    /**
     * The byte array output stream where the audio data is stored before being played or
     * saved.
     */
    private ByteArrayOutputStream byteArray;

    /**
     * Creates a new MusicSynthesizer.
     *
     * @param tempo The tempo of the music in beats per minute (BPM).
     * @param notes The notes to play in the audio stream.
     * @param synthetizer The note synthesizer used to generate the audio samples.
     */
    public SimpleMusicSynthesizer(int tempo, Iterable<Note> notes, NoteSynthesizer synthetizer) {
        this.tempo = tempo;
        this.notes = notes;
        this.synthesizer = synthetizer;
        this.byteArray = new ByteArrayOutputStream();
    }

    /*
     * (non-Javadoc)
     *
     * @see fr.univartois.butinfo.lensymphony.synthesizer.IMusicSynthesizer#synthesize()
     */
    @Override
    public void synthesize() {
        for (Note note : notes) {
            double[] samples = synthesizer.synthesize(note, tempo, DEFAULT_VOLUME);
            for (double sample : samples) {
                short sampleShort = (short) (sample * 32767);
                byteArray.write(sampleShort & 0xFF);
                byteArray.write((sampleShort >> 8) & 0xFF);
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see fr.univartois.butinfo.lensymphony.synthesizer.IMusicSynthesizer#getAudioData()
     */
    @Override
    public byte[] getAudioData() {
        return byteArray.toByteArray();
    }

}
