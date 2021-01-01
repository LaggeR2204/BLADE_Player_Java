package sample.helper;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Helper {
    public static AudioInputStream getStreamFromFile(String path) throws IOException, UnsupportedAudioFileException {
        File file = new File(path);
        AudioInputStream in= AudioSystem.getAudioInputStream(file);
        AudioInputStream din = null;
        AudioFormat baseFormat = in.getFormat();
        AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                baseFormat.getSampleRate(),
                16,
                baseFormat.getChannels(),
                baseFormat.getChannels() * 2,
                baseFormat.getSampleRate(),
                false);
        din = AudioSystem.getAudioInputStream(decodedFormat, in);
        return din;
    }
}
