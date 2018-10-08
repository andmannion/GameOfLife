package ie.ucd.engac.lifegamelogic.cards.CareerCards;

import java.util.ArrayList;
import java.util.Arrays;

import com.google.gson.Gson;

import ie.ucd.engac.lifegamelogic.cards.CardConfigHandler;

public class DefaultCareerCardConfigHandler implements CardConfigHandler<CareerCard> {
	private String jsonString;

	public DefaultCareerCardConfigHandler(String jsonString) {
		this.jsonString = jsonString;
	}

	public ArrayList<CareerCard> initialiseCards() {
		// Parse the json data into an array of HouseCards
		CareerCard[] careerCards = new Gson().fromJson(jsonString, CareerCard[].class);

		return new ArrayList<CareerCard>(Arrays.asList(careerCards));
	}
}