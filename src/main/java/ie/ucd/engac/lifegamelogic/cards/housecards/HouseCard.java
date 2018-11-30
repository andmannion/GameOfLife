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

	private final int spinForSalePriceEvenNum;

    /**
     *
     * @param oddNumberWasSpun true if an odd number was spun, false if even
     * @return the sale price as an integer
     */
	public int getSpinForSalePrice(boolean oddNumberWasSpun) {
		if(oddNumberWasSpun) {
            return spinForSalePriceOddNum;
        }
        else{
            return spinForSalePriceEvenNum;
        }
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
		string = string.concat("Type: " + houseType.toString() + "\n");
		string = string.concat(" Cost: " + purchasePrice + "\n");
		string = string.concat(" Sale (odd/even):\n" + spinForSalePriceOddNum + "/" + spinForSalePriceEvenNum + " \n");
		return string;
	}
}