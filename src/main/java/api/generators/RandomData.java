package api.generators;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.stream.Stream;

public class RandomData {
    private RandomData (){}

    public static String getUsername (){
        return RandomStringUtils.randomAlphabetic(10);
    }

    public static String getPassword (){
        return  RandomStringUtils.randomAlphabetic(5).toUpperCase() +
                RandomStringUtils.randomAlphabetic(5).toLowerCase() +
                RandomStringUtils.randomNumeric(3) + "#$";
    }
    public static String getName (){
        return RandomStringUtils.randomAlphabetic(5)+ " " +
                RandomStringUtils.randomAlphabetic(5);
    }

    public static String negativeName (){
        return RandomStringUtils.randomAlphabetic(5) + " "
                + RandomStringUtils.randomNumeric(2);
    }

    public static Stream<Float> NegativeAmount() {
        return Stream.of(
                -0.1f,
                0f,
                5000.1f
        );
    }

    public static Stream<Float> NegativeTransferAmount() {
        return Stream.of(
                -0.1f,
                0f,
                10000.1f
        );
    }

    public static Stream<Float> PositiveAmount() {
        return Stream.of(
                4999.9f,
                5000.0f,
                0.1f
        );
    }

    public static Stream<String> NegativeNames() {
        return Stream.of(
                "AliceJohnson",
                "Bo",
                "CharliefdsdfdfB",
                "Дмитрий_Иванов",
                ""
        );
    }

    public static float getAmount(){
        // Deposit/transfer amount must be strictly positive for valid scenarios.
        return Math.round((0.1f + (float) Math.random() * 999.9f) * 100) / 100f;
    }
}
