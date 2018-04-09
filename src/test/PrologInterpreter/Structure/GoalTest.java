package test.PrologInterpreter.Structure;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

import PrologInterpreter.Structure.Goal;
import PrologInterpreter.Structure.TermCons;

class GoalTest {

	@Test
	void createGoalListSingleItem() {
		TermCons c = mock(TermCons.class);
		
		Goal test = new Goal(c, null);
		
		assertNotNull(test);
		assertEquals(c, test.getHead());
		assertNull(test.getTail());
	}
	
	@Test
	void createGoalListWithTail() {
		TermCons c = mock(TermCons.class);
		Goal g = mock(Goal.class);
		
		Goal test = new Goal(c, g);
		
		assertNotNull(test);
		assertEquals(c, test.getHead());
		assertEquals(g, test.getTail());
	}
	
	@Test
	void copyGoalListSingleItem() {
		TermCons c = mock(TermCons.class);
		when(c.copyCons()).thenReturn(c);
		Goal test = new Goal(c, null);
		
		Goal result = test.copy();
		
		assertEquals(c, result.getHead());
		assertNull(result.getTail());
		
		verify(c, times(1)).copyCons();
	}
	
	@Test
	void copyGoalListMultipleItems() {
		TermCons c = mock(TermCons.class);
		when(c.copyCons()).thenReturn(c);
		Goal g = mock(Goal.class);
		when(g.copy()).thenReturn(g);
		Goal test = new Goal(c, g);
		
		Goal result = test.copy();
		
		assertEquals(c, result.getHead());
		assertEquals(g, result.getTail());
		
		verify(c, times(1)).copyCons();
		verify(g, times(1)).copy();
	}
	
	@Test
	void appendWhenFirstIsNull() {
		Goal g = mock(Goal.class);
		
		Goal result = Goal.append(null, g);
		
		assertNotNull(result);
		assertEquals(g, result);
	}
	
	@Test
	void appendWhenFirstIsNotNull() {
		TermCons c1 = mock(TermCons.class);
		TermCons c2 = mock(TermCons.class);
		TermCons c3 = mock(TermCons.class);
		TermCons c4 = mock(TermCons.class);
		Goal g1a = new Goal(c2, null);
		Goal g1 = new Goal(c1, g1a);
		Goal g2a = new Goal(c4, null);
		Goal g2 = new Goal(c3, g2a);
		
		Goal result = Goal.append(g1, g2);
		
		assertEquals(result.getHead(), c1);
		result = result.getTail();
		assertEquals(result.getHead(), c2);
		result = result.getTail();
		assertEquals(result.getHead(), c3);
		result = result.getTail();
		assertEquals(result.getHead(), c4);
		result = result.getTail();
		assertNull(result);
	}
}
