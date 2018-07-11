package github.saukiya.angelcardstats.data;


/*本类由 Saukiya 在 2018年2月26日 上午12:44:35 时创建
 *TIM:admin@Saukiya.cn
 *GitHub:https://github.com/Saukiya
 **/

public class StatsData {
    private Double damage = 0D;//攻击力
    private Double health = 0D;//血量
    private Double defense = 0D;//护甲
    private Double speed = 0D;//速度
    private Double critDamage = 0D;//暴击伤害
    private Double penetration = 0D;//护甲穿透
    private Double lifeSteal = 0D;//生命偷取
    private Double crit = 0D;//暴击几率
    private Double ignition = 0D;//点燃几率
    private Double wither = 0D;//凋零几率
    private Double poison = 0D;//中毒几率
    private Double blindness = 0D;//失明几率
    private Double slowness = 0D;//缓慢几率
    private Double lightning = 0D;//缓慢几率
    private Double real = 0D;//破甲几率
    private Double tearing = 0D;//撕裂几率
    private Double reflection = 0D;//反射几率
    private Double block = 0D;//格挡几率
    private Double dodge = 0D;//闪避几率
    private int cardValue = 0;//卡牌点数

    public Double getDamage() {
        return this.damage;
    }

    public void setDamage(Double d) {
        this.damage = d;
    }

    public Double getHealth() {
        return this.health;
    }

    public void setHealth(Double d) {
        this.health = d;
    }

    public Double getDefense() {
        return this.defense;
    }

    public void setDefense(Double d) {
        this.defense = d;
    }

    public Double getSpeed() {
        return this.speed;
    }

    public void setSpeed(Double d) {
        this.speed = d;
    }

    public Double getCritDamage() {
        return this.critDamage;
    }

    public void setCritDamage(Double d) {
        this.critDamage = d;
    }

    public Double getPenetration() {
        return this.penetration;
    }

    public void setPenetration(Double d) {
        this.penetration = d;
    }

    public Double getLifeSteal() {
        return this.lifeSteal;
    }

    public void setLifeSteal(Double d) {
        this.lifeSteal = d;
    }

    public Double getCrit() {
        return this.crit;
    }

    public void setCrit(Double d) {
        this.crit = d;
    }

    public Double getIgnition() {
        return this.ignition;
    }

    public void setIgnition(Double d) {
        this.ignition = d;
    }

    public Double getWither() {
        return this.wither;
    }

    public void setWither(Double d) {
        this.wither = d;
    }

    public Double getPoison() {
        return this.poison;
    }

    public void setPoison(Double d) {
        this.poison = d;
    }

    public Double getBlindness() {
        return this.blindness;
    }

    public void setBlindness(Double d) {
        this.blindness = d;
    }

    public Double getSlowness() {
        return this.slowness;
    }

    public void setSlowness(Double d) {
        this.slowness = d;
    }

    public Double getLightning() {
        return this.lightning;
    }

    public void setLightning(Double d) {
        this.lightning = d;
    }

    public Double getReal() {
        return this.real;
    }

    public void setReal(Double d) {
        this.real = d;
    }

    public Double getTearing() {
        return this.tearing;
    }

    public void setTearing(Double d) {
        this.tearing = d;
    }

    public Double getReflection() {
        return this.reflection;
    }

    public void setReflection(Double d) {
        this.reflection = d;
    }

    public Double getBlock() {
        return this.block;
    }

    public void setBlock(Double d) {
        this.block = d;
    }

    public Double getDodge() {
        return this.dodge;
    }

    public void setDodge(Double d) {
        this.dodge = d;
    }

    public int getCardValue() {
        return this.cardValue;
    }

    public void setCardValue(int cardValue) {
        this.cardValue = cardValue;
    }

    public StatsData add(StatsData data) {
        this.damage += data.damage;
        this.health += data.health;
        this.defense += data.defense;
        this.speed += data.speed;
        this.critDamage += data.critDamage;
        this.penetration += data.penetration;
        this.lifeSteal += data.lifeSteal;
        this.crit += data.crit;
        this.ignition += data.ignition;
        this.wither += data.wither;
        this.poison += data.poison;
        this.blindness += data.blindness;
        this.slowness += data.slowness;
        this.lightning += data.lightning;
        this.real += data.real;
        this.tearing += data.tearing;
        this.reflection += data.reflection;
        this.block += data.block;
        this.dodge += data.dodge;
        this.cardValue += data.cardValue;
        return this;
    }
}
