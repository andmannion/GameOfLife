package ie.ucd.engac.lifegamelogic.banklogic;

public class BankLoan {
	private int amount;
    private int repaymentAmount;
	
	public int getAmount() {
		return amount;
	}

    public int getPayBackAmount() {
        return repaymentAmount;
    }
	
	public BankLoan(int amount, int repaymentAmount) {
		this.amount = amount;
		this.repaymentAmount = repaymentAmount;
	}
}
