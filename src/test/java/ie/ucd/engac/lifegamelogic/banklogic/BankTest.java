package ie.ucd.engac.lifegamelogic.banklogic;

import TestOnly.TestHelpers;
import ie.ucd.engac.GameConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BankTest {
    private Bank bank;

    @BeforeEach
    void setUp() {
        TestHelpers.importGameConfig();
        bank = new Bank();
    }

    @AfterEach
    void tearDown() {
        bank = null;
    }

    @Test
    void takeOutALoan() {
        int totalMoneyExtracted = bank.getTotalMoneyExtracted();
        int playerNumber = 1;
        int loanAmount = bank.takeOutALoan(playerNumber);
        assertEquals(GameConfig.loan_amount,loanAmount,"incorrect loan amount");
        assertEquals(totalMoneyExtracted+GameConfig.loan_amount,bank.getTotalMoneyExtracted(),"Extracted money wrong");
        assertEquals(1, bank.getNumberOfOutstandingLoans(playerNumber));
        assertEquals(GameConfig.repayment_amount, bank.getOutstandingLoanTotal(playerNumber));
    }
}