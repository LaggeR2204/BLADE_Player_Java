package sample;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.sound.sampled.*;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.ID3v2;

public class Editor {
    public Editor()
    { }
    public static File mp3ToWav(File mp3Data, String output) throws UnsupportedAudioFileException, IOException {
        File wavFile = new File(output);
        AudioInputStream mp3Stream = AudioSystem.getAudioInputStream(mp3Data);
        AudioFormat sourceFormat = mp3Stream.getFormat();
        AudioFormat convertFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                sourceFormat.getSampleRate(), 16,
                sourceFormat.getChannels(),
                sourceFormat.getChannels() * 2,
                sourceFormat.getSampleRate(),
                false);
        // create stream that delivers the desired format
        AudioInputStream converted = AudioSystem.getAudioInputStream(convertFormat, mp3Stream);
        // write stream into a file with file format wav
            AudioSystem.write(converted, AudioFileFormat.Type.WAVE, wavFile);
            return wavFile;
    }

    public static void copyAudio(File inputFile, String outputFileName, int startSecond, int secondsToCopy) {
        AudioInputStream inputStream = null;
        AudioInputStream shortenedStream = null;
        try {
            AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(inputFile);
            AudioFormat format = fileFormat.getFormat();
            inputStream = AudioSystem.getAudioInputStream(inputFile);
            int bytesPerSecond = format.getFrameSize() * (int)format.getFrameRate();
            inputStream.skip(startSecond * bytesPerSecond);
            long framesOfAudioToCopy = secondsToCopy * (int)format.getFrameRate();
            shortenedStream = new AudioInputStream(inputStream, format, framesOfAudioToCopy);
            File destinationFile = new File(outputFileName);
            AudioSystem.write(shortenedStream, fileFormat.getType(), destinationFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
