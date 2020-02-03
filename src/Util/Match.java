package Util;

import javafx.scene.control.Button;

public class Match {

    private int matchId;
    private String homeTeam;
    private String awayTeam;
    private int matchday;
    private Button button1;
    private Button buttonX;
    private Button button2;

    public Match(int matchId, int matchday, String homeTeam, String awayTeam, Button button1, Button buttonX, Button button2) {
        this.matchId = matchId;
        this.matchday = matchday;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.button1 = button1;
        this.buttonX = buttonX;
        this.button2 = button2;
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

    public String getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(String awayTeam) {
        this.awayTeam = awayTeam;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public Button getButton1() {
        return button1;
    }

    public void setButton1(Button button1) {
        this.button1 = button1;
    }

    public Button getButtonX() {
        return buttonX;
    }

    public void setButtonX(Button buttonX) {
        this.buttonX = buttonX;
    }

    public Button getButton2() {
        return button2;
    }

    public void setButton2(Button button2) {
        this.button2 = button2;
    }


}