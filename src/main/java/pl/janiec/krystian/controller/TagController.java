package pl.janiec.krystian.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pl.janiec.krystian.models.Tag;
import pl.janiec.krystian.service.TagService;

@Controller
public class TagController {

    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping("/tag/{tagName}")
    public String postsWithTag(Model model, @PathVariable String tagName){
        Tag tag = tagService.findTagByName(tagName);
        if(tag == null){
            return "redirect:/";
        }
        model.addAttribute("view","tag/postsWithTag");
        model.addAttribute("tag", tag);
        return "layout";
    }
}
