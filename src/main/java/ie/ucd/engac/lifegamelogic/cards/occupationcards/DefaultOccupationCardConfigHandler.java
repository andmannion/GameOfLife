package ie.ucd.engac.lifegamelogic.cards.occupationcards;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import ie.ucd.engac.gsonExtender.RuntimeTypeAdapterFactory;
import ie.ucd.engac.lifegamelogic.cards.CardConfigHandler;
import ie.ucd.engac.lifegamelogic.cards.occupationcards.careercards.CareerCard;
import ie.ucd.engac.lifegamelogic.cards.occupationcards.collegecareercards.CollegeCareerCard;
import ie.ucd.engac.lifegamelogic.gameboardlogic.gameboardtiles.GameBoardTile;

public class DefaultOccupationCardConfigHandler implements CardConfigHandler<OccupationCard> {
	public ArrayList<OccupationCard> initialiseCards(){
		initialiseParser();
		
		RuntimeTypeAdapterFactory<OccupationCard> occupationCardAdapterFactory = RuntimeTypeAdapterFactory.of(OccupationCard.class,	"occupationSubtype");
		occupationCardAdapterFactory.registerSubtype(CareerCard.class);
		occupationCardAdapterFactory.registerSubtype(CollegeCareerCard.class);
		
		Gson gson = new GsonBuilder().registerTypeAdapterFactory(occupationCardAdapterFactory).create();

		for (JsonElement occupationCardAsJsonObj : occupationCardsAsJsonArray) {
			String id = ((JsonObject) vertexAsJsonObj).get("id").getAsString();
			JsonElement tileAsJsonObject = ((JsonObject) vertexAsJsonObj).get("gameBoardTile");

			GameBoardTile gameBoardTile = gson.fromJson(tileAsJsonObject, GameBoardTile.class);

			idToGameBoardTileMap.put(id, gameBoardTile);
		}
		
	}
	
	private void initialiseParser() {
		
	}
}
