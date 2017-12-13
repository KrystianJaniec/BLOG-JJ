package pl.janiec.krystian.models;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

@Entity
@Table(name="posts") 
public class Post implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	@Column(unique=true, nullable=false)
	private String title;

	@Column(columnDefinition="text",nullable=false)
	private String content;

	@ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	@JoinColumn(nullable=false)
	private User author;

	@ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	@JoinColumn(nullable=false)
    private Category category;

	@ManyToMany
    @JoinColumn(nullable = false)
	private Set<Tag> tags;

	public Post(){}

	public Post(String title, String content, User author, Category category, HashSet<Tag> tags) {
		this.title = title;
		this.content = content;
		this.author = author;
		this.category = category;
		this.tags = tags;
	}

    @Transient
    public String getSummary() {
        return this.getContent().substring(0, this.getContent().length()/3)+"...";
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", author=" + author +
                ", category=" + category +
                '}';
    }
}
