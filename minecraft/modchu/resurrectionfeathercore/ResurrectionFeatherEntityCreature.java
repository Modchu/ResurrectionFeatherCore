package modchu.resurrectionfeathercore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import modchu.lib.Modchu_Reflect;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ResurrectionFeatherEntityCreature {
	public static HashMap entityWhiteListData = new HashMap();
	public static HashMap entityNgListData = new HashMap();
	public static List<String> whiteList = new ArrayList<String>();
	public static List<String> ngList = new ArrayList<String>();
	public static int onDeathTimeDrop = 20;
	public static int onDeathTimeDespawn = 20;
	public static int allResurrectionWaitTime = 0;
	public static long allResurrectionTime = 0;
	private static long tempResurrectionTime;
	private static Random rand = new Random();
	public static Item item;
	public static ItemStack itemstack;

	public static boolean onDeathUpdate(EntityCreature entityCreature) {
		//ResurrectionFeather.Debug("onDeathUpdate()");
		if (entityCreature != null) ;
		else {
			//ResurrectionFeather.Debug("onDeathUpdate() entityCreature == null !!");
			return false;
		}
		if (allResurrectionWaitTime > 0) {
			--allResurrectionWaitTime;
		}
		if (whiteListCheck(entityCreature) && !ngListCheck(entityCreature)) {
			if (allResurrectionWaitTime > 0) {
				if (item != null
						&& itemstack != null) {
					item.itemInteractionForEntity(itemstack, null, entityCreature);
					tempResurrectionTime = Minecraft.getMinecraft().getSystemTime();
					allResurrectionWaitTime = 0;
				}
			} else if (allResurrectionTime > 0) {
				if (tempResurrectionTime + allResurrectionTime > Minecraft.getMinecraft().getSystemTime()) {
					//ResurrectionFeather.Debug("onDeathUpdate() tempResurrectionTime + allResurrectionTime"+(tempResurrectionTime + allResurrectionTime)+" getSystemTime="+Minecraft.getMinecraft().getSystemTime());
					resurrectionEntity(entityCreature);
				} else {
					//ResurrectionFeather.Debug("onDeathUpdate() else tempResurrectionTime + allResurrectionTime"+(tempResurrectionTime + allResurrectionTime)+" getSystemTime="+Minecraft.getMinecraft().getSystemTime());
					allResurrectionTime = 0;
				}
			}
			if (entityCreature.getHealth() <= 0.0F) {
				++entityCreature.deathTime;
				//ResurrectionFeather.Debug("onDeathUpdate() run. deathTime="+deathTime);
				//ResurrectionFeather.Debug("onDeathUpdate() run. onDeathTimeDrop="+onDeathTimeDrop+" onDeathTimeDespawn="+onDeathTimeDespawn);

				if (entityCreature.deathTime == onDeathTimeDrop) {
					int recentlyHit = (Integer) Modchu_Reflect.getFieldObject(EntityLivingBase.class, "field_70718_bc", "recentlyHit", entityCreature);
					boolean isPlayer = (Boolean) Modchu_Reflect.invokeMethod(EntityLivingBase.class, "func_70684_aJ", "isPlayer", entityCreature);
					if (!entityCreature.worldObj.isRemote && (recentlyHit > 0 || isPlayer) && !entityCreature.isChild()) {
						EntityPlayer attackingPlayer = (EntityPlayer) Modchu_Reflect.getFieldObject(EntityLivingBase.class, "field_70717_bb", "attackingPlayer", entityCreature);
						int experiencePoints = (Integer) Modchu_Reflect.invokeMethod(EntityLivingBase.class, "func_70693_a", "getExperiencePoints", new Class[]{ EntityPlayer.class }, entityCreature, new Object[]{ attackingPlayer });
						for (int i = experiencePoints; i > 0;) {
							int k = EntityXPOrb.getXPSplit(i);
							i -= k;
							entityCreature.worldObj.spawnEntityInWorld(new EntityXPOrb(entityCreature.worldObj, entityCreature.posX, entityCreature.posY, entityCreature.posZ, k));
						}
					}
				}

				if (entityCreature.deathTime == onDeathTimeDespawn) {
					entityCreature.setDead();

					for (int j = 0; j < 20; j++) {
						double d = rand.nextGaussian() * 0.02D;
						double d1 = rand.nextGaussian() * 0.02D;
						double d2 = rand.nextGaussian() * 0.02D;
						entityCreature.worldObj.spawnParticle("explode", (entityCreature.posX + (rand.nextFloat() * entityCreature.width * 2.0F)) - entityCreature.width, entityCreature.posY + (rand.nextFloat() * entityCreature.height), (entityCreature.posZ + (rand.nextFloat() * entityCreature.width * 2.0F)) - entityCreature.width, d, d1, d2);
					}
				}

				if (entityCreature.deathTime > (onDeathTimeDespawn + 100)) {
					entityCreature.deathTime = onDeathTimeDespawn - 1;
				}
				return false;
			}
		} else {
			if (entityCreature.deathTime > 19) entityCreature.deathTime = 19;
			//ResurrectionFeather.Debug("super.onDeathUpdate()");
			return true;
		}
		return false;
	}

	public static boolean whiteListCheck(EntityLivingBase entity) {
		String s;
		Class c;
		Class entityClass = entity.getClass();
		boolean flag = false;
		boolean simpleNameCheck = true;
		//if (whiteList.size() == 0) ResurrectionFeather.Debug("whiteListCheck whiteList.size() == 0 !!");
		for (int i = 0; i < whiteList.size(); i++) {
			s = whiteList.get(i);
			if (s.startsWith("[") && s.endsWith("]")) {
				simpleNameCheck = false;
				s = s.substring(1, s.length() - 1);
				//ResurrectionFeather.Debug("whiteListCheck simpleNameCheck == false. s="+s);
			} else {
				//ResurrectionFeather.Debug("whiteListCheck simpleNameCheck == true. s="+s);
				simpleNameCheck = true;
			}
			if (entityWhiteListData.containsKey(entityClass)) {
				//ResurrectionFeather.Debug("whiteListCheck entityWhiteListData ok. return true.");
				flag = true;
				break;
			} else {
				try {
					c = Class.forName(s);
					//ResurrectionFeather.Debug("whiteListCheck 2 c="+c);
					if (c.isInstance(entity)) {
						//ResurrectionFeather.Debug("whiteListCheck isInstance 2 ok. return true.");
						flag = true;
						entityWhiteListData.put(entityClass, c);
						break;
					}
				} catch (ClassNotFoundException e1) {
				}
			}
			if (!flag && simpleNameCheck && entity.getClass().getSimpleName().indexOf(s) > -1) {
				//ResurrectionFeather.Debug("whiteListCheck getSimpleName ok. return true.");
				flag = true;
				break;
			}
		}
		//ResurrectionFeather.Debug("whiteListCheck flag="+flag+" entity.getClass().getSimpleName())="+entity.getClass().getSimpleName());
		return flag;
	}

	public static boolean ngListCheck(EntityLivingBase entity) {
		String s;
		Class c;
		Class entityClass = entity.getClass();
		boolean flag = false;
		boolean simpleNameCheck = true;
		//if (ngList.size() == 0) ResurrectionFeather.Debug("ngListCheck ngList.size() == 0 !!");
		for (int i = 0; i < ngList.size(); i++) {
			s = ngList.get(i);
			if (s.startsWith("[") && s.endsWith("]")) {
				simpleNameCheck = false;
				s = s.substring(1, s.length() - 1);
				//ResurrectionFeather.Debug("ngListCheck simpleNameCheck == false. s="+s);
			} else {
				//ResurrectionFeather.Debug("ngListCheck simpleNameCheck == true. s="+s);
				simpleNameCheck = true;
			}
			if (entityNgListData.containsKey(entityClass)) {
				//ResurrectionFeather.Debug("ngListCheck entityWhiteListData ok. return true.");
				flag = true;
				break;
			} else {
				try {
					c = Class.forName(s);
					//ResurrectionFeather.Debug("ngListCheck 2 c="+c);
					if (c.isInstance(entity)) {
						//ResurrectionFeather.Debug("ngListCheck isInstance 2 ok. return true.");
						flag = true;
						entityNgListData.put(entityClass, c);
						break;
					}
				} catch (ClassNotFoundException e1) {
				}
			}
			if (!flag && simpleNameCheck && entity.getClass().getSimpleName().indexOf(s) > -1) {
				//ResurrectionFeather.Debug("ngListCheck getSimpleName ok. return true.");
				flag = true;
				break;
			}
		}
		//ResurrectionFeather.Debug("ngListCheck return flag="+flag);
		return flag;
	}

	public static boolean resurrectionEntity(EntityLivingBase entity) {
		if (entity instanceof EntityCreature
				&& ResurrectionFeatherEntityCreature.whiteListCheck(entity)
				&& !ResurrectionFeatherEntityCreature.ngListCheck(entity)) {
				EntityCreature entityCreature = (EntityCreature) entity;
			if (entityCreature.getHealth() <= 0.0F) {
				entityCreature.setHealth(entityCreature.getMaxHealth());
				entityCreature.isDead = false;
				entityCreature.deathTime = 0;
				for (int var1 = 0; var1 < 20; ++var1)
				{
					double var2 = rand.nextGaussian() * 1.0D;
					double var4 = rand.nextGaussian() * 1.0D;
					double var6 = rand.nextGaussian() * 1.0D;
					entityCreature.worldObj.spawnParticle("instantSpell", entityCreature.posX + (double)(rand.nextFloat() * entityCreature.width * 2.0F) - (double)entityCreature.width, entityCreature.posY + 0.5D + (double)(rand.nextFloat() * entityCreature.height), entityCreature.posZ + (double)(rand.nextFloat() * entityCreature.width * 2.0F) - (double)entityCreature.width, var2, var4, var6);
				}
				return true;
			}
		}
		return false;
	}
}