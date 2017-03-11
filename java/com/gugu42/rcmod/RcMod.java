package com.gugu42.rcmod;

import org.apache.logging.log4j.Logger;

import com.gugu42.rcmod.blocks.BlockCrate;
import com.gugu42.rcmod.blocks.BlockGadgetronAmmo;
import com.gugu42.rcmod.blocks.BlockShip;
import com.gugu42.rcmod.blocks.BlockShipFiller;
import com.gugu42.rcmod.blocks.BlockShipPlatform;
import com.gugu42.rcmod.blocks.BlockTNTCrate;
import com.gugu42.rcmod.blocks.BlockVendor;
import com.gugu42.rcmod.blocks.BlockVersaTargetGreen;
import com.gugu42.rcmod.capabilities.RcModCapabilityHandler;
import com.gugu42.rcmod.capabilities.bolt.Bolt;
import com.gugu42.rcmod.capabilities.bolt.BoltStorage;
import com.gugu42.rcmod.capabilities.bolt.IBolt;
import com.gugu42.rcmod.capabilities.suckcannon.ISuckCannon;
import com.gugu42.rcmod.capabilities.suckcannon.SuckCannon;
import com.gugu42.rcmod.capabilities.suckcannon.SuckCannonStorage;
import com.gugu42.rcmod.entity.RcEntities;
import com.gugu42.rcmod.gui.GuiBolt;
import com.gugu42.rcmod.gui.GuiSuckCannon;
import com.gugu42.rcmod.gui.GuiTooltips;
import com.gugu42.rcmod.handler.RcAchievementEventHandler;
import com.gugu42.rcmod.handler.RcEventHandler;
import com.gugu42.rcmod.handler.RcTickHandler;
import com.gugu42.rcmod.items.ItemClankBackpack;
import com.gugu42.rcmod.items.ItemRatchetEars;
import com.gugu42.rcmod.items.ItemShip;
import com.gugu42.rcmod.items.ItemThrusterPack;
import com.gugu42.rcmod.items.RcItems;
import com.gugu42.rcmod.network.GuiHandler;
import com.gugu42.rcmod.shipsys.ShipWaypointCommand;
import com.gugu42.rcmod.shipsys.ShipWaypointRemoveCommand;
import com.gugu42.rcmod.tileentity.TileEntityShip;
import com.gugu42.rcmod.tileentity.TileEntityShipFiller;
import com.gugu42.rcmod.tileentity.TileEntityShipPlatform;
import com.gugu42.rcmod.tileentity.TileEntityVendor;
import com.gugu42.rcmod.tileentity.TileEntityVersaTargetG;
import com.gugu42.rcmod.utils.ffmtutils.FFMTPacketHandler;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.stats.Achievement;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(modid = RcMod.MODID, version = "0.5.3b", name = "RcMod")
public class RcMod {
	@SidedProxy(clientSide = "com.gugu42.rcmod.ClientProxy", serverSide = "com.gugu42.rcmod.CommonProxy")
	public static CommonProxy       proxy;

	@Instance("rcmod")
	public static RcMod             instance;
	public static Logger            rcLogger;

	/* CAPABILITIES SAFE SPACE               */
	
	
	
	
	
	
	
	/*                  */
	
	//Creative tabs
	public static RcCreativeTab     rcTab;
	public static RcCreativeTab     rcWeapTab;
	public static RcCreativeTab     rcGadgTab;

	//Blocks
	public static Block             tntCrate;
	public static Block             crate;
	public static Block             ammoCrate;
	public static Block             vendor;
	public static Block             ship;
	public static Block             shipFiller;
	public static Block             versaTargetGreen;
	public static Block             shipPlatform;

	public static SoundType         crateStepSound;

	public static Item              clankBackpack;
	public static Item              ratchetEars;
	public static Item              thrusterPack;
	public static Item              shipItem;

	public ArmorMaterial            EnumArmorMaterialClank = EnumHelper.addArmorMaterial("Clank", null, 0, new int[] { 0, 0, 0, 0 }, 0, null, 0);

