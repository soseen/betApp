package Model;

import javafx.scene.control.Button;

public class League {

    private String leagueId;
    private String name;
    private String code;


    public League(String leagueId, String name, String code) {
        this.leagueId = leagueId;
        this.name = name;
        this.code = code;
    }

    public String getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(String leagueId) {
        this.leagueId = leagueId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}


