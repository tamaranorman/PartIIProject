package test.PrologInterpreter.Structure;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

import PrologInterpreter.Structure.Term;
import PrologInterpreter.Structure.TermVar;

class TermVarTest {

	@Test
	void creationOfTermVarIncreasesValue() {
		TermVar setup = new TermVar();
		int a = setup.getVarNo();
		TermVar result = new TermVar();
		
		assertNotNull(result);
		assertEquals(a+ 1, result.getVarNo());
		assertTrue(result.isUnunified());
	}
	
	@Test
	void creationOfTermVarWithVarNoAndInstance() {
		Term t = mock(Term.class);
		TermVar result = new TermVar(t, 3);
		
		assertNotNull(result);
		assertEquals(3, result.getVarNo());
		assertFalse(result.isUnunified());
	}
	
	@Test
	void printingAnUnassignedVariable() {
		TermVar test = new TermVar();
		int i = test.getVarNo();
		
		assertEquals("_" + i , test.print());
	}
	
	@Test
	void printingAnAssignedVariable() {
		Term t = mock(Term.class);
		when(t.print()).thenReturn("test");
		TermVar test = new TermVar(t, 2);
		
		assertEquals("test", test.print());
		
		verify(t, times(1)).print();
	}
	
	@Test
	void resettingAnAssignedVariable() {
		Term t = mock(Term.class);
		when(t.print()).thenReturn("test");
		TermVar test = new TermVar(t, 2);
		
		test.reset();
		
		assertTrue(test.isUnunified());
	}

}
