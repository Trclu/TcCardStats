package github.saukiya.angelcardstats.inventory;

import github.saukiya.angelcardstats.data.HolderData;
import github.saukiya.angelcardstats.data.SuitData;
import github.saukiya.angelcardstats.util.Message;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;

/*本类由 Saukiya 在 2018年2月26日 下午9:27:00 时创建
 *TIM:admin@Saukiya.cn
 *GitHub:https://github.com/Saukiya
 **/

public class SuitInventory {
    //TODO 展示套装列表
    static public void openSuitListInventory(Player player, int cardPage, int page) {
        String playerName = player.getName();
        int maxPage = SuitData.getSuitLoreMap().keySet().size() / 36 + 1;
        //这里的HolderData 只是为了存储CardInventory的page
        Inventory inv = Bukkit.createInventory(new HolderData(cardPage, playerName), 54, Message.getMsg(Message.INVENTORY_SUIT_LIST_NAME));
        ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§r");
        item.setItemMeta(meta);
        for (int i = 0; i < 9; i++) {//设置玻璃
            inv.setItem(i, item);
        }
        for (int i = 45; i < 54; i++) {
            inv.setItem(i, item);
        }
        //TODO 卡牌属性查看
        item.setType(Material.SKULL_ITEM);
        item.setDurability((short) 3);
        ItemMeta skullMeta = item.getItemMeta();
        ((SkullMeta) skullMeta).setOwner(playerName);
        skullMeta.setDisplayName(Message.getMsg(Message.INVENTORY_STATS_SKULL_NAME));
        item.setItemMeta(skullMeta);
        inv.setItem(4, item);
        item.setDurability((short) 0);

        item.setType(Material.ARROW);
        if (page < maxPage) {//下一页
            meta.setDisplayName(Message.getMsg(Message.INVENTORY_OPEN_PAGE_DOWN));
            item.setItemMeta(meta);
            item.setAmount(page + 1);
            inv.setItem(53, item);
        }

        if (page > 1) {//上一页
            meta.setDisplayName(Message.getMsg(Message.INVENTORY_OPEN_PAGE_UP));
            item.setItemMeta(meta);
            item.setAmount(page - 1);
            inv.setItem(45, item);
        }
        item.setAmount(1);

        item.setType(Material.ENCHANTED_BOOK);
        int i = 0;
        for (String suitName : SuitData.getSuitLoreMap().keySet()) {
            i++;
            if (i <= page * 36 - 36) continue;
            if (i > page * 36) break;
            meta.setDisplayName("§e" + suitName);
            List<String> suitLore = Message.getList(Message.INVENTORY_SUIT_LIST_SUIT_LORE, String.valueOf(SuitData.getSuitItemMap().get(suitName).size()), String.valueOf(SuitData.getSuitStatsMap().get(suitName).getCardValue()));
            suitLore.addAll(SuitData.getSuitLoreMap().get(suitName));
            suitLore.add(" ");
            suitLore.add(Message.getMsg(Message.INVENTORY_SUIT_LIST_CLICK));
            if (player.isOp()) {
                suitLore.add(" ");
                suitLore.add("§7§l§o此消息只有OP可见");
                suitLore.add("§c§l§o管理员可直接点击获取物品");
            }
            meta.setLore(suitLore);
            item.setItemMeta(meta);
            inv.setItem(i + 8, item);
        }
        player.openInventory(inv);
    }

    //TODO 展示套装
    static public void openSuitInventory(Player player, String suitName, int cardPage) {
        //这里的HolderData 只是为了存储CardInventory的page
        Inventory inv = Bukkit.createInventory(new HolderData(cardPage, null), 36, Message.getMsg(Message.INVENTORY_SUIT_NAME));
        ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§r");
        item.setItemMeta(meta);
        for (int i = 0; i < 9; i++) {//设置玻璃
            inv.setItem(i, item);
        }
        for (int i = 27; i < 36; i++) {
            inv.setItem(i, item);
        }
        //TODO 卡牌属性查看
        item.setType(Material.ENCHANTED_BOOK);
        item.setDurability((short) 3);
        meta.setDisplayName(Message.getMsg(Message.INVENTORY_STATS_SKULL_NAME));
        meta.setLore(Message.getList(Message.INVENTORY_SUIT_LORE, suitName));
        item.setItemMeta(meta);
        inv.setItem(4, item);
        item.setDurability((short) 0);

        for (ItemStack cardItem : SuitData.getSuitItemMap().get(suitName)) {
            inv.addItem(cardItem);
        }
        player.openInventory(inv);

    }
}
