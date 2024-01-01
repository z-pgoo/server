package zipgoo.server.utils.RandomNameGenerator;

import org.springframework.stereotype.Component;

public interface RandomNameGeneratorUtil {
    public String getFirstName();
    public String getSecondName();
    public NameDto getFullName();

}
