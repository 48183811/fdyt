package pgMapping;

/**
 * @program: spring
 * @description:
 * @author: ly
 * @create: 2020-05-15 17:03
 **/
public class StringArrayTypeDescriptor extends AbstractArrayTypeDescriptor<String[]> {

    public static final StringArrayTypeDescriptor INSTANCE = new StringArrayTypeDescriptor();

    public StringArrayTypeDescriptor() {
        super(String[].class);
    }

    @Override
    protected String getSqlArrayType() {
        return "text";
    }
}
