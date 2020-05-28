package pgMapping;

/**
 * @program: spring
 * @description:
 * int-array
 * 用于mapping Postgres 内 integer[] 字段类型
 * @author: ly
 * @create: 2020-05-15 16:59
 **/
import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.hibernate.usertype.DynamicParameterizedType;
import java.util.Properties;

public class IntArrayType extends AbstractSingleColumnStandardBasicType<int[]>
        implements DynamicParameterizedType {

    public IntArrayType() {
        super(
                ArraySqlTypeDescriptor.INSTANCE,
                IntArrayTypeDescriptor.INSTANCE
        );
    }

    @Override
    public String getName() {
        return "int-array";
    }

    @Override
    protected boolean registerUnderJavaType() {
        return true;
    }

    @Override
    public void setParameterValues(Properties parameters) {
        ((IntArrayTypeDescriptor) getJavaTypeDescriptor()).setParameterValues(parameters);
    }
}
