package block_editor;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import java.lang.reflect.Modifier;
import block_editor.blocks.*;
import block_editor.types.*;

public class TestUkol2 {

    private Block1 blok1;
    private Block1 blok2;
    private Type coordA;
    private Type coordB;
    private BlockDistance2D b;
    private Type out; 

    @Before
    public void SetUp(){

        coordA = new TypeCoordinate2D(0.0,0.0);
        coordB = new TypeCoordinate2D(3.0,4.0);
        b = new BlockDistance2D("Novy block", 1, coordA, coordB);
        out = new TypeSimple(0.0);
        blok1 = new Block1("Inkorporace");
        blok2 = new Block1("abcd");
    }

    @Test
    public void test01(){
        Assert.assertEquals(2, blok1.getNumberOfInputs());
        Assert.assertEquals(2, blok1.getNumberOfInputs());
    }

    @Test
    public void test02(){
        Assert.assertEquals(coordA.getName(), "coordinate");
        Assert.assertEquals(coordB.getName(), "coordinate");
    }
    @Test
    public void test03(){
        Assert.assertEquals(out.getName(), "simple");
        Assert.assertEquals(out.getVal("simple"), 0.0, 0.01);
    }
    @Test
    public void test04(){
        int exception = 0;
        try {
            out.getVal("not set");
        } catch (Exception e) {
            exception = 1;
        }
        Assert.assertEquals(exception, 1);
        b.outConnect(0, out);
        b.execute();
        b.step();
        Assert.assertEquals(out.getVal("simple"), 5.0, 0.01);
    }

    @Test
    public void test05() {
        Assert.assertTrue("Trida Block ma byt abstraktni.", 
            Modifier.isAbstract(Block.class.getModifiers()));
        Assert.assertTrue("b extends Block", b instanceof Block);
        Assert.assertTrue("blok1 extends Block", blok1 instanceof Block);
        Assert.assertTrue("blok2 extends Block", blok2 instanceof Block);
    }
    

}
