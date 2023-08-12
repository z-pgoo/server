package zipgoo.server.utils.RandomNameGenerator;

import lombok.Getter;

@Getter
public class NameDto {

    private final String name;
    NameDto(String name){
        this.name = name;
    }
}
