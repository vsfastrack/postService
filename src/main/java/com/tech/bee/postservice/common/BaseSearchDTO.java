package com.tech.bee.postservice.common;

import com.tech.bee.postservice.enums.Enums;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BaseSearchDTO {
    protected int pageIndex;
    protected int pageSize;
    protected Enums.SortDirection sortDir;
    protected String sortKey;
}
