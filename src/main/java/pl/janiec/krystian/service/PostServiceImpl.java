package pl.janiec.krystian.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.janiec.krystian.dto.PostDTO;
import pl.janiec.krystian.models.Category;
import pl.janiec.krystian.models.Post;
import pl.janiec.krystian.models.Tag;
import pl.janiec.krystian.models.User;
import pl.janiec.krystian.repository.CategoryRepository;
import pl.janiec.krystian.repository.PostRepository;
import pl.janiec.krystian.repository.UserRepository;

@Service
public class PostServiceImpl implements PostService {

    private final UserRepository userRepository;

    private final PostRepository postRepository;

    private final CategoryRepository categoryRepository;

    private final TagService tagService;

    @Autowired
    public PostServiceImpl(UserRepository userRepository, PostRepository postRepository, CategoryRepository categoryRepository, TagService tagService) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.categoryRepository = categoryRepository;
        this.tagService = tagService;
    }

    @Override
    public Post createPost(PostDTO postDTO, String username) {
        User user = userRepository.findByEmail(username);
        Category category = categoryRepository.findOne(postDTO.getCategoryId());
        HashSet<Tag> tags = tagService.findTagsByTagName(postDTO.getTag());

        Post post = new Post(postDTO.getTitle(), postDTO.getContent(), user, category, tags);
        return postRepository.saveAndFlush(post);
    }

    @Override
    public List<Post> findAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public Post postDetails(Long id) {
        return postRepository.findOne(id);
    }

    @Override
    public boolean isPostExists(Long id) {
        return postRepository.exists(id);
    }

    @Override
    public Post editPost(Long id, PostDTO postDTO) {
        Post post = postRepository.findOne(id);
        Category category = categoryRepository.findOne(postDTO.getCategoryId());
        HashSet<Tag> tags = tagService.findTagsByTagName(postDTO.getTag());

        post.setCategory(category);
        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());
        post.setTags(tags);

        return postRepository.saveAndFlush(post);
    }

    @Override
    public void deletePost(Long id) {
        Post post = postRepository.findOne(id);

        postRepository.delete(post);
    }

    @Override
    public Post getPost(Long id) {
        return postRepository.findOne(id);
    }
}
