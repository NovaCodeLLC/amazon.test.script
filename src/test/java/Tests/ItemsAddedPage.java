package Tests;

public interface ItemsAddedPage {
    public static final String pathValidateItemAddedSuccessfully = ".//h1[contains(text(),'Added to Cart')]";
    public static final String pathCartSubtotalValue = "(.//span[@class='a-color-price hlb-price a-inline-block a-text-bold'])[1]";
}
