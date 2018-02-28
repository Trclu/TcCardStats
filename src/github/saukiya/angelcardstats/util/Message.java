package github.saukiya.angelcardstats.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class Message {
	private static YamlConfiguration messages;
	final static File messageFile = new File("plugins" + File.separator + "AngelCardStats" + File.separator + "Message.yml");

	final public static String PLAYER_NO_LEVEL_USE = "Player.NoLevelUse";
	final public static String PLAYER_IS_NOT_CARD_ITEM = "Player.ThisIsNotCardItem";
	
	final public static String ADMIN_NO_PER_CMD = "Admin.NoPermissionCommand";
	final public static String ADMIN_NO_CMD = "Admin.NoCommand";
	final public static String ADMIN_NO_FORMAT = "Admin.NoFormat";
	final public static String ADMIN_NO_ONLINE = "Admin.NoOnline";
	final public static String ADMIN_NO_ITEM = "Admin.NoItem";
	final public static String ADMIN_HAS_ITEM = "Admin.HasItem";
	final public static String ADMIN_GIVE_ITEM = "Admin.GiveItem";
	final public static String ADMIN_NO_CONSOLE = "Admin.NoConsole";

	final public static String COMMAND_OPEN = "Command.open";
	final public static String COMMAND_GIVE = "Command.give";
	final public static String COMMAND_SAVE = "Command.save";
	final public static String COMMAND_RELOAD = "Command.reload";
	
	final public static String INVENTORY_NAME = "Inventory.Name";
	final public static String INVENTORY_PAGE_UP = "Inventory.PageUp";
	final public static String INVENTORY_PAGE_DOWN = "Inventory.PageDown";
	final public static String INVENTORY_LOCK_SLOT = "Inventory.LockSlot";
	final public static String INVENTORY_SKULL_NAME = "Inventory.SkullName";
	
	static void createMessage(){
        Bukkit.getConsoleSender().sendMessage("[AngelCardStats] §cCreate Message.yml");
		messages = new YamlConfiguration();
		messages.set(PLAYER_NO_LEVEL_USE, String.valueOf("&8[&d卡牌&8] &c你没有达到使用 {0} &c的等级要求!"));
		messages.set(PLAYER_IS_NOT_CARD_ITEM, String.valueOf("&8[&d卡牌&8] &c这个物品不是卡牌!"));
		
		messages.set(ADMIN_NO_PER_CMD, String.valueOf("&8[&d卡牌&8] &c你没有权限执行此指令"));
		messages.set(ADMIN_NO_CMD, String.valueOf("&8[&d卡牌&8] &c未找到此子指令:{0}"));
		messages.set(ADMIN_NO_FORMAT, String.valueOf("&8[&d卡牌&8] &c格式错误!"));
		messages.set(ADMIN_NO_ONLINE, String.valueOf("&8[&d卡牌&8] &c玩家不在线或玩家不存在!"));
		messages.set(ADMIN_NO_CONSOLE, String.valueOf("&8[&d卡牌&8] &c控制台不允许执行此指令!"));
		messages.set(ADMIN_NO_ITEM, String.valueOf("&8[&d卡牌&8] &c卡牌不存在!"));
		messages.set(ADMIN_HAS_ITEM, String.valueOf("&8[&d卡牌&8] &c已经存在名字为  &6{0}&c的卡牌!"));
		messages.set(ADMIN_GIVE_ITEM, String.valueOf("&8[&d卡牌&8] &c给予 &6{0} &a{1}&c个 &6{2}&c 卡牌!"));

		messages.set(COMMAND_OPEN, String.valueOf("打开卡牌菜单"));
		messages.set(COMMAND_GIVE, String.valueOf("给予玩家卡牌物品"));
		messages.set(COMMAND_SAVE, String.valueOf("保存当前的物品到Items.yml"));
		messages.set(COMMAND_RELOAD, String.valueOf("重新加载这个插件的配置"));
		
		messages.set(INVENTORY_NAME, String.valueOf("&6&l&o卡牌菜单"));
		messages.set(INVENTORY_PAGE_UP, String.valueOf("&e上一页"));
		messages.set(INVENTORY_PAGE_DOWN, String.valueOf("&e下一页"));
		messages.set(INVENTORY_LOCK_SLOT, String.valueOf("&c&o&m  锁槽  "));
		messages.set(INVENTORY_SKULL_NAME, String.valueOf("&6&o点击查看属性"));
		try {messages.save(messageFile);} catch (IOException e) {e.printStackTrace();}
	}
	
	static public void loadMessage(){
		createMessage();
		if(!messageFile.exists()){
//				createMessage();
		}else{
	        Bukkit.getConsoleSender().sendMessage("[AngelCardStats] §aFind Message.yml");
		}
		messages = new YamlConfiguration();
		try {messages.load(messageFile);} catch (IOException | InvalidConfigurationException e) {e.printStackTrace();Bukkit.getConsoleSender().sendMessage("§8[§6AngelCardStats§8] §a读取message时发生错误");}
	}
	

	public static String getMsg(String loc,String... args){
		String raw = messages.getString(loc);
		if (raw == null || raw.isEmpty()) {
			return "Null Message: " + loc;
		}
		raw = raw.replaceAll("&", "§");
		if (args == null) {
			return raw;
		}
		List<String> replaceList = Message.getList("replace");//替换一些类似world的英文
		for (int i = 0; i < args.length; i++) {
			for(int l=0; l <replaceList.size();l++){
				String str = replaceList.get(l);
				String str1 = str.split(":")[0];
				String str2 = str.split(":")[1].replace("&", "§");
				if(args[i].equals(str1))args[i] = args[i].replace(str1, str2);
			}
			raw = raw.replace("{" + i + "}", args[i]==null ? "null" : args[i]);//替换变量
		}
		
		return raw;
	}
	public static List<String> replaceColor(List<String> list){
		if(list!=null){
			for(int i= 0;i <list.size();i++){
				list.set(i,list.get(i).replace("&", "§"));
			}
		}
		return list;
	}
	
	public static List<String> getList(String loc,String... args){
		List<String> list = messages.getStringList(loc);
		if (list == null || list.isEmpty()) {
			list.add("Null Message: " + loc);
			return list;
		}
		if (args == null) {
			for(int e= 0;e <list.size();e++){
				list.set(e,list.get(e).replace("&", "§"));
			}
			return list;
		}
		//循环lore
		for(int e= 0;e <list.size();e++){
			String lore = list.get(e);
			for (int i= 0; i < args.length;i++){
				lore = lore.replace("&", "§").replace("{" + i + "}", args[i]==null ? "null" : args[i]);
			}
			list.set(e,lore);
		}
		return list;
	}
	
	public static void playerTitle(Player player,String loc){
		String str = messages.getString(loc).replace("&", "§");
		if(str.contains(":")){
			player.sendTitle(str.split(":")[0], str.split(":")[1], 2, 30, 3);
		}else{
			player.sendTitle(str, "", 2, 30, 3);
		}
	}
}
