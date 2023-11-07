package com.examples.pagination.customer;

import com.examples.pagination.pagination.CursorPaging;
import com.examples.pagination.pagination.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customers")
public class CustomerController {
  @Autowired private CustomerService customerService;

  @GetMapping
  public PageResponse<Customer> getAll(CursorPaging cursorPaging) {
    return customerService.getAllCustomers(cursorPaging);
  }
}
