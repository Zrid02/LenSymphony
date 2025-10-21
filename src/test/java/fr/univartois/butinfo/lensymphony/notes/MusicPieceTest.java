package fr.univartois.butinfo.lensymphony.notes;
import fr.univartois.butinfo.lensymphony.synthesizer.Stave;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
public class MusicPieceTest {
    public MusicPiece piece;

    @BeforeEach
    void setUp() {
        Stave stave1 = new Stave();
        Stave stave2 = new Stave();
        piece = new MusicPiece();
        piece.addStave(stave1);
        piece.addStave(stave2);
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
