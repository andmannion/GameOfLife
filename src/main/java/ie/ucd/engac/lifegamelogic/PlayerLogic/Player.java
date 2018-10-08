package ie.ucd.engac.lifegamelogic.PlayerLogic;

import java.util.ArrayList;

<<<<<<< HEAD:src/main/java/ie/ucd/engac/lifegamelogic/PlayerLogic/Player.java
import ie.ucd.engac.lifegamelogic.cards.ActionCards.ActionCard;
import ie.ucd.engac.lifegamelogic.cards.HouseCards.HouseCard;
import ie.ucd.engac.lifegamelogic.cards.CareerCards.CareerCard;


public class Player {
       // Action cards held by this player
    private ArrayList<ActionCard> actionCards;
    // House cards held by this player
    private ArrayList<HouseCard> houseCards;
    // Marital status
    private MaritalStatus maritalStatus;
    //private PlayerColour playerColour;
    private int playerNumber;
    private CareerCard careerCard;
    private int numLoans;

    public Player(int playerNumber) {
        //this.playerColour.fromInt(playerNumber);
        this.maritalStatus = MaritalStatus.Single;
        this.playerNumber = playerNumber;
        actionCards = new ArrayList<>();
        houseCards = new ArrayList<>();
        careerCard = null;
        numLoans = 0;
        System.out.println("Player.java, player constructor terminating");
    }


    // Reference to the Bank object? Dependency injection?

	// Marker of how far along the board the player is

	// Number of people in the car
    public int getNumDependants(){
        return 0;
    }

	// Current career card
    public CareerCard getCareerCard(){//TODO make this actual
        return null;
        }

	// Current amount of money
    public int getCurrentMoney(){
        return 0;
    }
    public int getNumLoans(){
        return numLoans;
    }
    public ArrayList<ActionCard> getActionCards() {
        return actionCards;
    }
    public ArrayList<HouseCard> getHouseCards() {
        return houseCards;
    }
    public int getPlayerNumber() {
        return playerNumber;
    }
	public MaritalStatus getMaritalStatus() 
	{
		return maritalStatus;
	}


}


