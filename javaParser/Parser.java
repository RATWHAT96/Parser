import computation.contextfreegrammar.*;
import computation.parser.*;
import computation.parsetree.*;
import computation.derivation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.LinkedList;


public class Parser implements IParser {

	// Method to check if the target word is in the language
	public boolean isInLanguage(ContextFreeGrammar cfg, Word target){
		if( getDerivation(cfg, target) != null ) {
			return true;
		} else {
			return false;
		}
	}

	//Method to get the derivation of the word
	public Derivation getDerivation(ContextFreeGrammar cfg, Word target) {
		//List for the words in the steps in the derivations to be added to the list fof derivations
		List<Word> tempwords = new ArrayList<Word>();
		//List for derivations
		List<Derivation> derivations = new ArrayList<Derivation>();
		//List for rules
		List<Rule> rules = cfg.getRules();
		// Creating list for terminal symbols in target word
		List<Symbol> targetList = new ArrayList<Symbol>();
		for (Symbol s : target) {
			if (s.isTerminal()) {
				targetList.add(s);
			}
		}

		int steps = 0;
		int index;
		int maxDepth = (target.length() * 2) - 1;

		Derivation start = new Derivation(new Word(cfg.getStartVariable()));
		derivations.add(start);


		while (steps < maxDepth) {
			List<Derivation> newDerivation = new ArrayList<Derivation>();
			// For all words in activeStrings
			outerloop:
			for (Derivation d : derivations) {
				// For all symbols in word
				Word w = d.getLatestWord();
				for (Rule r : rules) {
					int occurrences = w.count(r.getVariable());
					for (int o = 0; o < occurrences; o++) {
						//boolean used later on
						boolean please = false;
						index = w.indexOfNth(r.getVariable(), o);
						Word newWord = w.replace(index, r.getExpansion());

						//Creates a list of terminal symbols in the newWord
						List<Symbol> newWordList = new ArrayList<Symbol>();
						newWordList.clear();
						for (Symbol s : newWord) {
							if (s.isTerminal()) {
								newWordList.add(s);
							}
						}
						// for loop that check if the newWord has any terminals that are not in the target word and triggers condition to prevents new derivation step from being created
						for(Symbol x: newWordList) {
							int indexTarget = targetList.indexOf(x);
							if (indexTarget != -1) {
								please = false;
							} else {
								please = true;
								break;
							}
						}
						// Conditional to prevents new derivation step from being created when the newWord has any terminals that are not in the target word
						if (please) {
							continue;
						}
						// Conditional to check to check if any newWords are repeated and if a new word is longer than the target word
						if (tempwords.contains(newWord) || newWord.length() > target.length()) {
							//Skip
						// Conditional to check if the new word is the target word
						} else if (tempwords.contains(target)) {
							tempwords.add(newWord);
							Derivation newDer = new Derivation(d);
							newDer.addStep(newWord, r, index);
							newDerivation.add(newDer);
							break outerloop;
						// Conditional to add a derivation step to newDerivation
						} else {
							tempwords.add(newWord);
							Derivation newDer = new Derivation(d);
							newDer.addStep(newWord, r, index);
							newDerivation.add(newDer);
						}
					}
				}
			}
			steps++;
			derivations = newDerivation;
		}

		List<Word> finalWords = derivations.stream().map(Derivation::getLatestWord).collect(Collectors.toList());
		index = finalWords.indexOf(target);
		if (index == -1) {
			return null;
		}

		return derivations.get(index);
	}

	//Method to generate parse tree
	public ParseTreeNode generateParseTree(ContextFreeGrammar cfg, Word w) {
		Derivation treeDer =  getDerivation(cfg, w);
		if (treeDer == null){
			return null;
		} else {
			return createParseTree(cfg, treeDer);
		}
	}

	//Method to print parsetree
	public void printParseTree(ContextFreeGrammar cfg, Word w) {
		try {
			System.out.println(w + "\n");
			ParseTreeNode tree = this.generateParseTree(cfg, w);
			tree.print();
		} catch (NullPointerException e){
			System.out.println("This word is not in the language, therefore no parse tree");
		}
	}

	//Method to createparsetree
	private ParseTreeNode createParseTree(ContextFreeGrammar cfg, Derivation derivation) {
		// create a word containing the first word of the derivation
		Word finalWord = derivation.getLatestWord();

		//Linked list created that contains parse tree nodes for the the final word
		LinkedList<ParseTreeNode> backwardsDerive = new LinkedList<>();
		for(int i = 0; i < finalWord.length(); i++) {
			backwardsDerive.add(new ParseTreeNode(finalWord.get(i)));
		}

		for(Step s : derivation) {
			if(s.isStartSymbol()) {
				break;
			} else if(s.getRule().getExpansion().length() == 0) {
				assert(finalWord.equals(Word.emptyWord));
				return ParseTreeNode.emptyParseTree(s.getRule().getVariable());
			} else if(s.getRule().getExpansion().length() == 1) {
				backwardsDerive.set(s.getIndex(), new ParseTreeNode(s.getRule().getVariable(), new ParseTreeNode(s.getRule().getExpansion().get(0))));
			} else if(s.getRule().getExpansion().length() == 2) {
				backwardsDerive.set(s.getIndex()+1, new ParseTreeNode(s.getRule().getVariable(), backwardsDerive.get(s.getIndex()), backwardsDerive.get(s.getIndex()+1)));
				backwardsDerive.remove(backwardsDerive.get(s.getIndex()));
			}
		}
		assert(backwardsDerive.size() == 1);
		return backwardsDerive.get(0);
	}

}
