package Controller;



import Model.HibernateUtil;
import Util.Bet;
import Util.Match;
import Util.League;
import Model.AppModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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

        betsData = getCurrentBets();
    }

    private void displayBets(){

        bets = FXCollections.observableList(betsData);
        betsTable.getItems().removeAll();
        betsTable.setItems(bets);
        betsTable.refresh();


        for(int z=0; z < betsData.size(); z++) {
            betsData.get(z).getButtonEdit().setOnAction(this::editOrDeleteBet);
            betsData.get(z).getButtonRemove().setOnAction(this::editOrDeleteBet);
        }


    }

    @FXML
    public ObservableList<Match> getData(javafx.event.ActionEvent actionEvent) {


        model.getFixtures(leagueID);

        List <Match> fixtures = model.getmatchesList();

        fixturesData = FXCollections.observableList(fixtures);


        for (int i = 0; i < fixturesData.size(); i++) {
            fixturesData.get(i).getButton1().setOnAction(this::placePrediction);
            fixturesData.get(i).getButtonX().setOnAction(this::placePrediction);
            fixturesData.get(i).getButton2().setOnAction(this::placePrediction);
        }

        fixturesTable.setItems(fixturesData);
        displayBets();

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

    private void placePrediction(ActionEvent actionEvent) {

        String prediction;

        for (int i = 0; i < fixturesData.size(); i++) {
            if (actionEvent.getSource() == fixturesData.get(i).getButton1()) {
                prediction = "1";
                placeBet(prediction, i);
            } else if (actionEvent.getSource() == fixturesData.get(i).getButtonX()) {
                prediction = "X";
                placeBet(prediction, i);
            } else if (actionEvent.getSource() == fixturesData.get(i).getButton2()) {
                prediction = "2";
                placeBet(prediction, i);
            }
        }

        displayBets();

    }

    public List<Bet> getCurrentBets(){
        Task <List<Bet>> getData = new Task<List<Bet>>() {

            @Override
            protected List<Bet> call() throws Exception {
                return HibernateUtil.getData();
            }
        };


        getData.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                betsData = getData.getValue();
                ChoiceBox changePrediction = new ChoiceBox();
                changePrediction.getItems().addAll("1", "X", "2");

                for(int i=0; i<betsData.size(); i++){
                    betsData.get(i).setButtonEdit(new Button("Edit"));
                    betsData.get(i).setButtonRemove(new Button("Delete"));
                    betsData.get(i).setChangePreditcion(new ChoiceBox());
                }
                displayBets();
            }
        });

        new Thread(getData).start();


        return betsData;
    }



    private void editOrDeleteBet(ActionEvent actionEvent) {
        for(int i=0; i<betsData.size(); i++)
            if (actionEvent.getSource() == betsData.get(i).getButtonRemove()) {
                deleteBet(betsData.get(i).getMatchId());
            } else if (actionEvent.getSource() == betsData.get(i).getButtonEdit()) {
                if(betsData.get(i).getChangePrediction().getValue() != null){
                    editBet(betsData.get(i).getMatchId(), (String) betsData.get(i).getChangePrediction().getValue());
                }
            }

        displayBets();
    }

    public void placeBet(String prediction, int i) {

        Task <Bet> placeBet = new Task <Bet>() {

            @Override
            protected Bet call() throws Exception {
                if(fixturesData.size() > 0){
                    for(int z=0; z < betsData.size(); z++){
                        if(betsData.get(z).getMatchId() == fixturesData.get(i).getMatchId()){
                            return null;
                        }
                    }
                }

                ChoiceBox changePrediction = new ChoiceBox();
                changePrediction.getItems().addAll("1", "X", "2");
                Bet bet = new Bet(fixturesData.get(i).getMatchId(), fixturesData.get(i).getMatchday(), prediction, fixturesData.get(i).getHomeTeam(), fixturesData.get(i).getAwayTeam(), new Button("Edit"), new Button("Delete"), changePrediction);
                betsData.add(bet);

                return bet;
            }
        };

        placeBet.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {

                if(placeBet.getValue() != null){
                    Bet bet = placeBet.getValue();
                    HibernateUtil.placeBet(bet);
                }
                displayBets();
            }
        });

        new Thread(placeBet).start();

    }


    public void editBet(int id, String newPrediction) {

        Task<List<Bet>> editBet = new Task<List<Bet>>() {

            @Override
            protected List<Bet> call() throws Exception {
                for (int i = 0; i < betsData.size(); i++) {
                    if (betsData.get(i).getMatchId() == id && betsData.get(i).getPrediction() != newPrediction) {
                        betsData.get(i).setPrediction(newPrediction);

                        HibernateUtil.editBet(betsData.get(i));


                    }
                }

                return betsData;
            }

        };

        editBet.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                displayBets();
            }
        });

        new Thread(editBet).start();

    }

    public void deleteBet(int id) {

        Task<List<Bet>> deleteBet = new Task<List<Bet>>() {

            @Override
            protected List<Bet> call() throws Exception {
                for(int i=0; i<betsData.size(); i++){
                    if(betsData.get(i).getMatchId() == id){

                        HibernateUtil.deleteBet(betsData.get(i));
                        betsData.remove(betsData.get(i));
                    }
                };
                return betsData;
            }

        };

        deleteBet.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                displayBets();
            }
        });

        new Thread(deleteBet).start();
    }



}



