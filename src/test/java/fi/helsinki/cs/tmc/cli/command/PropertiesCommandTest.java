package fi.helsinki.cs.tmc.cli.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import fi.helsinki.cs.tmc.cli.Application;
import fi.helsinki.cs.tmc.cli.io.TestIo;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.HashMap;


public class PropertiesCommandTest {

    TestIo testio;
    Application app;
    HashMap<String, String> props;

    @Before
    public void setup() {
        testio = new TestIo();
        app = new Application(testio);
        app = Mockito.spy(app);
        when(app.saveProperties()).thenReturn(true);
        props = new HashMap<>();
        when(app.getProperties()).thenReturn(props);
    }

    @Test
    public void printsPropsWhenRanWithNoArgs() {
        props.put("hello", "world");
        props.put("toilet", "wonderland");
        app.run(new String[] {"prop"});
        assertTrue("Contains props", testio.out().contains("hello"));
        assertTrue("Contains props", testio.out().contains("world"));
        assertTrue("Contains props", testio.out().contains("toilet"));
        assertTrue("Contains props", testio.out().contains("wonderland"));
    }

    @Test
    public void failsCorrectlyWithUnevenArgs() {
        app.run(new String[] {"prop", "w"});
        assertTrue("Prints an error message", testio.out().contains("Invalid argument count"));
    }

    @Test
    public void setsPropsCorrectly() {
        app.run(new String[] {"prop", "cool", "yeah"});
        assertTrue("Is set to props", props.containsKey("cool"));
        assertTrue("Is set to props", props.containsValue("yeah"));
    }

    @Test
    public void setsMultiplePropsCorrectly() {
        testio.addConfirmationPrompt(true);
        app.run(new String[] {"prop", "cool", "yeah", "hello", "world"});
        assertEquals("yeah", props.get("cool"));
        assertEquals("world", props.get("hello"));
        assertTrue("Asked for confirmation", testio.allPromptsUsed());
    }

    @Test
    public void unsetsPropsCorrectly() {
        props.put("no", "e");
        app.run(new String[] {"prop", "-u", "no"});
        assertTrue("Is removed from props", !props.containsKey("no"));
        assertTrue("Is removed from props", !props.containsValue("e"));
    }

    @Test
    public void unsetsMultiplePropsCorrectly() {
        testio.addConfirmationPrompt(true);
        props.put("biggie", "smalls");
        props.put("snoop", "dogg");
        app.run(new String[] {"prop", "-u", "biggie", "snoop"});
        assertTrue("Is removed from props", !props.containsKey("biggie"));
        assertTrue("Is removed from props", !props.containsValue("smalls"));
        assertTrue("Is removed from props", !props.containsKey("snoop"));
        assertTrue("Is removed from props", !props.containsValue("dogg"));
        assertTrue("Asked for confirmation", testio.allPromptsUsed());
    }
}