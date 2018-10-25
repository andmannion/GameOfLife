package ie.ucd.engac.lifegamelogic.banklogic;

import ie.ucd.engac.fileutilities.FileUtilities;
import ie.ucd.engac.lifegamelogic.cards.actioncards.*;
import ie.ucd.engac.lifegamelogic.cards.housecards.HouseCard;
import ie.ucd.engac.lifegamelogic.cards.housecards.HouseCardDeck;
import ie.ucd.engac.lifegamelogic.cards.occupationcards.OccupationCard;
import ie.ucd.engac.lifegamelogic.cards.occupationcards.OccupationCardDeck;

public class Bank {
	private final String HOUSE_CARD_DECK_CONFIG_FILE_LOCATION = "src/main/resources/CardDecks/HouseCardConfig.json";
	private final String CAREER_CARD_DECK_CONFIG_FILE_LOCATION = "src/main/resources/CardDecks/CareerCardConfig.json";
	private final String COLLEGE_CAREER_CARD_DECK_CONFIG_FILE_LOCATION = "src/main/resources/CardDecks/CollegeCareerCardConfig.json";

	private ActionCardDeck actionCardDeck;
	private HouseCardDeck houseCardDeck;
	private OccupationCardDeck careerCardDeck;
	private OccupationCardDeck collegeCareerCardDeck;
	private int totalMoneyExtracted;
	
	private BankLoanBook bankLoanBook;

	public Bank() {
		initialiseCardDecks();
		initialiseLoanBook();
	}	

	private void initialiseCardDecks() {
		// TODO: ActionCardDeck requires config reading functionality implementation?
		
		actionCardDeck = new ActionCardDeck();
		actionCardDeck.shuffle();	
		
		String houseCardDeckConfigString = FileUtilities.GetEntireContentsAsString(HOUSE_CARD_DECK_CONFIG_FILE_LOCATION);
		String careerCardDeckConfigString = FileUtilities.GetEntireContentsAsString(CAREER_CARD_DECK_CONFIG_FILE_LOCATION);
		String collegeCareerCardDeckConfigString = FileUtilities.GetEntireContentsAsString(COLLEGE_CAREER_CARD_DECK_CONFIG_FILE_LOCATION);

		houseCardDeck = new HouseCardDeck(houseCardDeckConfigString);
		houseCardDeck.shuffle();
		careerCardDeck = new OccupationCardDeck(careerCardDeckConfigString);
		careerCardDeck.shuffle();
		collegeCareerCardDeck = new OccupationCardDeck(collegeCareerCardDeckConfigString);
		collegeCareerCardDeck.shuffle();
	}
	
	private void initialiseLoanBook() {
		bankLoanBook = new BankLoanBook();
	}
	
	public int getNumLoans(int playerID) {
		return bankLoanBook.getOutstandingBankLoans(playerID).size();
	}

	public int getTotalMoneyExtracted() {
		return totalMoneyExtracted;
	}
	
	public void extractMoney(int amountToExtract) {
		setTotalMoneyExtracted(getTotalMoneyExtracted() + amountToExtract);
	}

	public void setTotalMoneyExtracted(int totalMoneyExtracted) {
		this.totalMoneyExtracted = totalMoneyExtracted;
	}

	public OccupationCard getTopStandardCareerCard() {
		return careerCardDeck.popTopCard();
	}

	public void returnStandardCareerCard(OccupationCard standardCareerCardToBeReturned) {
		careerCardDeck.addCardToBottom(standardCareerCardToBeReturned);
	}

    public OccupationCard getTopCollegeCareerCard() {
        return collegeCareerCardDeck.popTopCard();
    }

   public void returnCollegeCareerCard(OccupationCard collegeCareerCardToBeReturned) {
    	collegeCareerCardDeck.addCardToBottom(collegeCareerCardToBeReturned);
    }

	public HouseCard getTopHouseCard() {
       return houseCardDeck.popTopCard();
    }

	public ActionCard getTopActionCard(){
		return actionCardDeck.popTopCard();
	}

    public void returnHouseCard(HouseCard houseCardToBeReturned) {
        houseCardDeck.addCardToBottom(houseCardToBeReturned);
    }
}
