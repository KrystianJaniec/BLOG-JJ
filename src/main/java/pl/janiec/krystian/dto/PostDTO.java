package pl.janiec.krystian.dto;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotBlank;

public class PostDTO {

	@NotBlank
	private String title;

	@NotBlank
	private String content;

	@NotBlank
	private String tag;

	@NotNull
	private Long categoryId;

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

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}
