package ie.ucd.engAC.LifeGameLogic.PlayerLogic;

import java.util.List;

import ie.ucd.engAC.LifeGameLogic.Cards.ActionCards.ActionCard;
import ie.ucd.engAC.LifeGameLogic.Cards.HouseCards.HouseCard;

public class Player {
	// Reference to the Bank object? Dependency injection?

	// Marker of how far along the board the player is

	// Number of people in the car

	// Current career card

	// Current amount of money

	// Current loans

	// Action cards held by this player
	private List<ActionCard> actionCards;

	// House cards held by this player
	private List<HouseCard> houseCards;

	// Marital status
	private MaritalStatus maritalStatus;
	
	public MaritalStatus getMaritalStatus() 
	{
		return this.maritalStatus;
	}

	public Player() {
		this.maritalStatus = MaritalStatus.Single;
	}
}


