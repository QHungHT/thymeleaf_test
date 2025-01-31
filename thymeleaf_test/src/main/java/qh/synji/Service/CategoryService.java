package qh.synji.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import qh.synji.Entity.CategoryEntity;

public interface CategoryService {

    <S extends CategoryEntity> S save(S entity);

    List<CategoryEntity> findByNameContaining(String name);

    Page<CategoryEntity> findByNameContaining(String name, Pageable pageable);

    <S extends CategoryEntity> Optional<S> findOne(Example<S> example);

    Page<CategoryEntity> findAll(Pageable pageable);

    List<CategoryEntity> findAll();

    List<CategoryEntity> findAllById(Iterable<Long> ids);

    Optional<CategoryEntity> findById(Long id);

    long count();

    void deleteById(Long id);

    void delete(CategoryEntity entity);

    <S extends CategoryEntity> List<S> findAll(Example<S> example, Sort sort);

    void deleteAll();
}
