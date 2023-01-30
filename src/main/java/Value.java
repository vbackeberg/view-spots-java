import com.fasterxml.jackson.annotation.JsonProperty;

public class Value {

    @JsonProperty("element_id")
    private int elementId;

    private double value;

    public Value() {
    }

    public int getElementId() {
        return elementId;
    }

    public void setElementId(int elementId) {

        this.elementId = elementId;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
