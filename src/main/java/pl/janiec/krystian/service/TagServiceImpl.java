package pl.janiec.krystian.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.janiec.krystian.models.Post;
import pl.janiec.krystian.models.Tag;
import pl.janiec.krystian.repository.TagRepository;

import java.util.HashSet;
import java.util.StringJoiner;

@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public HashSet<Tag> findTagsByTagName(String tag) {
        HashSet<Tag> allTags = new HashSet<>();
        String[] tags = tag.split(",\\s*");

        for(String tagName : tags){
            Tag currentTag = tagRepository.findByTagName(tagName);

            if(currentTag == null){
                currentTag = new Tag(tagName);
                tagRepository.saveAndFlush(currentTag);
            }
            allTags.add(currentTag);
        }
        return allTags;
    }

    @Override
    public String joinTags(Post post) {
        StringJoiner joiner = new StringJoiner(",");
        for (Tag tag : post.getTags()) {
            String tagName = tag.getTagName();
            joiner.add(tagName);
        }
        return joiner.toString();
    }

    @Override
    public Tag findTagByName(String tagName) {
        return tagRepository.findByTagName(tagName);
    }

}
