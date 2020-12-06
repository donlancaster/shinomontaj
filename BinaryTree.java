class BinaryTree {
    private Node root;
    private Node head;
    private final boolean done;


    public BinaryTree(){
        root = null;
        done = false;
        head = null;
    }

    public void insert(int data){
        if (done){
            System.out.println("Эта версия вставки более не поддерживается");
        } else {
            Node newNode = new Node(data);
            if (root == null) {
                root = newNode;
            } else {
                Node curr = root, prev;
                while (true) {
                    prev = curr;
                    if (data < curr.data) {
                        curr = curr.left;
                        if (curr == null) {
                            prev.left = newNode;
                            return;
                        }
                    } else {
                        curr = curr.right;
                        if (curr == null) {
                            prev.right = newNode;
                            return;
                        }
                    }
                }
            }
        }
    }

    //удаление
    public void deleteFromSymmetricTree(int data){
        Node prev = null;
        Node curr  = root;
        boolean found = false;

        while (curr != null) {
            if (data == curr.data) {
                found = true;
                break;
            }
            prev = curr;
            if (data < curr.data) {
                if (curr.lIsNotThread)
                    curr = curr.left;
                else
                    break;
            }
            else {
                if (curr.rIsNotThread)
                    curr = curr.right;
                else
                    break;
            }
        }

        if (!found)
            System.out.println("No such element");
        else if (curr.lIsNotThread && curr.rIsNotThread)
            deleteNodeWithTwoChildren(prev, curr);
        else if (curr.lIsNotThread)
            deleteNodeWithOneChild(prev, curr);
        else if (curr.rIsNotThread)
            deleteNodeWithOneChild(prev, curr);
        else
            deleteLeaf(prev, curr);
    }

    private void deleteLeaf(Node prev, Node curr){
        if (prev == null)
            root = null;
        else if (curr == prev.left) {
            prev.lIsNotThread = false;
            prev.left = curr.left;
        } else {
            prev.rIsNotThread = false;
            prev.right = curr.right;
        }
    }

    private void deleteNodeWithOneChild(Node prev, Node curr){
        Node child;

        if (curr.lIsNotThread)
            child = curr.left;
        else
            child = curr.right;

        if (prev == null)
            root = child;
        else if (curr == prev.left)
            prev.left = child;
        else
            prev.right = child;

        Node s = inSuccessor(curr);                                                         //находим преемника (узел со следующим бОльшим ключом)
        Node p = inPredecessor(curr);                                                       //находим предшественника (узел со следующим мЕньшим ключом)

        if (curr.lIsNotThread)                                                              //меняем ссылки
            p.right = s;
        else {
            if (curr.rIsNotThread)
                s.left = p;
        }
    }

    private void deleteNodeWithTwoChildren(Node prev, Node curr){
        Node prevSucc = curr;                                                               // предыдущий
        Node succ = curr.right;                                                             // текущий

        while (succ.lIsNotThread) {                                                         //пока у преемника есть ссылка на
            prevSucc = succ;                                                                //левое поддерево, идём влево
            succ = succ.left;
        }
        curr.data = succ.data;                                                              //заменяем ключ
        if (!succ.lIsNotThread && !succ.rIsNotThread)                                       //Преемник - лист. Удаляем его как лист
            deleteLeaf(prevSucc, succ);
        else
            deleteNodeWithOneChild(prevSucc, succ);                                         //у преемника есть хоть один потомок (другого не дано). Удаляем надлежащим образом
    }

    private Node inSuccessor(Node ptr) {                                                    //Метод для поиска преемника
        if (!ptr.rIsNotThread)                                                              //Правая ссылка - это нить. Возвращаем её сразу
            return ptr.right;

        ptr = ptr.right;                                                                    //Ищем подходящую ссылку
        while (ptr.lIsNotThread)
            ptr = ptr.left;
        return ptr;
    }

    private Node inPredecessor(Node ptr) {                                                  //Метод для поиска предшественника
        if (!ptr.lIsNotThread)                                                              //Левая ссылка - это нить. Возвращаем её сразу
            return ptr.left;

        ptr = ptr.left;                                                                     //Ищем подходящую ссылку
        while (ptr.rIsNotThread)
            ptr = ptr.right;
        return ptr;
    }

   //вставка в симметричное дерево
    public void insertToSymmetricTree(int data) {
        Node curr = root;
        Node prev = null;
        while (curr != null) {
            prev = curr;
            if (data < curr.data) {
                if (curr.lIsNotThread)
                    curr = curr.left;
                else
                    break;
            } else {
                if (curr.rIsNotThread)
                    curr = curr.right;
                else
                    break;
            }
        }

        Node newNode = new Node(data);
        newNode.lIsNotThread = false;
        newNode.rIsNotThread = false;

        if (prev == null) {
            root = newNode;
            newNode.left = null;
            newNode.right = null;
        } else if (data < (prev.data)) {
            newNode.left = prev.left;
            newNode.right = prev;
            prev.lIsNotThread = true;
            prev.left = newNode;
        } else {
            newNode.left = prev;
            newNode.right = prev.right;
            prev.rIsNotThread = true;
            prev.right = newNode;
        }
    }

  //метод прошивки
    Node y;
    public void makeSymmetricallyThreaded(){
        if (done){
            System.out.println("tree is already threaded");
        } else {
            head = new Node(-1);
            head.left  = root;
            head.right = head;

            y = head;

            recursiveSymmetricalThreadingR(root);         //правосторонняя
            y.right = head;
            y.rIsNotThread = false;                         //самый крайний правый лист должен ссылаться на голову

            y = head;

            recursiveSymmetricalThreadingL(root);           //левосторонняя
            y.left = head;
            y.lIsNotThread = false;                         //самый крайний левый лист должен ссылаться на голову
        }
    }

    private void recursiveSymmetricalThreadingR(Node x){
        if (x != null){
            recursiveSymmetricalThreadingR(x.left);
            doSymmetricalThreadingR(x);
            recursiveSymmetricalThreadingR(x.right);
        }
    }

    private void doSymmetricalThreadingR(Node p){
        if (y != null){
            if (y.right == null){
                y.rIsNotThread = false;
                y.right = p;
            } else {
                y.rIsNotThread = true;
            }
        }
        y = p;
    }

    private void recursiveSymmetricalThreadingL(Node x){
        if (x != null){
            if (x.rIsNotThread) recursiveSymmetricalThreadingL(x.right);
            doSymmetricalThreadingL(x);
            recursiveSymmetricalThreadingL(x.left);
        }
    }

    private void doSymmetricalThreadingL(Node p){
        if (y != null){
            if (y.left == null){
                y.lIsNotThread = false;
                y.left = p;
            } else {
                y.lIsNotThread = true;
            }
        }
        y = p;
    }

    //обходы
    public void threadedInOrderTraverse(){
        Node curr = root;
        if (root == null) { System.out.println("Tree is empty"); return; }
        System.out.println("Traversed inorder:");
        while (curr != head){
            while (curr.left != null && curr.lIsNotThread) curr = curr.left;
            curr.show();
            while (!curr.rIsNotThread && curr.right != null) {
                curr = curr.right;
                if (curr == head) {
                    System.out.println();
                    return;
                }
                curr.show();
            }
            curr = curr.right;
        }
        System.out.println();
    }

    public void threadedInOrderTraverseReverse(){
        Node curr = root;
        if (root == null) { System.out.println("Tree is empty"); return; }
        System.out.println("Traversed inorder (descending):");
        while (curr != head){
            while (curr.right != null && curr.rIsNotThread) curr = curr.right;
            curr.show();
            while (!curr.lIsNotThread) {
                curr = curr.left;
                if (curr == head) {
                    System.out.println();
                    return;
                }
                curr.show();
            }
            curr = curr.left;
        }
        System.out.println();
    }

//поиск
    public Node find(int x){
        Node curr = root;
        while (curr.data != x){
            if (x < curr.data)
                curr = curr.left;
            else
                curr = curr.right;
            if (curr == null) return null;
        }
        return curr;
    }

    public void getInOrder(){
        if (root == null) { System.out.println("Tree is empty"); return; }
        System.out.println("Inorder:");
        inOrder(root);
        System.out.println();
    }

    public void getPreOrder(){
        if (root == null) { System.out.println("Tree is empty"); return; }
        System.out.println("Preorder:");
        preOrder(root);
        System.out.println();
    }

    public void getPostOrder(){
        if (root == null) { System.out.println("Tree is empty"); return; }
        System.out.println("Postorder:");
        postOrder(root);
        System.out.println();
    }

    private void inOrder(Node root){
        if (root != null){
            inOrder(root.left);
            root.show();
            inOrder(root.right);
        }
    }

    private void preOrder(Node root){
        if (root != null){
            root.show();
            preOrder(root.left);
            preOrder(root.right);
        }
    }

    private void postOrder(Node root){
        if (root != null){
            postOrder(root.left);
            postOrder(root.right);
            root.show();
        }
    }

    //вспомогательный метод для поиска преемника узлу delNode.
    //преемник - либо (1) правый потомок delNode
    //           либо (2) "самый левый" потомок правого потомка delNode
    private Node getSuccessor(Node delNode) {
        //Становимся в правое поддерево удаляемого узла
        Node successorParent = delNode;
        Node successor = delNode;
        Node current = delNode.right;
        //Находим "самого левого"
        while(current != null) {
            successorParent = successor;
            successor = current;
            current = current.left;
        }
        //Если "самый левый" не является (1), а является (2), то
        //правый потомком преемника должно стать правое поддерево delNode
        if(successor != delNode.right) {
            successorParent.left = successor.right;
            successor.right = delNode.right;
        }
        return successor;
    }

    //удаление некоторого ключа из дерева.
    public boolean delete(int key) {
        Node current = root;
        Node parent = root;
        //Пока предполагаем, что удаляемый узел будет являться
        //левым потомком узла-родителя
        boolean isLeftChild = true;
        //Цикл аналогичен циклу в методе find(int x)
        //Только по пути запоминаем родителя
        //и обновляем, является ли найденный удаляемый узел левым
        // потомком либо нет
        while (current.data != key) {
            parent = current;
            if (key < current.data) {
                isLeftChild = true;
                current = current.left;
            } else {
                isLeftChild = false;
                current = current.right;
            }

            if (current == null)
                return false;
        }
        //Удаляемый узел все-таки найден.

        //Рассматриваем случай, когда удаляемый узел - лист
        if (current.left == null &&
                current.right == null) {
            if (current == root)
                root = null;                //Удаляемый узел - корень. Просто обнуляем его
            else if (isLeftChild)
                parent.left = null;         //Удаляемый узел - левый потомок своего родителя. Обнуляем левую ссылку родителя
            else
                parent.right = null;        //Удаляемый узел - правый потомок своего родителя. Обнуляем правую ссылку родителя
        }
        else
            //Рассматриваем случай, когда у удаляемого узла есть только левый потомок
            if (current.right == null) {
                if (current == root)
                    root = current.left;       //Удаляемый узел - корень. Правого поддерева нет. Просто перезапоминаем корень.
                else
                if (isLeftChild)          //Удаляемый узел - не корень. При этом удаляемый узел - левый потомок. Правого поддерева нет.
                    parent.left = current.left;         //Исключаем удаляемый узел из цепочки. Теперь родитель будет ссылаться на нового левого потомка.
                else
                    parent.right = current.left;        //Исключаем удаляемый узел из цепочки. Теперь родитель будет ссылаться на нового правого потомка.
            }
            else
                //Рассматриваем аналогичный случай, когда у удаляемого узла есть только правый потомок
                if (current.left == null) {
                    if (current == root)
                        root = current.right;
                    else
                    if (isLeftChild)
                        parent.left = current.right;
                    else
                        parent.right = current.right;
                }
                else
                //Рассматриваем случай, когда у удаляемого узла есть и левый, и правфй потомок
                {
                    //Находим преемника. Преемник - это узел со следующим по величине ключом.
                    Node successor = getSuccessor(current);
                    if (current == root)
                        root = successor;               //Если удаляемый узел - корень, то перезапоминаем его
                    else if (isLeftChild)
                        parent.left = successor;        //Удаляемый узел - левый потомок. Заменяем его преемником.
                    else
                        parent.right = successor;       //Аналогично в случае правого потомка.

                    successor.left = current.left;      //Переносим левое поддерево удаляемого узла.
                }
        return true;
    }
}