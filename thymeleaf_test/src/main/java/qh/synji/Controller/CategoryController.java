package qh.synji.Controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import jakarta.validation.Valid;
import qh.synji.Entity.CategoryEntity;
import qh.synji.Model.CategoryModel;
import qh.synji.Service.CategoryService;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin/categories")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @GetMapping("add")
    public String add(ModelMap model) {
        CategoryModel cateModel = new CategoryModel();
        cateModel.setEdit(false);
        model.addAttribute("category", cateModel);
        return "admin/categories/addOrEdit";  // Khớp với templates/admin/categories
    }

    @PostMapping("saveOrUpdate")
    public ModelAndView saveOrUpdate(ModelMap model, @Valid @ModelAttribute("category") CategoryModel cateModel,
                                     BindingResult result) {
        if (result.hasErrors()) {
            return new ModelAndView("admin/categories/addOrEdit");
        }

        CategoryEntity entity = new CategoryEntity();
        BeanUtils.copyProperties(cateModel, entity);
        categoryService.save(entity);

        String message = cateModel.isEdit() ? "Category is Edited!!!!!!!" : "Category is saved!!!!!!!";
        model.addAttribute("message", message);
        return new ModelAndView("forward:/admin/categories/searchpaginated", model);
    }

    @RequestMapping("")
    public String list(ModelMap model) {
        List<CategoryEntity> list = categoryService.findAll();
        model.addAttribute("categories", list);
        return "admin/categories/list";  // Đường dẫn đúng
    }

    @GetMapping("edit/{categoryId}")
    public ModelAndView edit(ModelMap model, @PathVariable("categoryId") Long categoryId) {
        Optional<CategoryEntity> optCategory = categoryService.findById(categoryId);
        CategoryModel cateModel = new CategoryModel();

        if (optCategory.isPresent()) {
            CategoryEntity entity = optCategory.get();
            BeanUtils.copyProperties(entity, cateModel);
            cateModel.setEdit(true);
            model.addAttribute("category", cateModel);
            return new ModelAndView("admin/categories/addOrEdit", model);
        }

        model.addAttribute("message", "Category is not existed!!!!!!!");
        return new ModelAndView("forward:/admin/categories", model);
    }

    @GetMapping("delete/{categoryId}")
    public ModelAndView delete(ModelMap model, @PathVariable("categoryId") Long categoryId) {
        categoryService.deleteById(categoryId);
        model.addAttribute("message", "Category is deleted!!!!");
        return new ModelAndView("forward:/admin/categories/searchpaginated", model);
    }

    @GetMapping("search")
    public String search(ModelMap model, @RequestParam(name = "name", required = false) String name) {
        List<CategoryEntity> list = StringUtils.hasText(name) ?
                categoryService.findByNameContaining(name) : categoryService.findAll();
        model.addAttribute("categories", list);
        return "admin/categories/search";  // Đường dẫn đúng
    }

    @RequestMapping("searchpaginated")
    public String searchPaginated(ModelMap model,
                                  @RequestParam(name = "name", required = false) String name,
                                  @RequestParam("page") Optional<Integer> page,
                                  @RequestParam("size") Optional<Integer> size) {

        int count = (int) categoryService.count();
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(3);

        Pageable pageable = PageRequest.of(currentPage - 1, pageSize, Sort.by("name"));
        Page<CategoryEntity> resultPage = StringUtils.hasText(name) ?
                categoryService.findByNameContaining(name, pageable) : categoryService.findAll(pageable);

        model.addAttribute("name", name);
        int totalPages = resultPage.getTotalPages();

        if (totalPages > 0) {
            int start = Math.max(1, currentPage - 2);
            int end = Math.min(currentPage + 2, totalPages);

            if (totalPages > count) {
                if (end == totalPages) start = end - count;
                else if (start == 1) end = start + count;
            }

            List<Integer> pageNumbers = IntStream.rangeClosed(start, end)
                    .boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        model.addAttribute("categoryPage", resultPage);
        return "admin/categories/searchPaging";  // Đường dẫn đúng
    }
}

