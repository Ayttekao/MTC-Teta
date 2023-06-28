package utils;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;
import uk.co.jemos.podam.api.RandomDataProviderStrategyImpl;

public class ConverterTestUtils {
    private final static PodamFactory podamFactory;

    static {
        var randomDataProviderStrategy = new RandomDataProviderStrategyImpl();
        randomDataProviderStrategy.setMaxDepth(1);
        randomDataProviderStrategy.setDefaultNumberOfCollectionElements(1);
        podamFactory = new PodamFactoryImpl(randomDataProviderStrategy);
    }
    public static <T> T buildPojo(Class<T> pojoClass) {
        return podamFactory.manufacturePojo(pojoClass);
    }

}
