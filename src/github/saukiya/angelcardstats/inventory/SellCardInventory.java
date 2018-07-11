package github.saukiya.angelcardstats.inventory;

import github.saukiya.angelcardstats.util.Message;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class SellCardInventory {

    static public void openSellCardInventory(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, Message.getMsg(Message.INVENTORY_SELL_CARD_NAME));
        ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§r");
        item.setItemMeta(meta);
        for (int i = 0; i < 9; i++) {
            inv.setItem(i, item);
        }
        for (int i = 18; i < 27; i++) {
            inv.setItem(i, item);
        }
        //头颅
        item.setType(Material.SKULL_ITEM);
        item.setDurability((short) 3);
        ItemMeta skullMeta = item.getItemMeta();
        ((SkullMeta) skullMeta).setOwner(player.getName());
        skullMeta.setDisplayName(Message.getMsg(Message.INVENTORY_SELL_CARD_SKULL_NAME));
        item.setItemMeta(skullMeta);
        inv.setItem(4, item);
        item.setDurability((short) 0);
        //TODO 卡牌确认购买
        item.setType(Material.BLAZE_POWDER);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.setDisplayName(Message.getMsg(Message.INVENTORY_SELL_CARD_SELL_CARD));
        item.setItemMeta(meta);
        inv.setItem(22, item);
        player.openInventory(inv);
    }

}
