package Model;

import Util.Bet;
import Util.Match;
import javafx.scene.control.Button;
import org.json.JSONArray;
import org.json.JSONObject;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import static Model.HibernateUtil.getSessionFactory;

public class AppModel {

    private static final String API_KEY = "d4a9110b90c6415bb3d252836a4bf034";

    public static List <Match> matchesList = new ArrayList<Match>();


    public static void getFixtures(String leagueID) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://api.football-data.org/v2/competitions/" + leagueID + "/matches?status=SCHEDULED")).header("X-Auth-Token", API_KEY).build();

        try {
            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenApply(AppModel::parse)
                    .thenAccept(System.out::println)
                    .join();

        } catch (Exception e) {
            System.out.println("Exceeded amount of api calls (10 per minute). Try again later");
            throw e;
        }

    }


    public static List<Match> parse(String response){

        JSONObject fixtures = new JSONObject(response);
        System.out.println(fixtures);
        JSONArray matches = fixtures.getJSONArray("matches");
        System.out.println(matches);
        JSONObject score = (JSONObject) matches.get(1);
        System.out.println(score);
        JSONObject firstScheduledMatch = (JSONObject) matches.get(0);
        int currentMatchday = (int)firstScheduledMatch.get("matchday");

        matchesList.removeAll(matchesList);

        for(int i=0; i<matches.length(); i++){
            JSONObject match = (JSONObject) matches.get(i);
            if((int)match.get("matchday") == currentMatchday){
                String homeTeam = (String) match.getJSONObject("homeTeam").get("name");
                String awayTeam = (String) match.getJSONObject("awayTeam").get("name");
                int matchID = (int) match.get("id");
                int matchday = (int) match.get("matchday");
                Button button1 = new Button("1");
                Button buttonx = new Button("X");
                Button button2 = new Button("2");
                matchesList.add(new Match(matchID, matchday, homeTeam, awayTeam, button1, buttonx, button2));

            }

        }
        return matchesList;
    }



    public List<Match> getmatchesList(){
        return matchesList;
    }


}
