package project.model.additiveModel;

public class AdditiveOrderResponse {
    private Long id;
    private String name;
    private Long additiveTypeId;
    private String additiveTypeName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getAdditiveTypeId() {
        return additiveTypeId;
    }

    public void setAdditiveTypeId(Long additiveTypeId) {
        this.additiveTypeId = additiveTypeId;
    }

    public String getAdditiveTypeName() {
        return additiveTypeName;
    }

    public void setAdditiveTypeName(String additiveTypeName) {
        this.additiveTypeName = additiveTypeName;
    }
}
