package org.equinox.repository.custom.impl;

import org.equinox.model.domain.Tag;
import org.hibernate.Session;
import org.equinox.repository.custom.TagCustomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class TagCustomRepositoryImpl implements TagCustomRepository {
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    public TagCustomRepositoryImpl(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public Tag save(Tag tag) {
        CriteriaBuilder criteriaBuilder = entityManagerFactory.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        Root<Tag> root = criteriaQuery.from(Tag.class);
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        criteriaQuery.where(criteriaBuilder.equal(root.get("name"), tag.getName()));
        List<Tag> tags = entityManager.createQuery(criteriaQuery).getResultList();

        if (!tags.isEmpty()) {
            tag.setId(tags.get(0).getId());
            return tag;
        } else {
            return entityManager.merge(tag);
        }
    }
}
