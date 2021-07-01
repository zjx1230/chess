package audio;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * 关于音频
 *
 * @author zjx
 * @since 2021/6/16 下午7:43
 */
public class AudioUtil {

    public static void playLuoZi() {
        try {
            AudioStream audioStream = new AudioStream(AudioUtil.class.getClassLoader().getResourceAsStream("audios/luozi.wav"));
            AudioPlayer.player.start(audioStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void playEat() {
        try {
            AudioStream audioStream = new AudioStream(AudioUtil.class.getClassLoader().getResourceAsStream("audios/eat.wav"));
            AudioPlayer.player.start(audioStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void playKill() {
        try {
            AudioStream audioStream = new AudioStream(AudioUtil.class.getClassLoader().getResourceAsStream("audios/kill.wav"));
            AudioPlayer.player.start(audioStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new AudioUtil();
    }
}
