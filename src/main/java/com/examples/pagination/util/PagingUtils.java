package com.examples.pagination.util;

import com.examples.pagination.pagination.PageResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import java.util.List;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@UtilityClass
public class PagingUtils {
  private static final String PAGE_NUMBER_LABEL = "pageNumber";
  private static final String PAGE_SIZE_LABEL = "pageSize";

  private static final Encoder ENCODER = Base64.getEncoder();
  private static final Decoder DECODER = Base64.getDecoder();
  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  public static <T> PageResponse<T> getPageResponse(Page<T> page) {
    List<T> content = page.getContent();

    String previousPageCursor = null;
    int pageNumber = page.getNumber();
    int pageSize = page.getSize();
    if (!page.isFirst()) {
      int previousPageNumber = pageNumber - 1;
      previousPageCursor = encode(previousPageNumber, pageSize);
    }
    String nextPageCursor = null;
    if (!page.isLast()) {
      int nextPageNumber = pageNumber + 1;
      nextPageCursor = encode(nextPageNumber, pageSize);
    }

    return new PageResponse<>(content, previousPageCursor, nextPageCursor);
  }

  public static String encode(int pageNumber, int pageSize) {
    ObjectNode bookmarkNode = OBJECT_MAPPER.createObjectNode();
    bookmarkNode.put(PAGE_NUMBER_LABEL, pageNumber);
    bookmarkNode.put(PAGE_SIZE_LABEL, pageSize);

    String plainText = bookmarkNode.toString();
    return ENCODER.encodeToString(plainText.getBytes(StandardCharsets.UTF_8));
  }

  @SneakyThrows
  public static PageRequest decode(String cipherText) {
    String decodedText = new String(DECODER.decode(cipherText));
    JsonNode bookmarkNode = OBJECT_MAPPER.readTree(decodedText);

    int pageNumber = bookmarkNode.get(PAGE_NUMBER_LABEL).asInt();
    int pageSize = bookmarkNode.get(PAGE_SIZE_LABEL).asInt();
    return PageRequest.of(pageNumber, pageSize);
  }
}
