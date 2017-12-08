package PrologInterpreter.Structure;

public class TestStructure {
	public static void main(String[] args){
	Atom a = new Atom("app");
	Atom c = new Atom("cons");
	TermCons nil = new TermCons(new Atom("nil"), 0, null);
	TermCons one = new TermCons(new Atom("1"), 0, null);
	TermCons two = new TermCons(new Atom("2"), 0, null);
	TermCons three = new TermCons(new Atom("3"), 0, null);
	
	//app(nil, x, x)
	Term v_x = new TermVar();
	TermCons lhs1 = new TermCons(a, 3, new Term[]{nil, v_x, v_x});
	Clause c1 = new Clause(lhs1, null);
	
	//app(cons(x, l), m, cons(x,n))) :- app(l, m, n)
	Term v_l = new TermVar();
	Term v_m = new TermVar();
	Term v_n = new TermVar();
	
	TermCons rhs2 = new TermCons(a, 3, new Term[]{v_l, v_m, v_n});
	TermCons lhs2 = new TermCons(a, 3, new Term[]{new TermCons(c, 2, new Term[]{v_x, v_l}),
												  v_m,
												  new TermCons(c, 2, new Term[]{v_x, v_n})});
	Clause c2 = new Clause(lhs2, new Goal(rhs2, null));
	
	//app(i, j, cons(1, cons(2, cons(3, nil))))
	TermVar v_i = new TermVar();
	TermVar v_j = new TermVar();
	TermCons rhs3 = new TermCons(a, 3, new Term[]{v_i,
												  v_j,
												  new TermCons(c, 2, new Term[]{one,
												  new TermCons(c, 2, new Term[]{two,
											      new TermCons(c, 2, new Term[]{three, nil})
												  })})});
	Goal g1 = new Goal(rhs3, null);
	
	Program test = new Program(c1, new Program(c2, null));
	
	TermVar[] v = new TermVar[]{v_i, v_j};
	String[] n = new String[]{"I", "J"};
	TermVarMapping map = new TermVarMapping(v, n, 2);
	
	//g1.solve(test, map);
	
	}
	
}
