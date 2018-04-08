package block_editor;
public class main {
    public static void main(String[] args) {
        Type coordA = new TypeCoordinate2D(0.0,0.0);
        Type coordB = new TypeCoordinate2D(3.0,4.0);
        Block b = new BlockDistance2D("Novy block", coordA, coordB);
        Type out = new TypeSimple(0.0);
        System.out.println(out.getName());
        System.out.println("before "+out.name+" "+out.getVal("simple"));
        b.outConnect(0, out);
        b.execute();
        b.step();
        System.out.println("not settttttt "+ out.getVal("simpleeee"));
        System.out.println("after "+out.name+" "+out.getVal("simple"));
        // Type a = new TypeA("ahoj");
        // System.out.println(a.getNumberOfItems());
    }
}