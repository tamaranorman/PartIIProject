package test.PrologInterpreter.Structure;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

import PrologInterpreter.Structure.Clause;
import PrologInterpreter.Structure.Program;

class ProgramTest {

	@Test
	void createProgramWithNullTail() {
		Clause c = mock(Clause.class);
		
		Program result = new Program(c);
		
		assertNotNull(result);
		assertEquals(c, result.getHead());
		assertNull(result.getTail());
	}
	
	@Test
	void createProgramWithTail() {
		Clause c = mock(Clause.class);
		Clause c2 = mock(Clause.class);
		
		Program test = new Program(c);
		new Program(c2);
		
		assertNotNull(test);
		assertEquals(c, test.getHead());
		assertEquals(c2, test.getTail().getHead());
	}
}
