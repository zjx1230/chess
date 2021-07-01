package dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * 传输的消息命令
 *
 * @author zjx
 * @since 2021/6/11 5:20 下午
 */
@Data
public class Command {

    private Integer userId;

    private Integer enemyId;

    private String commandType;

    private int oldX, oldY;

    private int newX, newY;

    private String enemyName;

    private String message;

    private short player;
}
