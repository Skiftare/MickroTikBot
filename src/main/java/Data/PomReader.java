package Data;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;

import java.io.FileReader;
import java.util.List;
import java.util.stream.Collectors;

public  class PomReader {
    private static List<String> authorsList;
    private final static String ERROR_MESSAGE = "Не удалось получить авторов.";

    static {
        initializeAuthorsString();
    }

    private static void initializeAuthorsString() {
        try (FileReader reader = new FileReader("pom.xml")) {
            MavenXpp3Reader mavenReader = new MavenXpp3Reader();
            Model model = mavenReader.read(reader);
            authorsList = model.getContributors().stream()
                    .map(developer -> developer.getName())
                    .collect(Collectors.toList());

        } catch (Exception e) {
            e.printStackTrace();
            authorsList.clear();
            authorsList.add(ERROR_MESSAGE);
        }
    }

    public static List<String> getAuthorsList() {
        return authorsList;
    }
}
