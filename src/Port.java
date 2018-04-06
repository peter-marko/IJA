public class Port {
    Type portType;
    String connectedTo;

    public Port (Type newType) {
        this.portType = newType;
        this.connectedTo = "";
    }

    public void connect (String connectTo) {
        this.connectedTo = connectTo;
    }

    public String getConnection() {
        return this.connectedTo;
    }
}
