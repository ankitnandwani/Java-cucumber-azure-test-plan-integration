package com.Entities.Request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShallowReference {
    private String id;
    private String name;
}
