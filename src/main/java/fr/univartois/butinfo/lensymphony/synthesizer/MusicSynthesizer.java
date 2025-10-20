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

import static fr.univartois.butinfo.lensymphony.synthesizer.NoteSynthesizer.SAMPLE_RATE;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

/**
 * The MusicSynthesizer interface defines the methods required to synthesize a sequence
 * of notes.
 *
 * @author Romain Wallon
 *
 * @version 0.1.0
 */
public interface MusicSynthesizer {

    /**
     * Generates the audio samples for the sequence of notes, and stores them for later
     * playback or saving, encoded as a double array.
     */
    void synthesize();

    /**
     * Gives the synthesized audio samples as a double array.
     * If the audio stream has not been synthesized yet, this method returns an empty
     * array.
     *
     * @return The synthesized audio samples as a double array.
     */
    double[] getSamples();

    /**
     * Returns the synthesized audio data as a byte array.
     * If the audio stream has not been synthesized yet, this method returns an empty
     * array.
     *
     * @return The synthesized audio data as a byte array.
     *
     * @see #synthesize()
     */
    default byte[] getAudioData() {
        double[] samples = getSamples();
        byte[] audioData = new byte[samples.length * 2];

        for (int i = 0; i < samples.length; i++) {
            short sampleShort = (short) (samples[i] * 32767);
            audioData[i * 2] = (byte) (sampleShort & 0xFF);
            audioData[i * 2 + 1] = (byte) ((sampleShort >> 8) & 0xFF);
        }
        return audioData;
    }

    /**
     * Plays the synthesized audio stream.
     * If the audio stream has not been synthesized yet, this method will not play
     * anything.
     *
     * @throws LineUnavailableException If an audio line cannot be opened.
     *
     * @see #synthesize()
     */
    default void play() throws LineUnavailableException {
        AudioFormat format = new AudioFormat(SAMPLE_RATE, Short.SIZE, 1, true, false);
        try (SourceDataLine line = AudioSystem.getSourceDataLine(format)) {
            line.open(format);
            line.start();
            byte[] audioData = getAudioData();
            line.write(audioData, 0, audioData.length);
            line.drain();
        }
    }

    /**
     * Saves the synthesized audio stream to a WAV file.
     * If the audio stream has not been synthesized yet, the resulting file will be empty.
     *
     * @param filename The name of the WAV file to save the audio stream to.
     *
     * @throws IOException If an I/O error occurs while writing the file.
     *
     * @see #synthesize()
     */
    default void save(String filename) throws IOException {
        AudioFormat format = new AudioFormat(SAMPLE_RATE, Short.SIZE, 1, true, false);
        byte[] audioData = getAudioData();
        ByteArrayInputStream bais = new ByteArrayInputStream(audioData);
        AudioInputStream ais = new AudioInputStream(bais, format, audioData.length);
        AudioSystem.write(ais, AudioFileFormat.Type.WAVE, new File(filename));
    }

}
