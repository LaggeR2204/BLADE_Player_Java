package sample;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchOnline{

    public List<Song> search(String textSearch){
        try{
            String path = "https://chiasenhac.vn/tim-kiem?q=" + textSearch.replace(" ", "+").trim();
            return crawl(path);
        }
        catch (Exception e){
            System.out.println(e.toString());
            return new ArrayList<Song>();
        }
    }

    private List<Song> crawl(String searchURL){
        List<Song> listSongSearch = new ArrayList<Song>();
        String html = FindFromURL(searchURL);

        Pattern pattern = Pattern.compile("<div class=\"media-left(.*?)</li>", Pattern.DOTALL);
        Pattern patternSearchSongName = Pattern.compile("title=\"(.*?)\">", Pattern.DOTALL);
        Pattern patternSearchSongURL = Pattern.compile("href=\"(.*?)\"", Pattern.DOTALL);
        Pattern patternSearchSinger = Pattern.compile("author\">(.*?)</div>", Pattern.DOTALL);
        Pattern patternSearchSongTime = Pattern.compile("<small class=\"time_stt\">(.*?)</small>", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(html);

        List<String> listSongHTML = new ArrayList<String>();

        while(matcher.find()){
            listSongHTML.add(matcher.group(1));
        }

        for (String item : listSongHTML) {
            Song newSongSearch = new Song();
            matcher = patternSearchSongName.matcher(item);
            if (matcher.find()){
                newSongSearch.setSongName(matcher.group(1));
            }
            matcher = patternSearchSongURL.matcher(item);
            if (matcher.find()){
                newSongSearch.setSongURL(matcher.group(1));
            }
            matcher = patternSearchSinger.matcher(item);
            if (matcher.find()){
                newSongSearch.setSinger(matcher.group(1).trim());
            }
            matcher = patternSearchSongTime.matcher(item);
            if (matcher.find()){
                int songNumber = Integer.parseInt(matcher.group(1).replace(",", "").trim());
                newSongSearch.setSongNumber(songNumber);
            }
            listSongSearch.add(newSongSearch);
        }

        return listSongSearch;
    }


    private String FindFromURL(String URL){
        String html = "";

        try {
            Document doc = Jsoup.connect(URL).ignoreHttpErrors(true)
                    .get();
            html = doc.html();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return html;
    }

    public void downloadSong(Song song, Stage stage){
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("MP3 files (*.mp3)", "*.mp3");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setInitialFileName(song.getSongName().replace(" ", "_"));

        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            Task<Void> downloadTask = new DownloadTask(song, file);

            Thread thread = new Thread(downloadTask);
            thread.setDaemon(true);
            thread.start();
        }
    }
}

class DownloadTask extends Task<Void>{
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
        System.out.println("Downloaded");
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