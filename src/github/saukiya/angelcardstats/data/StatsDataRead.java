package github.saukiya.angelcardstats.data;

import github.saukiya.angelcardstats.AngelCardStats;
import github.saukiya.angelcardstats.inventory.StatsInventory;
import github.saukiya.angelcardstats.util.Config;
import github.saukiya.angelcardstats.util.Message;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.*;
import java.util.regex.Pattern;

public class StatsDataRead {
    final static File dataFile = new File("plugins" + File.separator + "AngelCardStats" + File.separator + "Data.dat");
    //存储玩家属性
    static public Map<String, StatsData> statsDataMap = new HashMap<>();
    //存储玩家已激活的套装lore;
    static public Map<String, List<String>> suitLoreMap = new HashMap<>();

    static public List<String> getPlayerSuitLore(String playerName) {
        List<String> loreList = new ArrayList<String>();
        if (suitLoreMap.containsKey(playerName)) {
            loreList = suitLoreMap.get(playerName);
        }
        return loreList;
    }

    @SuppressWarnings("deprecation")
    static public void setPlayerSuitLore(String playerName, List<ItemStack> itemList, StatsData statsData) {
        List<String> loreList = new ArrayList<>();
        List<String> oldLoreList = getPlayerSuitLore(playerName);
        for (String suitName : SuitData.getSuitItemMap().keySet()) {
            List<ItemStack> suitItemList = SuitData.getSuitItemMap().get(suitName);
            int value = 0;
            for (ItemStack item : suitItemList) {
                if (itemList.contains(item)) {
                    value++;
                }
            }
            //   §7[§c✘§7]       §7[§a✔§7]
            String lore = "§7" + suitName + ": " + value + "/" + suitItemList.size();
            if (suitItemList.size() == value) {
                lore = "§e" + suitName + ": " + value + "/" + suitItemList.size();
                statsData.add(SuitData.getSuitStatsMap().get(suitName));
                if (oldLoreList.size() != 0 && !oldLoreList.contains(lore)) {
                    if (Bukkit.getOfflinePlayer(playerName).isOnline()) {
                        Bukkit.getPlayer(playerName).sendMessage(Message.getMsg(Message.PLAYER_ACTIVATION_SUIT, suitName));
                    }
                }
            }
            //TODO 写入Lore
            loreList.add(lore);
        }
        suitLoreMap.put(playerName, loreList);
    }

    static public int getCardValue(StatsData data) {
        int value = 0;
        value += Config.getDouble(Config.VALUE_DAMAGE) * data.getDamage();
        value += Config.getDouble(Config.VALUE_HEALTH) * data.getHealth();
        value += Config.getDouble(Config.VALUE_DEFENSE) * data.getDefense();
        value += Config.getDouble(Config.VALUE_SPEED) * data.getSpeed();
        value += Config.getDouble(Config.VALUE_CRIT_DAMAGE) * data.getCritDamage();
        value += Config.getDouble(Config.VALUE_PENETRATION) * data.getPenetration();
        value += Config.getDouble(Config.VALUE_LIFE_STEAL) * data.getLifeSteal();
        value += Config.getDouble(Config.VALUE_CRIT) * data.getCrit();
        value += Config.getDouble(Config.VALUE_IGNITION) * data.getIgnition();
        value += Config.getDouble(Config.VALUE_WITHER) * data.getWither();
        value += Config.getDouble(Config.VALUE_POISON) * data.getPoison();
        value += Config.getDouble(Config.VALUE_BLINDNESS) * data.getBlindness();
        value += Config.getDouble(Config.VALUE_SLOWNESS) * data.getSlowness();
        value += Config.getDouble(Config.VALUE_LIGHTNING) * data.getLightning();
        value += Config.getDouble(Config.VALUE_REAL) * data.getReal();
        value += Config.getDouble(Config.VALUE_TEARING) * data.getTearing();
        value += Config.getDouble(Config.VALUE_REFLECTION) * data.getReflection();
        value += Config.getDouble(Config.VALUE_BLOCK) * data.getBlock();
        value += Config.getDouble(Config.VALUE_DODGE) * data.getDodge();
        return value;
    }

