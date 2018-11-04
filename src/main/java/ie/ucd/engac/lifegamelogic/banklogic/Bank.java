package ie.ucd.engac.lifegamelogic.banklogic;

import ie.ucd.engac.GameConfig;
import ie.ucd.engac.fileutilities.FileUtilities;
import ie.ucd.engac.lifegamelogic.cards.actioncards.*;
import ie.ucd.engac.lifegamelogic.cards.housecards.HouseCard;
import ie.ucd.engac.lifegamelogic.cards.housecards.HouseCardDeck;
import ie.ucd.engac.lifegamelogic.cards.occupationcards.OccupationCard;
import ie.ucd.engac.lifegamelogic.cards.occupationcards.OccupationCardDeck;

public class Bank {
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
		
		String houseCardDeckConfigString = FileUtilities.GetEntireContentsAsString(GameConfig.house_card_deck_config_file_location);
		String careerCardDeckConfigString = FileUtilities.GetEntireContentsAsString(GameConfig.career_card_deck_config_file_location);
		String collegeCareerCardDeckConfigString = FileUtilities.GetEntireContentsAsString(GameConfig.college_career_card_deck_config_file_location);

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

	public int takeOutALoan(int playerIndex){
        extractMoney(GameConfig.loan_amount);
        bankLoanBook.addBorrowerLoan(playerIndex, GameConfig.loan_amount, GameConfig.repayment_amount);
        return GameConfig.loan_amount;
    }

	public void repayAllLoans(int playerIndex){
        bankLoanBook.repayAllLoans(playerIndex);
    }

    public int getNumberOfOutstandingLoans(int playerIndex) {
        return bankLoanBook.getNumberOfOutstandingBankLoans(playerIndex);
    }

    public int getOutstandingLoanTotal(int playerIndex) {
        return bankLoanBook.getOutstandingBankLoanTotal(playerIndex);
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
