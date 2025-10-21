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

package fr.univartois.butinfo.lensymphony;

import java.io.File;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import fr.univartois.butinfo.lensymphony.musicxml.MusicXMLSaxParser;
import fr.univartois.butinfo.lensymphony.notes.*;
import fr.univartois.butinfo.lensymphony.synthesizer.MusicSynthesizer;
import fr.univartois.butinfo.lensymphony.synthesizer.NoteSynthesizer;
import fr.univartois.butinfo.lensymphony.synthesizer.PureSound;
import fr.univartois.butinfo.lensymphony.synthesizer.SimpleMusicSynthesizer;

/**
 * The LenSymphony class provides a simple application to synthesize and play music from a
 * MusicXML file.
 * This file must be provided as a command line argument to the application.
 *
 * @author Romain Wallon
 *
 * @version 0.1.0
 */
public final class LenSymphony {

    /**
     * The note factory used to create notes.
     * TODO: You have to set it with your own implementation.
     */
    private static AbstractNoteFactory noteFactory = NoteFactory.getInstance();

    /**
     * The note synthesizer used to synthesize notes.
     * TODO: You have to set it with your own implementation.
     */
    private static NoteSynthesizer noteSynthesizer = new PureSound();

    /**
     * Disables instantiation.
     */
    private LenSymphony() {
        throw new AssertionError("No LenSymphony instances for you!");
    }

    /**
     * The main method of the application.
     *
     * @param args The command line arguments, which must contain exactly the path to the
     *        MusicXML file to play.
     *
     * @throws Exception If any error occurs.
     */
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            // The command line is invalid.
            throw new IllegalArgumentException("MusicXML file is required as single argument");
        }

        // Creating the SAX parser.
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();

        // Parsing the MusicXML file.
        MusicXMLSaxParser handler = new MusicXMLSaxParser(noteFactory);
        saxParser.parse(new File(args[0]), handler);

        // Creating a musical score from the parsed data.
        Score score = new Score(Instruments.XYLOPHONE,handler.getNotes());

        // Synthesizing and playing the music.
        MusicPiece musicPiece = new MusicPiece(handler.getTempo());
        musicPiece.addScore(score);
        MusicSynthesizer musicSynthetizer = new SimpleMusicSynthesizer(musicPiece.getTempo(), handler.getNotes(), noteSynthesizer);
        musicSynthetizer.synthesize();
        musicSynthetizer.play();
    }

}
