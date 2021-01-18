package sample;

import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import sample.Model.Song;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DownloadTask extends Task<Void> {
    public Song DowloadSong;
    public File DownloadFile;

    public DownloadTask(Song downloadSong, File file){
        this.DowloadSong = downloadSong;
        this.DownloadFile = file;
    }

    @Override
    protected void failed(){
        System.out.println("Download fail");
    }
    @Override
    protected void succeeded(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Notification");
        alert.setHeaderText(null);
        alert.setContentText("Download Completed !!!");

        alert.showAndWait();
    }

    @Override
    protected Void call(){

        String DownloadURL = "";
        String htmlSong = "";

        try {
            Document doc = Jsoup.connect(DowloadSong.getSongURL()).ignoreHttpErrors(true)
                    .get();
            htmlSong = doc.html();

        } catch (IOException e) {
            e.printStackTrace();
        }
        Pattern patternHTMLDownloadURL = Pattern.compile("<a class=\"download_item\" href=(.*?)128kbps</span>(.*?)</a>", Pattern.DOTALL);
        Pattern patternDownloadURL = Pattern.compile("href=\"(.*?)\" title", Pattern.DOTALL);

        Matcher matcher = patternHTMLDownloadURL.matcher(htmlSong);
        if (matcher.find()){
            matcher = patternDownloadURL.matcher(matcher.group(1));
            if (matcher.find()){
                DownloadURL = matcher.group(1);
            }
        }

        try {
            URLConnection conn = new URL(DownloadURL).openConnection();
            InputStream is = conn.getInputStream();

            OutputStream outstream = new FileOutputStream(new File(DownloadFile.getAbsolutePath()));
            byte[] buffer = new byte[4096];
            int len;
            while ((len = is.read(buffer)) > 0) {
                outstream.write(buffer, 0, len);
            }
            outstream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
