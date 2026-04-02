package com.adam9e96.chapter052paginationbasic.service;

import com.adam9e96.chapter052paginationbasic.dto.PageResponse;
import com.adam9e96.chapter052paginationbasic.entity.Post;
import com.adam9e96.chapter052paginationbasic.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;

    /**
     * 페이지 번호를 오프셋으로 변환하여 페이지네이션 조회를 수행한다.
     * <p>
     * 핵심 공식: {@code offset = page × size}
     * <ul>
     *     <li>page=0, size=10 → offset=0  (1~10번째 행)</li>
     *     <li>page=1, size=10 → offset=10 (11~20번째 행)</li>
     *     <li>page=2, size=10 → offset=20 (21~30번째 행)</li>
     * </ul>
     */
    public PageResponse<Post> findAllPosts(int page, int size) {
        // offset 계산
        int offset = page * size;
        // 데이터 조회
        List<Post> posts = postRepository.findAllWithPaging(offset, size);
        // 전체 데이터 수 조회
        long totalElements = postRepository.count();
        // 페이지 응답 생성
        return PageResponse.of(posts, page, size, totalElements);
    }

    @Transactional
    public Post savePost(Post post) {
        return postRepository.save(post);
    }
}
