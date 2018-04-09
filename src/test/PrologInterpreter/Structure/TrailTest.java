package test.PrologInterpreter.Structure;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

import PrologInterpreter.Structure.TermVar;
import PrologInterpreter.Structure.Trail;

class TrailTest {

	@Test
	public void whenAddingToTheTrail() {
		TermVar var = mock(TermVar.class);
		
		Trail.push(var);
		
		assertNotNull(Trail.note());
	}
	
	@Test
	public void whenUndoingTheTrailSingleElement() {
		TermVar var = mock(TermVar.class);
		Trail.push(var);
		
		Trail.undo(null);
		
		verify(var, times(1)).reset();
	}
	
	@Test
	public void whenUndoingTheTrailMultipleElement() {
		TermVar var1 = mock(TermVar.class);
		TermVar var2 = mock(TermVar.class);
		Trail.push(var1);
		Trail t = Trail.note();
		Trail.push(var2);
		
		Trail.undo(t);
		
		verify(var2, times(1)).reset();
		verify(var1, times(0)).reset();
	}

}
