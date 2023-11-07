package com.examples.pagination.customer;

import com.examples.pagination.pagination.CursorPaging;
import com.examples.pagination.pagination.PageResponse;
import java.util.List;

public interface CustomerService {

  void deleteAll();

  void saveAll(List<Customer> customers);

  PageResponse<Customer> getAllCustomers(CursorPaging cursorPaging);
}
