package ie.ucd.engac.lifegamelogic.banklogic;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * BORROWER ID IS THE PLAYERS NUMBER NOT INDEX
 */

public class BankLoanBook {
	private HashMap<Integer, ArrayList<BankLoan>> borrowerToLoansMap;
	
	BankLoanBook() {
		borrowerToLoansMap = new HashMap<>();
	}
	
	public void addBorrowerLoan(int borrowerID, int loanAmount, int repaymentAmount) {
		if(borrowerToLoansMap.containsKey(borrowerID)) {
			borrowerToLoansMap.get(borrowerID).add(new BankLoan(loanAmount,repaymentAmount));

		}
		else {
            ArrayList<BankLoan> bankLoans = new ArrayList<>();
            bankLoans.add(new BankLoan(loanAmount, repaymentAmount));

            borrowerToLoansMap.put(borrowerID, bankLoans);
        }
	}
	
	private ArrayList<BankLoan> getOutstandingBankLoans(int borrowerID){
		ArrayList<BankLoan> outstandingLoans = new ArrayList<>();
		
		if(borrowerToLoansMap.containsKey(borrowerID)) {
			outstandingLoans = borrowerToLoansMap.get(borrowerID);
		}
		
		return outstandingLoans;
	}

    int getNumberOfOutstandingBankLoans(int borrowerID){
        ArrayList<BankLoan> outstandingLoans = new ArrayList<>();

        if(borrowerToLoansMap.containsKey(borrowerID)) {
            outstandingLoans = borrowerToLoansMap.get(borrowerID);
        }

        return outstandingLoans.size();
    }

    int getOutstandingBankLoanTotal(int borrowerID){
		ArrayList<BankLoan> loans = getOutstandingBankLoans(borrowerID);
		int outstandingLoanTotal = 0;
		for (BankLoan loan:loans){
            outstandingLoanTotal += loan.getPayBackAmount();
        }
        return outstandingLoanTotal;
	}
	
	void repayAllLoans(int borrowerID) {
		borrowerToLoansMap.remove(borrowerID);
	}
}
