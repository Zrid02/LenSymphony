package fr.univartois.butinfo.lensymphony.notes;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the MusicPiece class.
 */
class MusicPieceTest {

    @Test
    void testMusicPiece() {
        MusicPiece piece = new MusicPiece(120);
        assertEquals(120, piece.getTempo());
        assertNotNull(piece.getScores());

        // Stub a score to test addScore and iterator
        Score score = new Score(Instruments.PIANO, List.of());
        piece.addScore(score);

        assertEquals(1, piece.getScores().size());
        assertSame(score, piece.getScores().get(0));

        int count = 0;
        for (Score s : piece) {
            count++;
            assertSame(score, s);
        }
        assertEquals(1, count, "Iterator should loop over added scores.");
    }
}