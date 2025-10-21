package fr.univartois.butinfo.lensymphony.notes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
public class MusicPieceTest {
    public MusicPiece piece;

    @BeforeEach
    void setUp() {
        List<Note> notes = new ArrayList<>();

        Score score1 = new Score(Instruments.XYLOPHONE,notes);
        Score score2 = new Score(Instruments.BASS_DRUM,notes);
        piece = new MusicPiece(0);
        piece.addScore(score1);
        piece.addScore(score2);
    }

    @Test
    void IteratorTest(){
        try {
            piece.iterator();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
