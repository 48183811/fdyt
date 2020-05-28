package pgMapping;


/**
 * @program: spring
 * @description:
 * @author: ly
 * @create: 2020-05-19 17:16
 **/
public class EnumArrayTypeDescriptor extends AbstractArrayTypeDescriptor<EnumType[]>{
    public static final EnumArrayTypeDescriptor INSTANCE = new EnumArrayTypeDescriptor();

    public EnumArrayTypeDescriptor() {
        super( EnumType[].class );
    }


    @Override
    protected String getSqlArrayType() {
        return "sys_user_authorization";
    }

}
