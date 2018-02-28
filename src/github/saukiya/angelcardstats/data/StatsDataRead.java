package github.saukiya.angelcardstats.data;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Pattern;

import github.saukiya.angelcardstats.AngelCardStats;
import github.saukiya.angelcardstats.data.StatsData;
import github.saukiya.angelcardstats.util.Config;
import github.saukiya.angelcardstats.util.Message;
import net.minecraft.server.v1_11_R1.NBTTagCompound;
import net.minecraft.server.v1_11_R1.NBTTagList;
import net.minecraft.server.v1_11_R1.NBTTagString;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_11_R1.inventory.CraftItemStack;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class StatsDataRead {
	public static YamlConfiguration data;
	final static File dataFile = new File("plugins" + File.separator + "AngelCardStats" + File.separator + "Data.dat");
	//存储玩家属性
	static public HashMap<UUID,StatsData> equipmentMap = new HashMap<UUID,StatsData>();
	static public HashMap<UUID,StatsData> mainHandMap = new HashMap<UUID,StatsData>();
	static public HashMap<UUID,StatsData> rpgInventoryMap = new HashMap<UUID,StatsData>();
	
	static public void loadData(){
		//检测data.dat是否存在
//		if (!dataFile.exists()){
//	        Bukkit.getConsoleSender().sendMessage("[AngelCardStats] §cCreate Data.dat");
//			data = new YamlConfiguration();
//			try {data.save(dataFile);} catch (IOException e) {e.printStackTrace();}
//		}else{
//	        Bukkit.getConsoleSender().sendMessage("[AngelCardStats] §aFind Data.dat");
//		}
//		data = new YamlConfiguration();
//		try {data.load(dataFile);} catch (IOException | InvalidConfigurationException e) {e.printStackTrace();Bukkit.getConsoleSender().sendMessage("[AngelCardStats] §c读取data时发生错误");}
	}
	static public Double getDefense(double d){
		double defense = 0.000D;
		defense = 150/(150+d);
		return defense;
	}
	static public Boolean probability(double d){
		if(d ==0)return false;
		double random = new Random().nextDouble();
		return d/100 > random;
	}
	//读取抛射物数据
	static public StatsData getProjectileData(UUID uuid){
		StatsData data = new StatsData();
		if(equipmentMap.containsKey(uuid)){
			data.add(equipmentMap.remove(uuid));
		}
		return data;
	}
	static public void setProjectileData(UUID uuid,StatsData statsData){
		equipmentMap.put(uuid, statsData);
	}
	//获取生物总数据
	static public StatsData getEntityData(LivingEntity entity,Boolean main,StatsData... statsData){
		StatsData data = new StatsData();
		UUID uuid = entity.getUniqueId();
		data.setHealth(20D);
		//生物默认数据
		if(entity instanceof Player){
			//TODO 设置中给予默认属性
			data.setHealth(20D);
			data.setSpeed(100D);
			data.setDefense(20D);
			data.setCritDamage(150D);
		}
		if(main){
			if(mainHandMap.containsKey(uuid)){
				data.add(mainHandMap.get(uuid));
			}
		}else{
			if(statsData.length>=1){
				data.add(statsData[0]);
			}
		}
		if(equipmentMap.containsKey(uuid)){
			data.add(equipmentMap.get(uuid));
		}
		if(rpgInventoryMap.containsKey(uuid)){
			data.add(rpgInventoryMap.get(uuid));
		}
		//TODO APIMap
		return data;
	}
	//清除生物数据
	static public void clearEntityData(LivingEntity entity){
		UUID uuid = entity.getUniqueId();
		if (equipmentMap.containsKey(uuid)){
			equipmentMap.remove(uuid);
		}
		if (mainHandMap.containsKey(uuid)){
			mainHandMap.remove(uuid);
		}
		if (rpgInventoryMap.containsKey(uuid)){
			rpgInventoryMap.remove(uuid);
		}
	}
	
	
	//加载生物装备槽的数据
	static public void loadEquipmentData(LivingEntity entity){
		int level = getLevel(entity);
		StatsData data = new StatsData();
		data.add(getItemData(entity,level,entity.getEquipment().getArmorContents()));
		equipmentMap.put(entity.getUniqueId(), data);
	}
	//加载生物手中的数据
	static public void loadMainHandData(LivingEntity entity){
		int level = getLevel(entity);
		ItemStack item = entity.getEquipment().getItemInMainHand();
		StatsData data = getItemData(entity,level,item);
		mainHandMap.put(entity.getUniqueId(), data);
	}
	//加载生物物品的数据
	static public void loadMainHandData(LivingEntity entity,ItemStack item){
		int level = getLevel(entity);
		StatsData data = getItemData(entity,level,item);
		mainHandMap.put(entity.getUniqueId(), data);
	}
	//清除与数字有关的颜色
	static public String clearColor(String lore){
		ArrayList<String> ColorList = new ArrayList<String>();
		for(int i=0;i<10;i++){
			ColorList.add("§"+i);
		}
		for(int i=0;i<ColorList.size();i++){
			lore = lore.replace(ColorList.get(i), "");
		}
		return lore;
	}
	//获取物品的属性
	static public StatsData getItemData(LivingEntity entity,int level,ItemStack... itemList){
		StatsData dataList = new StatsData();
		for(ItemStack item: itemList){
			StatsData data = new StatsData();
			if(item != null){
				if (item.hasItemMeta()){
					ItemMeta meta = item.getItemMeta();
					if (meta.hasLore()){
						ArrayList<String> loreList = (ArrayList<String>) meta.getLore();
						for (String lore:loreList){
							//循环每条lore是否有所需属性
							if (lore.contains(Config.getConfig(Config.NAME_DAMAGE))){
								data.setDamage(Double.valueOf(getStats(lore)));
							}
							else if (lore.contains(Config.getConfig(Config.NAME_HEALTH))){
								data.setHealth(Double.valueOf(getStats(lore)));
							}
							else if (lore.contains(Config.getConfig(Config.NAME_LEFENSE)) && !lore.contains(Config.getConfig(Config.NAME_PENETRATION))){
								data.setDefense(Double.valueOf(getStats(lore)));
							}
							else if (lore.contains(Config.getConfig(Config.NAME_SPEED))){
								data.setSpeed(Double.valueOf(getStats(lore)));
							}
							else if (lore.contains(Config.getConfig(Config.NAME_CRIT_DAMAGE))){
								data.setCritDamage(Double.valueOf(getStats(lore)));
							}
							else if (lore.contains(Config.getConfig(Config.NAME_PENETRATION))){
								data.setPenetration(Double.valueOf(getStats(lore)));
							}
							else if (lore.contains(Config.getConfig(Config.NAME_LIFE_STEAL))){
								data.setLifeSteal(Double.valueOf(getStats(lore)));
							}
							else if (lore.contains(Config.getConfig(Config.NAME_LIMIT_LEVEL))){
								//等级不够，给予空的数据
								if(Integer.valueOf(getStats(lore))>level){
									entity.sendMessage(Message.getMsg(Message.PLAYER_NO_LEVEL_USE, meta.getDisplayName()));
									data = new StatsData();
									break;
								}
							}
							else if (lore.contains(Config.getConfig(Config.NAME_CRIT)) && !lore.contains(Config.getConfig(Config.NAME_CRIT_DAMAGE))){
								data.setCrit(Double.valueOf(getStats(lore)));
							}
							else if (lore.contains(Config.getConfig(Config.NAME_IGNITION))){
								data.setIgnition(Double.valueOf(getStats(lore)));
							}
							else if (lore.contains(Config.getConfig(Config.NAME_WITHER))){
								data.setWither(Double.valueOf(getStats(lore)));
							}
							else if (lore.contains(Config.getConfig(Config.NAME_POISON))){
								data.setPoison(Double.valueOf(getStats(lore)));
							}
							else if (lore.contains(Config.getConfig(Config.NAME_BLINDNESS))){
								data.setBlindness(Double.valueOf(getStats(lore)));
							}
							else if (lore.contains(Config.getConfig(Config.NAME_SLOWNESS))){
								data.setSlowness(Double.valueOf(getStats(lore)));
							}
							else if (lore.contains(Config.getConfig(Config.NAME_LIGHTNING))){
								data.setLightning(Double.valueOf(getStats(lore)));
							}
							else if (lore.contains(Config.getConfig(Config.NAME_REAL))){
								data.setReal(Double.valueOf(getStats(lore)));
							}
							else if (lore.contains(Config.getConfig(Config.NAME_TEARING))){
								data.setTearing(Double.valueOf(getStats(lore)));
							}
							else if (lore.contains(Config.getConfig(Config.NAME_REFLECTION))){
								data.setReflection(Double.valueOf(getStats(lore)));
							}
							else if (lore.contains(Config.getConfig(Config.NAME_BLOCK))){
								data.setBlock(Double.valueOf(getStats(lore)));
							}
							else if (lore.contains(Config.getConfig(Config.NAME_DODGE))){
								data.setDodge(Double.valueOf(getStats(lore)));
							}
						}
					}
				}
			}
			dataList.add(data);
		}
		return dataList;
	}
	//获取生物等级
	static public int getLevel(LivingEntity entity){
		int level = 0;
		if(entity instanceof Player){
			level = ((Player) entity).getLevel();
		}else{
			level = 1;
		}
		return level;
	}
	//getlore内的属性值
	static public String getStats(String lore){
		lore = clearColor(lore);
		String str = "1";
		if(lore.contains(".")){
			int d1 = Integer.valueOf(Pattern.compile("[^0-9]").matcher(lore.replace(".", "/").split("/")[0]).replaceAll("").trim());
			int d2 = Integer.valueOf(Pattern.compile("[^0-9]").matcher(lore.replace(".", "/").split("/")[1]).replaceAll("").trim());
			str = String.valueOf(d1+"."+d2);
		}else{
			str = Pattern.compile("[^0-9]").matcher(lore).replaceAll("").trim();
		}
		return str;
	}	
	//设置卡牌标签  原nms方法 现已改为反射
	static public ItemStack setCardNBT(ItemStack item,String uuid){
		net.minecraft.server.v1_11_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
		NBTTagCompound itemNbt = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
		NBTTagList dataList  = new NBTTagList();
		NBTTagCompound data = new NBTTagCompound();
		data.setString("CardUUID", uuid);//设置uuid 已经不需要uuid
		dataList.add(data);
		System.out.println(dataList);
		itemNbt.set(AngelCardStats.getPlugin().getName(), dataList);
		itemNbt.set("AttributeModifiers",dataList);
		nmsItem.setTag(itemNbt);
		item = CraftItemStack.asBukkitCopy(nmsItem);
		System.out.println(item.getItemMeta());
		
		//获取写法
		net.minecraft.server.v1_11_R1.ItemStack nmsItem2 = CraftItemStack.asNMSCopy(item);
		NBTTagCompound itemNbt2 = (nmsItem2.hasTag()) ? nmsItem2.getTag() : new NBTTagCompound();
		System.out.println(itemNbt2);
		if(itemNbt2.hasKey(AngelCardStats.getPlugin().getName())){
			System.out.println("此为卡牌");
			NBTTagList dataList2 = itemNbt2.getList(AngelCardStats.getPlugin().getName(), 0);
		}else{
			System.out.println("不是卡牌");
		}
		return item;
	}

}
