package com.bookmarker.api.service;

import com.bookmarker.api.domain.Bookmark;
import com.bookmarker.api.domain.BookmarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor //lombok이 injection
public class BookmarkService {
    private final BookmarkRepository repository;

    //Constructor Injection
    //@RequiredArgsConstructor 대신에 직접 생성자 선언
//    public BookmarkService(BookmarkRepository repository) {
//        this.repository = repository;
//    }

    @Transactional(readOnly = true)
    public List<Bookmark> getBookmarks() {
        return repository.findAll();
    }
    
}