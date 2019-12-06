package io.usumu.api.common.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

public class Resource implements zone.refactor.spring.hateoas.contract.Entity {
    @SuppressWarnings("DefaultAnnotationParam")
    @ApiModelProperty(required = true, position = 0, notes = "The object type.")
    @JsonProperty(value = "@type", required = true, index = 0)
    public String getType() {
        Class<? extends Resource> currentClass = this.getClass();
        ApiModel annotation = currentClass.getAnnotation(ApiModel.class);
        if (annotation != null) {
            annotation.value();
            if (!annotation.value().isEmpty()) {
                return annotation.value();
            }
        }
        return currentClass.getSimpleName();
    }
}
