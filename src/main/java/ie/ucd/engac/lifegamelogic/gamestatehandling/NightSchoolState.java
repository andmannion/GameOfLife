package ie.ucd.engac.lifegamelogic.gamestatehandling;

import java.util.ArrayList;

import ie.ucd.engac.lifegamelogic.cards.occupationcards.OccupationCard;
import ie.ucd.engac.lifegamelogic.cards.occupationcards.collegecareercards.CollegeCareerCard;
import ie.ucd.engac.messaging.Chooseable;
import ie.ucd.engac.messaging.ChooseableString;
import ie.ucd.engac.messaging.DecisionRequestMessage;
import ie.ucd.engac.messaging.DecisionResponseMessage;
import ie.ucd.engac.messaging.LifeGameMessage;
import ie.ucd.engac.messaging.LifeGameMessageTypes;
import ie.ucd.engac.messaging.ShadowPlayer;
import ie.ucd.engac.messaging.SpinRequestMessage;

public class NightSchoolState implements GameState {
	private final String KEEP_CAREER_MSG = "Keep your current career";
	//private final int KEEP_CAREER_MSG_INDEX = 0;
	
	private final String ATTEND_NIGHT_SCHOOL_MSG = "Attend night school";
	private final int ATTEND_NIGHT_SCHOOL_INDEX = 1;
	
	private final int NIGHT_SCHOOL_TUITION_FEES = 100000;
	
	private ArrayList<OccupationCard> collegeCareerCardOptions; 
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
			
			ArrayList<Chooseable> pendingCollegeCareerCardChoices = new ArrayList<>();			
			collegeCareerCardOptions = new ArrayList<>();
			
			// Give choice of top college cards			
			CollegeCareerCard topCollegeCareerCard = (CollegeCareerCard) gameLogic.getTopCollegeCareerCard();
			
			collegeCareerCardOptions.add(topCollegeCareerCard);
			pendingCollegeCareerCardChoices.add((Chooseable)topCollegeCareerCard);
			
			topCollegeCareerCard = (CollegeCareerCard) gameLogic.getTopCollegeCareerCard();
			
			collegeCareerCardOptions.add(topCollegeCareerCard);
			pendingCollegeCareerCardChoices.add((Chooseable)topCollegeCareerCard);
			
			// Compose a response message with this information
			LifeGameMessage responseMessage = new DecisionRequestMessage(pendingCollegeCareerCardChoices,
																	     gameLogic.getCurrentPlayer().getPlayerNumber());
			
			gameLogic.setResponseMessage(responseMessage);
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
			
			LifeGameMessage responseMessage = new DecisionRequestMessage(pendingNightSchoolChoice,
																		 gameLogic.getCurrentPlayer().getPlayerNumber());			
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
		gameLogic.getCurrentPlayer().subtractFromBalance(NIGHT_SCHOOL_TUITION_FEES);
		
		// Assign the correct college career card
		OccupationCard selectedCollegeCareerCard = collegeCareerCardOptions.get(choiceIndex);		
		gameLogic.getCurrentPlayer().setOccupationCard(selectedCollegeCareerCard);
		
		String eventMsg = "Player " + gameLogic.getCurrentPlayer().getPlayerNumber() + ", you get to spin again.";
		
		LifeGameMessage responseMessage = new SpinRequestMessage(new ShadowPlayer(gameLogic.getCurrentPlayer()),
																 gameLogic.getCurrentPlayer().getPlayerNumber(),
																 eventMsg);			
		gameLogic.setResponseMessage(responseMessage);			
		return new HandlePlayerMoveState();
	}
	
	private GameState parsePendingNightSchoolDecision(GameLogic gameLogic, int choiceIndex) {
		if(choiceIndex == ATTEND_NIGHT_SCHOOL_INDEX) {
			// Player wishes to attend night school:			
			gameLogic.getCurrentPlayer().subtractFromBalance(NIGHT_SCHOOL_TUITION_FEES);
			
			OccupationCard currentOccupationCard = gameLogic.getCurrentPlayer().getOccupationCard();			
			gameLogic.returnOccupationCard(currentOccupationCard);
			
			OccupationCard topCollegeCareerCard = gameLogic.getTopCollegeCareerCard();			
			gameLogic.getCurrentPlayer().setOccupationCard(topCollegeCareerCard);
		}
		
		// Send same message either way
		String eventMsg = "Player " + gameLogic.getCurrentPlayer().getPlayerNumber() + ", you get to spin again.";
					
		LifeGameMessage responseMessage = new SpinRequestMessage(new ShadowPlayer(gameLogic.getCurrentPlayer()),
																 gameLogic.getCurrentPlayer().getPlayerNumber(),
																 eventMsg);			
		gameLogic.setResponseMessage(responseMessage);			
		return new HandlePlayerMoveState();
	}

}
