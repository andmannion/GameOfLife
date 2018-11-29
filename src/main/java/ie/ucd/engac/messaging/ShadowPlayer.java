package ie.ucd.engac.messaging;


import ie.ucd.engac.lifegamelogic.cards.housecards.HouseCard;
import ie.ucd.engac.lifegamelogic.cards.occupationcards.OccupationCard;
import ie.ucd.engac.lifegamelogic.cards.occupationcards.careercards.CareerCard;
import ie.ucd.engac.lifegamelogic.cards.occupationcards.collegecareercards.CollegeCareerCard;
import ie.ucd.engac.lifegamelogic.gameboard.gameboardtiles.GameBoardStopTile;
import ie.ucd.engac.lifegamelogic.gameboard.gameboardtiles.GameBoardTile;
import ie.ucd.engac.lifegamelogic.gameboard.gameboardtiles.GameBoardTileTypes;
import ie.ucd.engac.lifegamelogic.playerlogic.Player;
import ie.ucd.engac.lifegamelogic.playerlogic.PlayerColour;

import java.awt.*;
import java.util.ArrayList;

public class ShadowPlayer {

    private Color playerColour; //TODO AWT -> FX

    private int playerNumber;
    private int numDependants;
    private int numActionCards;
    private int loans;
    private int numLoans;
    private int bankBalance;

    private double xLocation;
    private double yLocation;

    private GameBoardTile currentTile;
    private OccupationCard occupation;
    private int martialStatus;
    private ArrayList<HouseCard> houses;

    public ShadowPlayer(Player player,int numLoans, int loans, GameBoardTile gameBoardTile){
        this.playerNumber = player.getPlayerNumber();
        this.playerColour = player.getPlayerColour();
        this.martialStatus = player.getMaritalStatus().toInt();
        this.numDependants = player.getNumberOfDependants();
        this.occupation = player.getOccupationCard();
        this.houses = player.getHouseCards();
        this.numLoans = numLoans;
        this.loans = loans;
        this.bankBalance = player.getCurrentMoney();
        this.numActionCards = player.getNumberOfActionCards();
        this.currentTile = gameBoardTile;
        if(gameBoardTile != null) {
            this.xLocation = gameBoardTile.getXLocation();
            this.yLocation = gameBoardTile.getYLocation();
        }
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public double getXLocation() {
        return xLocation;
    }

    public double getYLocation() {
        return yLocation;
    }

    public String playerColourToString(){ //TODO this makes a horrible horrible string
        return playerColour.toString();
    }

    public String playerNumToString(){
        Integer playerNumber = this.playerNumber;
        return playerNumber.toString();
    }

    //display house
    public String houseCardsToString(){
        if (houses.size() == 0){
            return "No house cards.";
        }
        else{
            String string = "";//TODO test that this obeys boundaries (it wont)
            int i = 0;
            for(HouseCard house:houses){
                string.concat(house.displayChoiceDetails());
            }
            return string;
        }
    }

    public String actionCardsToString(){
        Integer numActionCards = this.numActionCards;
        return numActionCards.toString();
    }

    public String careerCardToString(){
        if (occupation == null){
             return "No occupation card.";
        }
        else{
            switch(occupation.getOccupationCardType()){
                case Career:
                    return ((CareerCard) occupation).displayChoiceDetails();
                case CollegeCareer:
                    return ((CollegeCareerCard) occupation).displayChoiceDetails();
                default:
                    return null;
            }
        }
    }
    public String numLoansToString(){
        Integer loans = this.loans;
        Integer numLoans = this.numLoans;
        return numLoans.toString() + " loans worth " + loans.toString();
    }

    public String bankBalToString(){
        Integer bankBalance = this.bankBalance;
        return bankBalance.toString();
    }

    public String dependantsToString(){
        String string;
        if (martialStatus == 1){
            string = martialStatus + " spouse and " + (numDependants -1 )+ " children";
        }
        else string = "No dependants";
        return string;
    }

    public String currentTileToString(){
        String string;
        if(currentTile.getGameBoardTileType() == GameBoardTileTypes.Stop){
            string = "On stop tile of type " + ((GameBoardStopTile)currentTile).getGameBoardStopTileType().toString();
        }
        else{
            string = "On standard tile of type " + currentTile.getGameBoardTileType().toString();
        }
        return string;
    }
}
