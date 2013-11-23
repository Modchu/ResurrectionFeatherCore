package modchu.resurrectionfeathercore;

import java.util.Arrays;

import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.ModMetadata;

public class ResurrectionFeatherCore extends DummyModContainer {

	public ResurrectionFeatherCore() {
		super(new ModMetadata());
		ModMetadata meta = getMetadata();

		meta.modId = "resurrectionfeathercore";
		meta.name = "ResurrectionFeatherCore";
		meta.version = "1a";
		meta.credits = "modchu";
		meta.authorList = Arrays.asList(new String[] { "modchu" });
		meta.description = "";
		meta.url = "http://forum.minecraftuser.jp/viewtopic.php?f=13&t=2073";
		meta.updateUrl = "";
		meta.screenshots = new String[0];
		meta.logoFile = "";
	}
}