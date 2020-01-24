package pl.wut.wsd.dsm.infrastructure.persistence.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.SchemaAutoTooling;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;
import org.hibernate.context.internal.ThreadLocalSessionContext;
import org.hibernate.dialect.Dialect;
import org.hibernate.query.Query;
import pl.wut.wsd.dsm.infrastructure.persistence.Specification;
import pl.wut.wsd.dsm.infrastructure.persistence.model.Identifiable;
import pl.wut.wsd.dsm.infrastructure.persistence.sort.Pagination;
import pl.wut.wsd.dsm.infrastructure.persistence.sort.Sort;
import pl.wut.wsd.dsm.infrastructure.persistence.sort.SortType;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

public class HibernateTemplate {
    private final StandardServiceRegistry registry;
    private final SessionFactory sessionFactory;

    public HibernateTemplate(final String url, final String user, final String password,
                             final Class<? extends java.sql.Driver> driverClass,
                             final Class<? extends Dialect> dialectClass,
                             final Set<Class<?>> annotatedClasses) {
        final Properties settings = new Properties();
        settings.setProperty(Environment.DRIVER, driverClass.getCanonicalName());
        settings.setProperty(Environment.URL, url);
        settings.setProperty(Environment.USER, user);
        settings.setProperty(Environment.PASS, password);
        settings.setProperty(Environment.DIALECT, dialectClass.getCanonicalName());
        settings.setProperty(Environment.CURRENT_SESSION_CONTEXT_CLASS, ThreadLocalSessionContext.class.getCanonicalName());
        settings.setProperty(Environment.HBM2DDL_AUTO, SchemaAutoTooling.UPDATE.name());

        this.registry = new StandardServiceRegistryBuilder()
                .applySettings(settings)
                .build();

        final MetadataSources sources = new MetadataSources(registry);

        annotatedClasses.forEach(sources::addAnnotatedClass);
        final Metadata metadata = sources.getMetadataBuilder().build();

        this.sessionFactory = metadata.getSessionFactoryBuilder().build();
    }

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    public <T> Optional<T> findOne(final Specification<T> specification, final Class<T> clazz) {
        return fetchInTransaction(session -> {
            final Query<T> query = buildQuery(specification, clazz, session, Sort.unsorted());
            try {
                return Optional.of(query.getSingleResult());
            } catch (final NoResultException e) {
                return Optional.empty();
            }
        });
    }

    public <T> List<T> findAll(final Specification<T> specification, final Class<T> clazz) {
        return fetchInTransaction(session -> {
            final Query<T> query = buildQuery(specification, clazz, session, Sort.unsorted());
            return query.getResultList();
        });
    }

    public <T> List<T> findAll(final Specification<T> specification, final Class<T> clazz, final Pagination<T> pagination) {
        return fetchInTransaction(session -> {
            final Query<T> query = buildQuery(specification, clazz, session, pagination.sort());
            query.setFirstResult(pagination.startPosition());
            query.setMaxResults(pagination.pageSize());
            return query.getResultList();
        });
    }

    public <T extends Identifiable<?>> void saveOrUpdate(final T identifiable) {
        executeInTransaction(s -> s.saveOrUpdate(identifiable));
    }

    public <T extends Identifiable<ID>, ID extends Serializable> Optional<T> getOne(final ID id, final Class<T> clazz) {
        return fetchInTransaction(session -> Optional.ofNullable(session.get(clazz, id)));
    }

    public <T> void deleteAll(final Class<T> clazz) {
        executeInTransaction(s -> {
            final String hql = String.format("delete from %s", clazz.getSimpleName());
            final Query query = s.createQuery(hql);

            query.executeUpdate();
        });
    }

    private <T> Query<T> buildQuery(final Specification<T> specification, final Class<T> clazz, final Session session, final Sort<T> sort) {
        final CriteriaBuilder cb = session.getCriteriaBuilder();

        final CriteriaQuery<T> criteriaQuery = cb.createQuery(clazz);
        final Root<T> root = criteriaQuery.from(clazz);
        criteriaQuery.select(root).where(specification.toPredicate(root, cb));
        if (sort.type() == SortType.ASCENDING) {
            criteriaQuery.orderBy(cb.asc(root.get(sort.sortedBy())));
        } else if (sort.type() == SortType.DESCENDING) {
            criteriaQuery.orderBy(cb.desc(root.get(sort.sortedBy())));
        }

        return session.createQuery(criteriaQuery);
    }

    private <T> T fetchInTransaction(final Function<Session, T> fetcher) {
        final Session session = getSession();
        final Transaction transaction = session.beginTransaction();
        final T result = fetcher.apply(session);
        transaction.commit();

        return result;
    }

    private void executeInTransaction(final Consumer<Session> action) {
        final Session session = getSession();
        final Transaction transaction = session.beginTransaction();
        action.accept(session);
        transaction.commit();
    }


}
