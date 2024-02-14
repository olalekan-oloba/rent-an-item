package com.omegalambdang.rentanitem.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.function.Consumer;

@Component
public class TransactionUtils {

    @Autowired
    protected PlatformTransactionManager transactionManager;

    public <T>  void execute(Consumer<T> consumer) {
        //case jpa
        var transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.executeWithoutResult((Consumer<TransactionStatus>) consumer);
        //case non jpa just call the consumer accept method
        //consumer.accept((T) "");
    }

}
