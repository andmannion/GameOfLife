package ie.ucd.engac.lifegamelogic.banklogic;

import ie.ucd.engac.GameConfig;
import ie.ucd.engac.lifegamelogic.cards.actioncards.ActionCard;
import ie.ucd.engac.lifegamelogic.cards.actioncards.ActionCardDeck;
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
		
		actionCardDeck = new ActionCardDeck(GameConfig.action_card_deck_config_file_location);
		actionCardDeck.shuffle();
		houseCardDeck = new HouseCardDeck(GameConfig.house_card_deck_config_file_location);
		houseCardDeck.shuffle();
		careerCardDeck = new OccupationCardDeck(GameConfig.career_card_deck_config_file_location);
		careerCardDeck.shuffle();
		collegeCareerCardDeck = new OccupationCardDeck(GameConfig.college_career_card_deck_config_file_location);
		collegeCareerCardDeck.shuffle();
	}
	
	private void initialiseLoanBook() {
		bankLoanBook = new BankLoanBook();
	}

	public int takeOutALoan(int playerNumber){
        extractMoney(GameConfig.loan_amount);
        bankLoanBook.addBorrowerLoan(playerNumber, GameConfig.loan_amount, GameConfig.repayment_amount);
        return GameConfig.loan_amount;
    }

	public void repayAllLoans(int playerNumber){
        bankLoanBook.repayAllLoans(playerNumber);
    }

    public int getNumberOfOutstandingLoans(int playerNumber) {
        return bankLoanBook.getNumberOfOutstandingBankLoans(playerNumber);
    }

    public int getOutstandingLoanTotal(int playerNumber) {
        return bankLoanBook.getOutstandingBankLoanTotal(playerNumber);
    }

    public int getTotalMoneyExtracted() {
		return totalMoneyExtracted;
	}
	
	public void extractMoney(int amountToExtract) {
		setTotalMoneyExtracted(getTotalMoneyExtracted() + amountToExtract);
	}

	private void setTotalMoneyExtracted(int totalMoneyExtracted) {
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
