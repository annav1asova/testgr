import java.io.IOException;
import java.util.Scanner;

public class Trie {
    private int alphabetLength = 26;
    private Node root = new Node();

    private class Node {
        Node[] children = new Node[alphabetLength];
        String[] edgeLabel = new String[alphabetLength];
        boolean isLeaf = false; // вместо хранения $
        int amount;

        public void setToLeaf() {
            isLeaf = true;
            amount = 1;
        }

        public int increaseLeaf() {
            assert isLeaf;
            return ++amount;
        }
    }

    public int add(String name) {
        Node tmp = root;
        int i = 0;

        while (i < name.length() && tmp.edgeLabel[numLetter(name, i)] != null) {
            int index = numLetter(name, i);
            int j = 0;
            String label = tmp.edgeLabel[index];

            while (j < label.length() && i < name.length() && label.charAt(j) == name.charAt(i)) {
                i++;
                j++;
            }

            if (j == label.length()) { // закончилась метка на ребре, надо продолжать идти по след ребру
                tmp = tmp.children[index];
            } else if (i == name.length()) { // имя закончилось посреди ребра
                Node curChild = tmp.children[index];
                Node newChild = new Node();
                newChild.setToLeaf();
                String leftLabel = label.substring(j, label.length());
                tmp.children[index] = newChild;
                tmp.edgeLabel[index] = label.substring(0, j);
                newChild.children[numLetter(leftLabel, 0)] = curChild;
                newChild.edgeLabel[numLetter(leftLabel, 0)] = leftLabel;
                return newChild.amount;
            } else { // и имя, и метка продолжаются, но расходятся
                String leftLabel = label.substring(j, label.length());
                Node newChild = new Node();
                String leftName = name.substring(i, name.length());
                Node curChild = tmp.children[index];
                tmp.edgeLabel[index] = label.substring(0, j);
                tmp.children[index] = newChild;
                newChild.edgeLabel[numLetter(leftLabel, 0)] = leftLabel;
                newChild.children[numLetter(leftLabel, 0)] = curChild;
                newChild.edgeLabel[numLetter(leftName, 0)] = leftName;
                newChild.children[numLetter(leftName, 0)] = new Node();
                newChild.children[numLetter(leftName, 0)].setToLeaf();
                return newChild.children[numLetter(leftName, 0)].amount;
            }
        }

        if (i < name.length()) {
            tmp.edgeLabel[numLetter(name, i)] = name.substring(i, name.length());
            tmp.children[numLetter(name, i)] = new Node();
            tmp.children[numLetter(name, i)].setToLeaf();
            return tmp.children[numLetter(name, i)].amount;
        }

        tmp.isLeaf = true;
        tmp.increaseLeaf();
        return tmp.amount;
    }
    
    private int numLetter(String name, int i) {
        return name.charAt(i) - 'a';
    }


    public static void main(String[] args) throws IOException {
        new Trie().solve();
    }

    public void solve() throws IOException {
        Scanner in = new Scanner(System.in);
        Trie trie = new Trie();
        int n = in.nextInt();
        for (int i = 0; i < n; i++) {
            String name = in.next();
            int res = trie.add(name);
            if (res > 1)
                System.out.println("you are registered as " + name + (res - 1));
            else
                System.out.println("OK, your nick " + name);
        }
    }

}