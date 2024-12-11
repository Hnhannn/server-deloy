package com.yuki.service;

import com.yuki.dto.*;
import com.yuki.entity.*;
import com.yuki.repositoty.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
// import java.util.ArrayList;
// import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
 
@Service
public class BooksService {

    @Autowired
    private BookDAO bookDAO;

    @Autowired
    private BookChapterDAO bookChapterDAO;

    @Autowired
    private AuthorDAO authorDAO;

    @Autowired
    private BookCategoryDAO bookCategoryDAO;

    @Autowired
    private CategoryDAO categoryDAO;

    @Autowired
    private AuthorBookDAO authorBookDAO;

    @Autowired
    private PublisherDAO publisherDAO;


    @Autowired
    private BookTypeDAO bookTypeDAO;

    @Autowired
    private BookBookTypeDAO bookBookTypeDAO;
	

    public BooksService(BookDAO bookDAO) {
        this.bookDAO = bookDAO;
    }


    public Book bookCreate(BooksDTO booksDTO) {
        // Debug statement to check bookTypes
        System.out.printf("bookTypes: %s\n", booksDTO.getBookTypes());

        // Existing code to create a new Book object
        Book book = new Book();
        Publisher publisher = new Publisher();
        book.setBookImage(booksDTO.getBookImage());
        book.setBookStatus(true);
        book.setDescription(booksDTO.getDescription());
        book.setPrice(booksDTO.getPrice());
        book.setTitle(booksDTO.getTitle());
        book.setPostDate(LocalDateTime.now());
        publisher = publisherDAO.findById(booksDTO.getPublisherId()).orElse(null);
        book.setPublisher(publisher);

        // Save the new book to the database
        book = bookDAO.save(book);
        final Book temap = book;

        // Add chapters to the book if any
        if (booksDTO.getChapters() != null && !booksDTO.getChapters().isEmpty()) {
            List<BookChapter> bookChapters = booksDTO.getChapters().stream().map(chaptersDTO -> {
                BookChapter bookChapter = new BookChapter();
                bookChapter.setBook(temap);
                bookChapter.setChapterTitle(chaptersDTO.getChapterTitle());
                bookChapter.setChapterContent(chaptersDTO.getChapterContent());
                bookChapter.setAudioLink(chaptersDTO.getAudioLink());
                return bookChapterDAO.save(bookChapter); // Save chapter to the database
            }).collect(Collectors.toList());

            book.setBookChapters(bookChapters);
        }

        // Add categories to the book
        addCategoriesToBook(book, booksDTO);

        // Add authors to the book
        addAuthorsToBook(book, booksDTO);

        // Add book types to the book
        addBookTypesToBook(book, booksDTO);

        return book; // Return the created book
    }

    public boolean deleteBook(int bookId) {
        Optional<Book> bookOptional = bookDAO.findById(bookId);
        if (bookOptional.isPresent()) {
            Book book = bookOptional.get();
            book.setBookStatus(false);
            bookDAO.save(book);
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public Book updateBook(int bookId, BooksDTO booksDTO) {
        // Find the existing book by ID
        Book book = bookDAO.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + bookId));

        // Update book details
        book.setBookImage(booksDTO.getBookImage());
        book.setDescription(booksDTO.getDescription());
        book.setPrice(booksDTO.getPrice());
        book.setTitle(booksDTO.getTitle());

        Publisher publisher = publisherDAO.findById(booksDTO.getPublisherId()).orElse(null);
        book.setPublisher(publisher);

        // Update chapters if any
        if (booksDTO.getChapters() != null && !booksDTO.getChapters().isEmpty()) {
            List<BookChapter> bookChapters = booksDTO.getChapters().stream().map(chaptersDTO -> {
                BookChapter bookChapter = new BookChapter();
                bookChapter.setBook(book);
                bookChapter.setChapterTitle(chaptersDTO.getChapterTitle());
                bookChapter.setChapterContent(chaptersDTO.getChapterContent());
                bookChapter.setAudioLink(chaptersDTO.getAudioLink());
                return bookChapterDAO.save(bookChapter); // Save chapter to the database
            }).collect(Collectors.toList());
            book.setBookChapters(bookChapters);
        }

//        // Update categories
//        addCategoriesToBook(book, booksDTO);
//
//        // Update authors
//        addAuthorsToBook(book, booksDTO);


        updateBookTypesOfBook(book, booksDTO);

        // Save the updated book to the database
        return bookDAO.save(book);
    }

    private void addBookTypesToBook(Book book, BooksDTO booksDTO) {
        if (booksDTO.getBookTypes() != null && !booksDTO.getBookTypes().isEmpty()) {
            List<BookBookType> bookBookTypes = booksDTO.getBookTypes().stream().map(bookTypesDTO -> {
                BookType bookType = bookTypeDAO.findById(bookTypesDTO.getBookTypeID())
                        .orElseThrow(() -> new RuntimeException("type is not found: " + bookTypesDTO.getBookTypeID()));
                BookBookType bookBookType = new BookBookType();
                bookBookType.setBookType(bookType);
                bookBookType.setAccessType(bookTypesDTO.getAccessType());
                bookBookType.setBook(book);
                return bookBookTypeDAO.save(bookBookType); // Save book type to the database
            }).collect(Collectors.toList());
            book.setBookBookTypes(bookBookTypes);
        } else {
            System.out.println("Book types are null or empty");
        }
    }

    // Thêm thể loại cho sách
    private void addCategoriesToBook(Book book, BooksDTO booksDTO) {
        if (booksDTO.getCategories() != null && !booksDTO.getCategories().isEmpty()) {
            List<BookCategory> bookCategories = booksDTO.getCategories().stream().map(categoriesDTO -> {
                Category category = categoryDAO.findById(categoriesDTO.getCategoryId())
                        .orElseThrow(() -> new RuntimeException("Category not found with id: " + categoriesDTO.getCategoryId()));
                BookCategory bookCategory = new BookCategory();
                bookCategory.setCategory(category);
                bookCategory.setBook(book);
                return bookCategoryDAO.save(bookCategory); // Lưu thể loại sách vào cơ sở dữ liệu
            }).collect(Collectors.toList());
            book.setBookCategories(bookCategories);
        }
    }

    // Thêm tác giả cho sách
    private void addAuthorsToBook(Book book, BooksDTO booksDTO) {
        if (booksDTO.getAuthors() != null && !booksDTO.getAuthors().isEmpty()) {
            List<AuthorBook> authorBooks = booksDTO.getAuthors().stream().map(authorsDTO -> {
                Author author = authorDAO.findById(authorsDTO.getAuthorId())
                        .orElseThrow(() -> new RuntimeException("Author not found with id: " + authorsDTO.getAuthorId()));
                AuthorBook authorBook = new AuthorBook();
                authorBook.setAuthor(author);
                authorBook.setBook(book);
                return authorBookDAO.save(authorBook); // Lưu tác giả sách vào cơ sở dữ liệu
            }).collect(Collectors.toList());

            book.setAuthorBooks(authorBooks);
        }
    }

    @Transactional
    public void updateBookTypesOfBook(Book book, BooksDTO booksDTO) {
        // Remove existing book types
        bookBookTypeDAO.deleteByBook(book);

        authorBookDAO.deleteByBook(book);

        bookCategoryDAO.deleteByBook(book);

        // Add new categories
        addCategoriesToBook(book, booksDTO);

        // Add new authors
        addAuthorsToBook(book, booksDTO);

        // Add new book types
        addBookTypesToBook(book, booksDTO);
    }


}



