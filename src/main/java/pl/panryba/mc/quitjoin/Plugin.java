package pl.panryba.mc.quitjoin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class Plugin extends JavaPlugin implements Listener {
    private BukkitTask broadcastTask;
    private PluginApi api;
    
    private class BroadcastRunner implements Runnable {
        private final PluginApi api;

        private BroadcastRunner(PluginApi api) {
            this.api = api;
        }

        @Override
        public void run() {
            this.api.broadcast();
        }
    }
    
    @Override
    public void onEnable() {        
        FileConfiguration defaultConfig = new YamlConfiguration();
        InputStream defaultStream = getResource("messages.yml");
        
        try {
            defaultConfig.load(defaultStream);
        } catch (IOException | InvalidConfigurationException ex) {
            Logger.getLogger(Plugin.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                defaultStream.close();
            } catch (IOException ex) {
                Logger.getLogger(Plugin.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        FileConfiguration config = getConfig();
        
        String locale = config.getString("locale", "en");
        if(locale == null || locale.isEmpty()) {
            locale = "en";
        }
        
        File localeFile = new File(getDataFolder(), "messages_" + locale + ".yml");
        FileConfiguration localeConfig = new YamlConfiguration();
        try {
            localeConfig.load(localeFile);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Plugin.class.getName()).log(Level.WARNING, "Could not find file: " + localeFile);
        } catch (IOException | InvalidConfigurationException ex) {
            Logger.getLogger(Plugin.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        LanguageStrings strings = new LanguageStrings(localeConfig, defaultConfig);
        
        this.api = new PluginApi(new BroadcastOutput() {
            @Override
            public void broadcast(String message) {
                Bukkit.broadcastMessage(message);
            }
        }, strings);
        
        getServer().getPluginManager().registerEvents(this, this);
        this.broadcastTask = getServer().getScheduler().runTaskTimer(this, new Plugin.BroadcastRunner(this.api), 20 * 30, 20 * 30);
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll((Listener)this);
        
        if(this.broadcastTask != null) {
            this.broadcastTask.cancel();
            this.broadcastTask = null;
        }
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        this.api.joined(event.getPlayer().getName());
        event.setJoinMessage("");
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        this.api.left(event.getPlayer().getName());
        event.setQuitMessage("");
    }
}
