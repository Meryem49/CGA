package CGA.Framework;

public class RawMaterial
{
    public int diffTexIndex;
    public int specTexIndex;
    public int emitTexIndex;
    public float shininess;

    public RawMaterial(int diffTexIndex, int specTexIndex, int emitTexIndex, float shininess)
    {
        this.diffTexIndex = diffTexIndex;
        this.emitTexIndex = emitTexIndex;
        this.specTexIndex = specTexIndex;
        this.shininess = shininess;
    }

    public RawMaterial()
    {
        this.diffTexIndex = 0;
        this.emitTexIndex = 0;
        this.specTexIndex = 0;
        this.shininess = 0;
    }
}
