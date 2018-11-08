package ie.ucd.engac.lifegamelogic.playerlogic;

import java.util.Comparator;

public class PlayerMoneyComparator implements Comparator<Player> {
    public int compare(Player firstPlayer, Player secondPlayer) {
        return (firstPlayer.getCurrentMoney() - secondPlayer.getCurrentMoney());
    }
}
