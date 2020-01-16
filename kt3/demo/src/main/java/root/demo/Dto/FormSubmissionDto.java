package root.demo.Dto;

import java.io.Serializable;

public class FormSubmissionDto implements Serializable{

    String fieldId;
    Object fieldValue;

    public FormSubmissionDto() {
        super();
        // TODO Auto-generated constructor stub
    }

    public FormSubmissionDto(String fieldId, String fieldValue) {
        super();
        this.fieldId = fieldId;
        this.fieldValue = fieldValue;
    }

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    public Object getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(Object fieldValue) {
        this.fieldValue = fieldValue;
    }
}

