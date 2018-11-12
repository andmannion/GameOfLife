package ie.ucd.engac.lifegamelogic.cards.housecards;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonStreamParser;
import ie.ucd.engac.lifegamelogic.cards.CardConfigHandler;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public class DefaultHouseCardConfigHandler implements CardConfigHandler<HouseCard> {
	private JsonElement jsonElement;

	public DefaultHouseCardConfigHandler(String configPath) {
		this.jsonElement = resourceToJson(configPath);
	}

	public ArrayList<HouseCard> initialiseCards() {
		// Parse the json data into an array of HouseCards
		HouseCard[] houseCards = new Gson().fromJson(jsonElement, HouseCard[].class);

		return new ArrayList<>(Arrays.asList(houseCards));
	}

	private JsonElement resourceToJson(String path) {
		InputStream boardInputStream = DefaultHouseCardConfigHandler.class.getClassLoader().getResourceAsStream(path);
		JsonStreamParser streamParser = new JsonStreamParser(new InputStreamReader(boardInputStream));
		JsonElement jsonElement = null;

		try {
			jsonElement = (JsonElement) streamParser.next();
		} catch (Exception e) {
			System.err.println("Exception in LogicGameBoard...initialiseParser(): \n" + e.toString());
			System.exit(-1);
		}
		return jsonElement;
	}
}
