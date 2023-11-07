package com.examples.pagination.pagination;

import com.examples.pagination.util.PagingUtils;
import java.util.Objects;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;

@Data
public class CursorPaging {
  public static final int DEFAULT_PAGE_SIZE = 100;

  private Integer pageSize;
  private String bookmark;

  public PageRequest getPageRequest() {
    if (StringUtils.isBlank(bookmark)) {
      return PageRequest.ofSize(Objects.nonNull(pageSize) ? pageSize : DEFAULT_PAGE_SIZE);
    }

    return PagingUtils.decode(bookmark);
  }
}
