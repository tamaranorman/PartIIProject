package test.PrologInterpreter;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.HashMap;

import org.junit.jupiter.api.Test;

import com.igormaznitsa.prologparser.exceptions.PrologParserException;

import PrologInterpreter.BasicParser;
import PrologInterpreter.Structure.Clause;
import PrologInterpreter.Structure.GoalMappingPair;

class BasicParserTest {

	@Test
	public void testConstructorRunsCorrectly() {
		BasicParser parser = new BasicParser();
		
		assertNotNull(parser);
	}
	
	@Test
	public void testParseClauseSimple(){
		BasicParser parser = new BasicParser();
		HashMap<String, Integer> dictTest = new HashMap<>();
		boolean exceptionThrown = false;
		Clause result = null;
		
		try {
			result = parser.parseClause("select([], []).", dictTest);
		} catch (IOException | PrologParserException e) {
			exceptionThrown = true;
		}
		
		assertFalse(exceptionThrown);
		assertNotNull(result);
		assertEquals(result.getHead().getAtom().getAtomName(), "select");
		assertNull(result.getBody());
		assertTrue(dictTest.containsKey("select"));
	}
	
	@Test
	public void testParseClauseIncorrectSyntax(){
		BasicParser parser = new BasicParser();
		HashMap<String, Integer> dictTest = new HashMap<>();
		boolean exceptionThrown = false;
		Clause result = null;
		Exception exceptionTest = null;
		
		try {
			result = parser.parseClause("select([], []", dictTest);
		} catch (IOException | PrologParserException e) {
			exceptionThrown = true;
			exceptionTest = e;
		}
		
		assertTrue(exceptionThrown);
		assertNull(result);
		assertTrue(exceptionTest instanceof PrologParserException);
	}
	
	@Test
	public void testParseClauseWithTail(){
		BasicParser parser = new BasicParser();
		HashMap<String, Integer> dictTest = new HashMap<>();
		boolean exceptionThrown = false;
		Clause result = null;
		
		try {
			result = parser.parseClause("select(X, [H|T]) :- select(X, T).", dictTest);
		} catch (IOException | PrologParserException e) {
			exceptionThrown = true;
		}
		
		assertFalse(exceptionThrown);
		assertNotNull(result);
		assertEquals(result.getHead().getAtom().getAtomName(), "select");
		assertNotNull(result.getBody());
		assertTrue(dictTest.containsKey("select"));
	}
	
	@Test
	public void testParseGoalSimple() {
		BasicParser parser = new BasicParser();
		boolean exceptionThrown = false;
		GoalMappingPair result = null;
		
		try {
			result = parser.parseGoal("select(A, [1,2,3]).");
		} catch (IOException | PrologParserException e) {
			exceptionThrown = true;
		}
		
		assertFalse(exceptionThrown);
		assertNotNull(result);
		assertTrue(result.getMap().containsKey("A"));
		assertNotNull(result.getMap().get("A"));
		assertEquals(result.getGoal().getHead().getAtom().getAtomName(), "select");
		assertNull(result.getGoal().getTail());
	}
	
	@Test
	public void testParseGoalMultiple() {
		BasicParser parser = new BasicParser();
		boolean exceptionThrown = false;
		GoalMappingPair result = null;
		
		try {
			result = parser.parseGoal("select(A, [1,2,3]), select(B, [1,2]), match(A, B).");
		} catch (IOException | PrologParserException e) {
			exceptionThrown = true;
		}
		
		assertFalse(exceptionThrown);
		assertNotNull(result);
		assertTrue(result.getMap().containsKey("A"));
		assertTrue(result.getMap().containsKey("B"));
		assertNotNull(result.getMap().get("A"));
		assertNotNull(result.getMap().get("B"));
		assertEquals(result.getGoal().getHead().getAtom().getAtomName(), "select");
		assertNotNull(result.getGoal().getTail());
	}
	
	@Test
	public void testParseGoalFail() {
		BasicParser parser = new BasicParser();
		boolean exceptionThrown = false;
		GoalMappingPair result = null;
		Exception exceptionTest = null;
		
		try {
			result = parser.parseGoal("select(A, [1,2,3]), select(B, [1,2]), match(A, B)");
		} catch (IOException | PrologParserException e) {
			exceptionThrown = true;
			exceptionTest = e;
		}
		
		assertTrue(exceptionThrown);
		assertNull(result);
		assertTrue(exceptionTest instanceof PrologParserException);
	}

}
