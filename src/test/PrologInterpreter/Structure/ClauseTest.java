package test.PrologInterpreter.Structure;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

import PrologInterpreter.Structure.Clause;
import PrologInterpreter.Structure.Goal;
import PrologInterpreter.Structure.TermCons;

class ClauseTest {

	@Test
	void creatingGoalNoTail() {
		TermCons c = mock(TermCons.class);
		
		Clause result = new Clause(c, null);
		
		assertNotNull(result);
		assertEquals(c, result.getHead());
		assertNull(result.getBody());
	}

	@Test
	void creatingGoalWithTail() {
		TermCons c = mock(TermCons.class);
		Goal g = mock(Goal.class);
		
		Clause result = new Clause(c, g);
		
		assertNotNull(result);
		assertEquals(c, result.getHead());
		assertEquals(g, result.getBody());
	}
	
	@Test
	void copyingClauseNoTail() {
		TermCons c = mock(TermCons.class);
		when(c.copyCons()).thenReturn(c);
		Clause test = new Clause(c, null);
		
		Clause result = test.copy();
		
		assertNotNull(result);
		assertEquals(c, result.getHead());
		assertNull(result.getBody());
		
		verify(c, times(1)).copyCons();
	}
	@Test
	void copyingClauseWithTail() {
		TermCons c = mock(TermCons.class);
		when(c.copyCons()).thenReturn(c);
		Goal g = mock(Goal.class);
		when(g.copy()).thenReturn(g);
		Clause test = new Clause(c, g);
		
		Clause result = test.copy();
		
		assertNotNull(result);
		assertEquals(c, result.getHead());
		assertEquals(g, result.getBody());
		
		verify(c, times(1)).copyCons();
		verify(g, times(1)).copy();
	}
}
