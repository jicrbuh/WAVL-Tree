/**
 * WAVLTree
 * <p>
 * An implementation of a WAVL Tree with
 * distinct integer keys and info
 * Submitted by:
 * Oren Gal – 302378633 – orengal1
 * Chen Barnoy – 308547108 - chenbarnoy
 */

public class WAVLTree {
    private IWAVLNode root;
    private IWAVLNode min;
    private IWAVLNode max;
    private int size;

    //constructors (WAVLTree)
    public WAVLTree(IWAVLNode root) {
        this.root = root;
        this.min = root;
        this.max = root;
        this.size = 1;
    }

    public WAVLTree() {
        this.root = null;
        this.min = null;
        this.max = null;
        this.size = 0;
    }

    /**
     * public boolean empty()
     * <p>
     * returns true if and only if the tree is empty
     * <p>
     * Complexity: O(1)
     */
    public boolean empty() {
        WAVLNode castedRoot = (WAVLNode) root;
        return castedRoot.getSubtreeSize() == 0; // to be replaced by student code
    }

    /**
     * public String search(int k)
     * <p>
     * returns the info of an item with key k if it exists in the tree
     * otherwise, returns null
     * <p>
     * Complexity O(logn)
     */
    public String search(int k) {
        return treeSearch(k, this.root);
    }

    /**
     * public String treeSearch(int k, IWAVLNode currNode)
     * <p>
     * returns the info of an item with key k if it exists in the
     * sub-tree of currNode. otherwise, returns null
     * <p>
     * Complexity: O(logn)
     */
    public String treeSearch(int k, IWAVLNode currNode) {
        if (!currNode.isRealNode()) {
            return null;
        }
        if (currNode.getKey() == k) {
            return currNode.getValue();
        } else if (currNode.getKey() < k) {
            return treeSearch(k, currNode.getRight());
        } else {
            return treeSearch(k, currNode.getLeft());
        }
    }

    /**
     * public int getLeftRankDiff(WAVLNode currNode)
     * <p>
     * returns the difference in ranks between currNode and its left
     * child
     * <p>
     * Complexity: O(1)
     */
    public int getLeftRankDiff(WAVLNode currNode) {
        if (currNode.isRealNode()) {
            return currNode.getRank() - currNode.getLeft().getRank();
        } else {
            return 1;
        }
    }

    /**
     * public int getRightRankDiff(WAVLNode currNode)
     * <p>
     * returns the difference in ranks between currNode and its right
     * child
     * <p>
     * Complexity: O(1)
     */
    public int getRightRankDiff(WAVLNode currNode) {
        if (currNode.isRealNode()) {
            return currNode.getRank() - currNode.getRight().getRank();
        } else {
            return 1;
        }
    }

    /**
     * public WAVLNode rightRotate(WAVLNode currNode)
     * <p>
     * in a right rotations, currnode becomes the right child of his left (former) child and is demoted
     * and currNode's parent should be the parent of leftChild
     * <p>
     * Complexity: O(1)
     */
    public WAVLNode rightRotate(WAVLNode currNode) {
        WAVLNode leftChild = currNode.getLeft(); // Keep left child
        WAVLNode currNodeParent = currNode.getParent();//keep the parent
        int leftOrRight = currNode.relationWithParent();
        //update sizes
        rightUpdateSizes(currNode);
        // Change the right child of the leftChild to be mynode's child.
        currNode.setLeft(leftChild.getRight());
        if (leftChild.hasRightChild()) {
            leftChild.getRight().setParent(currNode);
        }
        // changing the ranks
        currNode.setRank(currNode.getRank() - 1);//demote currnode

        // Changing leftChild's parent to be mynode's parent
        leftChild.setParent(currNode.getParent());

        // Changing currNode to be the right child of leftChild
        leftChild.setRight(currNode);

        // currnode becomes the right child of his left (former) child
        currNode.setParent(leftChild);
        //update so that currNode parent will point to leftChild
        if (leftOrRight == 0) {
            currNodeParent.setLeft(leftChild);
        } else if (leftOrRight == 1) {
            currNodeParent.setRight(leftChild);
        }

        //if currNode was the root, change the tree's root to it's leftCHild
        if (currNode == (WAVLNode) root) {
            this.root = leftChild;
        }

        return leftChild;
    }

