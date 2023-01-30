import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(value = {"nodes"})
public class Mesh {

    Element[] elements;
    Value[] values;

    public Mesh() {
    }

    public Element[] getElements() {
        return elements;
    }

    public void setElements(Element[] elements) {
        this.elements = elements;
    }

    public Value[] getValues() {
        return values;
    }

    public void setValues(Value[] values) {
        this.values = values;
    }
}
