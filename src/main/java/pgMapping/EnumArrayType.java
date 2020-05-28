package pgMapping;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.hibernate.usertype.DynamicParameterizedType;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @program: spring
 * @description:
 * @author: ly
 * @create: 2020-05-19 17:05
 **/
public class EnumArrayType extends AbstractSingleColumnStandardBasicType<EnumType[]>
        implements DynamicParameterizedType {

    public EnumArrayType(){
        super(
                ArraySqlTypeDescriptor.INSTANCE,
                EnumArrayTypeDescriptor.INSTANCE
        );
    }


    @Override
    public String getName() {
        return "enum-array-type";
    }

    @Override
    protected boolean registerUnderJavaType() {
        return false;
    }

    @Override
    public void setParameterValues(Properties parameters) {
        ((EnumArrayTypeDescriptor) getJavaTypeDescriptor()).setParameterValues(parameters);
    }

}
