package Controller;

import Model.Bet;
import Model.Match;
import Model.League;
import Model.AppModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class AppController implements Initializable {

    public static

    AppModel model = new AppModel();

    ObservableList<Match> fixturesData;
    ObservableList<Bet> bets;
    List<Bet> betsData;
    ObservableList <League> availableLeagues = FXCollections.observableArrayList();
    private String leagueID = "2021";

    @FXML
    private TableView<Match> fixturesTable;

    @FXML
    private TableColumn<Match, Integer> columnId;

    @FXML
    private TableColumn<Match, Integer> columnMatchweek;

    @FXML
    private TableColumn<Match, String> columnHomeTeam;

    @FXML
    private TableColumn<Match, String> columnAwayTeam;

    @FXML
    private TableColumn<Match, Button> columnBet1;

    @FXML
    private TableColumn<Match, Button> columnBetX;

    @FXML
    private TableColumn<Match, Button> columnBet2;

    @FXML
    private TableView<Bet> betsTable;

    @FXML
    private TableColumn<Bet, Integer> columnId2;

    @FXML
    private TableColumn<Bet, Integer> columnMatchweek2;

    @FXML
    private TableColumn<Bet, Integer> columnPrediction;

    @FXML
    private TableColumn<Bet, String> columnHomeTeam2;

    @FXML
    private TableColumn<Bet, String> columnAwayTeam2;

    @FXML
    private TableColumn<Bet, Button> columnEdit;

    @FXML
    private TableColumn<Bet, Button> columnDelete;

    @FXML
    private TableColumn<Bet,ChoiceBox> columnChangePrediction;

    @FXML
    private ChoiceBox<String> chooseLeagueBox;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        loadLeagues();
        initializeTables();
    }

    private void loadLeagues() {
        availableLeagues.removeAll(availableLeagues);
        League PL = new League("2021", "Premier League", "PL");
        League PD = new League("2014", "Primiera Division", "PD");
        League SA = new League("2019", "Serie A", "SA");
        League BL = new League("2002", "Bundesliga", "BL1");
        League FL = new League("2015", "Ligue 1", "FL1");
        availableLeagues.addAll(PL, PD, SA, BL, FL);
        chooseLeagueBox.getItems().addAll(PL.getName(), PD.getName(), SA.getName(), BL.getName(), FL.getName());
        chooseLeagueBox.setValue(PL.getName());
    }

    private void initializeTables() {

        // Fixtures Table
        columnId.setCellValueFactory(new PropertyValueFactory<Match, Integer>("matchId"));
        columnMatchweek.setCellValueFactory(new PropertyValueFactory<Match, Integer>("matchday"));
        columnHomeTeam.setCellValueFactory(new PropertyValueFactory<Match, String>("homeTeam"));
        columnAwayTeam.setCellValueFactory(new PropertyValueFactory<Match, String>("awayTeam"));
        columnBet1.setCellValueFactory(new PropertyValueFactory<Match, Button>("button1"));
        columnBetX.setCellValueFactory(new PropertyValueFactory<Match, Button>("buttonX"));
        columnBet2.setCellValueFactory(new PropertyValueFactory<Match, Button>("button2"));
        fixturesTable.setItems(fixturesData);


        // Bets Table
        columnId2.setCellValueFactory(new PropertyValueFactory<Bet, Integer>("matchId"));
        columnMatchweek2.setCellValueFactory(new PropertyValueFactory<Bet, Integer>("matchday"));
        columnPrediction.setCellValueFactory(new PropertyValueFactory<Bet, Integer>("prediction"));
        columnHomeTeam2.setCellValueFactory(new PropertyValueFactory<Bet, String>("HomeTeam"));
        columnAwayTeam2.setCellValueFactory(new PropertyValueFactory<Bet, String>("AwayTeam"));
        columnDelete.setCellValueFactory(new PropertyValueFactory<Bet, Button>("buttonRemove"));
        columnEdit.setCellValueFactory(new PropertyValueFactory<Bet, Button>("buttonEdit"));
        columnChangePrediction.setCellValueFactory(new PropertyValueFactory<Bet, ChoiceBox>("changePrediction"));

        betsData = model.getData();
        displayBets();
    }

    @FXML
    public ObservableList<Match> getData(javafx.event.ActionEvent actionEvent) {


        model.getFixtures(leagueID);

        List <Match> fixtures = model.getmatchesList();

        fixturesData = FXCollections.observableList(fixtures);


        for (int i = 0; i < fixturesData.size(); i++) {
            fixturesData.get(i).getButton1().setOnAction(this::placeBet);
            fixturesData.get(i).getButtonX().setOnAction(this::placeBet);
            fixturesData.get(i).getButton2().setOnAction(this::placeBet);
        }

        fixturesTable.setItems(fixturesData);

        return fixturesData;
    }

    public void getLeague(javafx.event.ActionEvent actionEvent){
        String leagueName = chooseLeagueBox.getValue();

        for(int i=0; i<availableLeagues.size(); i++){
            if(leagueName == availableLeagues.get(i).getName())
                leagueID = availableLeagues.get(i).getLeagueId();
        }

        System.out.println(leagueID);
    }

    private void placeBet(ActionEvent actionEvent) {


        String prediction;

        for (int i = 0; i < fixturesData.size(); i++) {
            if (actionEvent.getSource() == fixturesData.get(i).getButton1()) {
                prediction = "1";
                model.placeBet(prediction, i);
            } else if (actionEvent.getSource() == fixturesData.get(i).getButtonX()) {
                prediction = "X";
                model.placeBet(prediction, i);
            } else if (actionEvent.getSource() == fixturesData.get(i).getButton2()) {
                prediction = "2";
                model.placeBet(prediction, i);
            }
        }

        displayBets();

    }
    private void displayBets(){


        betsData = model.getBetsList();
        bets = FXCollections.observableList(betsData);
        betsTable.getItems().removeAll();
        betsTable.setItems(bets);
        betsTable.refresh();

        for(int z=0; z < betsData.size(); z++) {
            betsData.get(z).getButtonEdit().setOnAction(this::editOrDeleteBet);
            betsData.get(z).getButtonRemove().setOnAction(this::editOrDeleteBet);
        }

    }

    private void editOrDeleteBet(ActionEvent actionEvent) {
        for(int i=0; i<betsData.size(); i++)
            if (actionEvent.getSource() == betsData.get(i).getButtonRemove()) {
                model.deleteBet(betsData.get(i).getMatchId());
            } else if (actionEvent.getSource() == betsData.get(i).getButtonEdit()) {
                if(betsData.get(i).getChangePrediction().getValue() != null){
                    model.editBet(betsData.get(i).getMatchId(), (String) betsData.get(i).getChangePrediction().getValue());
                }
            }

        displayBets();
    }



}



