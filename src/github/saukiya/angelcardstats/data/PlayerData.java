package github.saukiya.angelcardstats.data;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import github.saukiya.angelcardstats.util.Message;

public class PlayerData {
	static private HashMap<String,YamlConfiguration> yamlMap = new HashMap<String,YamlConfiguration>();
	final static private String BUY_SLOT = "BuySlot";
	
	static public void saveInventory(Inventory inv,Player player){
		if(inv.getName().equals(Message.getMsg(Message.INVENTORY_NAME))){
			int page = 1;
			page = ((HolderMeta)inv.getHolder()).getPage();
			ItemStack item = new ItemStack(Material.AIR);
			for(int i=9;i<45;i++){
				item = inv.getItem(i);
				if(item==null)continue;
				if(item.getType().equals(Material.BARRIER))continue;
				System.out.print(item);
			}
		}
	}
	
	static public void loadData(){
		yamlMap.clear();//说白清理就可以了
	}
	
	static File getFile(String playerName){
		return new File("plugins"+File.separator+"AngelCardStats"+File.separator+"PlayerData"+File.separator+playerName+".yml");
	}
	
	static YamlConfiguration newPlayerData(){//创建新的默认YAML格式
		YamlConfiguration playerData = new YamlConfiguration();
		playerData.set(BUY_SLOT, 0);
		return playerData;
	}
	
	@SuppressWarnings("deprecation")
	static public YamlConfiguration getPlayerData(String playerName){//获取并加载玩家YAML
		if(yamlMap.containsKey(playerName)){//如果玩家在常驻区，那么直接返回
			return yamlMap.get(playerName);
		}
		YamlConfiguration playerData = newPlayerData();//新建一个默认格式
		File file = getFile(playerName);
		if(file.exists()){//检查是否存在文件夹内，在的话就读取一下
			try {playerData.load(file);} catch (IOException | InvalidConfigurationException e) {}
		}else{//不在就保存一下
			try {playerData.save(file);} catch (IOException e) {}
		}
		if(Bukkit.getOfflinePlayer(playerName).isOnline()){//玩家在线时，将数据常驻在Map中
			yamlMap.put(playerName, playerData);
		}
		return playerData;
	}
	
	static public void savePlayerData(String playerName,YamlConfiguration... yaml){//保存玩家YAML到本地
		if(yamlMap.containsKey(playerName)){
			try {yamlMap.get(playerName).save(getFile(playerName));} catch (IOException e) {}
		}else{
			if(yaml.length>0)try {yaml[0].save(getFile(playerName));} catch (IOException e) {}
		}
	}
	
	static public void removePlayerData(String playerName){//从缓存区卸载玩家YAML
		if(yamlMap.containsKey(playerName))
			yamlMap.remove(playerName);
	}
	
	static public int getBuySlot(String playerName){
		YamlConfiguration yaml = getPlayerData(playerName);
		return yaml.getInt(BUY_SLOT);
	}
	

}
