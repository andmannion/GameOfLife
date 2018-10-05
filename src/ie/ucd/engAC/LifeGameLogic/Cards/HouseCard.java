package ie.ucd.engAC.LifeGameLogic.Cards;

import ie.ucd.engAC.LifeGameLogic.HouseType;

public class HouseCard extends Card{
	private HouseType houseType;
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