    /**
     * public void rightUpdateSizes(WAVLNode currNode)
     * <p>
     * updates the sizes of the left subtree of currNode and of currNode
     * <p>
     * Complexity: O(1)
     */
    public void rightUpdateSizes(WAVLNode currNode) {
        if (!currNode.getLeft().isRealNode() || !currNode.isRealNode()) {
            return;
        }
        WAVLNode LeftChild = currNode.getLeft();
        currNode.setSize(currNode.getLeft().getRight().getSubtreeSize() + currNode.getRight().getSubtreeSize() + 1);
        LeftChild.setSize(LeftChild.getLeft().getSubtreeSize() + currNode.getSubtreeSize() + 1);
    }

    /**
     * public void leftUpdateSizes(WAVLNode currNode)
     * <p>
     * updates the sizes of the right subtree of currNode and of currNode
     * <p>
     * Complexity: O(1)
     */
    public void leftUpdateSizes(WAVLNode currNode) {
        if (!currNode.getLeft().isRealNode() || !currNode.isRealNode()) {
            return;
        }
        WAVLNode rightChild = currNode.getRight();
        currNode.setSize(currNode.getRight().getLeft().getSubtreeSize() + currNode.getLeft().getSubtreeSize() + 1);
        rightChild.setSize(rightChild.getRight().getSubtreeSize() + currNode.getSubtreeSize() + 1);
    }

    /**
     * public WAVLNode rightDoubleRotate(WAVLNode currNode)
     * <p>
     * returns the new root of the sub-tree after the rotations.
     * b in slide 27 in the WAVL slideshow
     * <p>
     * Complexity: O(1)
     */
    public WAVLNode rightDoubleRotate(WAVLNode currNode) {
        WAVLNode toPromote = currNode.getRight().getLeft();
        rightRotate(currNode.getRight());
        promotion(toPromote);
        leftRotate(currNode);
        //demotion(currNode);
        //if currNode was the root, replace it with toPromote
        if (currNode == (WAVLNode) root) {
            this.root = toPromote;
        }
        return toPromote;
    }

    /*
     * public WAVLNode leftRotate(WAVLNode currNode)
     *
     * in a left rotation, currnode becomes the left child of its right (former) child and is demoted
     * and currNode's parent should be the parent of RightChild
     *
     * Complexity: O(1)
     */
    public WAVLNode leftRotate(WAVLNode currNode) {
        WAVLNode RightChild = currNode.getRight(); // save right child
        //update tree sizes
        leftUpdateSizes(currNode);
        // Change the right child of the leftChild to be mynode's child.
        currNode.setRight(RightChild.getLeft());
        WAVLNode currNodeParent = currNode.getParent();//keep the parent
        int leftOrRight = currNode.relationWithParent();
        if (RightChild.hasLeftChild()) {
            RightChild.getLeft().setParent(currNode);
        }

        // changing the ranks
        currNode.setRank(currNode.getRank() - 1);//demote currnode

        // Changing leftChild's parent to be mynode's parent
        RightChild.setParent(currNode.getParent());

        // Changing currNode to be the Left child of RightChild
        RightChild.setLeft(currNode);

        // currnode becomes the right child of his left (former) child
        currNode.setParent(RightChild);
        //update so that currNode parent will point to leftChild
        if (leftOrRight == 0) {
            currNodeParent.setLeft(RightChild);
        } else if (leftOrRight == 1) {
            currNodeParent.setRight(RightChild);
        }
        //if currNode was the root, change the tree's root to it's leftCHild
        if (currNode == (WAVLNode) root) {
            this.root = RightChild;
        }
        return RightChild;
    }

    /**
     * public WAVLNode rightDoubleRotate(WAVLNode currNode)
     * <p>
     * returns the new root of the sub-tree after the rotations.
     * b in slide 27 in the WAVL slideshow
     * <p>
     * Complexity: O(1)
     */
    public WAVLNode leftDoubleRotate(WAVLNode currNode) {
        WAVLNode toPromote = currNode.getLeft().getRight();
        leftRotate(currNode.getLeft());
        promotion(toPromote);
        rightRotate(currNode);
        //if currNode was the root, replace it with toPromote
        if (currNode == (WAVLNode) root) {
            this.root = toPromote;
        }
        return toPromote;
    }

