package constant;

import java.util.HashMap;
import java.util.Map;

/**
 * ChessType 棋子类型
 *
 * @author zjx
 * @since 2021/6/8 11:14 下午
 */
public class ChessType {

    public static final String[] chessType =  {
            "chess.General",
            "chess.Official",
            "chess.Official",
            "chess.Elephant",
            "chess.Elephant",
            "chess.Horse",
            "chess.Horse",
            "chess.Chariot",
            "chess.Chariot",
            "chess.Cannon",
            "chess.Cannon",
            "chess.Soldier",
            "chess.Soldier",
            "chess.Soldier",
            "chess.Soldier",
            "chess.Soldier",
            "chess.General",
            "chess.Official",
            "chess.Official",
            "chess.Elephant",
            "chess.Elephant",
            "chess.Horse",
            "chess.Horse",
            "chess.Chariot",
            "chess.Chariot",
            "chess.Cannon",
            "chess.Cannon",
            "chess.Soldier",
            "chess.Soldier",
            "chess.Soldier",
            "chess.Soldier",
            "chess.Soldier"
    };

    public static final Map<String, String> chessMap = new HashMap<>();

    static {
        chessMap.put("chess.General0", "images/chess0.png");
        chessMap.put("chess.Official0", "images/chess1.png");
        chessMap.put("chess.Elephant0", "images/chess2.png");
        chessMap.put("chess.Horse0", "images/chess3.png");
        chessMap.put("chess.Chariot0", "images/chess4.png");
        chessMap.put("chess.Cannon0", "images/chess5.png");
        chessMap.put("chess.Soldier0", "images/chess6.png");
        chessMap.put("chess.General1", "images/chess7.png");
        chessMap.put("chess.Official1", "images/chess8.png");
        chessMap.put("chess.Elephant1", "images/chess9.png");
        chessMap.put("chess.Horse1", "images/chess10.png");
        chessMap.put("chess.Chariot1", "images/chess11.png");
        chessMap.put("chess.Cannon1", "images/chess12.png");
        chessMap.put("chess.Soldier1", "images/chess13.png");
    }
}
