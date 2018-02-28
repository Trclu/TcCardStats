package github.saukiya.angelcardstats.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

public class Config {
	private static YamlConfiguration config;
	final static File configFile = new File("plugins" + File.separator + "AngelCardStats" + File.separator + "Config.yml");

	
	final public static String PLAYER_DEFAULT_SLOT = "PlayerDefaultSlot";
	
	final public static String NAME_DAMAGE = "Stats.Damage.Name";
	final public static String NAME_HEALTH = "Stats.Health.Name";
	final public static String NAME_LEFENSE = "Stats.Lefense.Name";
	final public static String NAME_SPEED = "Stats.Speed.Name";
	final public static String NAME_CRIT_DAMAGE = "Stats.CritDamage.Name";
	final public static String NAME_PENETRATION = "Stats.Penetration.Name";
	final public static String NAME_LIFE_STEAL = "Stats.LifeSteal.Name";
	final public static String NAME_LIMIT_LEVEL = "Stats.LimitLevel.Name";
	final public static String NAME_CRIT = "Stats.Crit.Name";
	final public static String NAME_IGNITION = "Stats.Ignition.Name";
	final public static String NAME_WITHER = "Stats.Wither.Name";
	final public static String NAME_POISON = "Stats.Poison.Name";
	final public static String NAME_BLINDNESS = "Stats.Blindness.Name";
	final public static String NAME_SLOWNESS = "Stats.Slowness.Name";
	final public static String NAME_LIGHTNING = "Stats.Lightning.Name";
	final public static String NAME_REAL = "Stats.Real.Name";
	final public static String NAME_TEARING = "Stats.Tearing.Name";
	final public static String NAME_REFLECTION = "Stats.Reflection.Name";
	final public static String NAME_BLOCK = "Stats.Block.Name";
	final public static String NAME_DODGE = "Stats.Dodge.Name";

	final public static String VALUE_DAMAGE = "Stats.Damage.Value";
	final public static String VALUE_HEALTH = "Stats.Health.Value";
	final public static String VALUE_LEFENSE = "Stats.Lefense.Value";
	final public static String VALUE_SPEED = "Stats.Speed.Value";
	final public static String VALUE_CRIT_DAMAGE = "Stats.CritDamage.Value";
	final public static String VALUE_PENETRATION = "Stats.Penetration.Value";
	final public static String VALUE_LIFE_STEAL = "Stats.LifeSteal.Value";
	final public static String VALUE_CRIT = "Stats.Crit.Value";
	final public static String VALUE_IGNITION = "Stats.Ignition.Value";
	final public static String VALUE_WITHER = "Stats.Wither.Value";
	final public static String VALUE_POISON = "Stats.Poison.Value";
	final public static String VALUE_BLINDNESS = "Stats.Blindness.Value";
	final public static String VALUE_SLOWNESS = "Stats.Slowness.Value";
	final public static String VALUE_LIGHTNING = "Stats.Lightning.Value";
	final public static String VALUE_REAL = "Stats.Real.Value";
	final public static String VALUE_TEARING = "Stats.Tearing.Value";
	final public static String VALUE_REFLECTION = "Stats.Reflection.Value";
	final public static String VALUE_BLOCK = "Stats.Block.Value";
	final public static String VALUE_DODGE = "Stats.Dodge.Value";
	
	static void createConfig(){
        Bukkit.getConsoleSender().sendMessage("[AngelCardStats] §cCreate Config.yml");
		config = new YamlConfiguration();
		config.set(NAME_DAMAGE, String.valueOf("攻击力"));
		config.set(NAME_HEALTH, String.valueOf("血量"));
		config.set(NAME_LEFENSE, String.valueOf("护甲"));
		config.set(NAME_SPEED, String.valueOf("速度"));
		config.set(NAME_CRIT_DAMAGE, String.valueOf("暴击伤害"));
		config.set(NAME_PENETRATION, String.valueOf("护甲穿透"));
		config.set(NAME_LIFE_STEAL, String.valueOf("生命偷取"));
		config.set(NAME_LIMIT_LEVEL, String.valueOf("限制等级"));
		config.set(NAME_CRIT, String.valueOf("暴击几率"));
		config.set(NAME_IGNITION, String.valueOf("点燃几率"));
		config.set(NAME_WITHER, String.valueOf("凋零几率"));
		config.set(NAME_POISON, String.valueOf("中毒几率"));
		config.set(NAME_BLINDNESS, String.valueOf("失明几率"));
		config.set(NAME_SLOWNESS, String.valueOf("缓慢几率"));
		config.set(NAME_LIGHTNING, String.valueOf("雷霆几率"));
		config.set(NAME_REAL, String.valueOf("破甲几率"));
		config.set(NAME_TEARING, String.valueOf("撕裂几率"));
		config.set(NAME_REFLECTION, String.valueOf("反射几率"));
		config.set(NAME_BLOCK, String.valueOf("格挡几率"));
		config.set(NAME_DODGE, String.valueOf("闪避几率"));

		config.set(VALUE_DAMAGE, 1);
		config.set(VALUE_HEALTH, 1);
		config.set(VALUE_LEFENSE, 1);
		config.set(VALUE_SPEED, 1);
		config.set(VALUE_CRIT_DAMAGE, 5);
		config.set(VALUE_PENETRATION, 5);
		config.set(VALUE_LIFE_STEAL, 2);
		config.set(VALUE_CRIT, 5);
		config.set(VALUE_IGNITION, 2);
		config.set(VALUE_WITHER, 2);
		config.set(VALUE_POISON, 2);
		config.set(VALUE_BLINDNESS, 2);
		config.set(VALUE_SLOWNESS, 2);
		config.set(VALUE_LIGHTNING, 2);
		config.set(VALUE_REAL, 2);
		config.set(VALUE_TEARING, 2);
		config.set(VALUE_REFLECTION, 2);
		config.set(VALUE_BLOCK, 2);
		config.set(VALUE_DODGE, 2);
		
		config.set(PLAYER_DEFAULT_SLOT, Integer.valueOf(36));
		try {config.save(configFile);} catch (IOException e) {e.printStackTrace();}
	}
	
	static public void loadConfig(){
		createConfig();
		//检测Config.yml是否存在
		if(!configFile.exists()){
			//创建Config.yml
//			createConfig();
			return;
		}else{
	        Bukkit.getConsoleSender().sendMessage("[AngelCardStats] §aFind Config.yml");
		}
		config = new YamlConfiguration();
		//读取config并存储
		try {config.load(configFile);} catch (IOException | InvalidConfigurationException e) {e.printStackTrace();Bukkit.getConsoleSender().sendMessage("[AngelCardStats] §c读取config时发生错误");}
	}

	public static String getConfig(String loc){
		String raw = config.getString(loc);
		if(raw == null || raw.isEmpty()){
			createConfig();
			raw = config.getString(loc);
			return raw;
		}
		raw = raw.replace("&", "§");
		return raw;
	}
	public static ArrayList<String> getList(String loc,String... args){
		ArrayList<String> list = (ArrayList<String>) config.getStringList(loc);
		if (list == null || list.isEmpty()) {
			createConfig();
			list = (ArrayList<String>) config.getStringList(loc);
		}
		for(int i=0;i<list.size();i++){
			list.set(i, list.get(i).replace("&", "§"));
		}
		return list;
	}
	
	public static void setConfig(String loc , Object arg){
		config.set(loc, arg);
		try {config.save(configFile);} catch (IOException e) {e.printStackTrace();}
	}
}
