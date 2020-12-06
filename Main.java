import java.util.LinkedHashSet;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        Random rand = new Random();
        LinkedHashSet<Integer> set = new LinkedHashSet<Integer>();
        BinaryTree tree = new BinaryTree();//обычное дерево
        //Создаем 1000 ключей и кладем их во множество
        for (int i = 0; i < 10; i++)
            set.add(rand.nextInt(100));
        //Проходимся по множеству и каждый его элемент вставляем в дерево двоичного поиска
        System.out.println("Elements added: ");
        for (Integer it : set) {
            System.out.print(it + " ");
            tree.insert(it);
        }
        System.out.println();
        tree.getInOrder();
        tree.makeSymmetricallyThreaded();
        tree.threadedInOrderTraverse();
        tree.threadedInOrderTraverseReverse();
        tree.insertToSymmetricTree(-1);
        tree.insertToSymmetricTree(-52);
        tree.insertToSymmetricTree(-250);
        tree.insertToSymmetricTree(120);
        tree.insertToSymmetricTree(170);
        tree.insertToSymmetricTree(130);
        tree.insertToSymmetricTree(150);
        tree.insertToSymmetricTree(190);
        tree.insertToSymmetricTree(410);
        tree.insertToSymmetricTree(-967);

        System.out.println("New elements added.");
        tree.threadedInOrderTraverse();
        tree.deleteFromSymmetricTree(99999);
        tree.deleteFromSymmetricTree(-99999);
        tree.deleteFromSymmetricTree(-99990);


        //уаляем первую половину того, что вставили
        int N = set.size() / 2;
        for (Integer it : set) {
            tree.deleteFromSymmetricTree(it);
            N--;
            if (N == 0) break;
        }

        //После удаления
        System.out.println("\nTree after removing half of the elements");
        tree.threadedInOrderTraverse();
    }
}