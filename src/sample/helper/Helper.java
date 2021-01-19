package sample.helper;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class Helper {
    public static String getFileExtension(File file) {
        String fileName = file.getPath();

        int index = fileName.lastIndexOf('.');
        if (index > 0) {
            String extension = fileName.substring(index + 1);
            return extension;
        }
        return "";
    }

    public static byte[] readMP3AudioFileData(final File file) throws IOException, UnsupportedAudioFileException {
        AudioInputStream mp3Stream = AudioSystem.getAudioInputStream(file);
        AudioFormat convertFormat = Helper.getPCMFormat();
        // create stream that delivers the desired format
        AudioInputStream converted = AudioSystem.getAudioInputStream(convertFormat, mp3Stream);
        // write byte data into a this.data
        return converted.readAllBytes();
    }

    public static byte[] readWAVAudioFileData(final String filePath) {
        byte[] data = null;
        try {
            final ByteArrayOutputStream baout = new ByteArrayOutputStream();
            final AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath));

            AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, baout);
            audioInputStream.close();
            baout.close();
            data = baout.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static AudioFormat getPCMFormat() {
        return new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                44100, 16, 2, 4, 44100, false);
    }

    public static Image getImage(byte[] data) {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(data);
            BufferedImage bImage = ImageIO.read(bis);
            Image songImage = SwingFXUtils.toFXImage(bImage, null);
            return songImage;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String formattedTime(int mCurrentPosition) {
        String totalOut;
        String totalNew;
        String seconds = String.valueOf(mCurrentPosition % 60);
        String minutes = String.valueOf(mCurrentPosition / 60);
        totalOut = minutes + ":" + seconds;
        totalNew = minutes + ":0" + seconds;
        if (seconds.length() == 1) {
            return totalNew;
        }
        return totalOut;
    }

    public static byte[] toByteArray(BufferedImage bi, String format)
            throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bi, format, baos);
        byte[] bytes = baos.toByteArray();
        return bytes;
    }

}
