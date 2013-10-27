/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.panryba.mc.quitjoin;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author Marcin
 */
public class PluginApiTest {
    private VariableOutput output;
    private final LanguageStrings strings;
    private PluginApi api;
    
    private class VariableOutput implements BroadcastOutput {
        private List<String> messages;
        
        public VariableOutput() {
            this.messages = new ArrayList<>();
        }
        
        @Override
        public void broadcast(String message) {
            this.messages.add(message);
        }
        
        public boolean contains(String message) {
            return this.messages.contains(message);
        }
        
        public List<String> getMessages() {
            return this.messages;
        }
        
        public void clear() {
            this.messages.clear();
        }
    }
    
    public PluginApiTest() {
        FileConfiguration defaultConfig = new YamlConfiguration();
        defaultConfig.set("joined", "joined: %s");
        defaultConfig.set("left", "left: %s");
        
        FileConfiguration config = new YamlConfiguration();
        config.set("joined", "j: %s");
        config.set("left", "l: %s");
        
        this.strings = new LanguageStrings(config, defaultConfig);
    }
    
    @Before
    public void setUp() {
        this.output = new VariableOutput();
        this.api = new PluginApi(output, strings);
    }
        
    @Test
    public void testEnterLeaveScenario() {
        api.joined("Test");        
        api.broadcast();
        assertTrue(output.contains("j: Test"));
        output.clear();
        
        api.left("Test");
        api.broadcast();
        assertTrue(output.contains("l: Test"));
        output.clear();
    }
    
    @Test
    public void TestNoTraffic() {
        api.broadcast();
        assertTrue(output.getMessages().isEmpty());
    }
    
    @Test
    public void TestEnterLeaveOnSameSession() {
        api.joined("Test");
        api.left("Test");
        api.broadcast();
        assertTrue(output.getMessages().isEmpty());
    }
    
    public void TestNoTrafficAfterJoin() {
        api.joined("Test");
        api.broadcast();
        output.clear();

        api.broadcast();
        assertTrue(output.getMessages().isEmpty());
    }
    
    public void TestNoTrafficAfterLeave() {
        api.left("Test");
        api.broadcast();
        output.clear();
        
        api.broadcast();
        assertTrue(output.getMessages().isEmpty());
    }
}