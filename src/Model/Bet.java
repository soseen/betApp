package Model;

import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;

import javax.persistence.*;


@Entity
public class Bet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;



    @Column(name = "matchID")
    private int matchId;

    @Column(name = "matchday")
    private int matchday;

    @Column(name = "prediction")
    private String prediction;

    @Column(name = "home")
    private String homeTeam;

    @Column(name = "away")
    private String awayTeam;

    private Button buttonEdit;
    private Button buttonRemove;
    private ChoiceBox changePrediction;


    public Bet(){}

    public Bet(int matchId, int matchday, String prediction, String homeTeam, String awayTeam, Button buttonEdit, Button buttonRemove, ChoiceBox changePrediction) {
        this.matchId = matchId;
        this.matchday = matchday;
        this.prediction = prediction;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.buttonEdit = buttonEdit;
        this.buttonRemove = buttonRemove;
        this.changePrediction = changePrediction;
    }



    public int getMatchId() {
        return matchId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }

    public int getMatchday() {
        return matchday;
    }

    public void setMatchday(int matchday) {
        this.matchday = matchday;
    }

    public String getPrediction(){
        return prediction;
    }

    public void setPrediction(String prediction) {
        this.prediction = prediction;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(String awayTeam) {
        this.awayTeam = awayTeam;
    }

    public Button getButtonEdit() {
        return buttonEdit;
    }

    public void setButtonEdit(Button buttonEdit) {
        this.buttonEdit = buttonEdit;
    }

    public Button getButtonRemove() {
        return buttonRemove;
    }

    public void setButtonRemove(Button buttonRemove) {
        this.buttonRemove = buttonRemove;
    }

    public ChoiceBox getChangePrediction() {
        return changePrediction;
    }

    public void setChangePreditcion(ChoiceBox changePrediction) {

        this.changePrediction = changePrediction;
        changePrediction.getItems().addAll("1", "X", "2");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}


