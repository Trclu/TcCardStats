package github.saukiya.angelcardstats.data;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class PlayerData {
    final static private String BUY_SLOT = "BuySlot";
    final static private String LEVEL = "Level";
    static private Map<String, YamlConfiguration> yamlMap = new HashMap<>();
    static private Map<String, List<ItemStack>> itemMap = new HashMap<>();

    static public void saveInventory(Inventory inv, Player openInvPlayer) {//用来保存inventory内的物品数据
        int page = 1;
        String playerName = openInvPlayer.getName();
        if (inv.getHolder() instanceof HolderData) {

            page = ((HolderData) inv.getHolder()).getPage();
            playerName = ((HolderData) inv.getHolder()).getPlayerName();
        }
        ItemStack item = null;
        YamlConfiguration playerData = getPlayerData(playerName);
        for (int i = 9; i < 45; i++) {
            item = inv.getItem(i);
            if (item == null) {
                playerData.set("CardItem." + (page * 36 + i - 36), null);
                continue;
            }
            if (item.getType().equals(Material.BARRIER)) continue; //检测是否为锁槽

            if (item.getAmount() > 1) {//检测物品数量是否大于1
                item.setAmount(1);
            }
            playerData.set("CardItem." + (page * 36 + i - 36), item);
        }

        savePlayerData(playerName, playerData);
    }

    @SuppressWarnings("deprecation")
    static public List<ItemStack> loadPlayerItem(String playerName) {//在Inventory关闭的时候执行此代码 只会保存itemList
        List<ItemStack> itemList = new ArrayList<ItemStack>();
        YamlConfiguration playerData = getPlayerData(playerName);

        if (playerData.getConfigurationSection("CardItem") != null) {
            for (String str : playerData.getConfigurationSection("CardItem").getKeys(false)) {
                ItemStack item = playerData.getItemStack("CardItem." + str);
                if (item != null) {
                    itemList.add(item);
                }
            }
        }
        if (Bukkit.getOfflinePlayer(playerName).isOnline()) itemMap.put(playerName, itemList);
        return itemList;
    }

    static public List<ItemStack> getPlayerItem(String playerName) {//在Inventory前的时候执行此代码 只会获取getitemList
//		if(itemMap.containsKey(playerName)){
//			return itemMap.get(playerName);
//		}else{
        List<ItemStack> itemList = new ArrayList<ItemStack>();
        YamlConfiguration playerData = getPlayerData(playerName);
        if (playerData.getConfigurationSection("CardItem") != null) {
            for (String str : playerData.getConfigurationSection("CardItem").getKeys(false)) {
                ItemStack item = playerData.getItemStack("CardItem." + str);
                if (item != null) {
                    itemList.add(item);
                }
            }
        }
        return itemList;
//		}
    }

    static public void loadData() {
        Bukkit.getConsoleSender().sendMessage("[AngelCardStats] §aReLoad PlayerData!");
        yamlMap.clear();//说白清理就可以了
    }

    static File getFile(String playerName) {
        return new File("plugins" + File.separator + "AngelCardStats" + File.separator + "PlayerData" + File.separator + playerName + ".yml");
    }

    static YamlConfiguration newPlayerData() {//创建新的默认YAML格式
        YamlConfiguration playerData = new YamlConfiguration();
        playerData.set(BUY_SLOT, 0);
        playerData.set(LEVEL, 0);
        return playerData;
    }

    static public Boolean isPlayer(String playerName) {
        return getFile(playerName).exists();
    }

    @SuppressWarnings("deprecation")
    static public YamlConfiguration getPlayerData(String playerName) {//获取并加载玩家YAML
        if (yamlMap.containsKey(playerName)) {//如果玩家在常驻区，那么直接返回
            return yamlMap.get(playerName);
        }
        YamlConfiguration playerData = newPlayerData();//新建一个默认格式
        File file = getFile(playerName);
        if (file.exists()) {//检查是否存在文件夹内，在的话就读取一下
            try {
                playerData.load(file);
            } catch (IOException | InvalidConfigurationException e) {
            }
        } else {//不在就保存一下
            try {
                playerData.save(file);
            } catch (IOException e) {
            }
        }
        if (Bukkit.getOfflinePlayer(playerName).isOnline()) {//玩家在线时，将数据常驻在Map中
            yamlMap.put(playerName, playerData);
        }
        return playerData;
    }

    static public void savePlayerData(String playerName, YamlConfiguration... yaml) {//保存玩家YAML到本地
        if (yamlMap.containsKey(playerName)) {
            try {
                yamlMap.get(playerName).save(getFile(playerName));
            } catch (IOException e) {
            }
        } else {
            if (yaml.length > 0) try {
                yaml[0].save(getFile(playerName));
            } catch (IOException e) {
            }
        }
    }

    static public int getPlayerDataLevel(String playerName) {
        return getPlayerData(playerName).getInt(LEVEL);
    }

    static public void removePlayerDataAndSaveLevel(String playerName, int level) {//从缓存区卸载玩家YAML 保存等级
        if (yamlMap.containsKey(playerName)) {
            yamlMap.get(playerName).set(LEVEL, level);
            savePlayerData(playerName);
            yamlMap.remove(playerName);
        }
    }

    static public int getBuySlot(String playerName) {
        YamlConfiguration yaml = getPlayerData(playerName);
        return yaml.getInt(BUY_SLOT);
    }

    static public void setBuySlot(String playerName, int slot) {
        getPlayerData(playerName).set(BUY_SLOT, slot);
        savePlayerData(playerName);
    }

//	// 设定一个nbt数据
//	static public ItemStack setCardID(ItemStack item,String name){
//		net.minecraft.server.v1_11_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
//		NBTTagCompound compund = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
//		compund.set(AngelCardStats.getPlugin().getName(), new NBTTagString(name));
//		nmsItem.setTag(compund);
//		return CraftItemStack.asBukkitCopy(nmsItem);
//	}
//	
//	// 获取一个nbt数据
//	static public String getCardID(ItemStack item){
//		net.minecraft.server.v1_11_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
//		NBTTagCompound compund = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
//		if (compund.hasKey(AngelCardStats.getPlugin().getName())){
//			return compund.getString(AngelCardStats.getPlugin().getName());
//		}
//		return null;
//	}
}
