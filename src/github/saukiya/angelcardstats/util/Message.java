package github.saukiya.angelcardstats.util;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.List;

public class Message {
    final public static String PLAYER_NO_LEVEL_USE = "Player.NoLevelUse";
    final public static String PLAYER_HAS_CARD_ITEM = "Player.HasCardItem";
    final public static String PLAYER_IS_NOT_CARD_ITEM = "Player.ThisIsNotCardItem";
    final public static String PLAYER_LOAD_STATS_DATA = "Player.LoadStatsData";
    final public static String PLAYER_COMPLETE_STATS_DATA = "Player.CompleteStatsData";
    final public static String PLAYER_BUY_SLOT = "Player.BuySlot";
    final public static String PLAYER_SELL_CARD = "Player.SellCard";
    final public static String PLAYER_CARD_VALUE_TOP_TITLE = "Player.CardValueTopTitle";
    final public static String PLAYER_CARD_VALUE_TOP = "Player.CardValueTop";
    final public static String PLAYER_CARD_VALUE_TOP_END = "Player.CardValueTopEnd";
    final public static String PLAYER_ACTIVATION_SUIT = "Player.ActivationSuit";
    final public static String ADMIN_NO_PER_CMD = "Admin.NoPermissionCommand";
    final public static String ADMIN_NO_CMD = "Admin.NoCommand";
    final public static String ADMIN_NO_FORMAT = "Admin.NoFormat";
    final public static String ADMIN_NO_ONLINE = "Admin.NoOnline";
    final public static String ADMIN_NO_ITEM = "Admin.NoItem";
    final public static String ADMIN_HAS_ITEM = "Admin.HasItem";
    final public static String ADMIN_GIVE_ITEM = "Admin.GiveItem";
    final public static String ADMIN_INFO_ITEM = "Admin.InfoItem";
    final public static String ADMIN_NO_CONSOLE = "Admin.NoConsole";
    final public static String INVENTORY_OPEN_NAME = "Inventory.Open.Name";
    final public static String INVENTORY_OPEN_BUY_SLOT_NAME = "Inventory.Open.BuySlot.Name";
    final public static String INVENTORY_OPEN_BUY_SLOT_LORE = "Inventory.Open.BuySlot.Lore";
    final public static String INVENTORY_OPEN_BUY_SLOT_NO_POINTS = "Inventory.Open.BuySlot.NoPoints";
    final public static String INVENTORY_OPEN_SUIT = "Inventory.Open.Suit";
    final public static String INVENTORY_OPEN_SELL_CARD = "Inventory.Open.SellCard";
    final public static String INVENTORY_OPEN_PAGE_UP = "Inventory.Open.PageUp";
    final public static String INVENTORY_OPEN_PAGE_DOWN = "Inventory.Open.PageDown";
    final public static String INVENTORY_OPEN_LOCK_SLOT = "Inventory.Open.LockSlot";
    final public static String INVENTORY_OPEN_SKULL_NAME = "Inventory.Open.SkullName";
    final public static String INVENTORY_STATS_NAME = "Inventory.Stats.Name";
    final public static String INVENTORY_STATS_SKULL_NAME = "Inventory.Stats.SkullName";
    final public static String INVENTORY_STATS_ATTACK = "Inventory.Stats.Attack";
    final public static String INVENTORY_STATS_DEFENSE = "Inventory.Stats.Defense";
    final public static String INVENTORY_STATS_FUNCTION = "Inventory.Stats.Function";
    final public static String INVENTORY_STATS_SUIT = "Inventory.Stats.Suit";
    final public static String INVENTORY_SELL_CARD_NAME = "Inventory.SellCard.Name";
    final public static String INVENTORY_SELL_CARD_SKULL_NAME = "Inventory.SellCard.SkullName";
    final public static String INVENTORY_SELL_CARD_SELL_CARD = "Inventory.SellCard.SellCard";
    final public static String INVENTORY_SELL_CARD_NO_CARD = "Inventory.SellCard.NoCard";
    final public static String INVENTORY_SELL_CARD_ENTER_SELL_CARD_NAME = "Inventory.SellCard.EnterSellCard.Name";
    final public static String INVENTORY_SELL_CARD_ENTER_SELL_CARD_LORE = "Inventory.SellCard.EnterSellCard.Lore";
    final public static String INVENTORY_SUIT_LIST_NAME = "Inventory.SuitList.Name";
    final public static String INVENTORY_SUIT_LIST_SUIT_LORE = "Inventory.SuitList.SuitLore";
    final public static String INVENTORY_SUIT_LIST_CLICK = "Inventory.SuitList.Click";
    final public static String INVENTORY_SUIT_NAME = "Inventory.Suit.Name";
    final public static String INVENTORY_SUIT_LORE = "Inventory.Suit.Lore";
    final public static String BATTLE_CRIT = "Battle.Crit";
    final public static String BATTLE_IGNITION = "Battle.Ignition";
    final public static String BATTLE_WITHER = "Battle.Wither";
    final public static String BATTLE_POISON = "Battle.Poison";
    final public static String BATTLE_BLINDNESS = "Battle.Blindness";
    final public static String BATTLE_SLOWNESS = "Battle.Slowness";
    final public static String BATTLE_LIGHTNING = "Battle.Lightning";
    final public static String BATTLE_REAL = "Battle.Real";
    final public static String BATTLE_TEARING = "Battle.Tearing";
    final public static String BATTLE_REFLECTION = "Battle.Reflection";
    final public static String BATTLE_BLOCK = "Battle.Block";
    final public static String BATTLE_DODGE = "Battle.Dodge";
    final public static String PLACEHOLDER_SUIT_ON = "Placeholder.Suit.On";
    final public static String PLACEHOLDER_SUIT_OFF = "Placeholder.Suit.Off";
    final public static String COMMAND_OPEN = "Command.open";
    final public static String COMMAND_INFO = "Command.info";
    final public static String COMMAND_TOP = "Command.top";
    final public static String COMMAND_STATS = "Command.stats";
    final public static String COMMAND_GIVE = "Command.give";
    final public static String COMMAND_SAVE = "Command.save";
    final public static String COMMAND_RELOAD = "Command.reload";
    final static File messageFile = new File("plugins" + File.separator + "AngelCardStats" + File.separator + "Message.yml");
    private static YamlConfiguration messages;

