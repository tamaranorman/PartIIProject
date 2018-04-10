package test.PrologInterpreter.Structure;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
	
	@Test
	void showAnswerEmptyList() {
		TermVarMapping test = new TermVarMapping();
		
		String[] result = test.showAnswer();
		
		assertEquals("true.", result[0]);
	}
	
	@Test
	void showASingleResult() {
		String s = "A";
		TermVar v = mock(TermVar.class);
		when(v.print()).thenReturn("test");
		TermVarMapping test = new TermVarMapping();
		test.put(s, v);
		
		String[] result = test.showAnswer();
		
		assertEquals("A = test;", result[0]);
	}
	
	@Test
	void showAMultipleResults() {
		String s1 = "A";
		String s2 = "B";
		TermVar v1 = mock(TermVar.class);
		when(v1.print()).thenReturn("test1");
		TermVar v2 = mock(TermVar.class);
		when(v2.print()).thenReturn("test2");
		TermVarMapping test = new TermVarMapping();
		test.put(s1, v1);
		test.put(s2, v2);
		
		
		String[] result = test.showAnswer();
		
		assertEquals("A = test1,", result[0]);
		assertEquals("B = test2;", result[1]);
	}
}
