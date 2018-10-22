package ie.ucd.engac.lifegamelogic.cards.housecards;

import ie.ucd.engac.lifegamelogic.cards.Card;
import ie.ucd.engac.messaging.Chooseable;

public class HouseCard extends Card implements Chooseable {
	private final HouseTypes houseType;				// Values are final as their value should not change after having been initialised to some value
	
	public HouseTypes getHouseType() {
		return houseType;
	}
	
	private final int purchasePrice;				// Should this be "generified" so that floats could possibly be used?
	
	public int getPurchasePrice() {
		return purchasePrice;
	}
	
	private final int spinForSalePriceOddNum;
	
	public int getSpinForSalePriceOddNum() {
		return spinForSalePriceOddNum;
	}
	
	private final int spinForSalePriceEvenNum;
	
	public int getSpinForSalePriceEvenNum() {
		return spinForSalePriceEvenNum;
	}
	
	public HouseCard(HouseTypes houseType,
					 int purchasePrice,
					 int spinForSalePriceOddNum,
					 int spinForSalePriceEvenNum) {
		
		this.houseType = houseType;
		this.purchasePrice = purchasePrice;
		this.spinForSalePriceOddNum = spinForSalePriceOddNum;
		this.spinForSalePriceEvenNum = spinForSalePriceEvenNum;
	}

	@Override
	public String displayChoiceDetails() {
		String string = "";
		string = string.concat("Type: " + houseType.toString());
		string = string.concat(" Cost: " + purchasePrice);
		string = string.concat(" Sale (odd/even): " + spinForSalePriceOddNum + "/" + spinForSalePriceEvenNum + " \n");
		return string;
	}
}