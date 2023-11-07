package com.examples.pagination.pagination;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;

@JsonInclude(Include.NON_NULL)
public record PageResponse<T>(List<T> content, String previousPageCursor, String nextPageCursor) {}
