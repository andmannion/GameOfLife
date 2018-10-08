package ie.ucd.engac.lifegamelogic.cards.CollegeCareerCards;

import java.util.ArrayList;
import java.util.Arrays;

import com.google.gson.Gson;

import ie.ucd.engac.lifegamelogic.cards.CardConfigHandler;

public class DefaultCollegeCareerCardConfigHandler implements CardConfigHandler<CollegeCareerCard> {
	private String jsonString;

	public DefaultCollegeCareerCardConfigHandler(String jsonString) {
		this.jsonString = jsonString;
	}

	public ArrayList<CollegeCareerCard> initialiseCards() {
		// Parse the json data into an array of HouseCards
		CollegeCareerCard[] collegeCareerCards = new Gson().fromJson(jsonString, CollegeCareerCard[].class);

		return new ArrayList<CollegeCareerCard>(Arrays.asList(collegeCareerCards));
	}
}