    /**
     * public void demotion(WAVLNode mynode)
     * <p>
     * Decreases the rank of mynode by 1
     * <p>
     * Complexity: O(1)
     */
    public void demotion(WAVLNode mynode) {
        mynode.decreaseRank();
    }

    /**
     * public void promotion(WAVLNode mynode)
     * <p>
     * increases the rank of mynode by 1
     * <p>
     * Complexity: O(1)
     */
    public void promotion(WAVLNode mynode) {
        mynode.increaseRank();
    }

    /**
     * public int checkInsertCase(WAVLNode mynode)
     * <p>
     * checks if current sub-tree is balanced after the insertion and returns which case is needed
     *
     * @pre - mynode != null
     * <p>
     * Complexity: O(1)
     **/
    public int checkInsertCase(WAVLNode mynode) {
        //0 if there's no problem
        //1 if (0,1) or (1,0)
        //2 if currNode is (0,2) and their left child is (1,2)
        //3 if currNode is (0,2) and their left child is (2,1)
        //4 if currNode is (2,0) and their right child is (1,2)
        //5 if currNode is (2,0) and their right child is (2,1)
        WAVLNode myLeft = mynode.getLeft();
        WAVLNode myRight = mynode.getRight();
        //if none of them == 0 then return there's no problem
        if ((getLeftRankDiff(mynode) != 0) && (getRightRankDiff(mynode) != 0)) {
            return 0;
        }
        //one of them == 0. if the other one == 1 then (0,1) or (1,0)
        else if (getRightRankDiff(mynode) == 1 || getLeftRankDiff(mynode) == 1) {
            return 1;
        }
        //if (0,2) check the left child ("myLeft")
        else if (getRightRankDiff(mynode) == 2) {
            if (getLeftRankDiff(myLeft) == 1) {
                return 2;
            } else {
                return 3;
            }
        }
//if (2,0) check the right child ("myRight")
        else if (getLeftRankDiff(mynode) == 2) {
            if (getLeftRankDiff(myRight) == 1) {
                return 4;
            } else {
                return 5;
            }
        }
        return -1;
    }

    /**
     * public int insert(int k, String i)
     * <p>
     * inserts an item with key k and info i to the WAVL tree.
     * the tree must remain valid (keep its invariants).
     * returns the number of rebalancing operations, or 0 if no rebalancing operations were necessary.
     * returns -1 if an item with key k already exists in the tree.
     * <p>
     * Complexity: O(logn)
     */
    public int insert(int k, String i) {

        WAVLNode newNode = new WAVLNode(k, i);
        newNode.addVirtualNodes();
        WAVLNode castedRoot = (WAVLNode) root;
        //if the tree is empty add new node as a root
        if (this.size() == 0) {
            this.root = newNode;
            this.min = newNode;
            this.max = newNode;
            this.size++;
            return 0;
        }
        //if the key already exists, return -1
        else {
            //inserts the node in the correct position
            int isfound = treeInsert(castedRoot, newNode);
            if (isfound == -1) {
                return -1;
            }
        }
        if (min.getKey() > newNode.getKey()) {
            this.min = newNode;
        }
        if (max.getKey() < newNode.getKey()) {
            this.max = newNode;
        }

        //changes sizes of subtrees
        int resizesNum = adjustSizes(newNode);
        //System.out.println("the number of nodes that increased their size is: " + resizesNum);
        //rebalancing
        WAVLNode currNode = newNode.getParent();
        int j = checkInsertCase(currNode);
        int corrections = 0;
        while (j > 0 && (currNode != null)) {
            switch (j) {
                //to-do update the root after rotations
                //1 if (0,1) or (1,0), promote  node and go to parent
                case 1:
                    promotion(currNode);
                    currNode = currNode.getParent();
                    corrections++;
                    break;

                //2 if currNode is (0,2) and their left child is (1,2)
                case 2:
                    currNode = rightRotate(currNode);
                    corrections = corrections + 2; //rotation+demotion
                    currNode = currNode.getParent();
                    break;

                //3 if currNode is (0,2) and their left child is (2,1)
                case 3:
                    currNode = leftDoubleRotate(currNode);
                    corrections = corrections + 5; // 2 rotations+ 2 demotion + 1 promotion
                    break;

                //4 if currNode is (2,0) and their right child is (1,2)
                case 4:
                    currNode = rightDoubleRotate(currNode);
                    corrections = corrections + 5; //2 rotations+ 2 demotion + 1 promotion
                    break;

                //5 if currNode is (2,0) and their right child is (2,1)
                case 5:
                    currNode = leftRotate(currNode);
                    corrections = corrections + 2; //rotation+demotion
                    currNode = currNode.getParent();
                    break;
            }
            if (currNode != null) {
                j = checkInsertCase(currNode);
            }


        }
        //increase sizes of nodes to the root
        this.size++;
        castedRoot = (WAVLNode) root;

        //should return the numbers of changes to the tree
        return corrections;
    }

