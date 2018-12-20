package ie.ucd.engac.lifegamelogic.cards.actioncards;

import com.google.gson.*;
import ie.ucd.engac.gsonExtender.RuntimeTypeAdapterFactory;
import ie.ucd.engac.lifegamelogic.cards.CardConfigHandler;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class DefaultActionCardConfigHandler implements CardConfigHandler<ActionCard> {
    private JsonElement overallJSONElement;

    public DefaultActionCardConfigHandler(String configPath){
        overallJSONElement = resourceToJson(configPath);
    }

    public ArrayList<ActionCard> initialiseCards(){
        ArrayList<JsonArray> aggregateActionCardsAsJsonArrays = new ArrayList<>();

        RuntimeTypeAdapterFactory<ActionCard> actionCardAdapterFactory = RuntimeTypeAdapterFactory
                .of(ActionCard.class, "actionCardSubtype");
        actionCardAdapterFactory.registerSubtype(CareerChangeActionCard.class);
        actionCardAdapterFactory.registerSubtype(PlayersPayActionCard.class);
        actionCardAdapterFactory.registerSubtype(PayTheBankActionCard.class);
        actionCardAdapterFactory.registerSubtype(GetCashFromBankActionCard.class);

        Gson gson = new GsonBuilder().registerTypeAdapterFactory(actionCardAdapterFactory).create();

        JsonArray careerChangeCardsAsJsonArray = ((JsonObject) overallJSONElement).getAsJsonArray(ActionCardTypes.CareerChange.toString());
        JsonArray playersPayCardsAsJsonArray = ((JsonObject) overallJSONElement).getAsJsonArray(ActionCardTypes.PlayersPay.toString());
        JsonArray payTheBankCardsAsJsonArray = ((JsonObject) overallJSONElement).getAsJsonArray(ActionCardTypes.PayTheBank.toString());
        JsonArray getCashFromTheBankCardsAsJsonArray = ((JsonObject) overallJSONElement).getAsJsonArray(ActionCardTypes.GetCashFromBank.toString());

        aggregateActionCardsAsJsonArrays.add(careerChangeCardsAsJsonArray);
        aggregateActionCardsAsJsonArrays.add(playersPayCardsAsJsonArray);
        aggregateActionCardsAsJsonArrays.add(payTheBankCardsAsJsonArray);
        aggregateActionCardsAsJsonArrays.add(getCashFromTheBankCardsAsJsonArray);

        ArrayList<ActionCard> actionCards = new ArrayList<>();

        for(JsonArray subElementArray : aggregateActionCardsAsJsonArrays) {
            for (JsonElement actionCardAsJsonObj : subElementArray) {
                ActionCard actionCard = gson.fromJson(actionCardAsJsonObj, ActionCard.class);
                actionCards.add(actionCard);
            }
        }
        return actionCards;
    }

    private JsonElement resourceToJson(String path){

        InputStream boardInputStream = DefaultActionCardConfigHandler.class.getClassLoader().getResourceAsStream(path);
        JsonStreamParser streamParser = new JsonStreamParser(new InputStreamReader(boardInputStream));
        JsonElement jsonElement = null;

        try {
            jsonElement = streamParser.next();
        } catch (Exception e) {
            System.err.println("Exception in ActionCardDeck...resourceToJson(): \n" + e.toString());
            System.exit(-1);
        }
        return jsonElement;
    }
}
