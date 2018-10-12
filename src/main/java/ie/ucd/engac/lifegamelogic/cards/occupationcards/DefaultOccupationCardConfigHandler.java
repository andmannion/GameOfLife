package ie.ucd.engac.lifegamelogic.cards.occupationcards;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import ie.ucd.engac.gsonExtender.RuntimeTypeAdapterFactory;
import ie.ucd.engac.lifegamelogic.cards.CardConfigHandler;
import ie.ucd.engac.lifegamelogic.cards.occupationcards.careercards.CareerCard;
import ie.ucd.engac.lifegamelogic.cards.occupationcards.collegecareercards.CollegeCareerCard;
import ie.ucd.engac.lifegamelogic.gameboardlogic.gameboardtiles.GameBoardTile;

public class DefaultOccupationCardConfigHandler implements CardConfigHandler<OccupationCard> {
	private String jsonBoardLayout;
	private JsonElement overallJSONElement;	

	public DefaultOccupationCardConfigHandler(String jsonBoardLayout) {
		this.jsonBoardLayout = jsonBoardLayout;
	}

	@Override
	public ArrayList<OccupationCard> initialiseCards() {
		initialiseParser(jsonBoardLayout);

		JsonArray occupationCardsAsJsonArray = overallJSONElement.getAsJsonArray();

		RuntimeTypeAdapterFactory<OccupationCard> occupationCardAdapterFactory = RuntimeTypeAdapterFactory
				.of(OccupationCard.class, "occupationSubtype");
		occupationCardAdapterFactory.registerSubtype(CareerCard.class);
		occupationCardAdapterFactory.registerSubtype(CollegeCareerCard.class);

		Gson gson = new GsonBuilder().registerTypeAdapterFactory(occupationCardAdapterFactory).create();

		ArrayList<OccupationCard> occupationCards = new ArrayList<OccupationCard>();

		for (JsonElement occupationCardAsJsonObj : occupationCardsAsJsonArray) {
			OccupationCard occupationCard = gson.fromJson(occupationCardAsJsonObj, OccupationCard.class);
			occupationCards.add(occupationCard);
		}

		return occupationCards;
	}
	
	private void initialiseParser(String jsonBoardLayout) {
		JsonParser parser = new JsonParser();
		overallJSONElement = null;

		try {
			overallJSONElement = parser.parse(jsonBoardLayout);
		} catch (Exception e) {
			System.out.println("Exception in OccupationCardConfigHandler....initialiseParser(): \n" + e.toString());
			System.exit(-1);
		}
	}
}
