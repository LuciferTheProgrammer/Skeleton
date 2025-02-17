import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestOr {

    @Test
    public void testOrBothFalse() {
        Bit b1 = new Bit(false);
        Bit b2 = new Bit(false);
        Bit result = new Bit(false); // initial value doesn't matter if correctly overwritten
        Bit.or(b1, b2, result);
        assertEquals(Bit.boolValues.FALSE, result.getValue(), "FALSE OR FALSE should be FALSE");
    }

    @Test
    public void testOrFirstTrue() {
        Bit b1 = new Bit(true);
        Bit b2 = new Bit(false);
        Bit result = new Bit(false);
        Bit.or(b1, b2, result);
        assertEquals(Bit.boolValues.TRUE, result.getValue(), "TRUE OR FALSE should be TRUE");
    }

    @Test
    public void testOrSecondTrue() {
        Bit b1 = new Bit(false);
        Bit b2 = new Bit(true);
        Bit result = new Bit(false);
        Bit.or(b1, b2, result);
        assertEquals(Bit.boolValues.TRUE, result.getValue(), "FALSE OR TRUE should be TRUE");
    }

    @Test
    public void testOrBothTrue() {
        Bit b1 = new Bit(true);
        Bit b2 = new Bit(true);
        Bit result = new Bit(false);
        Bit.or(b1, b2, result);
        assertEquals(Bit.boolValues.TRUE, result.getValue(), "TRUE OR TRUE should be TRUE");
    }
}
