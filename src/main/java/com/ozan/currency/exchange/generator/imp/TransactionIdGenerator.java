package com.ozan.currency.exchange.generator.imp;

import com.ozan.currency.exchange.generator.IdGenerator;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TransactionIdGenerator implements IdGenerator {

    @Override
    public String generateId() {
        return UUID.randomUUID().toString().replace("-","").toUpperCase();
    }
}
