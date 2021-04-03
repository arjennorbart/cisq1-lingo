package nl.hu.cisq1.lingo.trainer.domain.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class UtilsTest {

    @Test
    @DisplayName("Should throw an IllegalStateException when trying to make an object of this class")
    void shouldThrowIllegalStateException() {
        assertThrows(
                IllegalStateException.class,
                Utils::new);
    }
}