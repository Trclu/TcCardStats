package github.saukiya.angelcardstats.inventory;

import github.saukiya.angelcardstats.AngelCardStats;
import github.saukiya.angelcardstats.data.HolderData;
import github.saukiya.angelcardstats.data.StatsData;
import github.saukiya.angelcardstats.data.StatsDataRead;
import github.saukiya.angelcardstats.util.Config;
import github.saukiya.angelcardstats.util.Message;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.List;
import java.util.Random;

public class StatsInventory {

    static void statsInventory(Player openInvPlayer, int page, String playerName) {
        StatsData data = StatsDataRead.getPlayerData(playerName);
        Inventory inv = Bukkit.createInventory(new HolderData(page, playerName), 27, Message.getMsg(Message.INVENTORY_STATS_NAME));
        ItemStack stainedGlass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
        ItemMeta glassMeta = stainedGlass.getItemMeta();
        glassMeta.setDisplayName("§c");
        stainedGlass.setItemMeta(glassMeta);
        ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        ItemMeta skullmeta = skull.getItemMeta();
        skullmeta.setDisplayName(Message.getMsg(Message.INVENTORY_STATS_SKULL_NAME, openInvPlayer.getDisplayName()));
        ((SkullMeta) skullmeta).setOwner(playerName);
        skull.setItemMeta(skullmeta);
        for (int i = 0; i < 9; i++) {
            if (i == 4) {
                inv.setItem(i, skull);
            } else {
                inv.setItem(i, stainedGlass);
            }
        }
        for (int i = 18; i < 27; i++) {
            inv.setItem(i, stainedGlass);
        }
        inv.setItem(10, StatsInventory.getAttackUI(data));
        inv.setItem(12, StatsInventory.getDefenseUI(data));
        inv.setItem(14, StatsInventory.getFunctionUI(data));
        inv.setItem(16, StatsInventory.getSuitUI(playerName));
        openInvPlayer.openInventory(inv);
    }

    static public void openStatsInventory(Player openInvPlayer, int page, String playerName) {
        if (openInvPlayer.getName().equals(playerName)) {
            statsInventory(openInvPlayer, page, playerName);
        } else {
            statsInventory(openInvPlayer, page, playerName);
            new BukkitRunnable() {
                @Override
                public void run() {
                    statsInventory(openInvPlayer, page, playerName);
                }

            }.runTask(AngelCardStats.getPlugin());
        }
    }

    static public ItemStack getAttackUI(StatsData data) {
        //		ItemStack item = ItemData.clearAttribute(new ItemStack(Material.DIAMOND_SWORD));
        ItemStack item = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.setDisplayName(Message.getMsg(Message.INVENTORY_STATS_ATTACK));
        List<String> lore = new ArrayList<>();
        double damage = data.getDamage();
        if (damage == 0) damage = 1;
        loreAdd(Config.getString(Config.NAME_DAMAGE), damage, lore, false);
        loreAdd(Config.getString(Config.NAME_PENETRATION), data.getPenetration(), lore, false);
        loreAdd(Config.getString(Config.NAME_LIFE_STEAL), data.getLifeSteal(), lore, true);
        loreAdd(Config.getString(Config.NAME_CRIT_DAMAGE), data.getCritDamage(), lore, true);
        lore.add("§r");
        loreAdd(Config.getString(Config.NAME_CRIT), data.getCrit(), lore, true);
        loreAdd(Config.getString(Config.NAME_IGNITION), data.getIgnition(), lore, true);
        loreAdd(Config.getString(Config.NAME_WITHER), data.getWither(), lore, true);
        loreAdd(Config.getString(Config.NAME_POISON), data.getPoison(), lore, true);
        loreAdd(Config.getString(Config.NAME_BLINDNESS), data.getBlindness(), lore, true);
        loreAdd(Config.getString(Config.NAME_SLOWNESS), data.getSlowness(), lore, true);
        loreAdd(Config.getString(Config.NAME_REAL), data.getReal(), lore, true);
        loreAdd(Config.getString(Config.NAME_TEARING), data.getTearing(), lore, true);
        loreAdd(Config.getString(Config.NAME_LIGHTNING), data.getLightning(), lore, true);
        if (lore.get(lore.size() - 1).equals("§r")) lore.remove(lore.size() - 1);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    static public ItemStack getDefenseUI(StatsData data) {
        ItemStack item = new ItemStack(Material.DIAMOND_CHESTPLATE);
        ItemMeta meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.setDisplayName(Message.getMsg(Message.INVENTORY_STATS_DEFENSE));
        List<String> lore = new ArrayList<>();
        loreAdd(Config.getString(Config.NAME_HEALTH), data.getHealth(), lore, false);
        loreAdd(Config.getString(Config.NAME_DEFENSE), data.getDefense(), lore, false);
        loreAdd(Config.getString(Config.NAME_REFLECTION), data.getReflection(), lore, true);
        loreAdd(Config.getString(Config.NAME_BLOCK), data.getBlock(), lore, true);
        loreAdd(Config.getString(Config.NAME_DODGE), data.getDodge(), lore, true);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    static public ItemStack getFunctionUI(StatsData data) {
        ItemStack item = new ItemStack(Material.WATCH);
        ItemMeta meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.setDisplayName(Message.getMsg(Message.INVENTORY_STATS_FUNCTION));
        List<String> lore = new ArrayList<>();
        loreAdd(Config.getString(Config.NAME_SPEED), data.getSpeed(), lore, true);
        loreAdd(Config.getString(Config.NAME_CARD_VALUE), Double.valueOf(data.getCardValue()), lore, false);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    static public ItemStack getSuitUI(String playerName) {
        ItemStack item = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = item.getItemMeta();

        //TODO 套装效果
        item.setType(Material.ENCHANTED_BOOK);
        meta.setDisplayName(Message.getMsg(Message.INVENTORY_STATS_SUIT));
        meta.setLore(StatsDataRead.getPlayerSuitLore(playerName));
        item.setItemMeta(meta);
        return item;
    }

    private static void loreAdd(String statsName, Double stats, List<String> loreList, Boolean percentage) {
        String lore = "§" + (new Random().nextInt(9) + 1) + statsName + ": " + stats;
        if (percentage) lore += "%";
        loreList.add(lore);
    }

}
