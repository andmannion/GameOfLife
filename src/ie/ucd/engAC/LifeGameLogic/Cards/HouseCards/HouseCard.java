package ie.ucd.engAC.LifeGameLogic.Cards.HouseCards;

import ie.ucd.engAC.LifeGameLogic.Cards.Card;

public class HouseCard extends Card{
	private final HouseType houseType;				// Values are final as their value should not change after having been initialised to some value
	private final int purchasePrice;				// Should this be "generified" so that floats could possibly be used?
	private final int spinForSalePriceOddNum;
	private final int spinForSalePriceEvenNum;
	
	public HouseCard(HouseType houseType,
					 int purchasePrice,
					 int spinForSalePriceOddNum,
					 int spinForSalePriceEvenNum) {
		
		this.houseType = houseType;
		this.purchasePrice = purchasePrice;
		this.spinForSalePriceOddNum = spinForSalePriceOddNum;
		this.spinForSalePriceEvenNum = spinForSalePriceEvenNum;
	}
}
