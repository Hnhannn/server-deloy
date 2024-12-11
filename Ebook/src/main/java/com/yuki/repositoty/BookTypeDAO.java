package com.yuki.repositoty;

// import com.yuki.entity.BookBookType;
import com.yuki.entity.BookType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// import java.util.Optional;

@Repository
public interface BookTypeDAO extends JpaRepository<BookType, Integer> {
//    Optional<BookBookType> findByBook_BookIDAndBookType_BookTypeID(int bookID, int bookTypeID);
}