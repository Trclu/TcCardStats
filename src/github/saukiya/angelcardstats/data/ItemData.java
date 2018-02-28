package github.saukiya.angelcardstats.data;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_11_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import github.saukiya.angelcardstats.AngelCardStats;
import github.saukiya.angelcardstats.util.Message;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.ComponentBuilder.FormatRetention;
import net.minecraft.server.v1_11_R1.NBTTagCompound;
import net.minecraft.server.v1_11_R1.NBTTagList;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.TranslatableComponent;

public class ItemData {
	private static YamlConfiguration itemData;
	private static HashMap<String,ItemStack> itemMap = new HashMap<String,ItemStack>();
	final static File dataFile = new File("plugins" + File.separator + "AngelCardStats" + File.separator + "Item.yml");

	static Class<?> CraftItemStack = null;
	static Class<?> NMSItemStack;
	static Class<?> NBTTagCompound;
	static Class<?> NBTTagList;
	static Class<?> NBTBase;
	
	static Method asNMSCopay;
	static Method getTag;
	static Method hasTag;
	static Method setString;
	static Method add;
	static Method set;
	static Method setTag;
	static Method asBukkitCopy;

	static Method hasKey;
//	static Method getList;


	
	public static void loadItemData() {
		if(CraftItemStack == null){//反射初次处理 防/acs reload时重新计算反射
			String packet = Bukkit.getServer().getClass().getPackage().getName();//CraftBukkit 包名
			String NMSname = "net.minecraft.server." + packet.substring(packet.lastIndexOf('.') + 1);//NMS 包名
			try {
				CraftItemStack = Class.forName(packet+".inventory.CraftItemStack");
				NMSItemStack = Class.forName(NMSname+".ItemStack");
				NBTTagCompound = Class.forName(NMSname+".NBTTagCompound");
				NBTTagList = Class.forName(NMSname+".NBTTagList");
				NBTBase = Class.forName(NMSname+".NBTBase");
				
				asNMSCopay = CraftItemStack.getDeclaredMethod("asNMSCopy", ItemStack.class);
				getTag = NMSItemStack.getDeclaredMethod("getTag");
				hasTag = NMSItemStack.getDeclaredMethod("hasTag");
				setString = NBTTagCompound.getDeclaredMethod("setString",new Class[]{String.class,String.class});
				add = NBTTagList.getDeclaredMethod("add", NBTBase);
				set = NBTTagCompound.getDeclaredMethod("set", new Class[]{String.class,NBTBase});
				setTag = NMSItemStack.getDeclaredMethod("setTag", NBTTagCompound);
				asBukkitCopy = CraftItemStack.getDeclaredMethod("asBukkitCopy", NMSItemStack);
				
				hasKey = NBTTagCompound.getDeclaredMethod("hasKey", String.class);
//				getList = NBTTagCompound.getDeclaredMethod("getList", new Class[]{String.class,int.class});
			} catch (ClassNotFoundException | NoSuchMethodException | SecurityException e1) {
				e1.printStackTrace();
			}
		}
		itemMap.clear();
		if (!dataFile.exists()) {
			createDefaultItem();
		}else {
	        Bukkit.getConsoleSender().sendMessage("[AngelCardStats] §aFind Item.yml");
		}
		itemData = new YamlConfiguration();
		try {itemData.load(dataFile);} catch (IOException | InvalidConfigurationException e) {e.printStackTrace();Bukkit.getConsoleSender().sendMessage("§8[§6AngelCardStats§8] §a读取Item时发生错误");}
		loadItemMap();
		Bukkit.getConsoleSender().sendMessage("[AngelCardStats] §a已读取 §6"+itemMap.size()+" §a个物品");
	}
	
	static public ItemStack setCard(ItemStack item,List<String> loreList){//通过反射给予nms卡牌标签 
		try {
			Object nmsItem = asNMSCopay.invoke(CraftItemStack,item);
			Object itemTag = ((Boolean)hasTag.invoke(nmsItem)) ? getTag.invoke(nmsItem): NBTTagCompound.newInstance();
			Object tagList = NBTTagList.newInstance();
			Object tag = NBTTagCompound.newInstance();
			if(loreList!=null){
				for(int i=0;i<loreList.size();i++){//存储默认的lore 已保证将来的洗炼系统
					setString.invoke(tag, "DefaultLore-"+i,loreList.get(i));
				}
			}
			add.invoke(tagList, tag);
			set.invoke(itemTag, AngelCardStats.getPlugin().getName() ,tagList);
			setTag.invoke(nmsItem, itemTag);
			item = (ItemStack) asBukkitCopy.invoke(CraftItemStack, nmsItem);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e) {
			e.printStackTrace();
			Bukkit.getConsoleSender().sendMessage("[AngelCardStats] §c反射出现问题！ 错误代码在ItemData.setNBT()方法！");
		}
		return item;
	}

	static public Boolean isCard(ItemStack item){//通过反射识别nms卡牌标签 
		try {
			Object nmsItem = asNMSCopay.invoke(CraftItemStack,item);
			if(!(Boolean)hasTag.invoke(nmsItem))return false;
			Object itemTag = getTag.invoke(nmsItem);
			if((boolean) hasKey.invoke(itemTag, AngelCardStats.getPlugin().getName()))return true;
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
			Bukkit.getConsoleSender().sendMessage("[AngelCardStats] §c反射出现问题！ 错误代码在ItemData.isCard()方法！");
		}
		return false;
	}
	
