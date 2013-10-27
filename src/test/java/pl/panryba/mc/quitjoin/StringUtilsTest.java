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
public class StringUtilsTest {
    @Test
    public void testJoinStrings() {
        Set<String> items = new HashSet<>();
        assertEquals("", StringUtils.joinStrings(items));
        
        items.add("test1");
        assertEquals("test1", StringUtils.joinStrings(items));
        
        items.add("test2");
        assertEquals("test1, test2", StringUtils.joinStrings(items));
    }
}