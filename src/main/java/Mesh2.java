import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.SetMultimap;


@JsonIgnoreProperties(value = {"values", "nodes"})
public class Mesh2 {
    ListMultimap<Integer, Integer> elements;

    public Mesh2() {
    }

    public ListMultimap<Integer, Integer> getElements() {
        return elements;
    }

    public void setElements(ListMultimap<Integer, Integer> elements) {
        this.elements = elements;
    }
}
