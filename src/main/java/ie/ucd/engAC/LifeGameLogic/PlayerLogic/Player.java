package main.java.ie.ucd.engAC.LifeGameLogic.PlayerLogic;

import java.util.List;

import main.java.ie.ucd.engAC.LifeGameLogic.Cards.ActionCards.ActionCard;
import main.java.ie.ucd.engAC.LifeGameLogic.Cards.HouseCards.HouseCard;

public class Player {
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

	// Action cards held by this player
	private List<ActionCard> actionCards;

	// House cards held by this player
	private List<HouseCard> houseCards;

	// Marital status
	private MaritalStatus maritalStatus;

	//private PlayerColour playerColour;
	
	public MaritalStatus getMaritalStatus() 
	{
		return this.maritalStatus;
	}

	public Player(int playerNumber) {
        //this.playerColour.fromInt(playerNumber);
        this.maritalStatus = MaritalStatus.Single;
        System.out.println("Player.java, player constructor terminating");
	}
}


