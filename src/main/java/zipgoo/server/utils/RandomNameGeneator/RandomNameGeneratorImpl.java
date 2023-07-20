package zipgoo.server.utils.RandomNameGeneator;

import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

@Slf4j
public class RandomNameGeneratorImpl implements RandomNameGeneratorUtil {
    public static final String ANIMAL_FILE_URL = "/Users/dlwnsgus07/Desktop/Project/server/src/main/java/zipgoo/server/utils/RandomNameGeneator/animalName.json";
    public static final String ADJECTIVE_FILE_URL = "";
    static Integer MAX_ANIMAL_COUNT = 41495;
    static Integer MAX_ADJECTIVE_COUNT = 0;
    JSONObject adjectiveObj;
    JSONObject animalObj;
    JSONParser parser = new JSONParser();

    public RandomNameGeneratorImpl() {
        try {
            FileReader animalReader = new FileReader(ANIMAL_FILE_URL);
            FileReader reader = new FileReader(ADJECTIVE_FILE_URL);
            animalObj = (JSONObject) parser.parse(animalReader);
            adjectiveObj = (JSONObject) parser.parse(reader);
        } catch (IOException | ParseException e) {
            log.error(e.getMessage());
        }
    }
    /**
     * 형용사를 바탕으로 랜덤으로 가져옴
     * @return 형용사
     */
    @Override
    public String getFirstName() {
        Random random = new Random();
        int randNum = random.nextInt(MAX_ADJECTIVE_COUNT);
        return (String)adjectiveObj.get(String.valueOf(randNum));
    }

    /**
     * 동물이름에서 하나를 뽑아서 가져옴
     * index 0 ~ 41495
     * @return 동물이름
     */
    @Override
    public String getSecondName() {
        Random random = new Random();
        int randNum = random.nextInt(MAX_ANIMAL_COUNT);
        return (String)animalObj.get(String.valueOf(randNum));
    }
}
