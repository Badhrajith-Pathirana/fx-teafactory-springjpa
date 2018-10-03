package lk.beempz.tf.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

public abstract class CrudDAOImpl<T,ID> implements CrudDAO<T,ID>{

//    protected Session session;

    @PersistenceContext
    protected EntityManager entityManager;
    /*@Autowired
    EntityManagerFactory entityManagerFactory;
*/
    /*@Override
    public Session getSession(){
        return entityManagerFactory.;
    }*/
    private Class<T> entity;
    public CrudDAOImpl() {
        entity = (Class<T>)((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    /*@Override
    public void setSession(Session session) {
        this.session = session;
    }*/

    @Override
    public void save(T entity) {
        entityManager.persist(entity);
    }

    @Override
    public void delete(ID id) {
        T t = entityManager.find(entity, id);
        entityManager.remove(t);
    }

    @Override
    public void update(T entity) {
        entityManager.persist(entity);
    }

    @Override
    public List<T> getAll() {
        return entityManager.createQuery("FROM "+entity.getName()).getResultList();
    }

    @Override
    public T findById(ID id) {
        return entityManager.find(entity,id);
    }

    @Override
    public T saveAndGetGenerated(T entity) {
        entityManager.persist(entity);
//        session.refresh(entity);
        return entity;
    }


}
