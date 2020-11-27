package melo.rodrigues.guilherme.desafiosupplyagro.custom.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Objects;

@Component
public class UnicoValidation implements ConstraintValidator<Unico, Object> {

    @Autowired
    private EntityManager em;

    private Unico anotacao;

    @Override
    public void initialize(Unico anotacao) {
        this.anotacao = anotacao;
    }

    @Override
    public boolean isValid(Object valor, ConstraintValidatorContext validatorContext) {

        Objects.requireNonNull(anotacao.coluna(), "Coluna deve ser informada");

        if (isEntidadeInvalida(anotacao)) {
            throw new IllegalArgumentException("Entidade deve conter a anotação @Entity");
        }

        Class<?> entidade = anotacao.entidade();
        String coluna = anotacao.coluna();

        CriteriaBuilder criteria = em.getCriteriaBuilder();
        CriteriaQuery<?> query = criteria.createQuery(entidade);
        Root<?> root = query.from(entidade);
        query.where(criteria.equal(root.get(coluna), valor));

        try {
            em.createQuery(query).getSingleResult();
            return false;
        } catch (NoResultException e) {
            return true;
        }
    }

    private boolean isEntidadeInvalida(Unico anotacao) {
        return Arrays.stream(anotacao.entidade().getAnnotations())
                .noneMatch(annotation -> annotation.annotationType().equals(Entity.class));
    }
}
