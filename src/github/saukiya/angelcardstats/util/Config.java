package github.saukiya.angelcardstats.util;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.List;

public class Config {
    final public static String PLAYER_DEFAULT_SLOT = "PlayerDefaultSlot";
    final public static String PLAYER_BUY_SLOT_ENABLED = "PlayerBuySlot.Enabled";
    final public static String PLAYER_BUY_SLOT_POINTS = "PlayerBuySlot.Points";
    final public static String PLAYER_BUY_SLOT_VALUE_POINTS = "PlayerBuySlot.ValuePoints";
    final public static String PLAYER_SELL_CARD_ENABLED = "PlayerSellCard.Enabled";
    final public static String PLAYER_SELL_CARD_VALUE_POINTS = "PlayerSellCard.ValuePoints";
    final public static String SUIT = "Suit";
    final public static String SUIT_DEFAULT_NAME = "Suit.Default.Name";
    final public static String SUIT_DEFAULT_LIST = "Suit.Default.List";
    final public static String SUIT_DEFAULT_EFFECT = "Suit.Default.Effect";
    final public static String NAME_DAMAGE = "Stats.Damage.Name";
    final public static String NAME_HEALTH = "Stats.Health.Name";
    final public static String NAME_DEFENSE = "Stats.Lefense.Name";
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
    final public static String NAME_CARD_VALUE = "Stats.CardValue.Name";
    final public static String VALUE_DAMAGE = "Stats.Damage.Value";
    final public static String VALUE_HEALTH = "Stats.Health.Value";
    final public static String VALUE_DEFENSE = "Stats.Lefense.Value";
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
    final static File configFile = new File("plugins" + File.separator + "AngelCardStats" + File.separator + "Config.yml");
    private static YamlConfiguration config;

    static void createConfig() {
        Bukkit.getConsoleSender().sendMessage("[AngelCardStats] §cCreate Config.yml");
        config = new YamlConfiguration();


        config.set(PLAYER_DEFAULT_SLOT, 36);
        config.set(PLAYER_BUY_SLOT_ENABLED, true);
        config.set(PLAYER_BUY_SLOT_POINTS, 40);
        config.set(PLAYER_BUY_SLOT_VALUE_POINTS, 10);
        config.set(PLAYER_SELL_CARD_ENABLED, true);
        config.set(PLAYER_SELL_CARD_VALUE_POINTS, 1.2D);

        List<String> itemList = new ArrayList<>();
        itemList.add("孙逊");
        itemList.add("貂蝉");
        itemList.add("庞德");
        List<String> effectList = new ArrayList<>();
        effectList.add("&c攻击力: +5");
        effectList.add("&d速度: +10%");
        effectList.add("&d雷霆几率: +33%");
        config.set(SUIT_DEFAULT_NAME, "三侠合璧");
        config.set(SUIT_DEFAULT_LIST, itemList);
        config.set(SUIT_DEFAULT_EFFECT, effectList);

        config.set(NAME_DAMAGE, "攻击力");
        config.set(NAME_HEALTH, "血量");
        config.set(NAME_DEFENSE, "护甲");
        config.set(NAME_SPEED, "速度");
        config.set(NAME_CRIT_DAMAGE, "暴击伤害");
        config.set(NAME_PENETRATION, "护甲穿透");
        config.set(NAME_LIFE_STEAL, "生命偷取");
        config.set(NAME_LIMIT_LEVEL, "限制等级");
        config.set(NAME_CRIT, "暴击几率");
        config.set(NAME_IGNITION, "点燃几率");
        config.set(NAME_WITHER, "凋零几率");
        config.set(NAME_POISON, "中毒几率");
        config.set(NAME_BLINDNESS, "失明几率");
        config.set(NAME_SLOWNESS, "缓慢几率");
        config.set(NAME_LIGHTNING, "雷霆几率");
        config.set(NAME_REAL, "破甲几率");
        config.set(NAME_TEARING, "撕裂几率");
        config.set(NAME_REFLECTION, "反射几率");
        config.set(NAME_BLOCK, "格挡几率");
        config.set(NAME_DODGE, "闪避几率");
        config.set(NAME_CARD_VALUE, "卡牌点数");

        config.set(VALUE_DAMAGE, 1);
        config.set(VALUE_HEALTH, 1);
        config.set(VALUE_DEFENSE, 1);
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
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static public void loadConfig() {
//		createConfig();
        //检测Config.yml是否存在
        if (!configFile.exists()) {
            //创建Config.yml
            createConfig();
            return;
        } else {
            Bukkit.getConsoleSender().sendMessage("[AngelCardStats] §aFind Config.yml");
        }
        config = new YamlConfiguration();
        //读取config并存储
        try {
            config.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            Bukkit.getConsoleSender().sendMessage("[AngelCardStats] §c读取config时发生错误");
        }
    }

    public static YamlConfiguration getConfig() {
        return config;
    }

    public static String getString(String loc) {
        String raw = config.getString(loc);
        if (raw == null || raw.isEmpty()) {
            createConfig();
            raw = config.getString(loc);
            return raw;
        }
        raw = raw.replace("&", "§");
        return raw;
    }

    public static List<String> getList(String loc, String... args) {
        List<String> list = config.getStringList(loc);
        if (list == null || list.isEmpty()) {
            createConfig();
            list = config.getStringList(loc);
        }
        for (int i = 0; i < list.size(); i++) {
            list.set(i, list.get(i).replace("&", "§"));
        }
        return list;
    }

    public static Double getDouble(String loc) {
        Double raw = 0.0D;
        if (config.getString(loc) != null) {
            raw = config.getDouble(loc);
        }
        return raw;
    }

    public static int getInt(String loc) {
        int raw = 0;
        if (config.getString(loc) != null) {
            raw = config.getInt(loc);
        }
        return raw;
    }


    public static void setConfig(String loc, Object arg) {
        config.set(loc, arg);
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
