package main.java.ie.ucd.engAC.LifeGameLogic.Cards.HouseCards;

import main.java.ie.ucd.engAC.LifeGameLogic.Cards.Card;

public class HouseCard extends Card{
	private final HouseType houseType;				// Values are final as their value should not change after having been initialised to some value
	
	public HouseType getHouseType() {
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
