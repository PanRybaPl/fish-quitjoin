/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.panryba.mc.quitjoin;

import org.bukkit.configuration.file.FileConfiguration;

public class LanguageStrings {
    private final FileConfiguration config;
    private final FileConfiguration defaults;
    
    public LanguageStrings(FileConfiguration config, FileConfiguration defaults) {
        this.config = config;
        this.defaults = defaults;
    }
    
    private String getString(String name) {
        String template = this.config.getString(name);
        
        if(template == null) {
            template = this.defaults.getString(name);
        }

        return template;
    }
    
    public String getJoined(String joinedList) {
        return String.format(getString("joined"), joinedList);
    }

    String getLeft(String leftList) {
        return String.format(getString("left"), leftList);
    }
}