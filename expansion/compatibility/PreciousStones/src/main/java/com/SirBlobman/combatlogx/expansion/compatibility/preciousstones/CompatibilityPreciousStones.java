package com.SirBlobman.combatlogx.expansion.compatibility.preciousstones;

import java.util.logging.Logger;

import com.SirBlobman.combatlogx.api.ICombatLogX;
import com.SirBlobman.combatlogx.api.expansion.ExpansionManager;
import com.SirBlobman.combatlogx.api.expansion.noentry.NoEntryExpansion;
import com.SirBlobman.combatlogx.api.expansion.noentry.NoEntryForceFieldListener;
import com.SirBlobman.combatlogx.api.expansion.noentry.NoEntryHandler;
import com.SirBlobman.combatlogx.api.expansion.noentry.NoEntryListener;
import com.SirBlobman.combatlogx.expansion.compatibility.preciousstones.handler.PreciousStonesNoEntryHandler;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class CompatibilityPreciousStones extends NoEntryExpansion {
    private NoEntryHandler noEntryHandler;
    public CompatibilityPreciousStones(ICombatLogX plugin) {
        super(plugin);
    }
    
    @Override
    public boolean canEnable() {
        PluginManager manager = Bukkit.getPluginManager();
        return manager.isPluginEnabled("PreciousStones");
    }
    
    @Override
    public void onActualEnable() {
        ICombatLogX plugin = getPlugin();
        ExpansionManager expansionManager = plugin.getExpansionManager();
        
        PluginManager manager = Bukkit.getPluginManager();
        Logger logger = getLogger();
        
        Plugin pluginPreciousStones = manager.getPlugin("PreciousStones");
        if(pluginPreciousStones == null) {
            logger.info("Could not find the PreciousStones plugin. This expansion will be automatically disabled.");
            expansionManager.disableExpansion(this);
            return;
        }
        
        String versionPreciousStones = pluginPreciousStones.getDescription().getVersion();
        logger.info("Successfully hooked into Residence v" + versionPreciousStones);
        
        saveDefaultConfig("preciousstones-compatibility.yml");
        this.noEntryHandler = new PreciousStonesNoEntryHandler(this);
        
        NoEntryListener listener = new NoEntryListener(this);
        expansionManager.registerListener(this, listener);
        
        Plugin pluginProtocolLib = manager.getPlugin("ProtocolLib");
        if(pluginProtocolLib != null) {
            NoEntryForceFieldListener forceFieldListener = new NoEntryForceFieldListener(this);
            expansionManager.registerListener(this, forceFieldListener);
            
            String versionProtocolLib = pluginProtocolLib.getDescription().getVersion();
            logger.info("Successfully hooked into ProtocolLib v" + versionProtocolLib);
        }
    }
    
    @Override
    public void onActualDisable() {
        // Do Nothing
    }
    
    @Override
    public void reloadConfig() {
        reloadConfig("preciousstones-compatibility.yml");
    }
    
    @Override
    public NoEntryHandler getNoEntryHandler() {
        return this.noEntryHandler;
    }
}
