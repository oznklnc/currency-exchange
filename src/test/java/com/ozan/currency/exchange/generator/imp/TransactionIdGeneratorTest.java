package com.ozan.currency.exchange.generator.imp;


import com.ozan.currency.exchange.base.UnitTest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import static org.assertj.core.api.Assertions.assertThat;

class TransactionIdGeneratorTest extends UnitTest {

    @InjectMocks
    private TransactionIdGenerator transactionIdGenerator;

    @Test
    void should_generate_transaction_id() {
        //when
        String transactionId = transactionIdGenerator.generateId();

        //then
        assertThat(transactionId).isNotNull().isUpperCase();
    }

}