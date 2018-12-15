package ie.ucd.engac.lifegamelogic.cards.occupationcards;

import com.google.gson.*;
import ie.ucd.engac.gsonExtender.RuntimeTypeAdapterFactory;
import ie.ucd.engac.lifegamelogic.cards.CardConfigHandler;
import ie.ucd.engac.lifegamelogic.cards.occupationcards.careercards.CareerCard;
import ie.ucd.engac.lifegamelogic.cards.occupationcards.collegecareercards.CollegeCareerCard;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class DefaultOccupationCardConfigHandler implements CardConfigHandler<OccupationCard> {
	private JsonElement overallJSONElement;

	public DefaultOccupationCardConfigHandler(String configPath) {
		overallJSONElement = resourceToJson(configPath);
	}

	@Override
	public ArrayList<OccupationCard> initialiseCards() {
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

	private JsonElement resourceToJson(String path){

		InputStream boardInputStream = DefaultOccupationCardConfigHandler.class.getClassLoader().getResourceAsStream(path);
		JsonStreamParser streamParser = new JsonStreamParser(new InputStreamReader(boardInputStream));
		JsonElement jsonElement = null;

		try {
			jsonElement = (JsonElement) streamParser.next();
		} catch (Exception e) {
			System.err.println("Exception in GameBoard...initialiseParser(): \n" + e.toString());
			System.exit(-1);
		}
		return jsonElement;
	}
}
