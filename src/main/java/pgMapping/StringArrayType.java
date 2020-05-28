package pgMapping;

import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.hibernate.usertype.DynamicParameterizedType;

import java.util.Properties;

/**
 * @program: spring
 * @description:
 * string-array
 * 用于mapping postgres内的 character varying[]
 * @author: ly
 * @create: 2020-05-15 16:56
 **/
public class StringArrayType extends AbstractSingleColumnStandardBasicType<String[]>
        implements DynamicParameterizedType {

    public StringArrayType() {
        super(
                ArraySqlTypeDescriptor.INSTANCE,
                StringArrayTypeDescriptor.INSTANCE
        );
    }

    @Override
    public String getName() {
        return "string-array";
    }

    @Override
    protected boolean registerUnderJavaType() {
        return true;
    }

    @Override
    public void setParameterValues(Properties parameters) {
        ((StringArrayTypeDescriptor) getJavaTypeDescriptor()).setParameterValues(parameters);
    }
}