	public RcTickHandler            rcTickHandler;

	/*
	 * Packet Handler - Not that hard but yeah
	 */
	public static FFMTPacketHandler rcModPacketHandler;

	public static final String      MODID                  = "rcmod";

	public static AchievementPage   rcAchievementPage;

	public static Achievement       achievement_VendorCraft, achievement_HelipackCraft;
	public static Configuration config;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		rcLogger = event.getModLog();
		rcModPacketHandler = new FFMTPacketHandler("com.gugu42.rcmod.network.packets");
		rcTab = new RcCreativeTab("rcTab");
		rcWeapTab = new RcCreativeTab("rcWeapTab");
		rcGadgTab = new RcCreativeTab("rcGadgTab");
		config = new Configuration(event.getSuggestedConfigurationFile());

		config.load();
		config.get("weapon_damage", "blaster", 4);
		config.get("weapon_damage", "pyrocitor", 6);
		config.get("weapon_damage", "ryno", 20);
		config.get("weapon_damage", "suck_cannon", 10);
		config.get("weapon_damage", "visibomb", 12);
		config.get("weapon_damage", "wrench_thrown", 5);
		config.get("weapon_damage", "wrench_direct", 6);
		
		
		
		config.save();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {

		/* -----Packet channels-----*/
		rcModPacketHandler.initialise("RCMD|bolt");
		rcModPacketHandler.initialise("RCMD|vend");
		rcModPacketHandler.initialise("RCMD|refill");

		/* -----Entity----- */

		RcEntities.initModEntities();
		RcEntities.initRc1Entities();

		/* -----Others before blocks ( stepsound )----- */

		//crateStepSound = new RcCustomStepSound("crate.break", 0.1f, 1.0f, Block.soundTypeWood, Block.soundTypeWood);

		/* -----Blocks----- */
		tntCrate = new BlockTNTCrate(Material.TNT).setUnlocalizedName("tntCrate");
		GameRegistry.register(tntCrate, new ResourceLocation(RcMod.MODID, "tntCrate"));
		crate = new BlockCrate(Material.WOOD).setUnlocalizedName("crate").setHardness(0.0f)/*.setStepSound(crateStepSound)*/;
		GameRegistry.register(crate, new ResourceLocation(RcMod.MODID, "crate"));
		ammoCrate = new BlockGadgetronAmmo(Material.WOOD).setUnlocalizedName("ammoCrate").setHardness(0.0f)/*.setStepSound(crateStepSound)*/;
		GameRegistry.register(ammoCrate, new ResourceLocation(RcMod.MODID, "ammoCrate"));

		vendor = new BlockVendor(Material.IRON).setUnlocalizedName("vendor").setHardness(10.0f);
		GameRegistry.register(vendor, new ResourceLocation(RcMod.MODID, "vendor"));

		ship = new BlockShip(Material.IRON).setUnlocalizedName("ship").setHardness(5.0f);
		GameRegistry.register(ship, new ResourceLocation(RcMod.MODID, "ship"));

		shipFiller = new BlockShipFiller(Material.IRON).setUnlocalizedName("shipFiller");
		GameRegistry.register(shipFiller, new ResourceLocation(RcMod.MODID, "shipFiller"));

		versaTargetGreen = new BlockVersaTargetGreen(Material.IRON).setUnlocalizedName("versaTargetGreen");
		GameRegistry.register(versaTargetGreen, new ResourceLocation(RcMod.MODID, "versaTargetGreen"));

		shipPlatform = new BlockShipPlatform().setUnlocalizedName("shipPlatform").setCreativeTab(rcTab);
		GameRegistry.register(shipPlatform, new ResourceLocation(RcMod.MODID, "shipPlatform"));

		GameRegistry.registerTileEntity(TileEntityVendor.class, "vendor");
		GameRegistry.registerTileEntity(TileEntityShip.class, "ship");
		GameRegistry.registerTileEntity(TileEntityShipFiller.class, "shipFiller");
		GameRegistry.registerTileEntity(TileEntityVersaTargetG.class, "versaTargetG");
		GameRegistry.registerTileEntity(TileEntityShipPlatform.class, "shipPlatform");

		/* -----Items----- */

		RcItems.initModItems();
		RcItems.initRc1Items();
		RcItems.initAmmoItems();

		/* -----Other Items----- */
		clankBackpack = new ItemClankBackpack(EnumArmorMaterialClank, 1, 1).setUnlocalizedName("clankHeli");
		GameRegistry.register(clankBackpack, new ResourceLocation(RcMod.MODID, "clankHeli"));
		ratchetEars = new ItemRatchetEars(EnumArmorMaterialClank, 1, 0).setUnlocalizedName("ratchetEars");
		GameRegistry.register(ratchetEars, new ResourceLocation(RcMod.MODID, "ratchetEars"));
		thrusterPack = new ItemThrusterPack(EnumArmorMaterialClank, 1, 1).setUnlocalizedName("thrusterpack");
		GameRegistry.register(thrusterPack, new ResourceLocation(RcMod.MODID, "thrusterpack"));

		shipItem = new ItemShip().setMaxStackSize(1).setCreativeTab(rcTab);
		GameRegistry.register(shipItem, new ResourceLocation(RcMod.MODID, "shipItem"));

		/* -----Other----- */

		achievement_VendorCraft = new Achievement("achievement.vendor", "vendor", 0, -1, this.vendor, (Achievement) null).registerStat().setSpecial();
		achievement_HelipackCraft = new Achievement("achievement.helipack", "helipack", 0, 1, this.clankBackpack, achievement_VendorCraft).registerStat();

		rcAchievementPage = new AchievementPage("Ratchet & Clank Mod", achievement_VendorCraft, achievement_HelipackCraft);
		AchievementPage.registerAchievementPage(rcAchievementPage);

		if (event.getSide() == Side.CLIENT)
			setCreativeTabsIcon();

		this.rcTickHandler = new RcTickHandler();
		proxy.registerRenderInformation();
		proxy.registerTileEntityRender();
		RcRecipes.addRecipes();

		FMLCommonHandler.instance().bus().register(new RcTickHandler());

		CapabilityManager.INSTANCE.register(IBolt.class, new BoltStorage(), Bolt.class);
		CapabilityManager.INSTANCE.register(ISuckCannon.class, new SuckCannonStorage(), SuckCannon.class);
		
        MinecraftForge.EVENT_BUS.register(new RcModCapabilityHandler());
		
	}

