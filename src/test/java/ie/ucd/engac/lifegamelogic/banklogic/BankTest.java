package ie.ucd.engac.lifegamelogic.banklogic;

import ie.ucd.engac.GameConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BankTest {
    private Bank bank;

    @BeforeEach
    void setUp() {
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

    }

}