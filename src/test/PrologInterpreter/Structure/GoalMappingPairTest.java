package test.PrologInterpreter.Structure;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

import PrologInterpreter.Structure.Goal;
import PrologInterpreter.Structure.GoalMappingPair;
import PrologInterpreter.Structure.TermVarMapping;

class GoalMappingPairTest {

	@Test
	void returnsCorrectItems() {
		Goal g = mock(Goal.class);
		TermVarMapping m = mock(TermVarMapping.class);
		
		GoalMappingPair test = new GoalMappingPair(g, m);
		
		assertNotNull(test);
		assertEquals(g, test.getGoal());
		assertEquals(m, test.getMap());
	}

}
