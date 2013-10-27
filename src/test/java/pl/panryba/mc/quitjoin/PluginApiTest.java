/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.panryba.mc.quitjoin;

import org.bukkit.ChatColor;
import org.jmock.Mockery;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author Marcin
 */
public class PluginApiTest {
    private Mockery context;
    
    private class VariableOutput implements BroadcastOutput {
        private String last;
        
        @Override
        public void broadcast(String message) {
            this.last = message;
        }
        
        public String getLast() {
            return this.last;
        }
    }
    
    @Before
    public void setUp() {
        this.context = new Mockery();
    }
    
    @Test
    public void testPlayerEntered() {
        VariableOutput output = new VariableOutput();
        PluginApi api = new PluginApi(output);
        
        api.joined("Test");
        
        api.broadcast();
        
        assertEquals(ChatColor.GRAY + "dolaczyli: Test", output.getLast());
    }
}