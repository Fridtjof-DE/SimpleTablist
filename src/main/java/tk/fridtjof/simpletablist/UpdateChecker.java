package tk.fridtjof.simpletablist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class UpdateChecker {

    public static boolean updateAvailable() {
        boolean versionAvailable = false;
        String newVersion = null;

        try {
            HttpsURLConnection connection = (HttpsURLConnection) new URL("https://www.fridtjof.tk/repo/minecraft/plugins/simpletablist/version.txt").openConnection();
            connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");

            connection.setRequestMethod("GET");
            newVersion = new BufferedReader(new InputStreamReader(connection.getInputStream())).readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(newVersion == null) {

        } else if(!SimpleTablist.VERSION.equals(newVersion)) {
            versionAvailable = true;
        } else {
            versionAvailable = false;
        }

        return versionAvailable;
    }
}