    /**
     * public int adjustSizes(WAVLNode newNode)
     * <p>
     * adds 1 to the rank of each node on the path from the new added
     * node to the root of the tree. returns the number of nodes updated.
     * <p>
     * Complexity: O(logn)
     */
    public int adjustSizes(WAVLNode newNode) {
        WAVLNode currNode = newNode;
        int numOfIncreases = 0;
        WAVLNode castedRoot = (WAVLNode) root;
        castedRoot.increaseSize();
        while (currNode.getKey() != castedRoot.getKey()) {
            currNode.increaseSize();
            currNode = currNode.getParent();

        }
        return numOfIncreases;
    }

    /**
     * public int treeInsert(WAVLNode currNode,WAVLNode newNode)
     * <p>
     * Inserts newNode as a child of currNode
     * <p>
     * Complexity: O(logn)
     */
    public int treeInsert(WAVLNode currNode, WAVLNode newNode) {
        WAVLNode position = treePosition(currNode, newNode.getKey());
        if (newNode.getKey() == position.getKey()) {
            return -1;
        }
        newNode.setParent(position);
        if (newNode.getKey() < position.getKey()) {
            position.setLeft(newNode);
        } else {
            position.setRight(newNode);
        }
        return 0;
    }

    /**
     * public WAVLNode treePosition(WAVLNode currNode, int key)
     * <p>
     * Returns the position to insert currNode
     * <p>
     * Complexity: O(logn)
     */
    public WAVLNode treePosition(WAVLNode currNode, int key) {
        WAVLNode position = currNode;
        while (currNode.isRealNode()) {
            position = currNode;
            if (key == currNode.getKey()) {
                return currNode;
            } else if (key < currNode.getKey()) {
                currNode = currNode.getLeft();
            } else {
                currNode = currNode.getRight();
            }
        }
        return position;
    }

