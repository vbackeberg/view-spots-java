import java.util.Set;

public class Element {
    private int id;

    private Set<Integer> nodes;

    public Element() {
    }

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
}
