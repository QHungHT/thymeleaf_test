package qh.synji.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;


import qh.synji.Entity.CategoryEntity;
import qh.synji.Repository.CategoryRepository;

@Service
public class CategoryServiceImpl {
	@Autowired
	CategoryRepository categoryRepository;

	public CategoryServiceImpl(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}
	public <S extends CategoryEntity> S save(S entity) {
	    // Nếu CategoryId là null, thì thêm mới entity vào database
	    if (entity.getCategoryId() == null) {
	        return categoryRepository.save(entity);
	    } 

	    // Nếu CategoryId không null, kiểm tra entity hiện tại đã tồn tại hay chưa
	    Optional<CategoryEntity> optionalEntity = findById(entity.getCategoryId());

	    if (optionalEntity.isPresent()) {
	        // Nếu tên của entity mới là rỗng, lấy tên từ entity cũ
	        if (StringUtils.isEmpty(entity.getName())) {
	            entity.setName(optionalEntity.get().getName());
	        }
	    } else {
	        // Trường hợp cần giữ nguyên tên hiện tại
	        entity.setName(entity.getName()); // Lưu lại images cũ (ý tưởng từ comment của bạn)
	    }

	    // Lưu entity vào database sau khi cập nhật
	    return categoryRepository.save(entity);
	}
	public List<CategoryEntity> findByNameContaining(String name) {
		return categoryRepository.findByNameContaining(name);
	}
	public Page<CategoryEntity> findByNameContaining(String name, Pageable pageable) {
		return categoryRepository.findByNameContaining(name, pageable);
	}
	public <S extends CategoryEntity> Optional<S> findOne(Example<S> example) {
		return categoryRepository.findOne(example);
	}
	public Page<CategoryEntity> findAll(Pageable pageable) {
		return categoryRepository.findAll(pageable);
	}
	public List<CategoryEntity> findAll() {
		return categoryRepository.findAll();
	}
	public List<CategoryEntity> findAllById(Iterable<Long> ids) {
		return categoryRepository.findAllById(ids);
	}
	public Optional<CategoryEntity> findById(Long id) {
		return categoryRepository.findById(id);
	}
	public long count() {
		return categoryRepository.count();
	}
	public void deleteById(Long id) {
		categoryRepository.deleteById(id);
	}
	public void delete(CategoryEntity entity) {
		categoryRepository.delete(entity);
	}
	public <S extends CategoryEntity> List<S> findAll(Example<S> example, Sort sort) {
		return categoryRepository.findAll(example, sort);
	}
	public void deleteAll() {
		categoryRepository.deleteAll();
	}
	

	
}