    /**
     * public int delete(int k)
     * <p>
     * deletes an item with key k from the binary tree, if it is there;
     * the tree must remain valid (keep its invariants).
     * returns the number of rebalancing operations, or 0 if no rebalancing operations were needed.
     * returns -1 if an item with key k was not found in the tree.
     * <p>
     * Complexity: O(logn)
     */
    public int delete(int k) {
        if (root == null) {
            return -1;
        }
        WAVLNode node = findKey((WAVLNode) root, k);
        if (node == null) {
            return -1;
        }
        if (k == min.getKey()) {
            min = successor(node);
        }
        if (k == max.getKey()) {
            max = predecessor(node);
        }
        WAVLNode nodeToBalance = null;
        if (size() == 1) {
            root = null;
            size--;
            return 0;
        }
        if (!node.hasParent()) {
            // If node is root, replace its fields with one of its children's fields
            // (by this stage it must have at least one) and continue the delete process with the chosen child.
            if (node.hasLeftChild()) {
                node.setKey(node.getLeft().getKey());
                node.setInfo(node.getLeft().getValue());
                node = node.getLeft();
            } else {
                node.setKey(node.getRight().getKey());
                node.setInfo(node.getRight().getValue());
                node = node.getRight();
            }
        }
        if (node.hasLeftChild() && node.hasRightChild()) {
            // If node has two children, replace its fields with its predecessor's/successor's fields
            // And continue the delete process with the predecessor/successor (respectively).
            if (node.getKey() < node.getParent().getKey()) {
                WAVLNode predecessorNode = predecessor(node);
                node.setKey(predecessorNode.getKey());
                node.setInfo(predecessorNode.getValue());
                node = predecessorNode;
            } else {
                WAVLNode successorNode = successor(node);
                node.setKey(successorNode.getKey());
                node.setInfo(successorNode.getValue());
                node = successorNode;
            }
        }
        if (!node.left.isRealNode() && !node.right.isRealNode()) {
            // If node is a leaf, just replace with a virtual node.
            nodeToBalance = node.getParent();
            if (node.isLeftChild()) {
                nodeToBalance.setLeft(null);
            } else {
                nodeToBalance.setRight(null);
            }
            nodeToBalance.addVirtualNodes();
        } else {
            // If node has exactly one child, delete the node and connect its parent to its child
            nodeToBalance = node.getParent();
            if (node.hasLeftChild()) {
                node.getLeft().setParent(node.getParent());
                if (node.isLeftChild()) {
                    nodeToBalance.setLeft(node.getLeft());
                } else {
                    nodeToBalance.setRight(node.getLeft());
                }
            } else {
                node.getRight().setParent(node.getParent());
                if (node.isLeftChild()) {
                    nodeToBalance.setLeft(node.getRight());
                } else {
                    nodeToBalance.setRight(node.getRight());
                }
            }
        }
        size--;

        //Re-balance stage
        int rebalanceOperations = 0;
        node = nodeToBalance;
        int deleteCase = deletionCase(node);
        while (deleteCase != 0) {
            switch (deleteCase) {
                case 1:
                    demotion(node);
                    node = node.getParent();
                    rebalanceOperations++;
                    break;
                case 2:
                    demotion(node);
                    demotion(node.getRight());
                    node = node.getParent();
                    rebalanceOperations += 2;
                    break;
                case 3:
                    demotion(node);
                    demotion(node.getLeft());
                    node = node.getParent();
                    rebalanceOperations += 2;
                    break;
                case 4:
                    promotion(leftRotate(node));
                    rebalanceOperations++;
                    break;
                case 5:
                    promotion(rightRotate(node));
                    rebalanceOperations++;
                    break;
                case 6:
                    promotion(rightRotate(node.getRight()));
                    promotion(leftRotate(node));
                    rebalanceOperations += 2;
                    break;
                case 7:
                    promotion(leftRotate(node.getLeft()));
                    promotion(rightRotate(node));
                    rebalanceOperations += 2;
                    break;
            }
            deleteCase = deletionCase(node);
        }
        // If a rotation moved the root away from the top, fix it.
        if (((WAVLNode) root).isLeftChild() || ((WAVLNode) root).isRightChild()) {
            root = ((WAVLNode) root).getParent();
        }
        return rebalanceOperations;
    }

    /**
     * private static WAVLNode findKey(WAVLNode node, int k)
     * <p>
     * #pre node != null
     * #post Returns the node containing the key k or null if no such node exists, starting with node.
     * <p>
     * Complexity O(logn)
     */
    private static WAVLNode findKey(WAVLNode node, int k) {
        if (node.getKey() == k) {
            return node;
        } else if (k < node.getKey() && node.hasLeftChild()) {
            return findKey(node.getLeft(), k);
        } else if (k > node.getKey() && node.hasRightChild()) {
            return findKey(node.getRight(), k);
        } else {
            return null;
        }
    }

