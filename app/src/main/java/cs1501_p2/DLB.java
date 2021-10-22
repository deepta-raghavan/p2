package cs1501_p2;

import java.text.StringCharacterIterator;
import java.util.ArrayList;

public class DLB implements Dict {

    DLBNode root = null;
    private int words;
    private ArrayList<String> wordlist;


    public void add(String s) {
        if (s != null && s.length() > 0) {
            if (root == null)    // Initialize first node if empty
            {
                root = new DLBNode(s.charAt(0));
                if (s.length() == 1)
                    root.setDown(new DLBNode('^'));
                else {
                    DLBNode temp = root;
                    int slen = s.length();
                    for (int i = 1; i < slen; i++) {
                        temp.setDown(new DLBNode(s.charAt(i)));
                        temp = temp.getDown();
                    }

                    temp.setDown(new DLBNode('^'));
                }

                words = 1;
                return;
            }

            int pos = 0,// Starting position of the string
                    sl1 = s.length() - 1;
            char ch = s.charAt(0);
            DLBNode temp = root;

            while (true) {
                if (ch > temp.getLet()) {
                    if (temp.getRight() != null) // Check the siblings
                    {
                        temp = temp.getRight();
                    } else // No siblings
                    {
                        temp.setRight(new DLBNode(ch));
                        temp = temp.getRight();
                        int slen = s.length();
                        pos++;
                        for (; pos < slen; pos++) {
                            temp.setDown(new DLBNode(s.charAt(pos)));
                            temp = temp.getDown();
                        }

                        temp.setDown(new DLBNode('^'));

                        words++;
                        return;
                    }
                } else if (ch == temp.getLet()) {
                    if (pos < sl1) {
                        pos++;
                        temp = temp.getDown();
                        ch = s.charAt(pos);
                    }
                    //reached the end of the string, check child for terminator '^'
                    else if (temp.getDown().getLet() != '^') {
                        temp = temp.getDown();
                        DLBNode temp2 = new DLBNode(temp.getLet());
                        temp2.setDown(temp.getDown());
                        temp2.setRight(temp.getRight());
                        DLBNode down = temp.getDown();
                        temp = new DLBNode('^');
                        temp.setDown(down);
                        temp.setRight(temp2);

                        words++;
                        return;
                    } else
                        return;
                } else  // ch < temp.data
                {
                    DLBNode temp2 = new DLBNode(temp.getLet());    // duplicate the current node's data
                    temp2.setDown(temp.getDown());            // link back to the temp's children
                    temp2.setRight(temp.getRight());        // link to temp's sibling
                    DLBNode down2 = temp.getDown();
                    temp = new DLBNode(ch);
                    temp.setDown(down2);
                    temp.setRight(temp2);                // link to the temp node


                    int slen = sl1 + 1;
                    pos++;
                    for (; pos < slen; pos++) {
                        temp.setDown(new DLBNode(s.charAt(pos)));
                        temp = temp.getDown();
                    }

                    temp.setDown(new DLBNode('^'));

                    words++;
                    return;
                }
            }
        }
        return;
    }


    public boolean contains(String key) {
        if (key == null || root == null) return false;

        key = key.toLowerCase(); //All passwords are lowercase
        StringCharacterIterator it = new StringCharacterIterator(key);
        DLBNode curr = root;

        while (it.getIndex() < it.getEndIndex()) { //Loop through each character in the word
            if (curr == null) { //If the node we're at is null, then obviously the word does not exist
                return false;
            }

            while (it.current() != curr.getLet()) { //Loop through sibling linked list
                if (curr.getRight() == null) {
                    return false; //No sibling node matches the current character
                } else {
                    curr = curr.getRight();
                }
            }

            curr = curr.getDown();
            it.next();
        }

        if (curr == null) { //No node exists to terminate the word, so it doesn't exist
            return false;
        } else if (curr.getLet() == '^') { //Reached the end of the word, so it exists
            return true;
        }

        while (curr.getRight() != null) { //Loop through sibling linked list
            if (curr.getLet() == '^') {
                return true;
            } else {
                curr = curr.getRight();
            }
        }

        return false;
    }


    public boolean containsPrefix(String pre) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < pre.length(); i++) {
            s.append(pre.charAt(i));
        }
        return containsPrefix(s, 0, s.length() - 1);
    }

    private boolean containsPrefix(StringBuilder s, int start, int end) {
        if (root != null) {
            char ch = s.charAt(start);
            DLBNode temp = root;

            while (true) {
                if (ch > temp.getLet()) {
                    if (temp.getRight() != null) // Check the siblings
                    {
                        temp = temp.getRight();
                    } else return false;        // Not a word/prefix in the dictionary

                } else if (ch == temp.getLet()) {
                    if (start < end) {
                        start++;
                        temp = temp.getDown();
                        ch = s.charAt(start);
                    }
                    // Check if word && prefix
                    else if (temp.getDown().getLet() == '^') {
                        if (temp.getDown().getRight() != null) return true;

                        return false;
                    } else return true; // Only a prefix
                } else return false; // ch < temp.data & not in the DLB
            }
        }
        return false;
    }

    public int searchByChar(char next)
    {
        if (root != null)
        {
            DLBNode temp = root;

            while(true)
            {
                if (next > temp.getLet())
                {
                    if(temp.getRight() != null) // Check the siblings
                    {
                        temp = temp.getRight();
                    }
                    else 	return -1;		// Not a word/prefix in the dictionary

                }
                else if (next == temp.getLet())
                {
                    // Check if word && prefix
                    if (temp.getDown().getLet() == '^')
                    {
                        if (temp.getDown().getRight() != null) return 2;

                        return 1;
                    }
                    else 	return 0; // Only a prefix
                }
                else  	return -1; // ch < temp.data & not in the DLB
            }
        }
        return -1;
    }


    public void resetByChar() {
        searchByChar(root.getLet());
    }


    public ArrayList<String> suggest() {
        ArrayList<String> x = new ArrayList<String>();
        String word = "";
        // Conditional to check if the head node is empty/null.
        if (root == null) {
            return new ArrayList<String>();
        }

        DLBNode d = root;
        boolean check = true;
        for(int i =0; i<5; i++) {
            while (check == true) {
                if (d == null) {
                    check = false;
                } else if (d.getRight() != null) {
                    word += d.getLet();
                    d = d.getRight();
                    word += d.getLet();
                } else{
                    if (d.getDown().getLet() == '^') {
                        x.add(word);
                    } else {
                        d = d.getDown();
                    }
                }
            }
        }
        return x;
    }

    @Override
    public ArrayList<String> traverse() {
        if (root == null) return null;
        ArrayList<String> arr = new ArrayList<String>();
        DLBNode curr = root;
        String word = "";

        while(true){
            if(curr.getLet() != '^'){
                word += curr.getLet();
                curr = curr.getDown();
            }
            else{
                arr.add(word);
                curr = curr.getRight();
            }
        }
    }


    public int count() {
        return words;
    }
}
