package zipgoo.server.utils.RandomNameGenerator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import zipgoo.server.repository.UserRepository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class RandomNameGeneratorTest {
    UserRepository userRepository;
    RandomNameGeneratorUtil generator = new RandomNameGeneratorImpl(userRepository);
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