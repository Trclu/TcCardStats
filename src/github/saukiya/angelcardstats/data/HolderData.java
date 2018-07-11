package github.saukiya.angelcardstats.data;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

/*本类由 Saukiya 在 2018年2月27日 下午10:29:30 时创建
 *TIM:admin@Saukiya.cn
 *GitHub:https://github.com/Saukiya
 **/

public class HolderData implements InventoryHolder {
    private final int page;
    private final String playerName;

    public HolderData(int page, String playerName) {
        this.page = page;
        this.playerName = playerName;
    }

    public int getPage() {
        return page;
    }

    public String getPlayerName() {
        return playerName;
    }

    @Override
    public Inventory getInventory() {
        return null;
    }

}
