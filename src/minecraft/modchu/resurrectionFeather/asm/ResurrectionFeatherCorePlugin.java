package modchu.resurrectionFeather.asm;

import java.io.File;
import java.util.Map;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

@TransformerExclusions({"modchu.resurrectionFeather.asm"})
@IFMLLoadingPlugin.MCVersion("1.6.2")
public class ResurrectionFeatherCorePlugin implements IFMLLoadingPlugin {

    static File location;

    @Override
    public String[] getLibraryRequestClass() {
        return null;
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[]{"modchu.resurrectionFeather.asm.ResurrectionFeatherTransformer"};
    }

    @Override
    public String getModContainerClass() {
        return "modchu.resurrectionFeather.ResurrectionFeatherCore";
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