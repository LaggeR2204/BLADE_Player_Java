package sample;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.sound.sampled.*;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.ID3v2;

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
    public static byte [] getAudioDataBytes(byte [] sourceBytes, AudioFormat audioFormat) throws UnsupportedAudioFileException, IllegalArgumentException, Exception{
        if(sourceBytes == null || sourceBytes.length == 0 || audioFormat == null){
            throw new IllegalArgumentException("Illegal Argument passed to this method");
        }

        ByteArrayInputStream bais = null;
        ByteArrayOutputStream baos = null;
        AudioInputStream sourceAIS = null;
        AudioInputStream convert1AIS = null;
        AudioInputStream convert2AIS = null;

        try{
            bais = new ByteArrayInputStream(sourceBytes);
            sourceAIS = AudioSystem.getAudioInputStream(bais);
            AudioFormat sourceFormat = sourceAIS.getFormat();
            AudioFormat convertFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, sourceFormat.getSampleRate(), 16, sourceFormat.getChannels(), sourceFormat.getChannels()*2, sourceFormat.getSampleRate(), false);
            convert1AIS = AudioSystem.getAudioInputStream(convertFormat, sourceAIS);
            convert2AIS = AudioSystem.getAudioInputStream(audioFormat, convert1AIS);

            baos = new ByteArrayOutputStream();

            byte [] buffer = new byte[8192];
            while(true){
                int readCount = convert2AIS.read(buffer, 0, buffer.length);
                if(readCount == -1){
                    break;
                }
                baos.write(buffer, 0, readCount);
            }
            return baos.toByteArray();
        } catch(UnsupportedAudioFileException uafe){
            //uafe.printStackTrace();
            throw uafe;
        } catch(IOException ioe){
            //ioe.printStackTrace();
            throw ioe;
        } catch(IllegalArgumentException iae){
            //iae.printStackTrace();
            throw iae;
        } catch (Exception e) {
            //e.printStackTrace();
            throw e;
        }finally{
            if(baos != null){
                try{
                    baos.close();
                }catch(Exception e){
                }
            }
            if(convert2AIS != null){
                try{
                    convert2AIS.close();
                }catch(Exception e){
                }
            }
            if(convert1AIS != null){
                try{
                    convert1AIS.close();
                }catch(Exception e){
                }
            }
            if(sourceAIS != null){
                try{
                    sourceAIS.close();
                }catch(Exception e){
                }
            }
            if(bais != null){
                try{
                    bais.close();
                }catch(Exception e){
                }
            }
        }
    }

    public static void mp3ToWav(File mp3Data, String output) throws UnsupportedAudioFileException, IOException {
        // open stream
        try {
            Mp3File mp3File = new Mp3File(mp3Data);
        } catch (Exception e)
        {
        }

        AudioInputStream mp3Stream = AudioSystem.getAudioInputStream(mp3Data);
        AudioFormat sourceFormat = mp3Stream.getFormat();
        // create audio format object for the desired stream/audio format
        // this is *not* the same as the file format (wav)
      //  getAudioDataBytes(sourceFormat.getSampleSizeInBits(), sourceFormat);
        AudioFormat convertFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                sourceFormat.getSampleRate(), 16,
                sourceFormat.getChannels(),
                sourceFormat.getChannels() * 2,
                sourceFormat.getSampleRate(),
                false);
        // create stream that delivers the desired format
        AudioInputStream converted = AudioSystem.getAudioInputStream(convertFormat, mp3Stream);
        // write stream into a file with file format wav
            AudioSystem.write(converted, AudioFileFormat.Type.WAVE, new File(output));

    }

}