	@SideOnly(Side.CLIENT)
	public void setCreativeTabsIcon() {
		rcTab.setTabIconItem(RcItems.bolt);
		rcWeapTab.setTabIconItem(RcItems.blaster);
		rcGadgTab.setTabIconItem(RcItems.swingShot);
	}

	@EventHandler
	public void PostInit(FMLPostInitializationEvent event) {
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
		MinecraftForge.EVENT_BUS.register(new RcEventHandler());
		FMLCommonHandler.instance().bus().register(new FurnaceEventHandler());
		FMLCommonHandler.instance().bus().register(new RcAchievementEventHandler());
		if (FMLCommonHandler.instance().getEffectiveSide().isClient()) {
			MinecraftForge.EVENT_BUS.register(new GuiBolt(Minecraft.getMinecraft()));
			MinecraftForge.EVENT_BUS.register(new GuiTooltips());
			MinecraftForge.EVENT_BUS.register(new DropBolts());
			GuiSuckCannon suckCannonGui = new GuiSuckCannon(Minecraft.getMinecraft());
			MinecraftForge.EVENT_BUS.register(suckCannonGui);
			FMLCommonHandler.instance().bus().register(suckCannonGui);
		}
	}

	@EventHandler
	public void serverLoad(FMLServerStartingEvent event) {
		event.registerServerCommand(new BoltCommand());
		event.registerServerCommand(new ResetTooltipsCommand());
		event.registerServerCommand(new ShipWaypointCommand());
		event.registerServerCommand(new ShipWaypointRemoveCommand());
	}
}
