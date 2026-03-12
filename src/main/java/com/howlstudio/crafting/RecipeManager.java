package com.howlstudio.crafting;
import com.hypixel.hytale.component.Ref; import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import java.nio.file.*; import java.util.*;
public class RecipeManager {
    private final Path dataDir;
    private final Map<String,String> recipes=new LinkedHashMap<>();
    public RecipeManager(Path d){dataDir=d;try{Files.createDirectories(d);}catch(Exception e){}load();}
    public int getCount(){return recipes.size();}
    public void save(){try{StringBuilder sb=new StringBuilder();for(var e:recipes.entrySet())sb.append(e.getKey()+"="+e.getValue()+"\n");Files.writeString(dataDir.resolve("recipes.txt"),sb.toString());}catch(Exception e){}}
    private void load(){try{Path f=dataDir.resolve("recipes.txt");if(!Files.exists(f)){addDefaults();return;}for(String l:Files.readAllLines(f)){String[]p=l.split("=",2);if(p.length==2)recipes.put(p[0].toLowerCase(),p[1]);}}catch(Exception e){addDefaults();}}
    private void addDefaults(){recipes.put("torch","1 stick + 1 coal = 4 torches");recipes.put("pickaxe","3 planks + 2 sticks = wooden pickaxe");recipes.put("sword","2 planks + 1 stick = wooden sword");recipes.put("chest","8 planks = chest");recipes.put("furnace","8 cobblestone = furnace");save();}
    public AbstractPlayerCommand getRecipeCommand(){
        return new AbstractPlayerCommand("recipe","Look up a crafting recipe. /recipe <item>"){
            @Override protected void execute(CommandContext ctx,Store<EntityStore> store,Ref<EntityStore> ref,PlayerRef playerRef,World world){
                String key=ctx.getInputString().trim().toLowerCase();if(key.isEmpty()){playerRef.sendMessage(Message.raw("Usage: /recipe <item>  |  /recipes to list all"));return;}
                String r=recipes.get(key);
                if(r!=null)playerRef.sendMessage(Message.raw("[Recipe] §6"+key+"§r: "+r));
                else{playerRef.sendMessage(Message.raw("[Recipe] No recipe found for: "+key));playerRef.sendMessage(Message.raw("§7Use /recipes to see all custom recipes"));}
            }
        };
    }
    public AbstractPlayerCommand getAddRecipeCommand(){
        return new AbstractPlayerCommand("addrecipe","[Admin] Add custom recipe. /addrecipe <item> <description>"){
            @Override protected void execute(CommandContext ctx,Store<EntityStore> store,Ref<EntityStore> ref,PlayerRef playerRef,World world){
                String[]args=ctx.getInputString().trim().split("\\s+",2);if(args.length<2){playerRef.sendMessage(Message.raw("Usage: /addrecipe <item> <description>"));return;}
                recipes.put(args[0].toLowerCase(),args[1]);save();playerRef.sendMessage(Message.raw("[Recipe] Added: §6"+args[0]+"§r = "+args[1]));
            }
        };
    }
    public AbstractPlayerCommand getListRecipesCommand(){
        return new AbstractPlayerCommand("recipes","List all custom recipes. /recipes [page]"){
            @Override protected void execute(CommandContext ctx,Store<EntityStore> store,Ref<EntityStore> ref,PlayerRef playerRef,World world){
                if(recipes.isEmpty()){playerRef.sendMessage(Message.raw("[Recipes] No recipes registered."));return;}
                int page=1;try{page=Math.max(1,Integer.parseInt(ctx.getInputString().trim()));}catch(Exception e){}
                int perPage=10;int start=(page-1)*perPage;var keys=new ArrayList<>(recipes.keySet());
                int total=(keys.size()+perPage-1)/perPage;
                playerRef.sendMessage(Message.raw("[Recipes] Page "+page+"/"+total+":"));
                for(int i=start;i<Math.min(start+perPage,keys.size());i++)playerRef.sendMessage(Message.raw("  §6"+keys.get(i)+"§r: "+recipes.get(keys.get(i))));
            }
        };
    }
}
