import java.io.IOException;
import java.util.*;

public class Order {
    private int alphabetLength = 26;

    public static void main(String[] args) throws IOException {
        new Order().solve();
    }

    public void solve() throws IOException {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        String firstName = sc.next();
        ArrayList<Pair<Integer, Integer>> pairs = new ArrayList<>();
        for (int i = 1; i < n; i++) {
            String secondName = sc.next();
            Pair pair = findPair(firstName, secondName);
            if (pair != null)
                pairs.add(pair);

            firstName = secondName;
        }

        LinkedList<Pair<Integer, TreeSet<Integer>>> precede = new LinkedList<>();
        ArrayList<TreeSet<Integer>> list = getPrecede(pairs);
        for (int i = 0; i < list.size(); i++) {
            precede.add(new Pair(i, list.get(i)));
        }
        String alphabet = getAlphabet(precede);
        if (alphabet == null) {
            System.out.println("Impossible");
        } else {
            System.out.println(alphabet);
        }

    }

    private Pair findPair(String firstName, String secondName) {
        int i = 0;
        while (i < firstName.length() && i < secondName.length()) {
            if (firstName.charAt(i) != secondName.charAt(i)) {
                return new Pair((int)firstName.charAt(i) - 'a', (int)secondName.charAt(i) - 'a');
            }
            i++;
        }
        return null;
    }


    private ArrayList<TreeSet<Integer>> getPrecede(ArrayList<Pair<Integer, Integer>> pairs) {
        ArrayList<TreeSet<Integer>> precede = new ArrayList<>();
        for (int i = 0; i < alphabetLength; i++) {
            precede.add(new TreeSet<>());
        }

        for (Pair pair: pairs) {
            int a = (int)pair.a;
            int b = (int)pair.b;
            TreeSet<Integer> updateTree = precede.get(b);
            updateTree.add(a);
            updateTree.addAll(precede.get(a));
            precede.set(b, updateTree);
        }
        return precede;
    }

    private String getAlphabet(LinkedList<Pair<Integer, TreeSet<Integer>>> precede) {
        StringBuilder alphabet = new StringBuilder();
        boolean flag = true;
        while (flag) {
            flag = false;
            Iterator<Pair<Integer, TreeSet<Integer>>> iter = precede.iterator();
            TreeSet<Integer> curEmpty = new TreeSet<>();
            while (iter.hasNext()) {
                Pair<Integer, TreeSet<Integer>> pair = iter.next();
                int i = pair.a;
                TreeSet<Integer> setOfPrecede = pair.b;

                if (setOfPrecede.isEmpty()) {
                    alphabet.append((char) (i + 'a'));
                    curEmpty.add(i);
                    iter.remove();
                    flag = true;
                }
            }

            for (Integer i: curEmpty) {
                for (Pair<Integer, TreeSet<Integer>> pair: precede) {
                    pair.b.remove(i);
                }
            }
        }
        if (alphabet.length() < alphabetLength)
            return null;

        return alphabet.toString();
    }

    private class Pair<F, S> {
        F a;
        S b;

        public Pair (F a, S b) {
            this.a = a;
            this.b = b;
        }
    }
}
