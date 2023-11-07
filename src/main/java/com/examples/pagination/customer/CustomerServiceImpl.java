package com.examples.pagination.customer;

import com.examples.pagination.pagination.CursorPaging;
import com.examples.pagination.pagination.PageResponse;
import com.examples.pagination.util.PagingUtils;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
class CustomerServiceImpl implements CustomerService {

  @Autowired private CustomerRepository customerRepository;

  @Override
  public void deleteAll() {
    customerRepository.deleteAll();
  }

  @Override
  public void saveAll(List<Customer> customers) {
    customerRepository.saveAll(customers);
  }

  @Override
  public PageResponse<Customer> getAllCustomers(CursorPaging cursorPaging) {
    PageRequest pageRequest = cursorPaging.getPageRequest();
    Page<Customer> customerPage = customerRepository.findAll(pageRequest);
    return PagingUtils.getPageResponse(customerPage);
  }
}
