package pl.janiec.krystian.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import pl.janiec.krystian.dto.PostDTO;
import pl.janiec.krystian.models.Category;
import pl.janiec.krystian.models.Post;
import pl.janiec.krystian.service.CategoryService;
import pl.janiec.krystian.service.PostService;
import pl.janiec.krystian.service.TagService;

import java.util.List;

@Controller
public class PostController {
	
	private final PostService postService;

	private final CategoryService categoryService;

	private final TagService tagService;

	@Autowired
	public PostController(PostService postService, CategoryService categoryService, TagService tagService) {
		this.postService = postService;
		this.categoryService = categoryService;
		this.tagService = tagService;
	}
	
	@GetMapping("/post/createPost")
	@PreAuthorize("authenticated")
	public String createNewPost(Model model){
		List<Category> categoryList = categoryService.findAll();

		model.addAttribute("view", "post/createPost");
        model.addAttribute("categoryList",categoryList);

		return "layout";
	}
	
	@PostMapping("/post/createPost")
	@PreAuthorize("authenticated")
	public String createNewPostProcess(@Valid PostDTO postDTO){
		postService.createPost(postDTO, ((UserDetails) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal()).getUsername());
		return "redirect:/";
	}
	
	@GetMapping("/post/{id}")
	public String postDetails(Model model, @PathVariable Long id){
		if(!postService.isPostExists(id)){
			return "redirect:/";
		}
		Post post = postService.postDetails(id);
		model.addAttribute("post",post);
		model.addAttribute("view","post/postDetails");
		return "layout";
	}
	
	@GetMapping("/post/editPost/{id}")
	@PreAuthorize("authenticated")
	public String editPost(Model model, @PathVariable Long id){
		if(!postService.isPostExists(id)){
			return "redirect:/";
		}
		Post post = postService.postDetails(id);
		List<Category> categoryList = categoryService.findAll();
		String tags = tagService.joinTags(post);

		model.addAttribute("post",post);
        model.addAttribute("categoryList",categoryList);
		model.addAttribute("view","post/editPost");
        model.addAttribute("tags",tags);
		return "layout";
	}
	
	@PostMapping("post/editPost/{id}")
	@PreAuthorize("authenticated")
	public String editPostProcess(@PathVariable Long id, @Valid PostDTO postDTO){
		if(!postService.isPostExists(id)){
			return "redirect:/";
		}
		Post post = postService.editPost(id, postDTO);
		return "redirect:/post/" + post.getId();
	}
	
	@GetMapping("post/deletePost/{id}")
	@PreAuthorize("authenticated")
	public String deletePost(Model model, @PathVariable Long id){
		if(!postService.isPostExists(id)){
			return "redirect:/";
		}
		Post post = postService.postDetails(id);
		model.addAttribute("post",post);
		model.addAttribute("view","post/deletePost");
		return "layout";
	}
	
	@PostMapping("post/deletePost/{id}")
	@PreAuthorize("authenticated")
	public String deletePostProcess(@PathVariable Long id){
		if(!postService.isPostExists(id)){
			return "redirect:/";
		}
		postService.deletePost(id);
		return "redirect:/";
	}
}
