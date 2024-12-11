package com.yuki.service;

import com.yuki.dto.AuthorsDTO;
import com.yuki.entity.Author;
// import com.yuki.entity.PackagePlan;
import com.yuki.repositoty.AuthorDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthorsService {
    @Autowired
    private AuthorDAO authorDAO;

    public Author createAuthor(AuthorsDTO authorDTO) {
        Author author = new Author();
        author.setAuthorName(authorDTO.getAuthorName());
        author.setStatus(true);
        return authorDAO.save(author);
    }

    public Author AuthorUpdate(int ID,AuthorsDTO authorDTO) {
        Optional<Author> author = authorDAO.findById(ID);
        if(author.isPresent()) {
            Author updateAuthor = author.get();
            updateAuthor.setAuthorName(authorDTO.getAuthorName());
            updateAuthor.setStatus(true);
            return authorDAO.save(updateAuthor);
        }
        return null;
    }

    public void deleteAuthor(int ID) {
        Optional<Author> author = authorDAO.findById(ID);
        if (author.isPresent()) {
            Author deleteAuthor = author.get();
            deleteAuthor.setStatus(false);
            authorDAO.save(deleteAuthor);
        }
    }


}
