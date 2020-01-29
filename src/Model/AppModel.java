package Model;

import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class AppModel {

    private static final String API_KEY = "d4a9110b90c6415bb3d252836a4bf034";

    public static List <Match> matchesList = new ArrayList<Match>();
    public static List<Bet> betsList = new ArrayList<Bet>();

    Configuration cfg = new Configuration().configure("hibernate.cfg.xml");
    SessionFactory sf = cfg.buildSessionFactory();



    public static void getFixtures(String leagueID) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://api.football-data.org/v2/competitions/" + leagueID + "/matches?status=SCHEDULED")).header("X-Auth-Token", API_KEY).build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(AppModel::parse)
                .thenAccept(System.out::println)
                .join();

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

    public List<Bet> getData(){

        Session session = sf.openSession();
        Query qry = session.createQuery("from Bet b");

        betsList = qry.list();

        ChoiceBox changePrediction = new ChoiceBox();
        changePrediction.getItems().addAll("1", "X", "2");

        for(int i=0; i<betsList.size(); i++){
           betsList.get(i).setButtonEdit(new Button("Edit"));
            betsList.get(i).setButtonRemove(new Button("Delete"));
            betsList.get(i).setChangePreditcion(new ChoiceBox());
        }

        return betsList;
    }


    public void placeBet(String prediction, int i) {

        ChoiceBox changePrediction = new ChoiceBox();
        changePrediction.getItems().addAll("1", "X", "2");

            if(betsList.size() > 0){
                for(int z=0; z < betsList.size(); z++){
                    if(betsList.get(z).getMatchId() == matchesList.get(i).getMatchId()){
                        return;
                    }
                }
            }

            Bet bet = new Bet(matchesList.get(i).getMatchId(), matchesList.get(i).getMatchday(), prediction, matchesList.get(i).getHomeTeam(), matchesList.get(i).getAwayTeam(), new Button("Edit"), new Button("Delete"), changePrediction);
            betsList.add(bet);
            Session session = sf.openSession();
            session.beginTransaction();
            session.save(bet);
            session.getTransaction().commit();
            session.close();

    }



    public void editBet(int id, String newPrediction){
        for(int i=0; i<betsList.size(); i++){
            if(betsList.get(i).getMatchId() == id){
                betsList.get(i).setPrediction(newPrediction);

                Session session = sf.openSession();
                session.beginTransaction();
                session.update(betsList.get(i));
                session.getTransaction().commit();
                session.close();
            }
        }
    }

    public void deleteBet(int id) {
        for(int i=0; i<betsList.size(); i++){
            if(betsList.get(i).getMatchId() == id){
                Session session = sf.openSession();
                session.beginTransaction();
                session.remove(betsList.get(i));
                session.getTransaction().commit();
                session.close();
                betsList.remove(betsList.get(i));
            }
        }
    }


    public List<Match> getmatchesList(){
        return matchesList;
    }

    public List<Bet> getBetsList() {
        return betsList;
    }

}
