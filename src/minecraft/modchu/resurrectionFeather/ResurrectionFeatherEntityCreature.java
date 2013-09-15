package modchu.resurrectionFeather;

import java.util.HashMap;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.src.mod_ResurrectionFeather;
import net.minecraft.world.World;

public abstract class ResurrectionFeatherEntityCreature extends EntityCreature
{
    public static HashMap entityWhiteListData = new HashMap();
    public static HashMap entityNgListData = new HashMap();
    private String packageName;

    public ResurrectionFeatherEntityCreature(World par1World)
    {
        super(par1World);
    }

    protected void onDeathUpdate()
    {
    	//mod_ResurrectionFeather.Debug("onDeathUpdate()");
    	if (whiteListCheck(this)
    			&& !ngListCheck(this)) {
    		++this.deathTime;
    		//mod_ResurrectionFeather.Debug("onDeathUpdate() run. deathTime="+deathTime);

    		if (this.deathTime == mod_ResurrectionFeather.onDeathTimeDrop)
    		{
    			if (!worldObj.isRemote && (recentlyHit > 0 || isPlayer()) && !isChild())
    			{
    				for (int i = getExperiencePoints(attackingPlayer); i > 0;)
    				{
    					int k = EntityXPOrb.getXPSplit(i);
    					i -= k;
    					worldObj.spawnEntityInWorld(new EntityXPOrb(worldObj, posX, posY, posZ, k));
    				}
    			}
    		}

    		if (this.deathTime == mod_ResurrectionFeather.onDeathTimeDespawn)
    		{
    			setDead();

    			for (int j = 0; j < 20; j++)
    			{
    				double d = rand.nextGaussian() * 0.02D;
    				double d1 = rand.nextGaussian() * 0.02D;
    				double d2 = rand.nextGaussian() * 0.02D;
    				worldObj.spawnParticle("explode", (posX + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, posY + (double)(rand.nextFloat() * height), (posZ + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, d, d1, d2);
    			}
    		}

    		if (this.deathTime > (mod_ResurrectionFeather.onDeathTimeDespawn + 100))
    		{
    			this.deathTime = mod_ResurrectionFeather.onDeathTimeDespawn - 1;
    		}
    	} else {
    		if (this.deathTime > 19) this.deathTime = 19;
    		//mod_ResurrectionFeather.Debug("super.onDeathUpdate()");
    		super.onDeathUpdate();
    		return;
    	}
    }

    public boolean whiteListCheck(EntityLiving entity) {
    	String s;
    	Class c;
    	Class entityClass = entity.getClass();
    	boolean flag = false;
    	boolean simpleNameCheck = true;
    	for (int i = 0 ; i < mod_ResurrectionFeather.whiteList.size() ; i++) {
    		s = (String) mod_ResurrectionFeather.whiteList.get(i);
    		if (s.startsWith("[")
    				&& s.endsWith("]")) {
    			simpleNameCheck = false;
    			s = s.substring(1, s.length() - 1);
    			//mod_ResurrectionFeather.Debug("whiteListCheck simpleNameCheck == false. s="+s);
    		} else {
    			simpleNameCheck = true;
    		}
    		if (entityWhiteListData.containsKey(entityClass)) {
    			//mod_ResurrectionFeather.Debug("whiteListCheck entityWhiteListData ok. return true.");
    			flag = true;
    			break;
    		} else {
    			try {
    				c = Class.forName(mod_ResurrectionFeather.mod_resurrectionFeather.getClassName(s));
    				//mod_ResurrectionFeather.Debug("whiteListCheck c="+c);
    				if (c.isInstance(entity)) {
    					//mod_ResurrectionFeather.Debug("whiteListCheck isInstance ok. return true.");
    					flag = true;
    					entityWhiteListData.put(entityClass, c);
    					break;
    				}
    			} catch (ClassNotFoundException e) {
    				try {
    					c = Class.forName(s);
    					//mod_ResurrectionFeather.Debug("whiteListCheck 2 c="+c);
    					if (c.isInstance(entity)) {
    						//mod_ResurrectionFeather.Debug("whiteListCheck isInstance 2 ok. return true.");
    						flag = true;
    						entityWhiteListData.put(entityClass, c);
    						break;
    					}
    				} catch (ClassNotFoundException e1) {
    				}
    			}
    		}
    		if (!flag
    				&& simpleNameCheck
    				&& entity.getClass().getSimpleName().indexOf(s) > -1) {
    			//mod_ResurrectionFeather.Debug("whiteListCheck getSimpleName ok. return true.");
    			flag = true;
    			break;
    		}
    	}
    	//mod_ResurrectionFeather.Debug("whiteListCheck flag="+flag+" entity.getClass().getSimpleName())="+entity.getClass().getSimpleName());
    	return flag;
    }

    public boolean ngListCheck(EntityLiving entity) {
    	String s;
    	Class c;
    	Class entityClass = entity.getClass();
    	boolean flag = false;
    	boolean simpleNameCheck = true;
    	for (int i = 0 ; i < mod_ResurrectionFeather.ngList.size() ; i++) {
    		s = (String) mod_ResurrectionFeather.ngList.get(i);
    		if (s.startsWith("[")
    				&& s.endsWith("]")) {
    			simpleNameCheck = false;
    			s = s.substring(1, s.length() - 1);
    			//mod_ResurrectionFeather.Debug("ngListCheck simpleNameCheck == false. s="+s);
    		} else {
    			simpleNameCheck = true;
    		}
    		if (entityNgListData.containsKey(entityClass)) {
    			//mod_ResurrectionFeather.Debug("ngListCheck entityWhiteListData ok. return true.");
    			flag = true;
    			break;
    		} else {
    			try {
    				c = Class.forName(mod_ResurrectionFeather.mod_resurrectionFeather.getClassName(s));
    				mod_ResurrectionFeather.Debug("ngListCheck c="+c);
    				if (c.isInstance(entity)) {
    					//mod_ResurrectionFeather.Debug("ngListCheck isInstance ok. return true.");
    					flag = true;
    					entityNgListData.put(entityClass, c);
    					break;
    				}
    			} catch (ClassNotFoundException e) {
    				try {
    					c = Class.forName(s);
    					mod_ResurrectionFeather.Debug("ngListCheck 2 c="+c);
    					if (c.isInstance(entity)) {
    						//mod_ResurrectionFeather.Debug("ngListCheck isInstance 2 ok. return true.");
    						flag = true;
    						entityNgListData.put(entityClass, c);
    						break;
    					}
    				} catch (ClassNotFoundException e1) {
    				}
    			}
    		}
    		if (!flag
    				&& simpleNameCheck
    				&& entity.getClass().getSimpleName().indexOf(s) > -1) {
    			//mod_ResurrectionFeather.Debug("ngListCheck getSimpleName ok. return true.");
    			flag = true;
    			break;
    		}
    	}
    	return flag;
    }
}
