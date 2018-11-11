package ie.ucd.engac.lifegamelogic.gamestatehandling;

import java.util.ArrayList;

import ie.ucd.engac.GameConfig;
import ie.ucd.engac.lifegamelogic.cards.Card;
import ie.ucd.engac.lifegamelogic.cards.occupationcards.OccupationCard;
import ie.ucd.engac.lifegamelogic.gameboardlogic.BoardLocation;
import ie.ucd.engac.lifegamelogic.playerlogic.Player;
import ie.ucd.engac.messaging.Chooseable;
import ie.ucd.engac.messaging.ChooseableString;
import ie.ucd.engac.messaging.DecisionRequestMessage;
import ie.ucd.engac.messaging.DecisionResponseMessage;
import ie.ucd.engac.messaging.LifeGameMessage;
import ie.ucd.engac.messaging.LifeGameMessageTypes;
import ie.ucd.engac.messaging.SpinRequestMessage;

public class NightSchoolState extends GameState {
	private final String KEEP_CAREER_MSG = "Keep your current career";
	public static final int KEEP_CAREER_INDEX = 0;
	
	private final String ATTEND_NIGHT_SCHOOL_MSG = "Attend night school";
	public static final int ATTEND_NIGHT_SCHOOL_INDEX = 1;
	
	private ArrayList<Card> collegeCareerCardOptions; 
	private boolean pendingCollegeCareerCardDecision = false;
	
	private ArrayList<String> nightSchoolOptions;
	private boolean pendingNightSchoolDecision = false;
	
	@Override
	public void enter(GameLogic gameLogic) {
		// Must prompt the user to choose between keeping the job,
		// or changing career.
		
		// Can't change career if don't already have one...
		if(gameLogic.getCurrentPlayer().getOccupationCard() == null) {
			
			pendingCollegeCareerCardDecision = true;
			
			collegeCareerCardOptions = new ArrayList<>();
			
			// Give choice of top college cards			
			OccupationCard firstCollegeCareerCard = gameLogic.getTopCollegeCareerCard();
            OccupationCard secondCollegeCareerCard = gameLogic.getTopCollegeCareerCard();

            // Create a list of the choices
            collegeCareerCardOptions.add(firstCollegeCareerCard);
            collegeCareerCardOptions.add(secondCollegeCareerCard);

            // Construct a message with these choices
            LifeGameMessage replyMessage = constructCardChoiceMessage(
                    gameLogic.getCurrentPlayer().getPlayerNumber(),
                    (Chooseable) firstCollegeCareerCard,
                    (Chooseable) secondCollegeCareerCard,
                    "Choose new college career card");

            // Need to store both choices so that we can assign the chosen one to the
            // correct player,
            // and push the unchosen one to the bottom of the correct deck.
            gameLogic.storePendingChoiceCards(collegeCareerCardOptions);
            gameLogic.setResponseMessage(replyMessage);
		}
		else
		{
			pendingNightSchoolDecision = true;
			
			nightSchoolOptions = new ArrayList<>();			
			ArrayList<Chooseable> pendingNightSchoolChoice = new ArrayList<>();
			
			// Must give the player the choice to either keep their current job or go to night school			
			pendingNightSchoolChoice.add(new ChooseableString(KEEP_CAREER_MSG));
			nightSchoolOptions.add(KEEP_CAREER_MSG);
			
			pendingNightSchoolChoice.add(new ChooseableString(ATTEND_NIGHT_SCHOOL_MSG));
			nightSchoolOptions.add(ATTEND_NIGHT_SCHOOL_MSG);

			String eventMessage = "Would you like to attend night school?";

			LifeGameMessage responseMessage = new DecisionRequestMessage(pendingNightSchoolChoice,
																		 gameLogic.getCurrentPlayer().getPlayerNumber(), eventMessage);
			gameLogic.setResponseMessage(responseMessage);
		}
	}

	@Override
	public GameState handleInput(GameLogic gameLogic, LifeGameMessage lifeGameMessage) {
		if(lifeGameMessage.getLifeGameMessageType() == LifeGameMessageTypes.OptionDecisionResponse) {
			if(pendingCollegeCareerCardDecision) {
				pendingCollegeCareerCardDecision = false;
				
				return parseCollegeCareerCardDecision(gameLogic, ((DecisionResponseMessage) lifeGameMessage).getChoiceIndex());
			}
			else if (pendingNightSchoolDecision) {
				pendingNightSchoolDecision = false;
				
				 
			    return parsePendingNightSchoolDecision(gameLogic, ((DecisionResponseMessage) lifeGameMessage).getChoiceIndex());
			}	
		}
		
		return null;
	}

	@Override
	public void exit(GameLogic gameLogic) {
		
	}
	
	private GameState parseCollegeCareerCardDecision(GameLogic gameLogic, int choiceIndex) {
		
		// First option was chosen
		gameLogic.subtractFromCurrentPlayersBalance(GameConfig.night_school_tuition_fees);
		
		// Assign the correct college career card
		OccupationCard selectedCollegeCareerCard = (OccupationCard) collegeCareerCardOptions.get(choiceIndex);		
		gameLogic.getCurrentPlayer().setOccupationCard(selectedCollegeCareerCard);
		
		String eventMsg = "Player " + gameLogic.getCurrentPlayer().getPlayerNumber() + ", you get to spin again.";
		
		LifeGameMessage responseMessage = new SpinRequestMessage(gameLogic.getShadowPlayer(gameLogic.getCurrentPlayerIndex()),
																 gameLogic.getCurrentPlayer().getPlayerNumber(),
																 eventMsg);			
		gameLogic.setResponseMessage(responseMessage);			
		return new HandlePlayerMoveState();
	}
	
	private GameState parsePendingNightSchoolDecision(GameLogic gameLogic, int choiceIndex) {
		if(choiceIndex == ATTEND_NIGHT_SCHOOL_INDEX) {
			// Player wishes to attend night school:			
			gameLogic.subtractFromCurrentPlayersBalance(GameConfig.night_school_tuition_fees);

			OccupationCard currentOccupationCard = gameLogic.getCurrentPlayer().getOccupationCard();
			if (currentOccupationCard != null){ //shouldnt be null, but just in case
				gameLogic.returnOccupationCard(currentOccupationCard);
			}

			OccupationCard topCollegeCareerCard = gameLogic.getTopCollegeCareerCard();	
			
			Player currentPlayer = gameLogic.getCurrentPlayer();			
			currentPlayer.setOccupationCard(topCollegeCareerCard);
			
			BoardLocation currentLocation = currentPlayer.getCurrentLocation();
			BoardLocation nightSchoolPathChoiceLocation = gameLogic.getGameBoard().getOutboundNeighbours(currentLocation).get(ATTEND_NIGHT_SCHOOL_INDEX);
			
			gameLogic.getCurrentPlayer().setPendingBoardForkChoice(nightSchoolPathChoiceLocation);
		}
		else {
			Player currentPlayer = gameLogic.getCurrentPlayer();	
			BoardLocation currentLocation = currentPlayer.getCurrentLocation();
			BoardLocation lifePathChoiceLocation = gameLogic.getGameBoard().getOutboundNeighbours(currentLocation).get(KEEP_CAREER_INDEX);
			gameLogic.getCurrentPlayer().setPendingBoardForkChoice(lifePathChoiceLocation);
		}
		
		// Send same message either way
		String eventMsg = "Player " + gameLogic.getCurrentPlayer().getPlayerNumber() + ", you get to spin again."; // TODO: How to display this?
				
		return new HandlePlayerMoveState(eventMsg);
	}

}