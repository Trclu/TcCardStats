package github.saukiya.angelcardstats.data;

import github.saukiya.angelcardstats.util.Config;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.List;

/*本类由 Saukiya 在 2018年3月4日 下午1:13:10 时创建
 *TIM:admin@Saukiya.cn
 *GitHub:https://github.com/Saukiya
 **/

public class SuitData {
    //套装名以及所需物品
    private static Map<String, List<ItemStack>> suitItemMap = new HashMap<>();
    //套装触发成功后给予的属性
    private static Map<String, StatsData> suitStatsMap = new HashMap<>();
    //套装名以及所需物品
    private static Map<String, List<String>> suitLoreMap = new HashMap<>();

    public static void loadSuitData() {
        getSuitItemMap().clear();//清除套装
        getSuitStatsMap().clear();//清除属性
        Bukkit.getConsoleSender().sendMessage("[AngelCardStats] §aLoad SuitData!");
        ItemStack item = new ItemStack(Material.APPLE);
        ItemMeta itemMeta = item.getItemMeta();
        List<ItemStack> items;
        items = new ArrayList<ItemStack>();
        items.add(item);
        for (String name : Config.getConfig().getConfigurationSection("Suit").getKeys(false)) {
            String suitName = Config.getString("Suit." + name + ".Name");
            //获取套装内物品
            List<String> itemNameList = Config.getList("Suit." + name + ".List");
            List<ItemStack> itemList = new ArrayList<>();
            for (String itemName : itemNameList) {
                ItemStack getItem = ItemData.getItem(itemName);
                if (getItem != null) {
                    itemList.add(getItem);
                } else {
                    Bukkit.getConsoleSender().sendMessage("[AngelCardStats] §c套装: §b" + name + " §c中的 §d" + itemName + " §c获取失败!");
                }
            }
            if (itemList.size() != itemNameList.size()) {
                Bukkit.getConsoleSender().sendMessage("[AngelCardStats] §c套装: §b" + name + " §c卸载!");
                continue;
            }
            getSuitItemMap().put(suitName, itemList);
            //设定属性
            List<String> itemLoreList = Config.getList("Suit." + name + ".Effect");
            itemMeta.setLore(itemLoreList);
            item.setItemMeta(itemMeta);
            items.set(0, item);
            StatsData data = StatsDataRead.getItemStatsData(null, items);
            getSuitStatsMap().put(suitName, data);
            getSuitLoreMap().put(suitName, itemLoreList);
        }
    }

    public static Map<String, List<ItemStack>> getSuitItemMap() {
        return suitItemMap;
    }

    public static Map<String, StatsData> getSuitStatsMap() {
        return suitStatsMap;
    }

    public static Map<String, List<String>> getSuitLoreMap() {
        return suitLoreMap;
    }

}
