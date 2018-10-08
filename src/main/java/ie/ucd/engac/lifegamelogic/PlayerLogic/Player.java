package ie.ucd.engac.lifegamelogic.PlayerLogic;

import java.util.List;

import ie.ucd.engac.lifegamelogic.cards.ActionCards.ActionCard;
import ie.ucd.engac.lifegamelogic.cards.HouseCards.HouseCard;

public class Player {
    // Action cards held by this player
    private List<ActionCard> actionCards;

    // House cards held by this player
    private List<HouseCard> houseCards;

    // Marital status
    private MaritalStatus maritalStatus;

    public int getPlayerNumber() {
        return playerNumber;
    }

    //private PlayerColour playerColour;
    private int playerNumber;

    public Player(int playerNumber) {
        //this.playerColour.fromInt(playerNumber);
        this.maritalStatus = MaritalStatus.Single;
        this.playerNumber = playerNumber;
        System.out.println("Player.java, player constructor terminating");
    }


    // Reference to the Bank object? Dependency injection?

	// Marker of how far along the board the player is

	// Number of people in the car
    public int getNumDependants(){
        return 0;
    }

	// Current career card
    public String getCareerCard(){//TODO make this actual
        return "None";
        }

	// Current amount of money
    public int getCurrentMoney(){
        return 0;
    }
	// Current loans
    public int getCurrentLoans(){
        return 0;
    }


	
	public MaritalStatus getMaritalStatus() 
	{
		return this.maritalStatus;
	}


}


