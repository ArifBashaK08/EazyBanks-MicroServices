package com.udemy.eazybytes.accounts.functions;

import com.udemy.eazybytes.accounts.services.IAccountsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public class AccountsFunction {
    private static final Logger logger = LoggerFactory.getLogger(AccountsFunction.class);

    public Consumer<Long> updateCommunication(IAccountsService iAccountsService){
        return accountNumber -> {
            logger.info("Updating communication status for the account number: {}", accountNumber.toString());
            iAccountsService.updateCommunicationStatus(accountNumber);
        };
    }

}
