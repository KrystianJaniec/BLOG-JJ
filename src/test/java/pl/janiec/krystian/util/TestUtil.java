package pl.janiec.krystian.util;

import pl.janiec.krystian.dto.CategoryDTO;
import pl.janiec.krystian.dto.PostDTO;
import pl.janiec.krystian.dto.UserDTO;
import pl.janiec.krystian.models.Category;
import pl.janiec.krystian.models.Post;
import pl.janiec.krystian.models.Tag;

import java.util.*;

public class TestUtil {

    public static PostDTO createPostDTO(String title, String content, String tag, Long categoryId) {
        PostDTO postDTO = new PostDTO();
        postDTO.setTitle(title);
        postDTO.setContent(content);
        postDTO.setTag(tag);
        postDTO.setCategoryId(categoryId);
        return postDTO;
    }

    public static UserDTO createUserDTO(String email, String fullName, String password) {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(email);
        userDTO.setFullName(fullName);
        userDTO.setPassword(password);
        return userDTO;
    }

    public static UserDTO createUserDTO(String email, String fullName, String password,String confirmPassword) {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(email);
        userDTO.setFullName(fullName);
        userDTO.setPassword(password);
        userDTO.setConfirmPassword(confirmPassword);
        return userDTO;
    }

    public static CategoryDTO createCategoryDTO(String categoryName){
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setCategoryName(categoryName);
        return categoryDTO;
    }

    public static HashSet<Tag> createTags(String tagName) {
        HashSet<Tag> tags = new HashSet<>();
        tags.add(new Tag(tagName));
        return tags;
    }

    public static HashSet<Tag> createTags(String tagName1,String tagName2) {
        HashSet<Tag> tags = new HashSet<>();
        tags.add(new Tag(tagName1));
        tags.add(new Tag(tagName2));
        return tags;
    }

    public static String joinTags(String tagName1,String tagName2){
        return String.join(",",tagName1,tagName2);
    }

    public static List<Post> createPosts(Post post1, Post post2) {
        List<Post> posts = new ArrayList<>();
        posts.add(post1);
        posts.add(post2);
        return posts;
    }

    public static Category createCategoryWithPosts(String categoryName, Set<Post> posts){
        Category category = new Category(categoryName);
        category.setPosts(posts);
        return category;
    }

    public static Post cretePostWithId(Long id) {
        Post post = new Post();
        post.setId(id);
        return post;
    }

    public static List<Category> createCategoryList(Category category1,Category category2){
        List<Category> categoryList = new ArrayList<>();
        categoryList.add(category1);
        categoryList.add(category2);
        return categoryList;
    }

    public static List<Category> createCategoryListWithId(Category category1,Long id1,Category category2, Long id2){
        List<Category> categoryList = new ArrayList<>();
        category1.setId(id1);
        category2.setId(id2);
        categoryList.add(category1);
        categoryList.add(category2);
        return categoryList;
    }

    public static Category createCategoryWithId(Long id){
        Category category = new Category();
        category.setId(id);
        return category;
    }

}
