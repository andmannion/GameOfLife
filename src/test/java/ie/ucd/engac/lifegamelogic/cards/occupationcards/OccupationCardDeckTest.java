package ie.ucd.engac.lifegamelogic.cards.occupationcards;

import TestOnly.TestHelpers;
import ie.ucd.engac.GameConfig;
import ie.ucd.engac.lifegamelogic.cards.CardDeck;
import ie.ucd.engac.lifegamelogic.cards.occupationcards.careercards.CareerCard;
import ie.ucd.engac.lifegamelogic.cards.occupationcards.careercards.CareerTypes;
import ie.ucd.engac.lifegamelogic.cards.occupationcards.collegecareercards.CollegeCareerCard;
import ie.ucd.engac.lifegamelogic.cards.occupationcards.collegecareercards.CollegeCareerTypes;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OccupationCardDeckTest {
    @Test
    void TestAllCollegeCareerCardsArePresent(){
        int DEFAULT_NUM_COLLEGE_CAREER_CARDS = CollegeCareerTypes.values().length;

        TestHelpers.importGameConfig();

        CardDeck<OccupationCard> testCollegeCareerCardDeck = new CardDeck<>(new DefaultOccupationCardConfigHandler(GameConfig.college_career_card_deck_config_file_location));

        // Must assert that the 12 unique CollegeCareerCards are present
        HashMap<CollegeCareerTypes, CollegeCareerCard> collegeCareerCardHashSet = new HashMap<>();

        int initialNumberOfCollegeCareerCards = testCollegeCareerCardDeck.getNumberOfRemainingCards();
        assertEquals(DEFAULT_NUM_COLLEGE_CAREER_CARDS, initialNumberOfCollegeCareerCards);

        int numCardsLeft = initialNumberOfCollegeCareerCards;

        while(numCardsLeft-- > 0){
            CollegeCareerCard collegeCareerCard = (CollegeCareerCard) testCollegeCareerCardDeck.popTopCard();
            collegeCareerCardHashSet.put(collegeCareerCard.getCareerType(), collegeCareerCard);
        }

        HashMap<CollegeCareerTypes, Boolean> foundMap = new HashMap<>();

        // Initialise expected types
        for (CollegeCareerTypes type:
             CollegeCareerTypes.values()) {
            foundMap.put(type, Boolean.FALSE);
        }

        // Go through each card in the Deck
        for (Map.Entry<CollegeCareerTypes, CollegeCareerCard> mapping:
             collegeCareerCardHashSet.entrySet()) {
            foundMap.put(mapping.getKey(), Boolean.TRUE);
        }

        // Make sure that each CollegeCareerType enum value has been found
        for (Boolean found:
             foundMap.values()) {
            assertTrue(found);
        }
        
        // Assert there are 12 unique cards present
        assertEquals(DEFAULT_NUM_COLLEGE_CAREER_CARDS, collegeCareerCardHashSet.size());
    }

    @Test
    void TestAllStandardCareerCardsArePresent(){
        int DEFAULT_NUM_STANDARD_CAREER_CARDS = CareerTypes.values().length;

        TestHelpers.importGameConfig();

        CardDeck<OccupationCard> testStandardCareerCardDeck = new CardDeck<>(new DefaultOccupationCardConfigHandler(GameConfig.career_card_deck_config_file_location));

        // Must assert that the 8 unique CareerCards are present
        HashMap<CareerTypes, CareerCard> careerCardHashSet = new HashMap<>();

        int initialNumberOfCollegeCareerCards = testStandardCareerCardDeck.getNumberOfRemainingCards();
        assertEquals(DEFAULT_NUM_STANDARD_CAREER_CARDS, initialNumberOfCollegeCareerCards);

        int numCardsLeft = initialNumberOfCollegeCareerCards;

        while(numCardsLeft-- > 0){
            CareerCard careerCard = (CareerCard) testStandardCareerCardDeck.popTopCard();
            careerCardHashSet.put(careerCard.getCareerType(), careerCard);
        }

        HashMap<CareerTypes, Boolean> foundMap = new HashMap<>();

        // Initialise expected types
        for (CareerTypes type:
                CareerTypes.values()) {
            foundMap.put(type, Boolean.FALSE);
        }

        // Go through each card in the Deck
        for (Map.Entry<CareerTypes, CareerCard> mapping:
                careerCardHashSet.entrySet()) {
            foundMap.put(mapping.getKey(), Boolean.TRUE);
        }

        // Make sure that each CollegeCareerType enum value has been found
        for (Boolean found:
                foundMap.values()) {
            assertTrue(found);
        }

        // Assert there are 12 unique cards present
        assertEquals(DEFAULT_NUM_STANDARD_CAREER_CARDS, careerCardHashSet.size());
    }
}