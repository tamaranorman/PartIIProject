package test.PrologInterpreter.Structure;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import PrologInterpreter.Structure.TermVar;
import PrologInterpreter.Structure.TermVarMapping;

class TermVarMappingTest {

	@Test
	void createMapping() {
		TermVarMapping result = new TermVarMapping();
		
		assertNotNull(result);
	}
	
	@Test
	void testPutAndGet() {
		String s = "test";
		TermVar v = mock(TermVar.class);
		TermVarMapping test = new TermVarMapping();
		
		test.put(s, v);
		assertTrue(test.containsKey(s));
		assertEquals(v, test.get(s));
	}
	
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	
	@Before
	public void setUpStreams() {
	    System.setOut(new PrintStream(outContent));
	}

	@After
	public void restoreStreams() {
	    System.setOut(System.out);
	}
	
	@Test
	void showAnswerEmptyList() {
		setUpStreams();
		
		TermVarMapping test = new TermVarMapping();
		
		test.showAnswer();
		String result = outContent.toString().trim();
		
		assertEquals("true", result);
		
		restoreStreams();
	}
	
	@Test
	void showASingleResult() {
		setUpStreams();
		
		String s = "A";
		TermVar v = mock(TermVar.class);
		when(v.print()).thenReturn("test");
		TermVarMapping test = new TermVarMapping();
		test.put(s, v);
		
		test.showAnswer();
		String result = outContent.toString().trim();
		
		assertEquals("A = test;", result);
		
		restoreStreams();
	}
	
	@Test
	void showAMultipleResults() {
		setUpStreams();
		
		String s1 = "A";
		String s2 = "B";
		TermVar v1 = mock(TermVar.class);
		when(v1.print()).thenReturn("test1");
		TermVar v2 = mock(TermVar.class);
		when(v2.print()).thenReturn("test2");
		TermVarMapping test = new TermVarMapping();
		test.put(s1, v1);
		test.put(s2, v2);
		
		test.showAnswer();
		String[] result = outContent.toString().split("\n");
		
		assertEquals("A = test1,", result[0].trim());
		assertEquals("B = test2;", result[1].trim());
		
		restoreStreams();
	}
}
