package Tests;

import org.testng.annotations.DataProvider;

/**
 * Created by Knightmarez on 2/25/2017.
 */
public class DataProviderTest {

    Object[][] testData;

    @DataProvider(name="searchData")
    public static Object[][] dataProvider(){
        return new Object[][] { { "testuser_1", "Test@123" }, { "testuser_1", "Test@123" }};
    }
}