    static void createMessage() {
        Bukkit.getConsoleSender().sendMessage("[AngelCardStats] §cCreate Message.yml");
        messages = new YamlConfiguration();
        messages.set(PLAYER_NO_LEVEL_USE, "&8[&d卡牌&8] &c你没有达到使用 {0} &c的等级要求!");
        messages.set(PLAYER_HAS_CARD_ITEM, "&8[&d卡牌&8] &c你已经有一张相同的卡牌存放在槽内了!");
        messages.set(PLAYER_IS_NOT_CARD_ITEM, "&8[&d卡牌&8] &c这个物品不是卡牌!");
        messages.set(PLAYER_LOAD_STATS_DATA, "&8[&d卡牌&8] &7正在更新属性中");
        messages.set(PLAYER_COMPLETE_STATS_DATA, "&8[&d卡牌&8] &7更新属性完毕!");
        messages.set(PLAYER_BUY_SLOT, "&8[&d卡牌&8] &e你成功购买了一个卡槽!，你现在拥有 &6{0}&e个卡槽！");
        messages.set(PLAYER_SELL_CARD, "&8[&d卡牌&8] &e你成功出售了卡牌!");
        messages.set(PLAYER_CARD_VALUE_TOP_TITLE, "&8[&d卡牌&8] &e卡牌点数排行榜");
        messages.set(PLAYER_CARD_VALUE_TOP, "&8[&d卡牌&8] &b{0}&3. &e{1} &3- &6{2}&3点数");
        messages.set(PLAYER_CARD_VALUE_TOP_END, "&8[&d卡牌&8] &e第{0}页 - 共{1}页");
        messages.set(PLAYER_ACTIVATION_SUIT, "&8[&d卡牌&8] &e成功激活套装! &6{0}!");

        messages.set(ADMIN_NO_PER_CMD, "&8[&d卡牌&8] &c你没有权限执行此指令");
        messages.set(ADMIN_NO_CMD, "&8[&d卡牌&8] &c未找到此子指令:{0}");
        messages.set(ADMIN_NO_FORMAT, "&8[&d卡牌&8] &c格式错误!");
        messages.set(ADMIN_NO_ONLINE, "&8[&d卡牌&8] &c玩家不在线或玩家不存在!");
        messages.set(ADMIN_NO_CONSOLE, "&8[&d卡牌&8] &c控制台不允许执行此指令!");//ADMIN_INFO_ITEM
        messages.set(ADMIN_INFO_ITEM, "&e&m ----==&e[&k|&b{0}&e&k|&e]&m==---- ");
        messages.set(ADMIN_NO_ITEM, "&8[&d卡牌&8] &c卡牌不存在!");
        messages.set(ADMIN_HAS_ITEM, "&8[&d卡牌&8] &c已经存在名字为  &6{0}&c的卡牌!");
        messages.set(ADMIN_GIVE_ITEM, "&8[&d卡牌&8] &c给予 &6{0} &a{1}&c个 &6{2}&c 卡牌!");

        List<String> buySlotLore = new ArrayList<>();
        buySlotLore.add("&e当前所拥有 &6{0}&e个卡槽");
        buySlotLore.add("&c购买下一个卡槽所需 &6{1}&c修为");
        messages.set(INVENTORY_OPEN_NAME, "&d&l&o卡牌菜单");
        messages.set(INVENTORY_OPEN_BUY_SLOT_NAME, "&6购买卡槽");
        messages.set(INVENTORY_OPEN_BUY_SLOT_LORE, buySlotLore);
        messages.set(INVENTORY_OPEN_BUY_SLOT_NO_POINTS, "&c你的修为不足以购买!");
        messages.set(INVENTORY_OPEN_SUIT, "&6套装展示");
        messages.set(INVENTORY_OPEN_SELL_CARD, "&6回收卡牌");
        messages.set(INVENTORY_OPEN_PAGE_UP, "&e上一页");
        messages.set(INVENTORY_OPEN_PAGE_DOWN, "&e下一页");
        messages.set(INVENTORY_OPEN_LOCK_SLOT, "&c&o&m  锁槽  ");
        messages.set(INVENTORY_OPEN_SKULL_NAME, "&6&o点击查看属性");

        List<String> sellCardLore = new ArrayList<>();
        sellCardLore.add("&e确定要出售他们吗?");
        sellCardLore.add("&6出售后可获得 &c{0}&6修为");
        messages.set(INVENTORY_SELL_CARD_NAME, "&d&l&o卡牌出售");
        messages.set(INVENTORY_SELL_CARD_SKULL_NAME, "&e&l&o请放入你需要出售的物品");
        messages.set(INVENTORY_SELL_CARD_SELL_CARD, "&e&l&o点击出售");
        messages.set(INVENTORY_SELL_CARD_NO_CARD, "&c&l&o请放入卡牌后再点击此按钮!");
        messages.set(INVENTORY_SELL_CARD_ENTER_SELL_CARD_NAME, "&e&l&o确定出售");
        messages.set(INVENTORY_SELL_CARD_ENTER_SELL_CARD_LORE, sellCardLore);

        messages.set(INVENTORY_STATS_NAME, "&d&l&o卡牌属性");
        messages.set(INVENTORY_STATS_SKULL_NAME, "&6&l&o点击切换到卡牌菜单");
        messages.set(INVENTORY_STATS_ATTACK, "&a&l&o攻击属性");
        messages.set(INVENTORY_STATS_DEFENSE, "&9&l&o防御属性");
        messages.set(INVENTORY_STATS_FUNCTION, "&6&l&o功能属性");
        messages.set(INVENTORY_STATS_SUIT, "&b&l&o激活套装");

        List<String> suitListLore = new ArrayList<>();
        suitListLore.add("&7套装所需物品数量: &6{0} &7个");
        suitListLore.add("&7套装增幅卡牌点数: &6{1} &7点");
        suitListLore.add("&e&m -----==&e[&k|&b套装属性&e&k|&e]&m==----- ");
        messages.set(INVENTORY_SUIT_LIST_NAME, "&d&l&o卡牌套装列表");
        messages.set(INVENTORY_SUIT_LIST_SUIT_LORE, suitListLore);
        messages.set(INVENTORY_SUIT_LIST_CLICK, "&a&l&o点击查看所需物品");

        List<String> suitLore = new ArrayList<String>();
        suitLore.add("&b当前展示套装: &e{0}");
        messages.set(INVENTORY_SUIT_NAME, "&d&l&o套装所需物品");
        messages.set(INVENTORY_SUIT_LORE, suitLore);

        messages.set(BATTLE_CRIT, "                  &e&o暴击");
        messages.set(BATTLE_IGNITION, "                  &c&o点燃");
        messages.set(BATTLE_WITHER, "                  &8&o凋零");
        messages.set(BATTLE_POISON, "                  &8&o中毒");
        messages.set(BATTLE_BLINDNESS, "                  &8&o失明");
        messages.set(BATTLE_SLOWNESS, ":                  &b&o缓慢");
        messages.set(BATTLE_LIGHTNING, "                  &a&o雷霆");
        messages.set(BATTLE_REAL, "                  &c&o破甲");
        messages.set(BATTLE_TEARING, "                  &c&o撕裂");
        messages.set(BATTLE_REFLECTION, "                  &d&o反射");
        messages.set(BATTLE_BLOCK, ":                  &e&o格挡");
        messages.set(BATTLE_DODGE, "                  &e&o闪避");

        messages.set(PLACEHOLDER_SUIT_ON, "&e激活");
        messages.set(PLACEHOLDER_SUIT_OFF, "&c未激活");

        messages.set(COMMAND_OPEN, "打开卡牌菜单");
        messages.set(COMMAND_INFO, "列出手中物品可读属性");
        messages.set(COMMAND_TOP, "查看卡牌点数排行榜");
        messages.set(COMMAND_STATS, "查看自己的属性");
        messages.set(COMMAND_GIVE, "给予玩家卡牌物品");
        messages.set(COMMAND_SAVE, "保存手中物品到Items.yml");
        messages.set(COMMAND_RELOAD, "重新加载这个插件的配置");
        try {
            messages.save(messageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static public void loadMessage() {
//		createMessage();
        if (!messageFile.exists()) {
            createMessage();
        } else {
            Bukkit.getConsoleSender().sendMessage("[AngelCardStats] §aFind Message.yml");
        }
        messages = new YamlConfiguration();
        try {
            messages.load(messageFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            Bukkit.getConsoleSender().sendMessage("§8[§6AngelCardStats§8] §a读取message时发生错误");
        }
    }


    public static String getMsg(String loc, String... args) {
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
            for (int l = 0; l < replaceList.size(); l++) {
                String str = replaceList.get(l);
                String str1 = str.split(":")[0];
                String str2 = str.split(":")[1].replace("&", "§");
                if (args[i].equals(str1)) args[i] = args[i].replace(str1, str2);
            }
            raw = raw.replace("{" + i + "}", args[i] == null ? "null" : args[i]);//替换变量
        }

        return raw;
    }

    public static List<String> replaceColor(List<String> list) {
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                list.set(i, list.get(i).replace("&", "§"));
            }
        }
        return list;
    }

    public static List<String> getList(String loc, String... args) {
        List<String> list = messages.getStringList(loc);
        if (list == null || list.isEmpty()) {
            list.add("Null Message: " + loc);
            return list;
        }
        if (args == null) {
            for (int e = 0; e < list.size(); e++) {
                list.set(e, list.get(e).replace("&", "§"));
            }
            return list;
        }
        //循环lore
        for (int e = 0; e < list.size(); e++) {
            String lore = list.get(e);
            for (int i = 0; i < args.length; i++) {
                lore = lore.replace("&", "§").replace("{" + i + "}", args[i] == null ? "null" : args[i]);
            }
            list.set(e, lore);
        }
        return list;
    }

    @SuppressWarnings("deprecation")
    public static void playerTitle(Player player, String loc) {
        String str = messages.getString(loc).replace("&", "§");
        if (str.contains(":")) {
//			player.sendTitle(str.split(":")[0], str.split(":")[1], 2, 30, 3); //这个方法只有1.11.2以上才有
            player.sendTitle(str.split(":")[0], str.split(":")[1]);
        } else {
//			player.sendTitle(str, "", 2, 30, 3);
            player.sendTitle(str, "");
        }
    }
}
