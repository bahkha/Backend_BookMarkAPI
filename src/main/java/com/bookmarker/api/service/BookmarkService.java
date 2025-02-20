package com.bookmarker.api.service;

import com.bookmarker.api.domain.Bookmark;
import com.bookmarker.api.domain.BookmarkRepository;
import com.bookmarker.api.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor //lombok이 injection
public class BookmarkService {
    private final BookmarkRepository repository;
    private final BookmarkMapper mapper;

    //Constructor Injection
    //@RequiredArgsConstructor 대신에 직접 생성자 선언
//    public BookmarkService(BookmarkRepository repository) {
//        this.repository = repository;
//    }

    @Transactional(readOnly = true)
    public BookmarksDTO getBookmarks(Integer page) {
        //JPA의 페이지번호가 0부터 시작하기때문
        int pageNo = page < 1 ? 0 : page - 1;
        Pageable pageable = PageRequest.of(pageNo, 10, Sort.Direction.DESC, "id");
        Page<BookmarkDTO> bookmarkPage = repository.findAll(pageable)
        //map(Function) Function의 추상메소드 R apply(T t)
        // .map(bookmark -> mapper.toDTO(bookmark));
        //Method Refrence
                .map(mapper::toDTO);
        Page<BookmarkDTO> bookmark2Page = repository.findBookmarks(pageable);
        return new BookmarksDTO(bookmark2Page);

    }

    @Transactional(readOnly = true)
    public BookmarksDTO<?> searchBookmarks(String query, Integer page) {
        int pageNo = page < 1 ? 0 : page - 1 ;
        Pageable pageable = PageRequest.of(pageNo, 10, Sort.Direction.DESC, "createdAt");
        //Page<BookmarkDTO> bookmarkPage = repository.searchBookmarks(query, pageable);
        //Page<BookmarkDTO> bookmarkPage = repository.findByTitleContainsIgnoreCase(query, pageable);
        //return new BookmarksDTO<>(bookmarkPage);

        Page<BookmarkVM> bookmarkPage =
                repository.findByTitleContainingIgnoreCase(query, pageable);
        return new BookmarksDTO<>(bookmarkPage);
    }

    public BookmarkDTO createBookmark(CreateBookmarkRequest request) {
        Bookmark bookmark = new Bookmark(request.getTitle(), request.getUrl(), Instant.now());
        Bookmark savedBookmark = repository.save(bookmark);
        return mapper.toDTO(savedBookmark);
    }

}