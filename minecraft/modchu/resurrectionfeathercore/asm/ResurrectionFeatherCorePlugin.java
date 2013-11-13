package modchu.resurrectionfeathercore.asm;

import java.io.File;
import java.util.Map;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

@TransformerExclusions({"modchu.resurrectionfeathercore.asm"})
@IFMLLoadingPlugin.MCVersion("1.6.4")
public class ResurrectionFeatherCorePlugin implements IFMLLoadingPlugin {

    static File location;

    @Override
    public String[] getLibraryRequestClass() {
        return null;
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[]{"modchu.resurrectionfeathercore.asm.ResurrectionFeatherTransformer"};
    }

    @Override
    public String getModContainerClass() {
        return "modchu.resurrectionfeathercore.ResurrectionFeatherCore";
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map data) {
        if (data.containsKey("coremodLocation"))
        {
            location = (File) data.get("coremodLocation");
        }
    }
 }