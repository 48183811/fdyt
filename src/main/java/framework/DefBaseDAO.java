package framework;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.Serializable;

/**
 * 用于本系统默认操作数据库的sessionFactory注入
 * @param <M>
 * @param <ID>
 */
public class DefBaseDAO<M, ID extends Serializable> extends BaseDAO<M, ID> {

	@Autowired
    public void setSessionFactoryOverride(@Qualifier("def") SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
}
