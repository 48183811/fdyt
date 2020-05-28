package pgMapping;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;

/**
 * @program: spring
 * @description:
 * enumType
 * 用于mapping Postgres 内 enum 字段类型
 * @author: ly
 * @create: 2020-05-15 16:59
 **/
public class EnumType extends org.hibernate.type.EnumType{


//    public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws SQLException {
//        return rs.getString(names[0]);
//    }

    public void nullSafeSet(PreparedStatement st,Object value,int index,SessionImplementor session) throws HibernateException, SQLException {

        if(value == null) {
            st.setNull( index, Types.OTHER );
        }
        else {
            st.setObject( index, value.toString(), Types.OTHER );
        }
    }
}
