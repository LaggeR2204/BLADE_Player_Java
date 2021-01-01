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
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChartOnline {
    public List<Song> crawlVietNamChart(){
        return crawlChart("vietnam");
    }

    public List<Song> crawlUSUKChart(){
        return crawlChart("us-uk");
    }

    public List<Song> crawlKoreaChart(){
        return crawlChart("korea");
    }

    private List<Song> crawlChart(String nation){
        List<Song> listSong = new ArrayList<Song>();

        Pattern pattern = Pattern.compile("<div class=\"d-flex justify-content-between mb-3 box1 music-listen-title(.*?)</div", Pattern.DOTALL);
        Pattern patternChartSongName = Pattern.compile("class=\"title\">(.*?)-", Pattern.DOTALL);
        Pattern patternChartSinger = Pattern.compile("- (.*?)</h1>", Pattern.DOTALL);
        Pattern patternChartSongImageDetail = Pattern.compile("<div id=\"companion_cover(.*?)</div>", Pattern.DOTALL);
        Pattern patternChartSongImageURL = Pattern.compile("img src=\"(.*?)\" alt=", Pattern.DOTALL);
        Pattern patternChartSongTime = Pattern.compile("headset</i>(.*?)&", Pattern.DOTALL);
        Matcher matcher;

        for (int i=1; i<=20; i++){
            Song newSongChart = new Song();
            newSongChart.setSongURL("https://chiasenhac.vn/nhac-hot/" + nation + ".html?playlist=" + i);

            String htmlFullSong = FindFromURL(newSongChart.getSongURL());
            matcher = pattern.matcher(htmlFullSong);
            String htmlSongInfo = "";
            if (matcher.find()){
                htmlSongInfo = matcher.group(1).trim();
            }
            matcher = patternChartSongName.matcher(htmlSongInfo);
            if (matcher.find()){
                newSongChart.setSongName(matcher.group(1).trim());
            }
            matcher = patternChartSinger.matcher(htmlSongInfo);
            if (matcher.find()){
                newSongChart.setSinger(matcher.group(1).trim());
            }
            matcher = patternChartSongTime.matcher(htmlSongInfo);
            if (matcher.find()){
                newSongChart.setSongNumber(Integer.parseInt(matcher.group(1).replace(",", "").trim()));
            }
            matcher = patternChartSongImageDetail.matcher(htmlFullSong);
            if (matcher.find()){
                String htmlImageInfo = matcher.group(1);
                matcher = patternChartSongImageURL.matcher(htmlImageInfo);
                if (matcher.find()){
                    if (!matcher.group(1).trim().equals("https://chiasenhac.vn/imgs/no_cover.jpg")){
                        String imageURL = matcher.group(1).trim();
                        try {
                            BufferedImage bufferedImage = ImageIO.read(new URL(imageURL));
                            Image image = SwingFXUtils.toFXImage(bufferedImage, null);
                            if (!image.isError()) {
                                newSongChart.setSongImage(image);
                            }

                        } catch (IOException e) {
                            //e.printStackTrace();
                            newSongChart.setSongImage(null);
                        }
                    }
                    else{
                        newSongChart.setSongImage(null);
                    }
                }

            }

            listSong.add(newSongChart);
        }

        return listSong;
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
