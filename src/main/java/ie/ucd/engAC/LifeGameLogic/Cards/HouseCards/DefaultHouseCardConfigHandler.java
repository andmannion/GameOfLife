package main.java.ie.ucd.engAC.LifeGameLogic.Cards.HouseCards;

import java.util.ArrayList;
import java.util.Arrays;

import com.google.gson.Gson;

public class DefaultHouseCardConfigHandler implements HouseCardConfigHandler {
	private String jsonString;
	
	public DefaultHouseCardConfigHandler(String jsonString) {
		this.jsonString = jsonString;
	}
	
	public ArrayList<HouseCard> initialiseCards(){
		// Parse the json data into an array of HouseCards		
		HouseCard[] houseCards = new Gson().fromJson(jsonString, HouseCard[].class);
		
		return new ArrayList<HouseCard>(Arrays.asList(houseCards));
	}
}
