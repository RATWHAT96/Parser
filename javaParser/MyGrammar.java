import computation.contextfreegrammar.*;
import java.util.ArrayList;
import java.util.HashSet;

public class MyGrammar {
	public static ContextFreeGrammar makeGrammar() {
		Variable E0 = new Variable("E0");
		Variable E = new Variable('E');
		Variable G = new Variable('G');
		Variable T = new Variable('T');
		Variable H = new Variable('H');
		Variable F = new Variable('F');
		Variable I = new Variable('I');
		Variable J = new Variable('J');
		Variable K = new Variable('K');
		Variable L = new Variable('L');
		Variable A = new Variable('A');
		Variable M = new Variable('M');

		HashSet<Variable> variables = new HashSet<>();
		variables.add(E0);
		variables.add(E);
		variables.add(G);
		variables.add(T);
		variables.add(H);
		variables.add(F);
		variables.add(I);
		variables.add(J);
		variables.add(K);
		variables.add(L);
		variables.add(A);
		variables.add(M);

		Terminal p = new Terminal('p');
		Terminal r = new Terminal('r');
		Terminal plus = new Terminal('+');
		Terminal minus = new Terminal('-');
		Terminal fBracket = new Terminal('(');
		Terminal bBracket = new Terminal(')');
		Terminal and = new Terminal('&');

		HashSet<Terminal> terminals = new HashSet<>();
		terminals.add(p);
		terminals.add(r);
		terminals.add(plus);
		terminals.add(minus);
		terminals.add(fBracket);
		terminals.add(bBracket);
		terminals.add(and);

		ArrayList<Rule> rules = new ArrayList<>();
		rules.add(new Rule(E0, new Word(G,T)));
		rules.add(new Rule(E0, new Word(H,F)));
		rules.add(new Rule(E0, new Word(I,J)));
		rules.add(new Rule(E0, new Word(K,F)));
		rules.add(new Rule(E0, new Word(p)));
		rules.add(new Rule(E0, new Word(r)));

		rules.add(new Rule(E, new Word(G,T)));
		rules.add(new Rule(E, new Word(H,F)));
		rules.add(new Rule(E, new Word(I,J)));
		rules.add(new Rule(E, new Word(K,F)));
		rules.add(new Rule(E, new Word(p)));
		rules.add(new Rule(E, new Word(r)));

		rules.add(new Rule(T, new Word(H,F)));
		rules.add(new Rule(T, new Word(I,J)));
		rules.add(new Rule(T, new Word(K,F)));
		rules.add(new Rule(T, new Word(p)));
		rules.add(new Rule(T, new Word(r)));

		rules.add(new Rule(F, new Word(I,J)));
		rules.add(new Rule(F, new Word(K,F)));
		rules.add(new Rule(F, new Word(p)));
		rules.add(new Rule(F, new Word(r)));

		rules.add(new Rule(G, new Word(E,L)));
		rules.add(new Rule(H, new Word(T,A)));
		rules.add(new Rule(I, new Word(M,E)));

		rules.add(new Rule(J, new Word(bBracket)));
		rules.add(new Rule(K, new Word(minus)));
		rules.add(new Rule(L, new Word(plus)));
		rules.add(new Rule(A, new Word(and)));
		rules.add(new Rule(M, new Word(fBracket)));

		ContextFreeGrammar cfg = new ContextFreeGrammar(variables, terminals, rules, E0);
		return cfg;
	}
}


