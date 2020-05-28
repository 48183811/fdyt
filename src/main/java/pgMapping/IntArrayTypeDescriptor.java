package pgMapping;

/**
 * @program: spring
 * @description:
 * @author: ly
 * @create: 2020-05-15 17:04
 **/
public class IntArrayTypeDescriptor extends AbstractArrayTypeDescriptor<int[]> {

    public static final IntArrayTypeDescriptor INSTANCE = new IntArrayTypeDescriptor();

    public IntArrayTypeDescriptor() {
        super( int[].class );
    }

    @Override
    protected String getSqlArrayType() {
        return "integer";
    }
}
