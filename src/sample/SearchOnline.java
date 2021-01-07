package sample;

import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import sample.Model.Song;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
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
        Pattern patternSearchSongImageURL = Pattern.compile("img src=\"(.*?)\" alt", Pattern.DOTALL);
        Pattern patternSearchSongTime = Pattern.compile("<small class=\"time_stt\">(.*?)</small>", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(html);

        List<String> listSongHTML = new ArrayList<String>();

        while(matcher.find()){
            listSongHTML.add(matcher.group(1));
        }

        for (String item : listSongHTML) {
            Song newSongSearch = new Song();

            matcher = patternSearchSongTime.matcher(item);
            if (matcher.find()){
                int songNumber = Integer.parseInt(matcher.group(1).replace(",", "").trim());
                newSongSearch.setSongNumber(songNumber);
            }

            if (newSongSearch.getSongNumber() >= 0){
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
                matcher = patternSearchSongImageURL.matcher(item);
                if (matcher.find()){
                    if (!matcher.group(1).trim().equals("https://chiasenhac.vn/imgs/no_cover.jpg")){
                        String imageURL = matcher.group(1).trim();
                        try {
                            BufferedImage bufferedImage = ImageIO.read(new URL(imageURL));
                            Image image = SwingFXUtils.toFXImage(bufferedImage, null);
                            if (!image.isError()) {
                                newSongSearch.setSongImage(image);
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                    else
                        newSongSearch.setSongImage(null);
                }
                listSongSearch.add(newSongSearch);
            }

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