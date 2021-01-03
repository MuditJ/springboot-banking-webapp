package com.epsilon.training.spring.banking;

import com.epsilon.training.spring.banking.entities.Account;
import com.epsilon.training.spring.banking.entities.Customer;
import com.epsilon.training.spring.banking.entities.Transaction;
import com.epsilon.training.spring.banking.repositories.AccountRepository;
import com.epsilon.training.spring.banking.repositories.CustomerRepository;
import com.epsilon.training.spring.banking.repositories.TransactionRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MainController {

    private AccountRepository accountRepository;
    private CustomerRepository customerRepository;
    private TransactionRepository transactionRepository;

    @Autowired
    public MainController(AccountRepository accountRepository, CustomerRepository customerRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
        this.transactionRepository = transactionRepository;
    }

    @GetMapping("/home")
    public String home() {
        return "index";
    }

    /*

    @GetMapping("/newCustomer")
    public String addCustomer(Model model) {
        Customer c = new Customer("ABC", "ABC123");
        customerRepository.save(c);
        System.out.println(customerRepository.count());
        return "redirect:/customers/all";
    }
*/ 

    @GetMapping("/customers/new")
    public String newCustomer(Model model) {
        model.addAttribute("customer", new Customer());
        return "createCustomer";
    }

    //This endpoint takes in POST data from a form, validates and adds to DB
    @PostMapping("/addCustomer")
    public String addCustomerFromForm(@Validated Customer customer, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "form";
        }
        //If no error, persist the validated entity in the db
        customerRepository.save(customer);
        //Redirect to show all customers
        return "redirect:/customers/all";

    }

    @GetMapping("/customers/all")
    public String getCustomers(Model model) {
        model.addAttribute("customers", customerRepository.findAll());
        return "customers";
    }

    @GetMapping("/customers/{id}/accounts")
    public String getAccounts(@PathVariable int id, Model model) {
        Optional<Customer> customer = customerRepository.findById(id);
        if (customer.isPresent()) {
            //Get all accounts for this customer id
            model.addAttribute("customer", customer.get());
            model.addAttribute("accounts", accountRepository.findByCustomerId(customer.get().getId()));
        }
        return "accounts";
    }

    @GetMapping("/customers/{id}/accounts/new")
    public String newAccount(@PathVariable int id, Model model) {
        //Get customer object in order to create new account:
        Customer c = customerRepository.findById(id).get();
        //This account object will be used for binding form data
        Account account = new Account();
        model.addAttribute("account", account);
        model.addAttribute("id",id); //To be used for making POST request on form submission
        return "createAccount";
    }

    @PostMapping("/customers/{id}/accounts/new")
    public String addAccountFromForm(@Validated Account account, BindingResult bindingResult, @PathVariable int id) {
        if (bindingResult.hasErrors()) {
            return "form";
        }
        //If no error, persist the validated entity in the db after setting the customer and createdOn fields
        account.setCustomer(customerRepository.findById(id).get());
        account.setCreatedOn(LocalDate.now());
        System.out.println(account.getCustomer());
        accountRepository.save(account);
        //Check the customer to whom this account belongs:
        //System.out.println(account.getCustomer());
        //Redirect to show all customers
        return "redirect:/customers/all";
    }

    /*

    @GetMapping("/customers/{id}/newAccount")
    public String addAccount(@PathVariable int id) {
        Optional<Customer> customer = customerRepository.findById(id);
        if (customer.isPresent()) {
            //Create new account with default balance of 500
            Account acc = new Account(500, LocalDate.now(), customer.get());
            accountRepository.save(acc);
        }
        return "redirect:/customers/all";
    }
*/ 

    @GetMapping("/customers/{id}/accounts/{accId}")
    public String getAccount(@PathVariable int id, @PathVariable int accId, Model model) {
        Optional<Customer> customer = customerRepository.findById(id);
        if (customer.isPresent()) {
            List<Account> accounts = accountRepository.findByCustomerId(customer.get().getId());
            for (Account acc : accounts) {
                if (acc.getId() == accId) {
                    model.addAttribute("account", acc);
                    break;
                }
            }
        }
        return "account";
    }

    @GetMapping("/customers/{id}/accounts/{accId}/transactions/deposit")
    public String newDeposit(@PathVariable int id, @PathVariable int accId, Model model) {
        //Get the account and add it as model attribute 
        Account account = accountRepository.findById(accId).get();
        Transaction t = new Transaction();
        model.addAttribute("account", account);
        model.addAttribute("id", id);
        model.addAttribute("transaction", t);
        return "makeDeposit";
    }

    @GetMapping("/customers/{id}/accounts/{accId}/transactions/withdrawal")
    public String newWithdrawal(@PathVariable int id, @PathVariable int accId, Model model) {
        //Get the account and add it as model attribute 
        Account account = accountRepository.findById(accId).get();
        Transaction t = new Transaction();
        model.addAttribute("account", account);
        model.addAttribute("id", id);
        model.addAttribute("transaction", t);
        return "makeWithdrawal";
    }

    @Transactional
    @PostMapping("/customers/{id}/accounts/{accId}/transactions/deposit")
    public String makeDepositFromForm(@Validated Transaction transaction, BindingResult bindingResult, @PathVariable int accId) {
        if (bindingResult.hasErrors()) {
            return "form";
        }
        //If no errors:
        transaction.setIsDeposit(true);
        transaction.setAccount(accountRepository.findById(accId).get());
        transaction.setTransactionTime(LocalDateTime.now());
        transactionRepository.save(transaction);
        //Perform update operation for the account involved
        accountRepository.incrementAccountBalance(transaction.getTransactionAmount(), accId);
        return "redirect:/customers/all";
    }

    @Transactional
    @PostMapping("/customers/{id}/accounts/{accId}/transactions/withdrawal")
    public String makeWithdrawalFromForm(@Validated Transaction transaction, BindingResult bindingResult, @PathVariable int accId) {
        if (bindingResult.hasErrors()) {
            return "form";
        }
        //Check if current balance is >= transaction Amount:
        if (accountRepository.findById(accId).get().getBalance() <= transaction.getTransactionAmount()) {
            return "redirect:/customers/all";
        } else {
            transaction.setIsDeposit(false);
            transaction.setAccount(accountRepository.findById(accId).get());
            transaction.setTransactionTime(LocalDateTime.now());
            transactionRepository.save(transaction);
            //Perform update operation for the account involved
            accountRepository.decrementAccountBalance(transaction.getTransactionAmount(), accId);
            return "redirect:/customers/all";
        }
    }



    @GetMapping("/customers/{id}/accounts/{accId}/transactions")
    public String getTransactions(@PathVariable int id, @PathVariable int accId, Model model) {
        Optional<Customer> customer = customerRepository.findById(id);
        if (customer.isPresent()) {
            Optional<Account> account = accountRepository.findById(accId);
            if (account.isPresent()) {
                model.addAttribute("transactions", transactionRepository.findByAccountId(account.get().getId()));
            }
        }
        model.addAttribute("account",accountRepository.findById(accId).get());
        return "transactions";
    }

    //Update account balance as well as create a new transaction
    //For now, a default withdrawal of 500 is to be done
    @Transactional
    @GetMapping("/customers/{id}/accounts/{accId}/withdraw")
    public String makeWithdrawal(@PathVariable int id, @PathVariable int accId) {
        //Assume all ids are valid
        
        Account acc = accountRepository.findById(accId).get();
        //Create new Transaction
        Transaction t = new Transaction(LocalDateTime.now(), 500, false, acc);
        transactionRepository.save(t);
        System.out.println(transactionRepository.count());

        //Perform update:
        accountRepository.decrementAccountBalance(500, accId);
        return "redirect:/customers/all";
        
        
    }



}