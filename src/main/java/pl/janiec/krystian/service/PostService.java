package pl.janiec.krystian.service;

import java.util.List;

import pl.janiec.krystian.dto.PostDTO;
import pl.janiec.krystian.models.Post;

public interface PostService {

    Post createPost(PostDTO postDTO, String username);

    List<Post> findAllPosts();

    Post getPost(Long id);

    Post postDetails(Long id);

    boolean isPostExists(Long id);

    Post editPost(Long id, PostDTO postDTO);

    void deletePost(Long id);

}
