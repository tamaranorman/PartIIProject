package test.PrologInterpreter.Structure;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import PrologInterpreter.Structure.Atom;

class AtomTest {

	@Test
	public void returnsCorrectString() {
		Atom test = new Atom("test");
		
		assertEquals(test.getAtomName(), "test");
	}
	
	@Test
	public void equalsMethodWhenCorrect() {
		Atom test = new Atom("test");
		Atom compare = new Atom("test");
		
		assertTrue(test.equals(compare));
	}
	
	@Test
	public void equalsMethodWhenIncorrect() {
		Atom test = new Atom("test");
		Atom compare = new Atom("notTest");
		
		assertFalse(test.equals(compare));
	}
	
	@Test
	public void equalsMethodWhenNotCorrectObject() {
		Atom test = new Atom("test");
		Object compare = new Object();
		
		assertFalse(test.equals(compare));
	}

}
