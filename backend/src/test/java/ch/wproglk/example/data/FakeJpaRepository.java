package ch.wproglk.example.data;

import jakarta.persistence.EntityNotFoundException;
import org.apache.commons.collections4.IteratorUtils;
import org.springframework.data.domain.*;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.util.Assert;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public abstract class FakeJpaRepository<E, ID>
{
    protected final TreeMap<ID, E> db;

    public FakeJpaRepository()
    {
        db = new TreeMap<>();
    }

    public abstract <S extends E> S save(S entity);

    public <S extends E> List<S> saveAll(Iterable<S> entities)
    {
        return StreamSupport.stream(entities.spliterator(), false)
                            .map(this::save)
                            .collect(Collectors.toList());
    }

    public <S extends E> S saveAndFlush(S entity)
    {
        return save(entity);
    }

    public List<E> findAll()
    {
        return new ArrayList<>(db.values());
    }

    public List<E> findAll(Sort sort)
    {
        return findAll();
    }

    public Page<E> findAll(Pageable pageable)
    {
        List<E> pagedList = findAll(pageable.getSort()).stream()
                                                       .skip(pageable.getOffset())
                                                       .limit(pageable.getPageSize())
                                                       .collect(Collectors.toList());
        return new PageImpl<>(pagedList, pageable, pagedList.size());
    }

    public List<E> findAllById(Iterable<ID> ids)
    {
        return db.entrySet()
                 .stream()
                 .filter(e -> IteratorUtils.contains(ids.iterator(), e.getKey()))
                 .map(Map.Entry::getValue)
                 .collect(Collectors.toList());
    }

    public long count()
    {
        return db.size();
    }

    public void deleteById(ID id)
    {
        db.remove(id);
    }

    public void delete(E entity)
    {
        db.values().removeIf(entity::equals);
    }

    public void deleteAll(Iterable<? extends E> entities)
    {
        db.values().removeAll(IteratorUtils.toList(entities.iterator()));
    }

    public void deleteAll()
    {
        db.clear();
    }

    public Optional<E> findById(ID id)
    {
        Assert.notNull(id, "ID is required!");
        return Optional.ofNullable(db.get(id));
    }

    public boolean existsById(ID id)
    {
        return db.containsKey(id);
    }

    public void flush()
    {
        // Noop
    }

    public void deleteInBatch(Iterable<E> entities)
    {
        deleteAll(entities);
    }

    public void deleteAllInBatch()
    {
        deleteAll();
    }

    public E getOne(ID id)
    {
        return Optional.ofNullable(db.get(id)).orElseThrow(EntityNotFoundException::new);
    }

    public <S extends E> Optional<S> findOne(Example<S> example)
    {
        throw new UnsupportedOperationException("This method is not supported by the fake test implementation " + this.getClass());
    }

    public <S extends E> List<S> findAll(Example<S> example)
    {
        throw new UnsupportedOperationException("This method is not supported by the fake test implementation " + this.getClass());
    }

    public <S extends E> List<S> findAll(Example<S> example, Sort sort)
    {
        throw new UnsupportedOperationException("This method is not supported by the fake test implementation " + this.getClass());
    }

    public <S extends E> Page<S> findAll(Example<S> example, Pageable pageable)
    {
        throw new UnsupportedOperationException("This method is not supported by the fake test implementation " + this.getClass());
    }

    public <S extends E> long count(Example<S> example)
    {
        throw new UnsupportedOperationException("This method is not supported by the fake test implementation " + this.getClass());
    }

    public <S extends E> boolean exists(Example<S> example)
    {
        throw new UnsupportedOperationException("This method is not supported by the fake test implementation " + this.getClass());
    }

    public <S extends E> List<S> saveAllAndFlush(Iterable<S> entities)
    {
        throw new UnsupportedOperationException("This method is not supported by the fake test implementation " + this.getClass());
    }

    public void deleteAllInBatch(Iterable<E> entities)
    {
        throw new UnsupportedOperationException("This method is not supported by the fake test implementation " + this.getClass());
    }

    public void deleteAllById(Iterable<? extends ID> integers)
    {
        throw new UnsupportedOperationException("This method is not supported by the fake test implementation " + this.getClass());
    }

    public void deleteAllByIdInBatch(Iterable<ID> integers)
    {
        throw new UnsupportedOperationException("This method is not supported by the fake test implementation " + this.getClass());
    }

    public E getById(ID id)
    {
        return getOne(id);
    }

    public <S extends E, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction)
    {
        throw new UnsupportedOperationException("This method is not supported by the fake test implementation " + this.getClass());
    }

    public E getReferenceById(ID id)
    {
        return db.get(id);
    }

    public int getNextId()
    {
        return db.size() + 1;
    }
}
