package bioinformatics;

import algs34.LinearProbingHashST;

public class AminoAcid {
	private static final String[] FULL_NAMES = {"alanine", "arginine", "asparagine", "aspartic acid", "cysteine", "glutamine", "glutamic acid", "glycine", "histidine", "isoleucine", "leucine", "lysine", "methionine", "phenylalanine", "proline", "serine", "threonine", "tryptophan", "tyrosine", "valine", "stop"};
	private static final String[] SHORT_NAMES = {"ala", "arg", "asn", "asp", "cys", "gln", "glu", "gly", "his", "ile", "leu", "lys", "met", "phe", "pro", "ser", "thr", "trp", "tyr", "val", "stp"};
	private static final String[] ONE_LETTER = {"A", "R", "N", "D", "C", "Q", "E", "G", "H", "I", "L", "K", "M", "F", "P", "S", "T", "W", "Y", "V", "X"};
	private static class StringPair {
		public String first, second;
		public StringPair(String first, String second) {
			this.first = first;
			this.second = second;
		}
	}
	
	private static LinearProbingHashST<String, StringPair> st;
	
	static {
		st = new LinearProbingHashST<>(43);
		for (int i = 0; i < ONE_LETTER.length; i++) {
			st.put(ONE_LETTER[i], new StringPair(FULL_NAMES[i], SHORT_NAMES[i]));
		}
	}
	
	public static String fullName(String oneLetter) {
		return st.get(oneLetter).first;
	}
	
	public static String shortName(String oneLetter) {
		return st.get(oneLetter).second;
	}
}
