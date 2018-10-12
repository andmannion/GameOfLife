package ie.ucd.engac.lifegamelogic.cards.occupationcards.careercards;

import java.util.ArrayList;
import java.util.Arrays;

import com.google.gson.Gson;

import ie.ucd.engac.gsonExtender.RuntimeTypeAdapterFactory;
import ie.ucd.engac.lifegamelogic.cards.CardConfigHandler;
import ie.ucd.engac.lifegamelogic.cards.occupationcards.OccupationCard;
import ie.ucd.engac.lifegamelogic.cards.occupationcards.collegecareercards.CollegeCareerCard;
import ie.ucd.engac.lifegamelogic.gameboardlogic.gameboardtiles.GameBoardTile;

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