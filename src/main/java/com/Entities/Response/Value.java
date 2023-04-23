package com.Entities.Response;

import com.Entities.Request.ShallowReference;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Value {
    private int id;
    private ShallowReference testPoint;
}
