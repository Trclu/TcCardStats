package github.saukiya.angelcardstats.event;

import github.saukiya.angelcardstats.AngelCardStats;
import github.saukiya.angelcardstats.data.HolderData;
import github.saukiya.angelcardstats.data.ItemData;
import github.saukiya.angelcardstats.data.PlayerData;
import github.saukiya.angelcardstats.data.StatsDataRead;
import github.saukiya.angelcardstats.inventory.CardInventory;
import github.saukiya.angelcardstats.inventory.SellCardInventory;
import github.saukiya.angelcardstats.inventory.StatsInventory;
import github.saukiya.angelcardstats.inventory.SuitInventory;
import github.saukiya.angelcardstats.util.Config;
import github.saukiya.angelcardstats.util.Message;
import github.saukiya.angelpoints.api.AngelPointsAPI;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.List;

public class OnInventoryEvent implements Listener {
    static Boolean isAmountCancelled(Player player, ItemStack item) {//清除多余的物品
        if (item.getAmount() > 1) {
            int amount = item.getAmount() - 1;
            Inventory playerInv = player.getInventory();
            ItemStack itemClone = item.clone();
            itemClone.setAmount(amount);
            for (int i = 0; i < 36; i++) {
                ItemStack iItem = playerInv.getItem(i);
                if (iItem == null) {
                    item.setAmount(1);
                    playerInv.setItem(i, itemClone);
                    player.updateInventory();
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    // 更新配置
    @EventHandler
    void onInventoryOpenEvent(InventoryOpenEvent event) {
        Inventory inv = event.getInventory();
        Player player = (Player) event.getView().getPlayer();
        Inventory dinv = player.getInventory();
        for (int i = 0; i < inv.getSize(); i++) {
            ItemStack item = inv.getItem(i);
            if (item != null) {
                String cardID = ItemData.isCard(item);
                if (cardID != null) {
                    int amount = item.getAmount();
                    ItemStack updateItem = ItemData.update(item, cardID);
                    updateItem.setAmount(amount);
                    inv.setItem(i, updateItem);
                }
            }
        }
        for (int i = 0; i < dinv.getSize(); i++) {
            ItemStack item = dinv.getItem(i);
            if (item != null) {
                String cardID = ItemData.isCard(item);
                if (cardID != null) {
                    int amount = item.getAmount();
                    ItemStack updateItem = ItemData.update(item, cardID);
                    updateItem.setAmount(amount);
                    dinv.setItem(i, updateItem);
                }
            }
        }
    }

    @EventHandler
    void onInventoryClickEvent(InventoryClickEvent event) {
        Inventory inv = event.getInventory();
        Player player = (Player) event.getView().getPlayer();
        int slot = event.getRawSlot();
        ItemStack item = event.getCurrentItem();
        if (item == null) return;
        Material material = item.getType();
        if (inv.getName().equals(Message.getMsg(Message.INVENTORY_OPEN_NAME))) {
            int page = ((HolderData) inv.getHolder()).getPage();
            String playerName = ((HolderData) inv.getHolder()).getPlayerName();
            if (!event.getClick().equals(ClickType.LEFT)) {
                event.setCancelled(true);
                return;
            }
            if (slot <= 53) {
                if (slot >= 9 && slot < 45) {
                    //TODO 检测是否点击的是锁槽
                    if (item.getType().equals(Material.BARRIER)) event.setCancelled(true);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (isAmountCancelled(player, item)) {//判定手持数量是否大于1
                                event.setCancelled(true);
                            }
                        }

                    }.runTask(AngelCardStats.getPlugin());
                } else {
                    event.setCancelled(true);
                    if (material.equals(Material.ARROW)) {//换页
                        CardInventory.openCardInventory(player, item.getAmount(), playerName);
                    } else if (material.equals(Material.SKULL_ITEM)) {//打开属性菜单
                        StatsInventory.openStatsInventory(player, page, playerName);
                    } else if (material.equals(Material.GOLD_INGOT)) {//购买物品
                        int buySlot = PlayerData.getBuySlot(playerName);
                        int buyPoints = Config.getInt(Config.PLAYER_BUY_SLOT_POINTS) + (Config.getInt(Config.PLAYER_BUY_SLOT_VALUE_POINTS) * buySlot);
                        if (AngelPointsAPI.isPlayerPoints(playerName, buyPoints)) {
                            AngelPointsAPI.takePlayerPoints(playerName, buyPoints);
                            //TODO 设置玩家新Slot
                            buySlot++;
                            buyPoints += Config.getInt(Config.PLAYER_BUY_SLOT_VALUE_POINTS);
                            PlayerData.setBuySlot(playerName, buySlot);
                            int hasSlot = Integer.valueOf(Config.getString(Config.PLAYER_DEFAULT_SLOT)) + buySlot;
                            player.sendMessage(Message.getMsg(Message.PLAYER_BUY_SLOT, String.valueOf(hasSlot)));
                            //以下为防止bug
                            //修改购买卡槽点击按钮
                            ItemStack buySlotItem = new ItemStack(Material.GOLD_INGOT);
                            ItemMeta buySlotMeta = buySlotItem.getItemMeta();
                            buySlotMeta.setDisplayName(Message.getMsg(Message.INVENTORY_OPEN_BUY_SLOT_NAME));
                            buySlotMeta.setLore(Message.getList(Message.INVENTORY_OPEN_BUY_SLOT_LORE, String.valueOf(Integer.valueOf(Config.getString(Config.PLAYER_DEFAULT_SLOT)) + buySlot), String.valueOf(Config.getInt(Config.PLAYER_BUY_SLOT_POINTS) + (Config.getInt(Config.PLAYER_BUY_SLOT_VALUE_POINTS) * buySlot))));
                            buySlotItem.setItemMeta(buySlotMeta);
                            inv.setItem(51, buySlotItem);
                            //修改锁槽
                            Boolean p = false;
                            for (int i = 9; i < 45; i++) {
                                if (inv.getItem(i) != null && inv.getItem(i).getType().equals(Material.BARRIER)) {
                                    inv.setItem(i, null);
                                    if (i == 44) p = true;
                                    break;
                                }
                            }
                            //修改下一页
                            if (p && !inv.getItem(53).getType().equals(Material.ARROW)) {
                                ItemStack pageItem = new ItemStack(Material.ARROW);
                                ItemMeta pageMeta = pageItem.getItemMeta();
                                pageMeta.setDisplayName(Message.getMsg(Message.INVENTORY_OPEN_PAGE_DOWN));
                                pageItem.setItemMeta(pageMeta);
                                pageItem.setAmount(page + 1);
                                inv.setItem(53, pageItem);
                            }
                        } else {
                            ItemMeta itemMeta = item.getItemMeta();
                            List<String> list = new ArrayList<>();
                            list.add(Message.getMsg(Message.INVENTORY_OPEN_BUY_SLOT_NO_POINTS));
                            itemMeta.setLore(list);
                            item.setItemMeta(itemMeta);
                        }
                    } else if (material.equals(Material.BLAZE_POWDER)) {//出售物品
                        SellCardInventory.openSellCardInventory(player);
                    } else if (material.equals(Material.ENCHANTED_BOOK)) {
                        SuitInventory.openSuitListInventory(player, page, 1);
                    }
                }
            } else {
                //判断在玩家背包中是否点击的是卡牌。
                if (item.getAmount() != 0) {
                    if (ItemData.isCard(item) == null) {
                        player.sendMessage(Message.getMsg(Message.PLAYER_IS_NOT_CARD_ITEM));
                        event.setCancelled(true);
                        return;
                    } else {
                        //TODO 判定手持数量是否大于1
                        if (isAmountCancelled(player, item)) {
                            event.setCancelled(true);
                            return;
                        }
                    }
                    //TODO 检测物品是否重复
                    PlayerData.saveInventory(inv, player);
                    for (ItemStack items : PlayerData.getPlayerItem(playerName)) {
                        if (items.getItemMeta().getLore().equals(item.getItemMeta().getLore())) {
                            player.sendMessage(Message.getMsg(Message.PLAYER_HAS_CARD_ITEM));
                            event.setCancelled(true);
                            break;
                        }
                    }
                }
            }
        } else if (inv.getName().equals(Message.getMsg(Message.INVENTORY_STATS_NAME))) {
            event.setCancelled(true);
            int page = ((HolderData) inv.getHolder()).getPage();
            String playerName = ((HolderData) inv.getHolder()).getPlayerName();
            if (slot == 4) {
                CardInventory.openCardInventory(player, page, playerName);
            }
        } else if (inv.getName().equals(Message.getMsg(Message.INVENTORY_SELL_CARD_NAME))) {
            if (!event.getClick().equals(ClickType.LEFT)) {
                event.setCancelled(true);
                return;
            }
            if (slot < 27) {
                if (slot < 9 || slot >= 18) {
                    event.setCancelled(true);
                    if (item.getType().equals(Material.BLAZE_POWDER)) {
                        ItemMeta itemMeta = item.getItemMeta();

                        List<ItemStack> itemList = new ArrayList<>();
                        for (int i = 9; i < 18; i++) {
                            ItemStack invItem = inv.getItem(i);
                            if (invItem != null) {
                                for (int e = 0; e < invItem.getAmount(); e++) {
                                    itemList.add(inv.getItem(i));
                                }
                            }
                        }
                        int value = (int) (StatsDataRead.getItemStatsData(null, itemList).getCardValue() * Config.getDouble(Config.PLAYER_SELL_CARD_VALUE_POINTS));

                        if (item.getItemMeta().hasEnchants()) {
                            itemMeta.setDisplayName(Message.getMsg(Message.INVENTORY_SELL_CARD_SELL_CARD));
                            itemMeta.setLore(null);
                            itemMeta.removeEnchant(Enchantment.DURABILITY);
                            item.setItemMeta(itemMeta);
                            //TODO 给予修为值
                            if (value != 0) {
                                for (int i = 9; i < 18; i++) {
                                    inv.setItem(i, null);
                                }
                                AngelPointsAPI.addPlayerPoints(player.getName(), value);
                                player.sendMessage(Message.getMsg(Message.PLAYER_SELL_CARD));
                            }
                        } else {
                            if (value != 0) {
                                itemMeta.setDisplayName(Message.getMsg(Message.INVENTORY_SELL_CARD_ENTER_SELL_CARD_NAME));
                                //TODO 代入卡牌点数
                                itemMeta.setLore(Message.getList(Message.INVENTORY_SELL_CARD_ENTER_SELL_CARD_LORE, String.valueOf(value)));
                                itemMeta.addEnchant(Enchantment.DURABILITY, 1, false);
                                item.setItemMeta(itemMeta);
                            } else {
                                itemMeta.setDisplayName(Message.getMsg(Message.INVENTORY_SELL_CARD_NO_CARD));
                                item.setItemMeta(itemMeta);
                            }
                        }
                    }
                }
            } else {
                //判断在玩家背包中是否点击的是卡牌。
                if (item.getAmount() != 0) {
                    if (ItemData.isCard(item) == null) {
                        player.sendMessage(Message.getMsg(Message.PLAYER_IS_NOT_CARD_ITEM));
                        event.setCancelled(true);
                    }
                }
            }
            if (!item.equals(inv.getItem(22))) {
                ItemStack enterItem = inv.getItem(22);
                ItemMeta itemMeta = enterItem.getItemMeta();
                if (itemMeta.hasEnchants()) {
                    itemMeta.setDisplayName(Message.getMsg(Message.INVENTORY_SELL_CARD_SELL_CARD));
                    itemMeta.setLore(null);
                    itemMeta.removeEnchant(Enchantment.DURABILITY);
                    enterItem.setItemMeta(itemMeta);
                    inv.setItem(22, enterItem);
                }
            }
        } else if (inv.getName().equals(Message.getMsg(Message.INVENTORY_SUIT_LIST_NAME))) {
            event.setCancelled(true);
            int cardPage = ((HolderData) inv.getHolder()).getPage();
            if (slot < 54) {
                if (item.getType().equals(Material.AIR)) return;
                if (slot == 4) {
                    CardInventory.openCardInventory(player, cardPage);
                } else if (slot >= 9 && slot < 45) {
                    String suitName = item.getItemMeta().getDisplayName().replace("§e", "");
                    SuitInventory.openSuitInventory(player, suitName, cardPage);
                } else if (material.equals(Material.ARROW)) {//换页
                    SuitInventory.openSuitListInventory(player, cardPage, item.getAmount());
                }
            }
        } else if (inv.getName().equals(Message.getMsg(Message.INVENTORY_SUIT_NAME))) {
            event.setCancelled(true);
            int cardPage = ((HolderData) inv.getHolder()).getPage();
            if (item.getType().equals(Material.AIR)) return;
            if (slot == 4) {
                CardInventory.openCardInventory(player, cardPage);
            }
            if (slot >= 9 && slot < 27) {
                if (player.isOp()) {
                    player.getInventory().addItem(item);
                    player.sendMessage(Message.getMsg(Message.ADMIN_GIVE_ITEM, player.getName(), String.valueOf(1), item.getItemMeta().getDisplayName()));
                }
            }
        }
    }

    @EventHandler
    void onInventoryCloseEvent(InventoryCloseEvent event) {
        Inventory inv = event.getInventory();
        Player player = (Player) event.getView().getPlayer();
        if (inv.getName().equals(Message.getMsg(Message.INVENTORY_OPEN_NAME))) {
            String playerName = ((HolderData) inv.getHolder()).getPlayerName();
            PlayerData.saveInventory(inv, player);
            if (player.getName().equals(playerName)) {
                player.sendMessage(Message.getMsg(Message.PLAYER_LOAD_STATS_DATA));
                StatsDataRead.loadPlayerStats(player);
            }
        } else if (inv.getName().equals(Message.getMsg(Message.INVENTORY_SELL_CARD_NAME))) {
            for (int i = 9; i < 18; i++) {
                if (inv.getItem(i) != null) {
                    player.getInventory().addItem(inv.getItem(i));
                }
            }
        }
    }


}
