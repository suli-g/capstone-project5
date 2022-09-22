package Tests;
/**
 * Testing System.in as shown in
 * {@link https://stackoverflow.com/questions/1647907}
 */
import org.junit.Test;

import IO.Input;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;    
    
public class InputTest {
    private final InputStream systemIn = System.in;
    private final PrintStream systemOut = System.out;

    private ByteArrayInputStream testIn;
    private ByteArrayOutputStream testOut;

    @Before
    public void setupOutput(){
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
    }

    
    /** 
     * @param data
     */
    private void provideInput(String data) {
        testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);
    }
    
    
    /** 
     * @return String
     */
    private String getOutput() {
        return testOut.toString();
    }

    @After
    public void restoreSystemInputOutput() {
        System.setIn(systemIn);
        System.setOut(systemOut);
    }

    @Test
    public void testQuery() {
        String testInput = "Hello";
        provideInput(testInput);
        Input.query("");

        assertEquals("(leave blank to skip): ", getOutput());
    }

    @Test
    public void testExpect() {
        String testInput = "Hello";
        provideInput(testInput);
        Input.expect("");

        assertEquals(": ", getOutput());
    }

    @Test
    public void testToInteger() {
        String testInput = "9";
        provideInput(testInput);
        Input input = Input.expect("");

        assertEquals(input.toInteger(), 9);
    }

    @Test
    public void testToDouble() {
        String testInput = "9.5";
        provideInput(testInput);
        Input input = Input.expect("");

        assertEquals(input.toDouble(), 9.5);
    }

    @Test
    public void testIncorrectInput() {
        String testInput = "hello world";
        provideInput(testInput);
        Input input = Input.expect("");
        assertThrows(NumberFormatException.class, () -> {
            input.toDouble();
            input.toInteger();
        });
    }
}
    