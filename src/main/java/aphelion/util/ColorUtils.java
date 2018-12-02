package aphelion.util;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ColorUtils {
    private static final List<String> colors = Arrays.asList(
            "orange",
            "red",
            "purple",
            "pink",
            "#ff6666",
            "#004d39",
            "#00838f",
            "#0099cc",
            "#ffdb4d"
    );

    public static String getRandomColor() {
        int randomIndex = ThreadLocalRandom.current().nextInt(0, colors.size() - 1);
        return colors.get(randomIndex);
    }
}
