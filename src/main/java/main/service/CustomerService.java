package main.service;

import main.interfaces.CustomerRepository;
import main.logic.Customer;
import main.repositories.CustomerRepositoryBinaryIml;
import main.repositories.CustomerRepositoryTxtIml;

import java.util.*;
import java.util.stream.Collectors;

public class CustomerService {
    private final CustomerRepository repository1 = new CustomerRepositoryTxtIml();
    private final CustomerRepository repository2 = new CustomerRepositoryBinaryIml();

    public void outputListTxt(List<Customer> customers, String fileName) {
        repository1.outputList(customers, fileName);
    }

    public void outputListBinary(List<Customer> customers, String fileName) {
        repository2.outputList(customers, fileName);
    }

    public List<Customer> readListTxt(String fileName) {
        return repository1.readList(fileName);
    }

    public List<Customer> readListBinary(String fileName) {
        return repository2.readList(fileName);
    }
    
    public List<Customer> findCustomersByLetters(List<Customer> customers, String prefix) {
        return customers.stream()
                .filter(customer -> customer.getCustomerName().toLowerCase().startsWith(prefix.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Customer> findCustomersByCreditCardRange(List<Customer> customers, int a, int b) {
        return customers.stream()
                .filter(customer -> {
                    int cardNumber = customer.getCardNumber();
                    return cardNumber >= a && cardNumber <= b;
                })
                .collect(Collectors.toList());
    }

    public List<Customer> findCustomerByArrears(List<Customer> customers) {
        return customers.stream()
                .filter(customer -> customer.getAccBalance() < 0)
                .collect(Collectors.toList());
    }

    public List<Customer> customersByTotalPurchases(List<Customer> customers) {
        return customers.stream()
                .sorted(Comparator.comparingDouble(Customer::getTotalPurchases)
                        .reversed()
                        .thenComparing(Customer::getCustomerName))
                .collect(Collectors.toList());
    }

    public List<Customer> findCustomerById(List<Customer> customers, int customerId) {
        return customers.stream()
                .filter(customer -> customer.getId() == customerId)
                .collect(Collectors.toList());
    }

    public void addCustomer(List<Customer> customers, Customer newCustomer) {
        customers.add(newCustomer);
    }

    public boolean removeCustomerById(List<Customer> customers, int id) {
        return customers.removeIf(customer -> customer.getId() == id);
    }

    public Map<String, List<Customer>> groupCustomersByCityAndName(List<Customer> customers) {
        return customers.stream()
                .collect(Collectors.groupingBy(
                        Customer::getCity,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> list.stream()
                                        .sorted(Comparator.comparing(Customer::getCustomerName))
                                        .collect(Collectors.toList())
                        )
                ));
    }

    public Map<String, Integer> calcByTotalPurchase(List<Customer> customers) {
        return customers.stream()
                .collect(Collectors.groupingBy(
                        Customer::getCity,
                        TreeMap::new,
                        Collectors.summingInt(Customer::getTotalPurchases)
                ));
    }

}