package zipgoo.server.utils.RandomNameGenerator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class RandomNameGeneratorTest {
    RandomNameGeneratorUtil generator = new RandomNameGeneratorImpl();
    String firstName = "";
    String secondName = "";

    @Test
    void getFirstName() {
        firstName = generator.getFirstName();
        System.out.println("firstName = " + firstName);
        assertThat(firstName).isNotEqualTo("");
    }

    @Test
    void getSecondName() {
        secondName = generator.getSecondName();
        System.out.println("secondName = " + secondName);
        assertThat(secondName).isNotEqualTo("");
    }

    @Test
    void getFullName(){
        System.out.println("FullName = " + generator.getFullName());
    }
}