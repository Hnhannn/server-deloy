package com.yuki.service;

import com.yuki.dto.CategoriesDTO;
import com.yuki.entity.Author;
import com.yuki.entity.Category;
import com.yuki.repositoty.CategoryDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
// import java.util.List;
import java.util.Optional;

@Service
public class CategoriesService {

    @Autowired
    private CategoryDAO categoryDAO;

    // Create a new category
    public Category createCategory(CategoriesDTO category) {
        Category cate = new Category();
        cate.setCategoryName(category.getCategoryName());
        cate.setStatus(true);
        return categoryDAO.save(cate);
    }

    // Update a category
    public Category updateCategory(int id, CategoriesDTO category) {
        Optional<Category> existingCate = categoryDAO.findById(id);
        if (existingCate.isPresent()) {
            Category cate = existingCate.get();
            cate.setCategoryName(category.getCategoryName());
            cate.setStatus(true);
            return categoryDAO.save(cate);
        }
        return null;
    }

    public void deleteCategory(int ID) {
        Optional<Category> category = categoryDAO.findById(ID);
        if (category.isPresent()) {
            Category deleteCategory = category.get();
            deleteCategory.setStatus(false);
            categoryDAO.save(deleteCategory);
        }
    }

}
