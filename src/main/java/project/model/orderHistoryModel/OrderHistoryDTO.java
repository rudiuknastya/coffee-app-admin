package project.model.orderHistoryModel;

import jakarta.validation.constraints.Size;

public class OrderHistoryDTO {
    private Long id;
    @Size(max=200, message = "Розмір поля має бути не більше 200 символів")
    private String comment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
