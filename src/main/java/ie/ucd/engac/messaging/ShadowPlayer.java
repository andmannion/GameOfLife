package ie.ucd.engac.messaging;


import ie.ucd.engac.lifegamelogic.cards.housecards.HouseCard;
import ie.ucd.engac.lifegamelogic.cards.occupationcards.OccupationCard;
import ie.ucd.engac.lifegamelogic.cards.occupationcards.careercards.CareerCard;
import ie.ucd.engac.lifegamelogic.cards.occupationcards.collegecareercards.CollegeCareerCard;
import ie.ucd.engac.lifegamelogic.gamestatehandling.GameLogic;
import ie.ucd.engac.lifegamelogic.playerlogic.Player;
import ie.ucd.engac.lifegamelogic.playerlogic.PlayerColour;

import java.util.ArrayList;

public class ShadowPlayer {

    private PlayerColour playerColour;

    private int playerNumber;
    private int numDependants;
    private int numActionCards;
    private int loans;
    private int numLoans;
    private int bankBalance;

    private OccupationCard occupation;
    private int martialStatus;
    private ArrayList<HouseCard> houses;

    public ShadowPlayer(Player player, GameLogic gameLogic){
        playerNumber = player.getPlayerNumber();
        playerColour = player.getPlayerColour();
        martialStatus = player.getMaritalStatus().toInt();
        numDependants = player.getNumberOfDependants();
        occupation = player.getOccupationCard();
        houses = player.getHouseCards();
        numLoans = player.getNumberOfLoans(gameLogic);
        loans = player.getTotalLoansOutstanding(gameLogic);
        bankBalance = player.getCurrentMoney();
        numActionCards = player.getActionCards().size();
    }

    public String playerColourToString(){
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
                    return ((CareerCard) occupation).convertDrawableString();
                case CollegeCareer:
                    return ((CollegeCareerCard) occupation).convertDrawableString();
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
}
