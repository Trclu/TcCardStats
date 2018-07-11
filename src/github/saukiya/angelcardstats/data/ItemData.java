package github.saukiya.angelcardstats.data;

import github.saukiya.angelcardstats.AngelCardStats;
import github.saukiya.angelcardstats.util.Message;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ItemData {
    final static File dataFile = new File("plugins" + File.separator + "AngelCardStats" + File.separator + "Item.yml");
    static Class<?> CraftItemStack = null;
    static Class<?> NMSItemStack;
    static Class<?> NBTTagCompound;
    static Class<?> NBTTagString;
    static Class<?> NBTBase;
    static Constructor<?> NewNBTTagString;
    static Method asNMSCopay;
    static Method getTag;
    static Method hasTag;
    static Method setString;
    static Method set;
    static Method setTag;
    static Method asBukkitCopy;
    static Method hasKey;
    static Method getString;
    static String version;
    private static YamlConfiguration itemData;
    private static HashMap<String, ItemStack> itemMap = new HashMap<String, ItemStack>();

    public static void loadItemData() {
        if (CraftItemStack == null) {//反射初次处理 防/acs reload时重新计算反射
            String packet = Bukkit.getServer().getClass().getPackage().getName();//CraftBukkit 包名
            String NMSname = "net.minecraft.server." + packet.substring(packet.lastIndexOf('.') + 1);//NMS 包名
//			version = packet.substring(packet.lastIndexOf('.')+1;
            Bukkit.getConsoleSender().sendMessage("[AngelCardStats] §aServerPaket: " + packet.replace(".", "-").split("-")[3]);
            version = packet;
            try {
                //这些都是 Class<?>
                CraftItemStack = Class.forName(packet + ".inventory.CraftItemStack");
                NMSItemStack = Class.forName(NMSname + ".ItemStack");
                NBTTagCompound = Class.forName(NMSname + ".NBTTagCompound");
                NBTTagString = Class.forName(NMSname + ".NBTTagString");
                NBTBase = Class.forName(NMSname + ".NBTBase");
                NewNBTTagString = NBTTagString.getConstructor(String.class);
                //这些都是Method
                asNMSCopay = CraftItemStack.getDeclaredMethod("asNMSCopy", ItemStack.class);
                getTag = NMSItemStack.getDeclaredMethod("getTag");
                hasTag = NMSItemStack.getDeclaredMethod("hasTag");
                setString = NBTTagCompound.getDeclaredMethod("setString", new Class[]{String.class, String.class});
                set = NBTTagCompound.getDeclaredMethod("set", new Class[]{String.class, NBTBase});
                setTag = NMSItemStack.getDeclaredMethod("setTag", NBTTagCompound);
                asBukkitCopy = CraftItemStack.getDeclaredMethod("asBukkitCopy", NMSItemStack);

                hasKey = NBTTagCompound.getDeclaredMethod("hasKey", String.class);
                getString = NBTTagCompound.getDeclaredMethod("getString", String.class);
//				getList = NBTTagCompound.getDeclaredMethod("getList", new Class[]{String.class,int.class});
                Bukkit.getConsoleSender().sendMessage("[AngelCardStats] §aLoad Method!");
            } catch (ClassNotFoundException | NoSuchMethodException | SecurityException e1) {
                e1.printStackTrace();
            }
        }
        itemMap.clear();
        if (!dataFile.exists()) {
            createDefaultItem();
        } else {
            Bukkit.getConsoleSender().sendMessage("[AngelCardStats] §aFind Item.yml");
        }
        itemData = new YamlConfiguration();
        try {
            itemData.load(dataFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            Bukkit.getConsoleSender().sendMessage("§8[§6AngelCardStats§8] §a读取Item时发生错误");
        }
        loadItemMap();
        Bukkit.getConsoleSender().sendMessage("[AngelCardStats] §a已读取 §6" + itemMap.size() + " §a个物品");
    }

    static public ItemStack setCard(ItemStack item, String name) {//通过反射给予nms卡牌标签
        try {
            Object nmsItem = asNMSCopay.invoke(CraftItemStack, item);
            Object itemTag = ((Boolean) hasTag.invoke(nmsItem)) ? getTag.invoke(nmsItem) : NBTTagCompound.newInstance();
            Object tagString = NewNBTTagString.newInstance(name);
            set.invoke(itemTag, AngelCardStats.getPlugin().getName(), tagString);
            setTag.invoke(nmsItem, itemTag);
            item = (ItemStack) asBukkitCopy.invoke(CraftItemStack, nmsItem);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e) {
            //
        }
        return item;
    }

    static public ItemStack update(ItemStack item, String name) {//更新物品
        if (itemMap.containsKey(name)) return itemMap.get(name);
        return item;
    }

    static public String isCard(ItemStack item) {//通过反射识别nms卡牌标签
        try {
            Object nmsItem = asNMSCopay.invoke(CraftItemStack, item);
            Object itemTag = ((Boolean) hasTag.invoke(nmsItem)) ? getTag.invoke(nmsItem) : NBTTagCompound.newInstance();
            if ((boolean) hasKey.invoke(itemTag, AngelCardStats.getPlugin().getName()))
                return (String) getString.invoke(itemTag, AngelCardStats.getPlugin().getName());
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e) {
            //
        }
        return null;
    }

    //加载Map
    @SuppressWarnings("deprecation")
    public static void loadItemMap() {
        for (String name : itemData.getKeys(false)) {
            String itemName = itemData.getString(name + ".Name");
            String id = itemData.getString(name + ".ID");
            int itemMaterial = 0;
            int itemDurability = 0;
            if (id.contains(":")) {
                itemMaterial = Integer.valueOf(id.split(":")[0]);
                itemDurability = Integer.valueOf(id.split(":")[1]);
            } else {
                itemMaterial = Integer.valueOf(id);
            }
            if (itemMaterial == 0) itemMaterial = 268;
            List<String> itemLore = itemData.getStringList(name + ".Lore");
            Boolean enchant = itemData.getBoolean(name + ".Enchant");
            Boolean unbreakable = itemData.getBoolean(name + ".Unbreakable");

            ItemStack item = new ItemStack(itemMaterial, 1, (short) itemDurability);
            ItemMeta itemMeta = item.getItemMeta();
            if (itemName != null) itemMeta.setDisplayName(itemName.replace("&", "§"));
            if (itemLore != null) itemMeta.setLore(Message.replaceColor(itemLore));
            if (unbreakable != null) itemMeta.spigot().setUnbreakable(true);
            if (enchant != null && enchant) {
                itemMeta.addEnchant(Enchantment.DURABILITY, 1, true);
                itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            item.setItemMeta(itemMeta);
            itemMap.put(name, setCard(item, name));
        }
    }

    public static void saveItemData() {
        try {
            itemData.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //创建默认的Item.yml
    static void createDefaultItem() {
        Bukkit.getConsoleSender().sendMessage("[AngelCardStats] §cCreate Item.yml");
        itemData = new YamlConfiguration();
        itemData.set("默认.Name", String.valueOf("&c炎之卡牌"));
        itemData.set("默认.ID", Integer.valueOf("339"));
        itemData.set("默认.Lore", Arrays.asList("&c攻击力: +10", "&d速度: +100%", "&d点燃几率: +33%", "&d撕裂几率: +10%", "", "&6限制等级: 10级"));
        itemData.set("默认.Enchant", true);
        itemData.set("默认.Unbreakable", true);

        itemData.set("孙逊.Name", "&f[&6SSS&f]&6陆逊英雄卡");
        itemData.set("孙逊.ID", "339");
        itemData.set("孙逊.Lore", Arrays.asList("&c攻击力: +10", "&d速度: +3%", "&e暴击几率: +5%", "&a暴击伤害: +1%", "&e====== &d[三侠合璧] &e======", "&X&d攻击力: +1000"));
        itemData.set("孙逊.Enchant", true);
        itemData.set("孙逊.Unbreakable", true);

        itemData.set("貂蝉.Name", "&f[&6SSS&f]&6貂蝉英雄卡");
        itemData.set("貂蝉.ID", "339");
        itemData.set("貂蝉.Lore", Arrays.asList("&c攻击力: +10", "&6护甲+10", "&c护甲穿透: +10", "&b闪避几率: +10%", "&e====== &d[三侠合璧] &e======", "&X&d攻击力: +1000"));
        itemData.set("貂蝉.Enchant", true);
        itemData.set("貂蝉.Unbreakable", true);

        itemData.set("庞德.Name", "&f[&6SSS&f]&6庞德英雄卡");
        itemData.set("庞德.ID", "339");
        itemData.set("庞德.Lore", Arrays.asList("&c攻击力: +10", "&c护甲穿透: +15", "&a暴击伤害: +7%", "&d凋零几率: +1%", "&e====== &d[三侠合璧] &e======", "&X&d攻击力: +1000"));
        itemData.set("庞德.Enchant", true);
        itemData.set("庞德.Unbreakable", true);

        try {
            itemData.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Boolean isItem(String itemName) {
        return itemMap.containsKey(itemName);
    }

    public static ItemStack getItem(String itemName) {
        return itemMap.getOrDefault(itemName, null);
    }

    //保存方法
    @SuppressWarnings("deprecation")
    public static void saveItem(String itemName, ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemStack.getType();
        String name = itemMeta.getDisplayName();
        int id = itemStack.getTypeId();
        List<String> lore = itemMeta.getLore();
        if (lore != null) {
            for (int i = 0; i < lore.size(); i++) {
                lore.set(i, lore.get(i).replace("§", "&"));
            }
        }
        Boolean enchant = itemMeta.hasEnchants();
        Boolean unbreakable = itemMeta.spigot().isUnbreakable();
        itemData.set(itemName + ".ID", id);
        if (name != null) itemData.set(itemName + ".Name", name.replace("§", "&"));
        if (lore != null) itemData.set(itemName + ".Lore", lore);
        if (unbreakable != null) itemData.set(itemName + ".Unbreakable", unbreakable);
        if (enchant != null) itemData.set(itemName + ".Enchant", enchant);
        saveItemData();
        loadItemMap();
    }

    //发送物品列表给指令者
    @SuppressWarnings("deprecation")
    public static void sendItemMapToPlayer(CommandSender sender, int page, String... searchs) {
//		TextComponent message = new TextComponent( "Click me" );
//		message.setClickEvent( new ClickEvent( ClickEvent.Action.OPEN_URL, "http://spigotmc.org" ) );
//		ComponentBuilder cb = new ComponentBuilder(" ");
//		cb.
//		message.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Goto the Spigot website!").create()) );
//		player.spigot().sendMessage( message );
        if (sender instanceof Player) {
            sender.sendMessage("§e物品列表§b - §e点击获取");
        } else {
            sender.sendMessage("§e物品列表");
        }
        String search = "";
        if (searchs.length > 0) {
            search = searchs[0];
            sender.sendMessage("§c正在搜索关键词: " + search);
        }
        int z = 0;
        for (String key : itemMap.keySet()) {
            ItemStack item = itemMap.get(key);
            ItemMeta itemMeta = item.getItemMeta();
            String itemName = "§cN/A";
            if (itemMeta.hasDisplayName()) {
                itemName = itemMeta.getDisplayName();
            } else {
                itemName = item.getType().name();
            }
            //搜索功能！
            if (itemName.contains(search) || key.contains(search)) ;
            else continue;
            List<String> itemLore = itemMeta.getLore();
            ComponentBuilder bc = new ComponentBuilder(itemName + "§b - " + item.getTypeId() + ":" + item.getDurability());
            if (itemLore != null) {
                for (String lore : itemLore) {
                    bc.append("\n" + lore);
                }
            } else {
                bc.append("\n§cN/A");
            }
            z++;
            String str = "§b" + z + " - §a" + key + " §7(" + itemName + "§7)";
            if (sender instanceof Player) {
                TextComponent message = new TextComponent(str);
                message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, bc.create()));
                message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/acs give " + key));
                ((Player) sender).spigot().sendMessage(message);

            } else {
                sender.sendMessage(str);
            }
        }
        if (z == 0) {
            sender.sendMessage("§c搜索失败! 请核对关键词!");
        }
    }
}
