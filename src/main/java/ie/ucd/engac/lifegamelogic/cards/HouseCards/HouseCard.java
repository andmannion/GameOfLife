package ie.ucd.engac.lifegamelogic.cards.HouseCards;

import ie.ucd.engac.lifegamelogic.cards.Card;

public class HouseCard extends Card{
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

    public String convertDrawableString(){
        String string = new String();
        string.concat("Type: " + houseType.toString());
        string.concat(" Cost: " + purchasePrice);
        string.concat(" Sale (odd/even): " + spinForSalePriceOddNum + "/" + spinForSalePriceEvenNum);
        return string;
    }
}
