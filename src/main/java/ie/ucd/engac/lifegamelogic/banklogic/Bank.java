package ie.ucd.engac.lifegamelogic.banklogic;

import ie.ucd.engac.GameConfig;
import ie.ucd.engac.lifegamelogic.cards.actioncards.ActionCard;
import ie.ucd.engac.lifegamelogic.cards.housecards.HouseCard;
import ie.ucd.engac.lifegamelogic.cards.occupationcards.OccupationCard;
import ie.ucd.engac.lifegamelogic.cards.CardDeck;
import ie.ucd.engac.lifegamelogic.cards.actioncards.DefaultActionCardConfigHandler;
import ie.ucd.engac.lifegamelogic.cards.housecards.DefaultHouseCardConfigHandler;
import ie.ucd.engac.lifegamelogic.cards.occupationcards.DefaultOccupationCardConfigHandler;

public class Bank {
	private CardDeck<ActionCard> actionCardDeck;
	private CardDeck<HouseCard> houseCardDeck;
	private CardDeck<OccupationCard> careerCardDeck;
	private CardDeck<OccupationCard> collegeCareerCardDeck;
	private int totalMoneyExtracted;
	
	private BankLoanBook bankLoanBook;

	public Bank() {
		initialiseCardDecks();
		initialiseLoanBook();
	}	

	private void initialiseCardDecks() {
		
		actionCardDeck = new CardDeck<ActionCard>(new DefaultActionCardConfigHandler(GameConfig.action_card_deck_config_file_location));
		actionCardDeck.shuffle();
		houseCardDeck = new CardDeck<HouseCard>(new DefaultHouseCardConfigHandler(GameConfig.house_card_deck_config_file_location));
		houseCardDeck.shuffle();
		careerCardDeck = new CardDeck<OccupationCard>(new DefaultOccupationCardConfigHandler(GameConfig.career_card_deck_config_file_location));
		careerCardDeck.shuffle();
		collegeCareerCardDeck = new CardDeck<OccupationCard>(new DefaultOccupationCardConfigHandler(GameConfig.college_career_card_deck_config_file_location));
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
