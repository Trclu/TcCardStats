package github.saukiya.angelcardstats.event;

import github.saukiya.angelcardstats.AngelCardStats;
import github.saukiya.angelcardstats.data.StatsData;
import github.saukiya.angelcardstats.data.StatsDataRead;
import github.saukiya.angelcardstats.util.Message;
import org.bukkit.EntityEffect;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

/*本类由 Saukiya 在 2018年2月5日 上午2:43:50 时创建
 *TIM:admin@Saukiya.cn
 *GitHub:https://github.com/Saukiya
 **/

public class OnDamageEvent implements Listener {

//	@EventHandler
//	void onProjectileHitEvent(EntityShootBowEvent event){
//		Entity projectile = event.getProjectile();
//		System.out.println(projectile);
//		LivingEntity entity = event.getEntity();
//		int level = StatsDataRead.getLevel(entity);
//		ItemStack item = event.getBow();
//		StatsData data = StatsDataRead.getEntityData(player);
//		System.out.println(data.getDamage());
//		StatsDataRead.setProjectileData(projectile.getUniqueId(), data);
//	}

    @EventHandler
//伤害事件
    void onEntityDamageByPlayerEvent(EntityDamageByEntityEvent event) {
        double damage = event.getDamage();
        LivingEntity entity = null;
        Player damager = null;
        StatsData entityData = new StatsData();
        StatsData damagerData = null;

        //当攻击者为投抛物时，
        if (event.getDamager() instanceof Projectile) {//攻击者获取
            Projectile projectile = (Projectile) event.getDamager();
            if (projectile.getShooter() instanceof Player) {
                damager = (Player) projectile.getShooter();
            }
        } else if (event.getDamager() instanceof Player) {
            damager = (Player) event.getDamager();
        }
        if (event.getEntity() instanceof LivingEntity) {//受害者获取
            entity = (LivingEntity) event.getEntity();
        }
        //若有一方为null 则取消
        if (entity == null || damager == null) return;

        //读取生物数据
        if (entity instanceof Player) entityData = StatsDataRead.getPlayerData(((Player) entity).getName());
        if (damager instanceof Player) damagerData = StatsDataRead.getPlayerData(damager.getName());


        damage += damagerData.getDamage();//增加生物攻击力

        //闪避
        if (StatsDataRead.probability(entityData.getDodge())) {
            event.setCancelled(true);
            Message.playerTitle(damager, Message.BATTLE_DODGE);
            return;
        }
        //暴击
        if (StatsDataRead.probability(damagerData.getCrit())) {
            damage *= damagerData.getCritDamage() / 100;
            Message.playerTitle(damager, Message.BATTLE_CRIT);
        }
        //破甲 当破甲后，无法减免、格挡、反射
        if (!StatsDataRead.probability(damagerData.getReal())) {
            //护甲
            double defense = StatsDataRead.getDefense(entityData.getDefense() - damagerData.getPenetration());
            damage = damage * defense;
            //反射
            if (StatsDataRead.probability(entityData.getReflection())) {
                damager.playEffect(EntityEffect.HURT);
                damager.setHealth(damager.getHealth() - (damage / (2 + new Random().nextInt(4))));
                Message.playerTitle(damager, Message.BATTLE_REFLECTION);
            }
            //格挡
            else if (StatsDataRead.probability(entityData.getBlock())) {
                damage /= 2 + new Random().nextInt(4);
                Message.playerTitle(damager, Message.BATTLE_BLOCK);
            }
        } else {
            Message.playerTitle(damager, Message.BATTLE_REAL);
        }
        //吸血
        if (damagerData.getLifeSteal() != 0) {
            double setHealth = damager.getHealth() + (damage * damagerData.getLifeSteal() / 100);
            if (setHealth > damager.getMaxHealth()) setHealth = damager.getMaxHealth();
            damager.setHealth(setHealth);
        }
        //点燃
        if (StatsDataRead.probability(damagerData.getIgnition())) {
            entity.setFireTicks(60);
            //TODO 点燃消息
            Message.playerTitle(damager, Message.BATTLE_IGNITION);
        }
        //凋零
        if (StatsDataRead.probability(damagerData.getWither())) {
            entity.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 60 + new Random().nextInt(60), 1));
            Message.playerTitle(damager, Message.BATTLE_WITHER);
        }
        //中毒
        if (StatsDataRead.probability(damagerData.getPoison())) {
            entity.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 60 + new Random().nextInt(60), 1));
            //TODO 中毒消息
            Message.playerTitle(damager, Message.BATTLE_POISON);
        }
        //失明
        if (StatsDataRead.probability(damagerData.getBlindness())) {
            entity.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60 + new Random().nextInt(60), 1));
            Message.playerTitle(damager, Message.BATTLE_BLINDNESS);
        }
        //缓慢
        if (StatsDataRead.probability(damagerData.getSlowness())) {
            entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60 + new Random().nextInt(60), 1));
            Message.playerTitle(damager, Message.BATTLE_SLOWNESS);
        }
        //雷霆
        if (StatsDataRead.probability(damagerData.getLightning())) {
            Message.playerTitle(damager, Message.BATTLE_LIGHTNING);
            entity.getWorld().strikeLightningEffect(entity.getLocation());
            entity.damage(entity.getHealth() * new Random().nextDouble() / 10);
        }
        //撕裂
        if (StatsDataRead.probability(damagerData.getTearing())) {
            //TODO 撕裂消息
            Message.playerTitle(damager, Message.BATTLE_TEARING);
            LivingEntity runnableEntity = entity;
            new BukkitRunnable() {
                Double health = runnableEntity.getHealth();
                int i = 0;

                @Override
                public void run() {
                    i++;
                    if (i >= 10) cancel();
                    if (runnableEntity.isDead()) cancel();
                    if (event.isCancelled()) cancel();
                    runnableEntity.setNoDamageTicks(0);
                    runnableEntity.damage(health / 100);
                }
            }.runTaskTimer(AngelCardStats.getPlugin(), 5, 1);
        }

        event.setDamage(damage);
    }
}
