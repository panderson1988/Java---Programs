package avltree;

import stdlib.StdIn;
import stdlib.StdOut;

public class TestAVLTreeST {
    /**
     * Unit tests the {@code AVLTreeST} data type.
     */
    public static void main(String[] args) {
        AVLTreeST<String, Integer> st = new AVLTreeST<String, Integer>();
        StdIn.fromFile("data/radio_alphabet.txt");
        String[] words = StdIn.readAllStrings();
        int count = 0;
        for (String word: words) {
            st.put(word, count);
            count++;
        }
        st.drawTree();
        
        // st.delete("Papa");
        // st.drawTree();
    }
}