    /**
     * private static WAVLNode predecessor(WAVLNode node)
     * #pre node != null
     * #post Returns the node with the largest key which is smaller then node.getKey().
     * #post Returns null if no such node exists (i.e This node is the minimum of the tree).
     * <p>
     * Complexity: O(logn)
     */
    private static WAVLNode predecessor(WAVLNode node) {
        if (!node.getLeft().isRealNode()) {
            boolean hasParent = node.getParent() != null;
            boolean isRightChild = node.isRightChild();
            if (hasParent && isRightChild) {
                return node.getParent();
            } else if (hasParent) {
                while (node.isLeftChild()) {
                    node = node.getParent();
                }
                return node.getParent();
            } else {
                return null;
            }
        } else {
            node = node.getLeft();
            while (node.hasRightChild()) {
                node = node.getRight();
            }
            return node;
        }
    }

    /**
     * private int deletionCase(WAVLNode node)
     * <p>
     * #post Returns the deletion case of the node
     * 0   - No further changes should be made.
     * 1   - Demotion required.
     * 2/3 - Double demotion required.
     * 4/5 - Rotation required. left/right
     * 6/7 - Double rotation required, right-left/left-right
     * <p>
     * Complexity O(1)
     */
    private int deletionCase(WAVLNode node) {
        if (node == null) return 0;
        boolean leftSideIncorrect = getLeftRankDiff(node) == 3;
        boolean rightSideIncorrect = getRightRankDiff(node) == 3;
        if (!node.getLeft().isRealNode() && !node.getRight().isRealNode() && getLeftRankDiff(node) == 2 && getRightRankDiff(node) == 2) {
            return 1;
        }
        if (!(leftSideIncorrect || rightSideIncorrect)) {
            return 0; // Everything is correct
        }
        // At this point we know there is a problem.
        if (getLeftRankDiff(node) == 2 || getRightRankDiff(node) == 2) {
            return 1;
        }
        if (leftSideIncorrect) {
            if (getLeftRankDiff(node.getRight()) == 2 && getRightRankDiff(node.getRight()) == 2) {
                return 2;
            } else if (getRightRankDiff(node.getRight()) == 1) {
                return 4;
            } else {
                return 6;
            }
        } else {
            if (getRightRankDiff(node.getLeft()) == 2 && getLeftRankDiff(node.getLeft()) == 2) {
                return 3;
            } else if (getLeftRankDiff(node.getLeft()) == 1) {
                return 5;
            } else {
                return 7;
            }
        }

    }

    /**
     * public String min()
     * <p>
     * Returns the info of the item with the smallest key in the tree,
     * or null if the tree is empty
     * <p>
     * Complexity O(1)
     */
    public String min() {
        if (this.empty()) {
            return null;
        }
        return this.min.getValue();
    }


    /**
     * public String max()
     * <p>
     * Returns the info of the item with the largest key in the tree,
     * or null if the tree is empty
     * <p>
     * Complexity O(1)
     */
    public String max() {
        if (this.empty()) {
            return null;
        }
        return this.max.getValue();
    }

    /**
     * public int[] keysToArray()
     * <p>
     * Returns a sorted array which contains all keys in the tree,
     * or an empty array if the tree is empty.
     * <p>
     * Complexity O(n)
     */
    public int[] keysToArray() {
        int[] arr = new int[this.size()]; // size of the array is the tree's size
        //in-order walk
        WAVLNode currnode = (WAVLNode) this.min;

        for (int i = 0; i < this.size(); i++) {
            arr[i] = currnode.getKey();
            currnode = successor(currnode);
        }

        return arr;
    }

    /**
     * public WAVLNode successor(WAVLNode currnode )
     * <p>
     * #pre node != null
     * #post Returns the node with the smallest key which is larger then node.getKey().
     * #post Returns null if no such node exists (i.e This node is the maximum of the tree).
     * <p>
     * Complexity O(logn)
     */
    public WAVLNode successor(WAVLNode currnode) {
        if (currnode == max) {
            return null;
        }
        if (currnode.getRight().isRealNode()) {
            currnode = currnode.getRight();
            while (currnode.getLeft().isRealNode()) {
                currnode = currnode.getLeft();
            }
        } else {
            while (currnode.getParent().getRight() == currnode) {
                currnode = currnode.getParent();
            }
        }
        return currnode;
    }