	//加载Map
	@SuppressWarnings("deprecation")
	public static void loadItemMap() {
		for(String Name:itemData.getKeys(false)) {
			String itemName = itemData.getString(Name+".Name");
			String id = itemData.getString(Name+".ID");
	        int itemMaterial = 0;
	        int itemDurability = 0;
	        if (id.contains(":")){
	        	itemMaterial = Integer.valueOf(id.split(":")[0]);
	        	itemDurability = Integer.valueOf(id.split(":")[1]);
	        }else{
	        	itemMaterial = Integer.valueOf(id);
	        }
	        if (itemMaterial==0)itemMaterial = 268;
			List<String> itemLore = itemData.getStringList(Name+".Lore");
			Boolean enchant = itemData.getBoolean(Name+".Enchant");
			Boolean unbreakable = itemData.getBoolean(Name+".Unbreakable");
			
			ItemStack item = new ItemStack(itemMaterial,1,(short) itemDurability);
			ItemMeta itemMeta = item.getItemMeta();
			if (itemName != null) itemMeta.setDisplayName(itemName.replace("&", "§"));
			if (itemLore != null) itemMeta.setLore(Message.replaceColor(itemLore));
			if (unbreakable != null) itemMeta.setUnbreakable(unbreakable);
			if (enchant!=null){
				itemMeta.addEnchant(Enchantment.DURABILITY, 1, true);
				itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			}
			item.setItemMeta(itemMeta);
			itemMap.put(Name, setCard(item,itemLore));
		}
	}
	
	public static void saveItemData(){
		try {itemData.save(dataFile);} catch (IOException e) {e.printStackTrace();}
	}
	//创建默认的Item.yml
	static void createDefaultItem() {
        Bukkit.getConsoleSender().sendMessage("[AngelCardStats] §cCreate Item.yml");
        itemData = new YamlConfiguration();
        ArrayList<String> loreList = new ArrayList<String>();
        loreList.add("&c攻击力: +10");
        loreList.add("&d速度: +100%");
        loreList.add("&d点燃几率: +33%");
        loreList.add("&d撕裂几率: +10%");
        loreList.add("");
        loreList.add("&6限制等级: 10级");
        itemData.set("默认.Name", String.valueOf("炎之卡牌"));
        itemData.set("默认.ID", Integer.valueOf("339"));
        itemData.set("默认.Lore", loreList);
        itemData.set("默认.Enchant", true);
        itemData.set("默认.Unbreakable", true);
		try {itemData.save(dataFile);} catch (IOException e) {e.printStackTrace();}
	}
	
	public static Boolean isItem(String itemName) {
		return itemMap.containsKey(itemName);
	}
	
	public static ItemStack getItem(String itemName) {
		if(itemMap.containsKey(itemName)) {
			return itemMap.get(itemName);
		}else{
			return null;
		}
	}
	//保存方法
	@SuppressWarnings("deprecation")
	public static void saveItem(String itemName,ItemStack itemStack) {
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemStack.getType();
		String name = itemMeta.getDisplayName();
		int id = itemStack.getTypeId();
		ArrayList<String> lore = (ArrayList<String>) itemMeta.getLore();
		if(lore !=null) {
			for (int i=0;i<lore.size();i++){
				lore.set(i, lore.get(i).replace("§", "&"));
			}
		}
		Boolean enchant = itemMeta.hasEnchants();
		Boolean unbreakable = itemMeta.isUnbreakable();
		itemData.set(itemName+".ID", id);
		if(name!=null)itemData.set(itemName+".Name", name.replace("§", "&"));
		if(lore!=null)itemData.set(itemName+".Lore", lore);
		if(unbreakable!=null)itemData.set(itemName+".Unbreakable", unbreakable);
		if(enchant!=null)itemData.set(itemName+".Enchant", enchant);
		saveItemData();
		loadItemMap();
	}
	//发送物品列表给指令者
	public static void sendItemMapToPlayer(CommandSender sender,int page) {
//		TextComponent message = new TextComponent( "Click me" );
//		message.setClickEvent( new ClickEvent( ClickEvent.Action.OPEN_URL, "http://spigotmc.org" ) );
//		ComponentBuilder cb = new ComponentBuilder(" ");
//		cb.
//		message.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Goto the Spigot website!").create()) );
//		player.spigot().sendMessage( message );
		if(sender instanceof Player){
			sender.sendMessage("§e物品列表§b - §e点击获取");
		}else{
			sender.sendMessage("§e物品列表");
		}
		int z=0;
		for(String key:itemMap.keySet()) {
			z++;
			ItemStack item = itemMap.get(key);
			ItemMeta itemMeta = item.getItemMeta();
			String itemName = "§cN/A";
			if(itemMeta.hasDisplayName()) {
				itemName = itemMeta.getDisplayName();
			}else {
				itemName = item.getType().name();
			}
			List<String> itemLore = itemMeta.getLore();
			ComponentBuilder bc = new ComponentBuilder(itemName+"§b - "+item.getTypeId()+":"+item.getDurability());
			if(itemLore!=null) {
				for(String lore:itemLore) {
					bc.append("\n"+lore);
				}
			}else {
				bc.append("\n§cN/A");
			}
			String str = "§b"+z+" - §a"+key+" §7("+itemName+"§7)";
			if(sender instanceof Player){
				TextComponent message = new TextComponent(str);
				message.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT , bc.create()) );
				message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/acs give "+key));
				((Player) sender).spigot().sendMessage(message);
			}else{
				sender.sendMessage(str);
			}
		}
	}
}
