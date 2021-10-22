package cs1501_p2;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.io.BufferedReader;

public class AutoCompleter implements AutoComplete_Inter {
    DLB dlbtrie = new DLB();
    UserHistory uh;
    public AutoCompleter(String dict){
        BufferedReader reader = null;

        try {
            File file = new File(dict);
            reader = new BufferedReader(new FileReader(file));

            String line;
            while ((line = reader.readLine()) != null) {
                dlbtrie.add(reader.readLine());
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public AutoCompleter(String dict1, String dict2){
        BufferedReader reader = null;
        BufferedReader reader2 = null;

        try {
            File file = new File(dict1);
            File file2 = new File(dict2);
            reader = new BufferedReader(new FileReader(file));
            reader2 = new BufferedReader(new FileReader(file2));

            String line;
            String line2;
            while ((line = reader.readLine()) != null) {
                dlbtrie.add(line);
            }
            while((line2 = reader2.readLine()) != null){
                uh.add(line2);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
                reader2.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<String> nextChar(char next) {
        ArrayList<String> x = new ArrayList<String>();
        String word = "";
        // Conditional to check if the head node is empty/null.
        if (dlbtrie.root == null) {
            return new ArrayList<String>();
        }

        DLBNode d = dlbtrie.root;
        boolean check = true;
        for(int i =0; i<5; i++) {
            while (check == true) {
                if (d == null) {
                    check = false;
                } else if (d.getRight() != null && d.getLet() != next) {
                    word += d.getLet();
                    d = d.getRight();
                    word += d.getLet();
                } else if (d.getLet() == next) {
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



    public void finishWord(String cur) {
        DLBNode d = dlbtrie.root;
        boolean check = true;
        for (int i = 0; i < cur.length(); i++) {
            char c = cur.charAt(i);
            while(true) {
                if (d.getLet() == c) {
                    d = d.getDown();
                    if (d.getLet() == '^') {
                        return;
                    }
                }
            }

        }
    }


    public void saveUserHistory(String fname) {

            File file = new File(fname);
    }
}
