package ie.ucd.engac.lifegamelogic.playerlogic;

import java.util.Comparator;

public class PlayerMoneyComparatorDescending implements Comparator<Player> {
    public int compare(Player firstPlayer, Player secondPlayer) {
        return (secondPlayer.getCurrentMoney() - firstPlayer.getCurrentMoney());
    }
}