    /**
     * public String[] infoToArray()
     * <p>
     * Returns an array which contains all info in the tree,
     * sorted by their respective keys,
     * or an empty array if the tree is empty.
     * <p>
     * Complexity: O(logn)
     */
    public String[] infoToArray() {
        String[] arr = new String[this.size()]; // size of the array is the tree's size
        //in-order walk
        WAVLNode currnode = (WAVLNode) this.min;

        for (int i = 0; i < this.size(); i++) {
            arr[i] = currnode.getValue();
            currnode = successor(currnode);
        }

        return arr;
    }

    /**
     * public int size()
     * <p>
     * Returns the number of nodes in the tree.
     * <p>
     * Complexity: O(1)
     */
    public int size() {
        //WAVLNode castedRoot = (WAVLNode)root;
        if (this.root == null) {
            return 0;
        }
        return this.size; // to be replaced by student code
    }

    /**
     * public int getRoot()
     * <p>
     * Returns the root WAVL node, or null if the tree is empty
     * <p>
     * precondition: none
     * postcondition: none
     * <p>
     * Complexity: O(1)
     */
    public IWAVLNode getRoot() {

        return this.root;
    }

    /**
     * public int select(int i)
     * <p>
     * Returns the value of the i'th smallest key (return -1 if tree is empty)
     * Example 1: select(1) returns the value of the node with minimal key
     * Example 2: select(size()) returns the value of the node with maximal key
     * Example 3: select(2) returns the value 2nd smallest minimal node, i.e the value of the node minimal node's successor
     * <p>
     * precondition: size() >= i > 0
     * postcondition: none
     * <p>
     * Complexity O(logn)
     */
    public String select(int i) {
        return select(i, root);
    }

    /**
     * public String select(int i, IWAVLNode node)
     * <p>
     * Returns the value of the i'th smallest key in node's subtree(return -1 if tree is empty)
     * Example 1: select(1) returns the value of the node with minimal key
     * Example 2: select(size()) returns the value of the node with maximal key
     * Example 3: select(2) returns the value 2nd smallest minimal node, i.e the value of the node minimal node's successor
     * <p>
     * precondition: node.getSubtreeSize() >= i > 0
     * postcondition: none
     * <p>
     * Complexity O(logn)
     */
    public String select(int i, IWAVLNode node) {
        if (this.size() < i || this.size() == 0) {
            return "-1";
        }
        IWAVLNode currnode = node.getLeft();
        int currsize = currnode.getSubtreeSize();
        if (currsize == i - 1) {
            return node.getValue();
        }
        if (currsize > i - 1) {
            return select(i, currnode);
        }
        return select(i - currsize - 1, node.getRight());
    }

    /**
     * public interface IWAVLNode
     * ! Do not delete or modify this - otherwise all tests will fail !
     */
    public interface IWAVLNode {
        public int getKey(); //returns node's key (for virtuval node return -1)

        public String getValue(); //returns node's value [info] (for virtuval node return null)

        public IWAVLNode getLeft(); //returns left child (if there is no left child return null)

        public IWAVLNode getRight(); //returns right child (if there is no right child return null)

        public boolean isRealNode(); // Returns True if this is a non-virtual WAVL node (i.e not a virtual leaf or a sentinal)

        public int getSubtreeSize(); // Returns the number of real nodes in this node's subtree (Should be implemented in O(1))
    }

    /**
     * public class WAVLNode
     * <p>
     * If you wish to implement classes other than WAVLTree
     * (for example WAVLNode), do it in this file, not in
     * another file.
     * This class can and must be modified.
     * (It must implement IWAVLNode)
     */
    public class WAVLNode implements IWAVLNode {
        private WAVLNode parent;
        private WAVLNode left;
        private WAVLNode right;
        private int key;
        private String info;
        private int rank;
        private int size;

        //constructors (WAVLNode)
        public WAVLNode(WAVLNode parent, WAVLNode left, WAVLNode right, int key, String info, int rank) {
            this.parent = parent;
            this.left = left;
            this.right = right;
            this.key = key;
            this.info = info;
            this.rank = rank;
        }

        public WAVLNode(int key, String info) {
            this.key = key;
            this.info = info;
            //all other fields should be null or 0
        }

        /**
         * Getters and setters
         * <p>
         * Complexity: O(1)
         */
        public int getKey() {
            return this.key;
        }

