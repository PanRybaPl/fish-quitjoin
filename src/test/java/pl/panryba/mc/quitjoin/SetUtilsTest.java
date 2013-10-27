/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.panryba.mc.quitjoin;

import java.util.HashSet;
import java.util.Set;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Marcin
 */
public class SetUtilsTest {
    
    @Test
    public void testDiff() {
        Set<String> before = new HashSet<>();
        Set<String> after = new HashSet<>();
        
        before.add("a");
        after.add("a");
        after.add("b");
        
        Set<String> result = SetUtils.diff(after, before);
        
        assertFalse(result.contains("a"));
        assertTrue(result.contains("b"));
    }
}