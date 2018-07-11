package github.saukiya.angelcardstats.inventory;

import github.saukiya.angelcardstats.data.HolderData;
import github.saukiya.angelcardstats.data.PlayerData;
import github.saukiya.angelcardstats.util.Config;
import github.saukiya.angelcardstats.util.Message;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class CardInventory {

    //为什么会用到playerNames 让管理员能打开玩家的卡牌菜单
    static public void openCardInventory(Player openInvPlayer, int page, String... playerNames) {//打开
        String playerName = openInvPlayer.getName();
        if (playerNames.length > 0) {
            playerName = playerNames[0];
            if (!PlayerData.isPlayer(playerName)) {
                openInvPlayer.sendMessage(Message.getMsg(Message.ADMIN_NO_ONLINE));
                return;
            }
        }
        //get 玩家当前所拥有的卡牌槽数量：默认槽+购买槽(防止之后修改默认槽后出现的变数)
        int buySlot = PlayerData.getBuySlot(playerName);
        int slot = Integer.valueOf(Config.getString(Config.PLAYER_DEFAULT_SLOT)) + buySlot;
        int maxPage = slot / 36 + 1;//获得最大页数
        Inventory inv = Bukkit.createInventory(new HolderData(page, playerName), 54, Message.getMsg(Message.INVENTORY_OPEN_NAME));
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
        skullMeta.setDisplayName(Message.getMsg(Message.INVENTORY_OPEN_SKULL_NAME));
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
        //TODO 出售卡牌按钮
        if (openInvPlayer.getName().equals(playerName)) {//只有打开菜单的人是本人，才会显示这几个按钮
            if (Config.getString(Config.PLAYER_SELL_CARD_ENABLED).equals("true")) {
                item.setType(Material.BLAZE_POWDER);
                meta.setDisplayName(Message.getMsg(Message.INVENTORY_OPEN_SELL_CARD));
                item.setItemMeta(meta);
                inv.setItem(47, item);
            }
            //TODO 购买卡槽按钮
            if (Config.getString(Config.PLAYER_BUY_SLOT_ENABLED).equals("true")) {
                item.setType(Material.GOLD_INGOT);
                meta.setDisplayName(Message.getMsg(Message.INVENTORY_OPEN_BUY_SLOT_NAME));
                meta.setLore(Message.getList(Message.INVENTORY_OPEN_BUY_SLOT_LORE, String.valueOf(slot), String.valueOf(Config.getInt(Config.PLAYER_BUY_SLOT_POINTS) + (Config.getInt(Config.PLAYER_BUY_SLOT_VALUE_POINTS) * buySlot))));
                item.setItemMeta(meta);
                inv.setItem(51, item);
                meta.setLore(null);
            }
            //TODO 套装展示
            item.setType(Material.ENCHANTED_BOOK);
            meta.setDisplayName(Message.getMsg(Message.INVENTORY_OPEN_SUIT));
            item.setItemMeta(meta);
            inv.setItem(49, item);
        }

        //TODO 防止以前的物品
        YamlConfiguration playerData = PlayerData.getPlayerData(playerName);
        for (int i = 9; i < 45; i++) {
            ItemStack cardItem = playerData.getItemStack("CardItem." + (page * 36 + i - 36));
            if (cardItem != null) {
                inv.setItem(i, cardItem);
            }
        }

        //根据 slot 锁槽
        slot = slot - ((page - 1) * 36);//现在的slot值为24
        item.setType(Material.BARRIER);
        meta.setDisplayName(Message.getMsg(Message.INVENTORY_OPEN_LOCK_SLOT));
        item.setItemMeta(meta);
        for (int i = 9; i < 45; i++) {
            if (i - slot - 9 >= 0) {
                inv.setItem(i, item);
            }
        }
        openInvPlayer.openInventory(inv);
    }

}
