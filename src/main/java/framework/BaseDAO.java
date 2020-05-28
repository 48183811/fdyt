package framework;

import constant.Sys;
import enm.RESULT;
import org.hibernate.*;
import org.springframework.orm.hibernate4.HibernateCallback;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.lang.InstantiationException;

/**
 * 基础dao
 * @param <M>
 * @param <ID>
 */
@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
public class BaseDAO<M, ID extends Serializable> extends HibernateDaoSupport {
	
	private final static String CAP_ORDER_BY = "ORDER BY";
	private final static String LOW_ORDER_BY = "order by";
	
	private Class<M> mc;

	@SuppressWarnings(Sys.UNCHECKED)
	public BaseDAO() {
		if (mc == null) 
			mc = (Class<M>)((ParameterizedTypeImpl)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	/***
	 * 分页查询SQL
	 * @param pageBean 分页信息
	 * @param sql sql语句
	 * @param parm 参数
	 * @param countSql 计数sql语句
	 * @param countParm 计数sql语句参数
	 * @return 是否查询成功
	 */
	@SuppressWarnings({ Sys.UNCHECKED, Sys.RAWTYPES })
	protected RESULT queryToPageBean(final PageBean pageBean, 
						final String sql, final List<Object> parm, 
						final String countSql, final List<Object> countParm) {
		
		return (RESULT)getHibernateTemplate().execute(new HibernateCallback() {
			public RESULT doInHibernate(Session session) throws HibernateException {
				//李辉勤修改
				//SQLQuery query = session.createSQLQuery(sql);
				Session currentSession = getSessionFactory().getCurrentSession();
				SQLQuery query = currentSession.createSQLQuery(sql);
				//李辉勤修改

				query.setFirstResult((pageBean.getPage() - 1) * pageBean.getPageSize());
				query.setMaxResults(pageBean.getPageSize());

				for (int i = 0; i < parm.size(); i ++) {
					query.setParameter(i, parm.get(i));
				}
				pageBean.setResultList(query.list());

				//李辉勤修改
				/** 获取符合条件的总数 */
				//query = session.createSQLQuery(countSql);
				query = currentSession.createSQLQuery(countSql);
				//李辉勤修改

				for (int i = 0; i < countParm.size(); i ++)
					query.setParameter(i, countParm.get(i));
				pageBean.setTotal(Long.parseLong(query.uniqueResult().toString()));
				
				/** 计算pageBean的其他信息 */
				pageBean.compute();
				
				return RESULT.SUCCESS;
			}
		});
	}

	/***
	 * 分页查询HQL
	 * @param hql hql语句
	 * @param pageBean 分页信息
	 * @param list 查询hql语句所用到的参数
	 * @return
	 */
	@SuppressWarnings({ Sys.UNCHECKED, Sys.RAWTYPES })
	protected RESULT findToPageBean(final String hql, final PageBean pageBean, final List<Object> list) {
		return (RESULT)getHibernateTemplate().execute(new HibernateCallback() {
			public RESULT doInHibernate(Session session) throws HibernateException {
				Query query = session.createQuery(hql.toString());

				//黄勋修改
				query.setFirstResult((pageBean.getPage() - 1) * pageBean.getPageSize());
//				query.setFirstResult(pageBean.getStart());
				//黄勋修改

				query.setMaxResults(pageBean.getPageSize());

				for (int i = 0; null != list && i < list.size(); i ++) {
					query.setParameter(i, list.get(i));
				}

				/** 获取需要显示的结果和符合条件的总数 */
				pageBean.setResultList(query.list());

				String countHql = "SELECT COUNT(*) ";
				if (hql.indexOf(LOW_ORDER_BY) != -1)
					countHql += hql.substring(0, hql.indexOf(LOW_ORDER_BY) - 1);
				else if (hql.indexOf(CAP_ORDER_BY) != -1)
					countHql += hql.substring(0, hql.indexOf(CAP_ORDER_BY) - 1);

				if (null != list && 0 < list.size())
					pageBean.setTotal((Long) getHibernateTemplate().find(countHql, list.toArray()).listIterator().next());
				else
					pageBean.setTotal((Long) getHibernateTemplate().find(countHql).listIterator().next());

				/** 计算pageBean的其他信息 */
				pageBean.compute();

				return RESULT.SUCCESS;
			}
		});
	}

	/***
	 * 根据HQL及参数查询结果（例：FROM Test t WHERE t.code = ?）“?”就为之后传入的参数
	 * @param hql HQL语句
	 * @param values 参数
	 * @param <X>
	 * @return
	 */
	@SuppressWarnings(Sys.UNCHECKED)
	protected <X>List<X> find(String hql, Object...values) {
		return (List<X>)getHibernateTemplate().find(hql, values);
	}

	/***
	 * 根据HQL及参数获取查询结果（可指定从第几条开始获取，共获取多少条记录）
	 * @param hql   HQL语句
	 * @param first 从第几条开始
	 * @param total 共获取多少条记录
	 * @param parms 参数数组（hql中的“?”号）
	 * @param <X>
	 * @return
	 */
	@SuppressWarnings({ Sys.UNCHECKED, Sys.RAWTYPES })
	protected <X>List<X> find(final String hql, final Integer first, final Integer total, final Object...parms) {
		List<X> list = (List<X>)getHibernateTemplate().executeWithNativeSession(new HibernateCallback() { // 实现HibernateCallback接口必须实现的方法
			public List<X> doInHibernate(Session session) throws HibernateException {
				Query query = session.createQuery(hql);
				
				if (null != first)
					query.setFirstResult(first);
				if (null != total)
					query.setMaxResults(total);
				
				if (null != parms && parms.length > 0)
					for (int i = 0; i < parms.length; i ++){
						query.setParameter(i, parms[i]);
					}
				List<X> list = query.list();
				return list;
			}
		});
		return list;
	}

	/***
	 * 根据sql及参数获取查询结果（可指定从第几条开始获取，共获取多少条记录）
	 * @param sql sql语句
	 * @param clazz 查询结果实体类
	 * @param first 从第几条开始
	 * @param total 共获取多少条记录
	 * @param parms 参数数组（hql中的“?”号）
	 * @param <X>
	 * @return
	 */
	@SuppressWarnings({ Sys.UNCHECKED, Sys.RAWTYPES })
	protected <X>List<X> dtos(final String sql, final Class clazz, final Integer first, final Integer total, final Object...parms) {
		List<X> list = (List<X>)getHibernateTemplate().executeWithNativeSession(new HibernateCallback() { // 实现HibernateCallback接口必须实现的方法
			public List<X> doInHibernate(Session session) throws HibernateException {
				Connection conn = null;
				List<X> list = null;

				try {
					Field[] fields = clazz.getDeclaredFields();
					list = new ArrayList<X>();
					conn = SessionFactoryUtils.getDataSource(session.getSessionFactory()).getConnection();
					String csql = sql;
					if (null != first && null != total){
						//csql += " LIMIT " + first + "," + total;
						csql += " LIMIT " + total + " OFFSET " + first;
					}
					else if (null != total){
						//csql += " LIMIT " + total;
						csql += " LIMIT " + total + " OFFSET 0 ";
					}

					PreparedStatement stsm = conn.prepareStatement(csql);
					if (null != parms && parms.length > 0) {
						for (int i = 0; i < parms.length; i ++) {
							if (null != parms[i]) {
								if (Sys.TYPE_STRING.equals(parms[i].getClass().getName()))
									stsm.setString(i + 1, parms[i].toString());
								else if (Sys.TYPE_BYTE.equals(parms[i].getClass().getName()))
									stsm.setInt(i + 1, Byte.valueOf(parms[i].toString()));
								else if (Sys.TYPE_SHORT.equals(parms[i].getClass().getName()))
									stsm.setInt(i + 1, Short.valueOf(parms[i].toString()));
								else if (Sys.TYPE_INTEGER.equals(parms[i].getClass().getName()))
									stsm.setInt(i + 1, Integer.valueOf(parms[i].toString()));
								else if (Sys.TYPE_LONG.equals(parms[i].getClass().getName()))
									stsm.setLong(i + 1, Long.valueOf(parms[i].toString()));
								else if (Sys.TYPE_FLOAT.equals(parms[i].getClass().getName()))
									stsm.setFloat(i + 1, Float.valueOf(parms[i].toString()));
								else if (Sys.TYPE_DATE.equals(parms[i].getClass().getName()))
									stsm.setDate(i + 1, new java.sql.Date(((Date)parms[i]).getTime()));
								else if (Sys.TYPE_BOOLEAN.equals(parms[i].getClass().getName()))
									stsm.setBoolean(i + 1, Boolean.valueOf(parms[i].toString()));
							}
						}
					}
					
					ResultSet rs = stsm.executeQuery();
					Object entry = null;
					while (rs.next()) {
						try {
							entry = clazz.newInstance();
							for (Field f : fields) {
								if (!Sys.TYPE_LIST.equals(f.getType().getName()) && !Sys.TYPE_MAP.equals(f.getType().getName())) {
									if (isExistColumn(rs, f.getName()) && rs.getObject(f.getName()) != null) {
										f.setAccessible(true);
										if (Sys.TYPE_BYTE.equals(f.getType().getName()))
											f.set(entry, Byte.parseByte(rs.getObject(f.getName()).toString()));
										else if (Sys.TYPE_SHORT.equals(f.getType().getName()))
											f.set(entry, Short.parseShort(rs.getObject(f.getName()).toString()));
										else if (Sys.TYPE_INTEGER.equals(f.getType().getName()))
											f.set(entry, Integer.parseInt(rs.getObject(f.getName()).toString()));
										else if (Sys.TYPE_LONG.equals(f.getType().getName()))
											f.set(entry, Long.parseLong(rs.getObject(f.getName()).toString()));
										else if (Sys.TYPE_FLOAT.equals(f.getType().getName()))
											f.set(entry, Float.parseFloat(rs.getObject(f.getName()).toString()));
										else if (Sys.TYPE_BOOLEAN.equals(f.getType().getName()))
											f.set(entry, Boolean.parseBoolean(rs.getObject(f.getName()).toString()));
										else if (Sys.TYPE_STRING.equals(f.getType().getName()))
											f.set(entry, String.valueOf(rs.getObject(f.getName())));
										else
											f.set(entry, rs.getObject(f.getName()));
										/********* 字符串及日期型不需要特殊处理 *********/
									}
								}
							}
						} catch (IllegalArgumentException | IllegalAccessException | InstantiationException e) {
							e.printStackTrace();
						}
						list.add((X)entry);
					}

					rs.close();
					stsm.close();
					conn.close();
					
					return list;
				} catch (SQLException e) {
					System.out.println("在BaseDAO中进行查询时发生错误, 有可能是因为并发数量太大所致. Exception: " + e.toString());
					e.printStackTrace();
					return null;
				}
			}
		});
		return list;
	}

	/***
	 * 执行SQL语句，根据PageBean参数进行查询，并将结果以Class定义的类型封装到PageBean.resultList中
	 * @param pb 分页对象
	 * @param clazz PageBean.resultList中保存的对象类型
	 * @param sql 查询语句
	 * @param parm 查询语句相关的参数
	 * @param countSql 获取总纪录数的SQL语句
	 * @param countParm 获取总纪录数的SQL语句对应的参数
	 * @return
	 */
	@SuppressWarnings({Sys.UNCHECKED, Sys.RAWTYPES})
	protected RESULT dtosToPageBean(PageBean pb, Class clazz, String sql, List<Object> parm, String countSql, List<Object> countParm) {
		return (RESULT)getHibernateTemplate().executeWithNativeSession(new HibernateCallback() { // 实现HibernateCallback接口必须实现的方法
			public RESULT doInHibernate(Session session) throws HibernateException {
				Connection conn = null;
				List<Object> list = null;

				try {
					Field[] fields = clazz.getDeclaredFields();
					list = new ArrayList<Object>();
					conn = SessionFactoryUtils.getDataSource(session.getSessionFactory()).getConnection();
					
					// 获取查询结果
					String csql = sql + " LIMIT " + String.valueOf(pb.getPageSize()) + " OFFSET " + String.valueOf((pb.getPage()-1)*pb.getPageSize());
					PreparedStatement stsm = conn.prepareStatement(csql);
					for (int i = 0; i < parm.size(); i ++) {
						if (null != parm.get(i)) {
							if (Sys.TYPE_STRING.equals(parm.get(i).getClass().getName()))
								stsm.setString(i + 1, parm.get(i).toString());
							else if (Sys.TYPE_BYTE.equals(parm.get(i).getClass().getName()))
								stsm.setInt(i + 1, Byte.valueOf(parm.get(i).toString()));
							else if (Sys.TYPE_SHORT.equals(parm.get(i).getClass().getName()))
								stsm.setInt(i + 1, Short.valueOf(parm.get(i).toString()));
							else if (Sys.TYPE_INTEGER.equals(parm.get(i).getClass().getName()))
								stsm.setInt(i + 1, Integer.valueOf(parm.get(i).toString()));
							else if (Sys.TYPE_LONG.equals(parm.get(i).getClass().getName()))
								stsm.setLong(i + 1, Long.valueOf(parm.get(i).toString()));
							else if (Sys.TYPE_FLOAT.equals(parm.get(i).getClass().getName()))
								stsm.setFloat(i + 1, Float.valueOf(parm.get(i).toString()));
							else if (Sys.TYPE_DATE.equals(parm.get(i).getClass().getName()))
								stsm.setDate(i + 1, new java.sql.Date(((Date)parm.get(i)).getTime()));
							else if (Sys.TYPE_BOOLEAN.equals(parm.get(i).getClass().getName()))
								stsm.setBoolean(i + 1, Boolean.valueOf(parm.get(i).toString()));
						}
					}
					

					ResultSet rs = stsm.executeQuery();
					Object entry = null;
					while (rs.next()) {
						try {
							entry = clazz.newInstance();
							for (Field f : fields) {
								f.setAccessible(true);
								
								// 判断列名是否存在，不存在就不用取
								if (!isExistColumn(rs, f.getName())) {
									continue;
								}
								
								if (null != rs.getObject(f.getName())) {
									
									if (Sys.TYPE_BYTE.equals(f.getGenericType().getTypeName()))
										f.set(entry, Byte.parseByte(rs.getObject(f.getName()).toString()));
									else if (Sys.TYPE_SHORT.equals(f.getGenericType().getTypeName()))
										f.set(entry, Short.parseShort(rs.getObject(f.getName()).toString()));
									else if (Sys.TYPE_INTEGER.equals(f.getGenericType().getTypeName()))
										f.set(entry, Integer.parseInt(rs.getObject(f.getName()).toString()));
									else if (Sys.TYPE_LONG.equals(f.getGenericType().getTypeName()))
										f.set(entry, Long.parseLong(rs.getObject(f.getName()).toString()));
									else if (Sys.TYPE_FLOAT.equals(f.getGenericType().getTypeName()))
										f.set(entry, Float.parseFloat(rs.getObject(f.getName()).toString()));
									else 
										f.set(entry, rs.getObject(f.getName()));
								}
							}
						} catch (IllegalArgumentException | IllegalAccessException | InstantiationException e) {
							e.printStackTrace();
						}
						list.add(entry);
					}
					pb.setResultList(list);
					
					
					
					// 获取查询结果总数total
					stsm = conn.prepareStatement(countSql);
					for (int i = 0; i < parm.size(); i ++) {
						if (null != parm.get(i)) {
							if (Sys.TYPE_STRING.equals(parm.get(i).getClass().getName()))
								stsm.setString(i + 1, parm.get(i).toString());
							else if (Sys.TYPE_BYTE.equals(parm.get(i).getClass().getName()))
								stsm.setInt(i + 1, Byte.valueOf(parm.get(i).toString()));
							else if (Sys.TYPE_SHORT.equals(parm.get(i).getClass().getName()))
								stsm.setInt(i + 1, Short.valueOf(parm.get(i).toString()));
							else if (Sys.TYPE_INTEGER.equals(parm.get(i).getClass().getName()))
								stsm.setInt(i + 1, Integer.valueOf(parm.get(i).toString()));
							else if (Sys.TYPE_LONG.equals(parm.get(i).getClass().getName()))
								stsm.setLong(i + 1, Long.valueOf(parm.get(i).toString()));
							else if (Sys.TYPE_FLOAT.equals(parm.get(i).getClass().getName()))
								stsm.setFloat(i + 1, Float.valueOf(parm.get(i).toString()));
							else if (Sys.TYPE_DATE.equals(parm.get(i).getClass().getName()))
								stsm.setDate(i + 1, new java.sql.Date(((Date)parm.get(i)).getTime()));
							else if (Sys.TYPE_BOOLEAN.equals(parm.get(i).getClass().getName()))
								stsm.setBoolean(i + 1, Boolean.valueOf(parm.get(i).toString()));
						}
					}
					
					rs = stsm.executeQuery();
					if (rs.next()) {
						pb.setTotal(rs.getLong(1));
					}
					pb.compute();
					
					
					rs.close();
					stsm.close();
					conn.close();
					
					return RESULT.SUCCESS;
				} catch (SQLException e) {
					System.out.println("在BaseDAO中进行查询时发生错误, 有可能是因为并发数量太大所致. Exception: " + e.toString());
					e.printStackTrace();
					return RESULT.ERROR;
				}
			}
		});
	}
	

	/***
	 * 执行自定义查询SQL
	 * @param sql 查询sql语句
	 * @param first 从第几条开始
	 * @param total 共获取多少条记录
	 * @param parms 参数
	 * @param <X>
	 * @return
	 */
	@SuppressWarnings({ Sys.UNCHECKED, Sys.RAWTYPES })
	protected <X>List<X> query(final String sql, final Integer first, final Integer total, final Object...parms) {
		List<X> list = (List<X>)getHibernateTemplate().executeWithNativeSession(new HibernateCallback() { // 实现HibernateCallback接口必须实现的方法
			public Object doInHibernate(Session session) throws HibernateException {
				SQLQuery query = session.createSQLQuery(sql);

				if (null != first)
					query.setFirstResult(first);
				if (null != total)
					query.setMaxResults(total);
				
				if (null != parms && parms.length > 0)
					for (int i = 0; i < parms.length; i ++)
						query.setParameter(i, parms[i]);
				
				List<X> list = query.list();
				return list;
			}
		});
		return list;
	}
	

	/***
	 * 执行HQL语句,未验证是否可参与事务
	 * @param hql HQL语句
	 * @param values 参数
	 * @return 该语句影响的行数
	 */
	@SuppressWarnings({ Sys.UNCHECKED, Sys.RAWTYPES })
	protected Integer execute(final String hql, final Object...values) {
		Integer result = (Integer)getHibernateTemplate().execute(new HibernateCallback() {
			public Integer doInHibernate(Session session) throws HibernateException {
				Query q = session.createQuery(hql);
				for (int i = 0; i < values.length; i ++) {
					q.setParameter(i, values[i]);
				}
				return q.executeUpdate();
			}
		});
		getHibernateTemplate().flush();
		return result;
	}

	/***
	 * 执行SQL语句,未验证是否可参与事务
	 * @param sql sql语句
	 * @param values 参数
	 * @return 该语句影响的行数
	 */
	@SuppressWarnings({ Sys.UNCHECKED, Sys.RAWTYPES })
	protected Integer executeSql(final String sql, final Object...values) {
		Integer result = (Integer)getHibernateTemplate().execute(new HibernateCallback() {
			public Integer doInHibernate(Session session) throws HibernateException {
				//李辉勤修改
				//Query q = session.createSQLQuery(sql);
				Session currentSession = getSessionFactory().getCurrentSession();
				SQLQuery q = currentSession.createSQLQuery(sql);
				//李辉勤修改

				for (int i = 0; i < values.length; i ++) {
					q.setParameter(i, values[i]);
				}
				return q.executeUpdate();
			}
		});
		getHibernateTemplate().flush();
		return result;
	}

	/***
	 * 根据HQL及该语句用到的参数，获取一个对象
	 * @param hql HQL语句
	 * @param values 参数
	 * @return
	 */
	@SuppressWarnings(Sys.UNCHECKED)
	protected Object get(String hql, Object...values) {
		List<Object> list = (List<Object>)getHibernateTemplate().find(hql, values);
		if (null != list && list.size() > 0)
			return list.get(0);
		return null;
	}
	
	/**
	 * 根据id获取DAO中指定的对象
	 * @param id 主键
	 */
	public M get(final ID id) {
		if (null == id)
			return null;
		M m = getHibernateTemplate().get(mc, id);
		return  m;
	}

	/***
	 * 根据id获取指定类型的对象
	 * @param c 对象类型
	 * @param id 主键
	 * @return
	 */
	@SuppressWarnings(Sys.RAWTYPES)
	public Object get(Class c, String id) {
		return getHibernateTemplate().get(c.getName(), (Serializable)id);
	}

	/***
	 * 添加参数指定的对象
	 * @param model 要添加的对象
	 * @return
	 */
	public RESULT add(Object model) {
		if (null == model)
			return RESULT.PARAMETER_ERROR;
		getHibernateTemplate().save(model);
		return RESULT.SUCCESS;
	}

	/**
	 * 根据id删除DAO中泛型指定的对象
	 * @param id 主键
	 * @return
	 */
	public RESULT del(final ID id) {
		if (null == id)
			return RESULT.PARAMETER_ERROR;
		M model = get(id);
		if (null == model)
			return RESULT.NOT_FIND;
		return del(model);
	}

	/***
	 * 删除参数所指定的对象
	 * @param model 要删除的对象
	 * @return
	 */
	public RESULT del(Object model) {
		if (null == model)
			return RESULT.PARAMETER_ERROR;
		getHibernateTemplate().delete(model);
		getHibernateTemplate().flush();
		return RESULT.SUCCESS;
	}

	/***
	 * 根据id删除指定类型的对象
	 * @param c 要删除的对象类型
	 * @param id 主键
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public RESULT del(Class c, String id) {
		getHibernateTemplate().delete(c.getName(), id);
		getHibernateTemplate().flush();
		return RESULT.SUCCESS;
	}

	/***
	 * 删除数组中的所有对象
	 * @param list 要删除的对象数组
	 * @return
	 */
	public RESULT delAll(List<M> list) {
		getHibernateTemplate().deleteAll(list);
		getHibernateTemplate().flush();
		return RESULT.SUCCESS;
	}

	/***
	 * 更新参数中指定的对象
	 * @param model 要更新的对象
	 * @return
	 */
	public RESULT upd(Object model) {
		getHibernateTemplate().update(model);
		getHibernateTemplate().flush();
		return RESULT.SUCCESS;
	}

	/***
	 * 更新或添加参数中指定的对象，如果是更新，该对象的主键必须赋值
	 * @param model 要更新或添加的对象
	 * @return
	 */
	public RESULT sou(Object model) {
		if (null == model)
			return RESULT.PARAMETER_ERROR;
		getHibernateTemplate().saveOrUpdate(model);
		getHibernateTemplate().flush();
		return RESULT.SUCCESS;
	}

	/***
	 * 修改或添加数组中的所有对象
	 * @param list 要修改或添加的对象数组
	 * @return
	 */
	public RESULT souAll(List<M> list) {
		if (null != list && list.size() > 0) {
			for ( M m : list) {
				getHibernateTemplate().saveOrUpdate(m);
			}
			getHibernateTemplate().flush();
		}
		return RESULT.SUCCESS;
	}
	
	public List<M> all(Class<M> c) {
        return (List<M>)getHibernateTemplate().loadAll(c);
    }
	

	/***
	 * 根据hql语句获取一个model
	 * @param hql hql语句
	 * @param args 参数
	 * @return
	 */
	@SuppressWarnings("unchecked" )
	public M getUniqueResult(final String hql, final Object... args) {
		return (M) getHibernateTemplate().execute(new HibernateCallback<M>() {
			public M doInHibernate(Session session) throws HibernateException {
				Query query = session.createQuery(hql);
				for (int i = 0; i < args.length; i++) {
					query.setParameter(i, args[i]);
				}
				return (M) query.uniqueResult();
			}
		});
	}

	/***
	 * 获取一个字段（通常用于max、count等语句）
	 * @param sql  查询语句
	 * @param args 查询参数
	 * @param <X>
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <X> X getSole(final String sql, final Object... args) {
		return (X) getHibernateTemplate().execute(new HibernateCallback<X>() {
			public X doInHibernate(Session session) throws HibernateException {
				Query query = session.createQuery(sql);
				for (int i = 0; i < args.length; i++) {
					query.setParameter(i, args[i]);
				}
				return (X) query.uniqueResult();
			}
		});
	}

	/***
	 * 执行SQL语句
	 * @param sql sql语句
	 * @param parms 参数
	 * @param <X>
	 * @return
	 */
	@SuppressWarnings({Sys.RAWTYPES, Sys.UNCHECKED})
	public <X> X sole(final String sql, final Object... parms) {
		X res = (X)getHibernateTemplate().executeWithNativeSession(new HibernateCallback() {
			public X doInHibernate(Session session) throws HibernateException {
				SQLQuery query = session.createSQLQuery(sql);

				query.setFirstResult(0);
				query.setMaxResults(1);
				
				if (null != parms && parms.length > 0)
					for (int i = 0; i < parms.length; i ++)
						query.setParameter(i, parms[i]);
				
				List<X> list = query.list();
				if (null != list && list.size() > 0)
					return list.get(0);
				return null;
			}
		});
		getHibernateTemplate().flush();
		return res;
	}


	/***
	 * 判断查询结果集中是否存在某列
	 * @param rs 查询结果集
	 * @param columnName 列名
	 * @return true 存在; false 不存咋
	 */
	public boolean isExistColumn(ResultSet rs, String columnName) {
		try {
			if (rs.findColumn(columnName) > 0) {
				return true;
			}
		} catch (SQLException e) {
			return false;
		}
		return false;
	}

	/***
	 * 判断字符串是否不为null和空字符串
	 * @param str 为null或空字符串就返回false，否则true
	 * @return
	 */
	protected boolean isNotNull(String str) {
		return null == str || Sys.NONE.equals(str) ? false : true;
	}

//	@Autowired
    public void setSessionFactoryOverride(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);   
	}
}
