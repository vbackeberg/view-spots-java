import java.util.HashSet;

public class AllLowerAndToBeRemovedPair {
    boolean allLower;
    HashSet<ElementWithValue> elementsToBeRemoved;

    public AllLowerAndToBeRemovedPair(boolean allLower, HashSet<ElementWithValue> elementsToBeRemoved) {
        this.allLower = allLower;
        this.elementsToBeRemoved = elementsToBeRemoved;
    }
}
