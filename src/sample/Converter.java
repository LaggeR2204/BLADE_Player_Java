package sample;
import java.io.File;
import java.net.URL;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

public class Converter {
    public Converter(String iPath, String oPath)
    {
        try {
            AudioFileFormat inputFileFormat = AudioSystem.getAudioFileFormat(new File(iPath));
            AudioInputStream ais = AudioSystem.getAudioInputStream(new File(iPath));

            AudioFormat audioFormat = ais.getFormat();
            System.out.println("File Format Type: " + inputFileFormat.getType());
            System.out.println("File Format String: " + inputFileFormat.toString());
            System.out.println("File lenght: " + inputFileFormat.getByteLength());
            System.out.println("Frame length: " + inputFileFormat.getFrameLength());
            System.out.println("Channels: " + audioFormat.getChannels());
            System.out.println("Encoding: " + audioFormat.getEncoding());
            System.out.println("Frame Rate: " + audioFormat.getFrameRate());
            System.out.println("Frame Size: " + audioFormat.getFrameSize());
            System.out.println("Sample Rate: " + audioFormat.getSampleRate());
            System.out.println("Sample size (bits): " + audioFormat.getSampleSizeInBits());
            System.out.println("Big endian: " + audioFormat.isBigEndian());
            System.out.println("Audio Format String: " + audioFormat.toString());

            AudioInputStream encodedASI = AudioSystem.getAudioInputStream(AudioFormat.Encoding.PCM_SIGNED, ais);

            try {
                int i = AudioSystem.write(encodedASI, AudioFileFormat.Type.WAVE, new File(oPath));
                System.out.println("Bytes Written: " + i);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