        public String getValue() {
            return this.info;
        }

        public WAVLNode getLeft() {
            return this.left;
        }

        public WAVLNode getRight() {
            return this.right;
        }

        public int getRank() {
            return this.rank;
        }

        public WAVLNode getParent() {
            return this.parent;
        }

        public int getSubtreeSize() {
            return this.size;
        }

        public void setParent(WAVLNode myparent) {
            this.parent = myparent;
        }

        public void setLeft(WAVLNode newLeft) {
            this.left = newLeft;
        }

        public void setRight(WAVLNode newRight) {
            this.right = newRight;
        }

        public void setRank(int newRank) {
            this.rank = newRank;
        }

        public void setSize(int newSize) {
            this.size = newSize;
        }

        public void setKey(int key) {
            this.key = key;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        /**
         * public boolean isRealNode()
         * <p>
         * returns true if this is a real node, false if it is a virtual node
         * <p>
         * Complexity: O(1)
         */
        public boolean isRealNode() {
            return this.rank != -1;
        }

        /**
         * public void increaseSize()
         * <p>
         * increases the size of this node by 1
         * <p>
         * Complexity: O(1)
         */
        public void increaseSize() {
            this.size = this.size + 1;
        }

        /**
         * public void increaseRank()
         * <p>
         * increases the rank of this node by 1
         * <p>
         * Complexity: O(1)
         */
        public void increaseRank() {
            this.rank = this.rank + 1;
        }

        /**
         * public void decreaseRank()
         * <p>
         * decreases the rank of this node by 1
         * <p>
         * Complexity: O(1)
         */
        public void decreaseRank() {
            this.rank = this.rank - 1;
        }

        /**
         * public boolean hasRightChild()
         * <p>
         * returns true iff this node has a real right child
         * <p>
         * Complexity: O(1)
         */
        public boolean hasRightChild() {
            return (this.right.getKey() != -1);
        }

        /**
         * public boolean hasLeftChild()
         * <p>
         * returns true iff this node has a real left child
         * <p>
         * Complexity: O(1)
         */
        public boolean hasLeftChild() {
            return (this.left.getKey() != -1);
        }

        /**
         * public boolean hasParent()
         * <p>
         * returns true iff this node has a parent
         * <p>
         * Complexity: O(1)
         */
        public boolean hasParent() {
            return (this.parent != null);
        }

        /**
         * public void addVirtualNodes()
         * <p>
         * adds virtual nodes to this node
         * <p>
         * Complexity: O(1)
         */
        public void addVirtualNodes() {
            if (this.left == null) {
                this.left = new WAVLNode(this, null, null, -1, null, -1);
            }
            if (this.right == null) {
                this.right = new WAVLNode(this, null, null, -1, null, -1);
            }
        }

        /**
         * public int relationWithParent()
         * <p>
         * returns -1 if this node has no parent
         * returns 0 if this node is a left child
         * returns 1 if this node is a right child
         * <p>
         * Complexity: O(1)
         */
        public int relationWithParent() {
            if (parent == null) {
                return -1;
            }
            return parent.relationWithChild(this);
        }

        /**
         * public int relationWithChild(WAVLNode node)
         * <p>
         * returns 0 if node is a left child
         * returns 1 if node is a right child
         * returns -1 otherwise
         * <p>
         * Complexity: O(1)
         */
        public int relationWithChild(WAVLNode node) {
            if (node == null) {
                return -1;
            } else if (node == left) {
                return 0;
            } else if (node == right) {
                return 1;
            } else {
                return -1;
            }
        }

        /**
         * public boolean isRightChild()
         * <p>
         * returns true iff this node is a right child
         * <p>
         * Complexity: O(1)
         */
        public boolean isRightChild() {
            if (this.parent == null) {
                return false;
            }
            return this.parent.getRight() == this;
        }

        /**
         * public boolean isLefttChild()
         * <p>
         * returns true iff this node is a left child
         * <p>
         * Complexity: O(1)
         */
        public boolean isLeftChild() {
            if (this.parent == null) {
                return false;
            }
            return this.parent.left == this;
        }
    }
}
  

