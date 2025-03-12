package com.ozan.currency.exchange.audit;

import com.ozan.currency.exchange.base.UnitTest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class AuditAwareImplTest extends UnitTest {

    @InjectMocks
    private AuditAwareImpl auditAware;

    @Test
    void shouldReturnAnonymousAuditor() {
        //when
        Optional<String> currentAuditor = auditAware.getCurrentAuditor();
        assertThat(currentAuditor).isPresent().contains("anonymous");
    }

}