    //更新玩家血量、移动速度
    static void updateStats(Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                StatsData data = StatsDataRead.getPlayerData(player.getName());
                player.setMaxHealth(data.getHealth());
                player.setWalkSpeed((float) (data.getSpeed() / 500));
            }
        }.runTask(AngelCardStats.getPlugin());
    }

    static public void loadPlayerStats(Player player) {//加载在线玩家的StatsData
        new BukkitRunnable() {
            @Override
            public void run() {
                List<ItemStack> itemList = PlayerData.loadPlayerItem(player.getName());
                StatsData statsData = getItemStatsData(player, itemList);
                setPlayerSuitLore(player.getName(), itemList, statsData);
                CardValueData.setPlayerCardValue(player.getName(), statsData.getCardValue());
                statsDataMap.put(player.getName(), statsData);
                updateStats(player);
                player.sendMessage(Message.getMsg(Message.PLAYER_COMPLETE_STATS_DATA));
                if (player.getOpenInventory().getTitle().equals(Message.getMsg(Message.INVENTORY_STATS_NAME))) {
                    Inventory inv = player.getOpenInventory().getTopInventory();
                    if (inv.getHolder() instanceof HolderData) {
                        int page = ((HolderData) inv.getHolder()).getPage();
                        String playerName = ((HolderData) inv.getHolder()).getPlayerName();
//						player.closeInventory();
                        new BukkitRunnable() {//重新更新属性菜单
                            @Override
                            public void run() {
                                StatsInventory.openStatsInventory(player, page, playerName);
                            }
                        }.runTask(AngelCardStats.getPlugin());
                    }
                }
            }
        }.runTaskAsynchronously(AngelCardStats.getPlugin());
    }

    @SuppressWarnings("deprecation")
    static public StatsData leadPlayerStats(String playerName) {//只是保存在statsDataMap
        List<ItemStack> itemList = PlayerData.loadPlayerItem(playerName);
        if (Bukkit.getOfflinePlayer(playerName).isOnline()) {
            return getItemStatsData(Bukkit.getPlayer(playerName), itemList);
        } else {
            StatsData statsData = getItemStatsData(null, itemList, PlayerData.getPlayerDataLevel(playerName));
            setPlayerSuitLore(playerName, itemList, statsData);
            return statsData;
        }
    }

    static public Double getDefense(double d) {
        double defense = 0.000D;
        defense = 150 / (150 + d);
        return defense;
    }

    static public Boolean probability(double d) {
        if (d == 0) return false;
        double random = new Random().nextDouble();
        return d / 100 > random;
    }

    static public StatsData getPlayerData(String playerName) {//获取生物总数据
        StatsData data = new StatsData();
        //玩家默认数据
        data.setHealth(20D);
        data.setSpeed(100D);
        data.setCritDamage(100D);

        if (statsDataMap.containsKey(playerName)) {
            data.add(statsDataMap.get(playerName));
        } else {
            data.add(leadPlayerStats(playerName));
        }
        return data;
    }

    static public void clearPlayerData(Player player) {//清除生物数据
        UUID uuid = player.getUniqueId();
        if (statsDataMap.containsKey(uuid)) {
            statsDataMap.remove(uuid);
        }

    }

    static public String clearColor(String lore) {//清除与数字有关的颜色
        List<String> ColorList = new ArrayList<String>();
        for (int i = 0; i < 10; i++) {
            ColorList.add("§" + i);
        }
        for (int i = 0; i < ColorList.size(); i++) {
            lore = lore.replace(ColorList.get(i), "");
        }
        return lore;
    }

    static public StatsData getItemStatsData(Player player, List<ItemStack> itemList, int... levels) {//获取物品的属性
        StatsData dataList = new StatsData();
        int level = 0;
        if (levels.length > 0) {
            level = levels[0];
        } else {
            level = getLevel(player);
        }
        if (itemList.size() == 0) return dataList;
        for (int i = itemList.size() - 1; i >= 0; i--) {
            ItemStack item = itemList.get(i);
            StatsData data = new StatsData();
            if (item != null && item.hasItemMeta()) {
                ItemMeta meta = item.getItemMeta();
                if (meta.hasLore()) {
                    List<String> loreList = meta.getLore();
                    for (String lore : loreList) {
                        if (lore.contains("§X")) continue;
                        //循环每条lore是否有所需属性
                        if (lore.contains(Config.getString(Config.NAME_DAMAGE))) {
                            data.setDamage(data.getDamage() + Double.valueOf(getStats(lore)));
                        } else if (lore.contains(Config.getString(Config.NAME_HEALTH))) {
                            data.setHealth(data.getHealth() + Double.valueOf(getStats(lore)));
                        } else if (lore.contains(Config.getString(Config.NAME_DEFENSE)) && !lore.contains(Config.getString(Config.NAME_PENETRATION))) {
                            data.setDefense(data.getDefense() + Double.valueOf(getStats(lore)));
                        } else if (lore.contains(Config.getString(Config.NAME_SPEED))) {
                            data.setSpeed(data.getSpeed() + Double.valueOf(getStats(lore)));
                        } else if (lore.contains(Config.getString(Config.NAME_CRIT_DAMAGE))) {
                            data.setCritDamage(data.getCritDamage() + Double.valueOf(getStats(lore)));
                        } else if (lore.contains(Config.getString(Config.NAME_PENETRATION))) {
                            data.setPenetration(data.getPenetration() + Double.valueOf(getStats(lore)));
                        } else if (lore.contains(Config.getString(Config.NAME_LIFE_STEAL))) {
                            data.setLifeSteal(data.getLifeSteal() + Double.valueOf(getStats(lore)));
                        } else if (lore.contains(Config.getString(Config.NAME_LIMIT_LEVEL))) {
                            //等级不够，给予空的数据
                            if (Integer.valueOf(getStats(lore)) > level) {
                                if (player != null) {
                                    player.sendMessage(Message.getMsg(Message.PLAYER_NO_LEVEL_USE, meta.getDisplayName()));
                                }
                                data = new StatsData();
                                itemList.remove(i);
                                break;
                            }
                        } else if (lore.contains(Config.getString(Config.NAME_CRIT)) && !lore.contains(Config.getString(Config.NAME_CRIT_DAMAGE))) {
                            data.setCrit(data.getCrit() + Double.valueOf(getStats(lore)));
                        } else if (lore.contains(Config.getString(Config.NAME_IGNITION))) {
                            data.setIgnition(data.getIgnition() + Double.valueOf(getStats(lore)));
                        } else if (lore.contains(Config.getString(Config.NAME_WITHER))) {
                            data.setWither(data.getWither() + Double.valueOf(getStats(lore)));
                        } else if (lore.contains(Config.getString(Config.NAME_POISON))) {
                            data.setPoison(data.getPoison() + Double.valueOf(getStats(lore)));
                        } else if (lore.contains(Config.getString(Config.NAME_BLINDNESS))) {
                            data.setBlindness(data.getBlindness() + Double.valueOf(getStats(lore)));
                        } else if (lore.contains(Config.getString(Config.NAME_SLOWNESS))) {
                            data.setSlowness(data.getSlowness() + Double.valueOf(getStats(lore)));
                        } else if (lore.contains(Config.getString(Config.NAME_LIGHTNING))) {
                            data.setLightning(data.getLightning() + Double.valueOf(getStats(lore)));
                        } else if (lore.contains(Config.getString(Config.NAME_REAL))) {
                            data.setReal(data.getReal() + Double.valueOf(getStats(lore)));
                        } else if (lore.contains(Config.getString(Config.NAME_TEARING))) {
                            data.setTearing(data.getTearing() + Double.valueOf(getStats(lore)));
                        } else if (lore.contains(Config.getString(Config.NAME_REFLECTION))) {
                            data.setReflection(data.getReflection() + Double.valueOf(getStats(lore)));
                        } else if (lore.contains(Config.getString(Config.NAME_BLOCK))) {
                            data.setBlock(data.getBlock() + Double.valueOf(getStats(lore)));
                        } else if (lore.contains(Config.getString(Config.NAME_DODGE))) {
                            data.setDodge(data.getDodge() + Double.valueOf(getStats(lore)));
                        }
                    }
                    data.setCardValue(getCardValue(data));
                }
            }
            dataList.add(data);
        }
        return dataList;
    }

    //获取生物等级
    private static int getLevel(Player player) {
        if (player != null) {
            return ((Player) player).getLevel();
        }
        return 0;
    }

    //getlore内的属性值
    static public String getStats(String lore) {
        lore = clearColor(lore);
        String str = "0";
        if (lore.contains(".")) {
            int d1 = Integer.valueOf(Pattern.compile("[^0-9]").matcher(lore.replace(".", "/").split("/")[0]).replaceAll("").trim());
            int d2 = Integer.valueOf(Pattern.compile("[^0-9]").matcher(lore.replace(".", "/").split("/")[1]).replaceAll("").trim());
            str = String.valueOf(d1 + "." + d2);
        } else {
            str = Pattern.compile("[^0-9]").matcher(lore).replaceAll("").trim();
        }
        return str;
    }

}
