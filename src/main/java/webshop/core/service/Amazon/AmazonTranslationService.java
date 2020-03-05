package webshop.core.service.Amazon;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.translate.AmazonTranslate;
import com.amazonaws.services.translate.AmazonTranslateClient;
import com.amazonaws.services.translate.AmazonTranslateClientBuilder;

public class AmazonTranslationService {
    private static final Regions REGION = Regions.EU_CENTRAL_1;

    private AmazonTranslate translator;

    public AmazonTranslationService() {
        this.translator = AmazonTranslateClientBuilder
                .standard()
                .withCredentials(AmazonCredentials.getInstance().getAwsStaticCredentialsProvider())
                .withRegion(REGION)
                .build();
    }

    public AmazonTranslate getTranslator() {
        return translator;
    }

    public void setTranslator(AmazonTranslate translator) {
        this.translator = translator;
    }
}
