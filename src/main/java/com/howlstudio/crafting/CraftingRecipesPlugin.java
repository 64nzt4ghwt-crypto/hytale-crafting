package com.howlstudio.crafting;
import com.hypixel.hytale.server.core.command.system.CommandManager;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
/** CraftingRecipes — register custom recipes and let players look them up with /recipe. */
public final class CraftingRecipesPlugin extends JavaPlugin {
    private RecipeManager mgr;
    public CraftingRecipesPlugin(JavaPluginInit init){super(init);}
    @Override protected void setup(){
        System.out.println("[CraftingRecipes] Loading...");
        mgr=new RecipeManager(getDataDirectory());
        CommandManager.get().register(mgr.getRecipeCommand());
        CommandManager.get().register(mgr.getAddRecipeCommand());
        CommandManager.get().register(mgr.getListRecipesCommand());
        System.out.println("[CraftingRecipes] Ready. "+mgr.getCount()+" recipes.");
    }
    @Override protected void shutdown(){if(mgr!=null)mgr.save();System.out.println("[CraftingRecipes] Stopped.");}
}
