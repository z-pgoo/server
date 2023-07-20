package zipgoo.server.utils.RandomNameGeneator;

import org.springframework.stereotype.Component;

@Component
public interface RandomNameGeneratorUtil {
    public String getFirstName();
    public String getSecondName();

}
