package chess;

import constant.ChessType;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * 棋子工厂
 *
 * @author zjx
 * @since 2021/6/9 4:13 下午
 */
public class ChessFactory {

    public static Chess getChess(int i, int x, int y) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class clazz = Class.forName(ChessType.chessType[i]);
        Constructor constructor = clazz.getConstructor(int.class, int.class, int.class);
        return (Chess) constructor.newInstance(i, x, y);
    }
}
