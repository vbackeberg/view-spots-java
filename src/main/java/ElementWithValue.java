import java.util.HashSet;
import java.util.Set;

public class ElementWithValue {
    private double value;

    private int id;

    private HashSet<Integer> nodes;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public HashSet<Integer> getNodes() {
        return nodes;
    }

    public void setNodes(HashSet<Integer> nodes) {
        this.nodes = nodes;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public ElementWithValue(Element element, Value value) {
        this.value = value.getValue();
        this.id = element.getId();
        this.nodes = element.getNodes();

    }
}

