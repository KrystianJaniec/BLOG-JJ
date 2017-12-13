package pl.janiec.krystian.service;

import pl.janiec.krystian.models.Post;
import pl.janiec.krystian.models.Tag;

import java.util.HashSet;

public interface TagService {

    HashSet<Tag> findTagsByTagName(String tagName);

    String joinTags(Post post);

    Tag findTagByName(String tagName);
}
