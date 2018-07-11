package github.saukiya.angelcardstats.api;

import github.saukiya.angelcardstats.data.*;
import github.saukiya.angelcardstats.util.Config;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.List;

/*本类由 Saukiya 在 2018年3月2日 下午4:30:15 时创建
 *TIM:admin@Saukiya.cn
 *GitHub:https://github.com/Saukiya
 *
 *PS:虽然API我允许get离线玩家，但是保险起见，还请用在线玩家的名称吧。
 **/

public class AngelCardStatsAPI {
    //获取玩家是否拥有一个在Items.yml内的物品 (此处为物品编号，并非DisplayerName)
    static public Boolean isCardItem(String playerName, String itemName) {
        List<ItemStack> itemList = PlayerData.getPlayerItem(playerName);
        ItemStack item = ItemData.getItem(itemName);
        if (itemList.contains(item)) return true;
        return false;
    }

    //获取玩家的卡牌点数
    static public Double getCardValue(String playerName) {
        double value = 0;
        StatsData statsData = StatsDataRead.getPlayerData(playerName);
        value += statsData.getCardValue();
        return value;
    }

    //获取玩家的StatsData属性
    static public StatsData getPlayerStats(String playerName) {
        return StatsDataRead.getPlayerData(playerName);
    }

    //检查玩家是否有套装 suitName为套装名称，例如Suit.Default.Name
    @SuppressWarnings("deprecation")
    static public Boolean isSuit(String playerName, String suitName) {
        List<ItemStack> itemList = PlayerData.loadPlayerItem(playerName);
        int level = PlayerData.getPlayerDataLevel(playerName);
        if (Bukkit.getOfflinePlayer(playerName).isOnline()) {
            level = Bukkit.getPlayer(playerName).getLevel();
        }
        for (int i = itemList.size() - 1; i >= 0; i--) {
            ItemStack item = itemList.get(i);
            if (item != null && item.hasItemMeta() && item.getItemMeta().hasLore()) {
                ItemMeta meta = item.getItemMeta();
                if (meta.hasLore()) {
                    List<String> loreList = meta.getLore();
                    for (String lore : loreList) {
                        if (lore.contains(Config.getString(Config.NAME_LIMIT_LEVEL))) {
                            if (Integer.valueOf(StatsDataRead.getStats(lore)) > level) {
                                itemList.remove(i);
                            }
                        }
                    }
                }
            }
        }
        if (SuitData.getSuitItemMap().containsKey(suitName)) {
            List<ItemStack> suitItemList = SuitData.getSuitItemMap().get(suitName);
            if (itemList.containsAll(suitItemList)) {
                return true;
            }
        }
        return false;
    }
}
