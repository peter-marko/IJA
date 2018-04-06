import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

public class TestUkol2 {

    private Block blok1;
    private Block blok2;

    @Before
    public void SetUp(){
        blok1 = new Block1("Inkorporace");
    }

    @Test
    public void test01(){
        Assert.assertEquals(2, blok1.getNumberOfInputs());
    }

    @Test
    public void test02(){
        Assert.assertEquals(2, blok1.getNumberOfInputs());
    }

    @Test
    public void test03(){
        Assert.assertNotEquals(2, 5);
    }

}
