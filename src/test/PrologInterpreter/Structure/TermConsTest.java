package test.PrologInterpreter.Structure;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

import PrologInterpreter.Structure.Atom;
import PrologInterpreter.Structure.Term;
import PrologInterpreter.Structure.TermCons;
import PrologInterpreter.Structure.TermVar;

class TermConsTest {

	@Test
	void creatingAnAtom() {
		Atom t = new Atom("test");
		TermCons test = new TermCons(t, 0, null, false);
		
		assertNotNull(test);
		assertEquals(test.getAtom(), t);
		assertNull(test.getArgs());
	}
	
	@Test
	void printingAnAtom() {
		Atom t = new Atom("test");
		TermCons test = new TermCons(t, 0, null, false);
		
		assertEquals(test.print(), "test");
	}

	@Test
	void copyingAnAtom() {
		Atom t = new Atom("test");
		TermCons test = new TermCons(t, 0, null, false);
		
		Term result = test.copy();
		
		assertTrue(result instanceof TermCons);
		TermCons resultCons = (TermCons) result;
		
		assertEquals(resultCons.getAtom().getAtomName(), "test");
		assertNull(resultCons.getArgs());
		assertEquals(resultCons.print(), "test");
	}
	
	@Test
	void creatingACompoundTerm() {
		Atom t = new Atom("test");
		Term termMoq = mock(Term.class);
		Term[] terms = new Term[] {termMoq, termMoq};
		
		TermCons test = new TermCons(t, 2, terms, false);
			
		assertNotNull(test);
		assertEquals(test.getAtom(), t);
		assertNotNull(test.getArgs());
	}
	
	@Test
	void creatingACompoundTermWithVariableArgs() {
		Atom t = new Atom("test");
		Term termMoq = mock(TermVar.class);
		Term[] terms = new Term[] {termMoq, termMoq};
		
		TermCons test = new TermCons(t, 2, terms, true);
			
		assertNotNull(test);
		assertEquals(test.getAtom(), t);
		assertNotNull(test.getArgs());
		assertTrue(test.getContainsVar());
	}
	
	@Test
	void printingACompoundTermWithMockedTerms() {
		//Setup mocks
		Term a = mock(Term.class);
		when(a.print()).thenReturn("A");
		Term b = mock(Term.class);
		when(b.print()).thenReturn("B");
		Atom t = mock(Atom.class);
		when(t.getAtomName()).thenReturn("test");
		
		//Create test object
		TermCons test = new TermCons(t, 2, new Term[] {a, b}, false);
		
		//Test output and verify correct calls made
		assertEquals("test(A, B)", test.print());
		verify(a, times(1)).print();
		verify(b, times(1)).print();
		verify(t, times(1)).getAtomName();
	}
	
	@Test
	void copyingASimpleCompoundTerm() {
		Term a = mock(Term.class);
		when(a.print()).thenReturn("A");
		Atom t = new Atom("test");
		TermCons test = new TermCons(t, 2, new Term[] {a, a}, false);
		
		Term result = test.copy();
		assertTrue(result instanceof TermCons);
		TermCons resultCons = (TermCons) result;
		assertEquals(test.getAtom().getAtomName(), resultCons.getAtom().getAtomName());
		for(int i = 0; i < 2; i++) {
			assertEquals(test.getArgs()[i].print(), resultCons.getArgs()[i].print());
		}
	}
	
	@Test
	void copyingACompoundTermWithVars() {
		Term a = mock(TermVar.class);
		when(a.print()).thenReturn("A");
		when(a.copy()).thenReturn(a);
		Atom t = new Atom("test");
		TermCons test = new TermCons(t, 2, new Term[] {a, a}, true);
		
		Term result = test.copy();
		assertTrue(result instanceof TermCons);
		TermCons resultCons = (TermCons) result;
		assertEquals(test.getAtom().getAtomName(), resultCons.getAtom().getAtomName());
		for(int i = 0; i < 2; i++) {
			assertEquals(test.getArgs()[i].print(), resultCons.getArgs()[i].print());
		}
		verify(a, times(2)).copy();
	}
	
	@Test
	void printingASimpleList() {
		Term a = mock(Term.class);
		when(a.print()).thenReturn("A");
		Term b = mock(Term.class);
		when(b.print()).thenReturn("B");
		Term c = mock(Term.class);
		when(c.print()).thenReturn("C");
		Atom cons = new Atom("cons");
		TermCons nil = new TermCons(new Atom("nil"), 0, null, false);
		TermCons endOfList = new TermCons(cons, 2, new Term[] {c, nil}, false);
		TermCons innerTest = new TermCons(cons, 2, new Term[] {b, endOfList}, false);
		TermCons test = new TermCons(cons, 2, new Term[] {a, innerTest}, false);
		
		assertEquals("[A,B,C]", test.print());
	}
	
	@Test
	void printingAVariableEndingList() {
		Term a = mock(Term.class);
		when(a.print()).thenReturn("A");
		Term b = mock(Term.class);
		when(b.print()).thenReturn("B");
		Term c = mock(TermVar.class);
		when(c.print()).thenReturn("C");
		Atom cons = new Atom("cons");
		TermCons innerTest = new TermCons(cons, 2, new Term[] {b, c}, false);
		TermCons test = new TermCons(cons, 2, new Term[] {a, innerTest}, false);
		
		assertEquals("[A,B|C]", test.print());
	}
	
	@Test
	void printingAnAssignedVariableEndingList() {
		Term a = mock(Term.class);
		when(a.print()).thenReturn("A");
		Term b = mock(Term.class);
		when(b.print()).thenReturn("B");
		Term c = mock(TermVar.class);
		when(c.print()).thenReturn("[C]");
		Atom cons = new Atom("cons");
		TermCons innerTest = new TermCons(cons, 2, new Term[] {b, c}, false);
		TermCons test = new TermCons(cons, 2, new Term[] {a, innerTest}, false);
		
		assertEquals("[A,B,C]", test.print());
	}
	
	@Test
	void printingAListEndingWithAnAssignedNilTerm() {
		Term a = mock(Term.class);
		when(a.print()).thenReturn("A");
		Term b = mock(Term.class);
		when(b.print()).thenReturn("B");
		Term c = mock(TermVar.class);
		when(c.print()).thenReturn("nil");
		Atom cons = new Atom("cons");
		TermCons innerTest = new TermCons(cons, 2, new Term[] {b, c}, false);
		TermCons test = new TermCons(cons, 2, new Term[] {a, innerTest}, false);
		
		assertEquals("[A,B]", test.print());
	}
}
