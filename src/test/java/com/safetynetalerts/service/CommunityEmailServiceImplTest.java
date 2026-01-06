package com.safetynetalerts.service;

import com.safetynetalerts.model.Person;
import com.safetynetalerts.repository.person.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommunityEmailServiceImplTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private CommunityEmailServiceImpl service;

    @BeforeEach
    void setUp() {
        when(personRepository.findAll()).thenReturn(List.of(
                new Person("John", "Boyd", "1509 Culver St", "Culver", "97451", "841", "a@email.com"),
                new Person("Jane", "Doe", "X", "Culver", "97451", "842", "b@email.com"),
                new Person("Bob", "Foo", "Y", "OtherCity", "97451", "843", "c@email.com"),
                new Person("Dup", "Mail", "Z", "Culver", "97451", "844", "a@email.com") // duplicate
        ));
    }

    @Test
    void getEmailsByCity_shouldReturnDistinctSortedEmails() {
        // Act + Assert
        var res = service.getEmailsByCity("Culver");
        assertThat(res.emails()).containsExactly("a@email.com", "b@email.com");
    }

    @Test
    void getEmailsByCity_shouldReturnEmptyForUnknownCity() {
        // Act + Assert
        var res = service.getEmailsByCity("Unknown");
        assertThat(res.emails()).isEmpty();
    }
}
