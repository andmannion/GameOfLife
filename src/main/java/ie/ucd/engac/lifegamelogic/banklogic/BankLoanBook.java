package ie.ucd.engac.lifegamelogic.banklogic;

import java.util.ArrayList;
import java.util.HashMap;

public class BankLoanBook {
	private HashMap<Integer, ArrayList<BankLoan>> borrowerToLoansMap;
	
	public BankLoanBook() {
		borrowerToLoansMap = new HashMap<Integer, ArrayList<BankLoan>>();
	}
	
	public void addBorrowerLoan(int borrowerID, int loanAmount) {
		if(borrowerToLoansMap.containsKey(borrowerID)) {
			borrowerToLoansMap.get(borrowerID).add(new BankLoan(loanAmount));
			return;
		}
		
		ArrayList<BankLoan> bankLoans = new ArrayList<BankLoan>();
		bankLoans.add(new BankLoan(loanAmount));
		
		borrowerToLoansMap.put(borrowerID, bankLoans);
	}
	
	public ArrayList<BankLoan> getOutstandingBankLoans(int borrowerID){
		ArrayList<BankLoan> outstandingLoans = new ArrayList<BankLoan>();
		
		if(borrowerToLoansMap.containsKey(borrowerID)) {
			outstandingLoans = borrowerToLoansMap.get(borrowerID);
		}
		
		return outstandingLoans;
	}
	
	public void repayAllLoans(int borrowerID) {
		if(borrowerToLoansMap.containsKey(borrowerID)) {
			borrowerToLoansMap.remove(borrowerID);
		}
	}
}
