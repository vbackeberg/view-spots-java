import java.util.Set;

public class ElementWithValue {
    private double value;

    private int id;

    private Set<Integer> nodes;

    private boolean deleted = false;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Set<Integer> getNodes() {
        return nodes;
    }

    public void setNodes(Set<Integer> nodes) {
        this.nodes = nodes;
    }


    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted() {
        this.deleted = true;
    }

    public ElementWithValue(Element element, Value value) {
        this.value = value.getValue();
        this.id = element.getId();
        this.nodes = element.getNodes();

    }
}

