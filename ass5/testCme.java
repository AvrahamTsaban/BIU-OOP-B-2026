import java.util.ArrayList;

public class testCme {
    static ArrayList<String> sprites = new ArrayList<>();
    public static void main(String[] args) {
        sprites.add("A");
        sprites.add("B");
        ArrayList<String> copy = new ArrayList<>(sprites);
        for (String s : copy) {
            System.out.println(s);
            sprites.remove(s);
        }
    }
}
