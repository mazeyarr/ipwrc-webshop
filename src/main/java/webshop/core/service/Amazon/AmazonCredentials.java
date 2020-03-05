package webshop.core.service.Amazon;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import webshop.core.service.CoreService;

public class AmazonCredentials extends CoreService {
    private static volatile AmazonCredentials AMAZON_CREDENTIALS_INSTANCE;

    public static final String AWS_KMS_CMK_KEY_ID = getEnv().get("AWS_KMS_CMK_KEY_ID");
    public static final String AWS_USER = getEnv().get("AWS_USER");
    public static final String AWS_ACCESS_KEY_ID = getEnv().get("AWS_ACCESS_KEY_ID");
    public static final String AWS_SECRET_ACCESS_KEY = getEnv().get("AWS_SECRET_ACCESS_KEY");

    private BasicAWSCredentials awsCredentials = new BasicAWSCredentials(
            AWS_ACCESS_KEY_ID,
            AWS_SECRET_ACCESS_KEY
    );
    private AWSStaticCredentialsProvider awsStaticCredentialsProvider;

    public AmazonCredentials() {
        this.awsStaticCredentialsProvider = new AWSStaticCredentialsProvider(getAwsCredentials());
    }

    public BasicAWSCredentials getAwsCredentials() {
        return awsCredentials;
    }

    public AWSStaticCredentialsProvider getAwsStaticCredentialsProvider() {
        return awsStaticCredentialsProvider;
    }

    public static AmazonCredentials getInstance() {
        if (AMAZON_CREDENTIALS_INSTANCE == null) {
            synchronized (AmazonCredentials.class) {
                if (AMAZON_CREDENTIALS_INSTANCE == null) {
                    AMAZON_CREDENTIALS_INSTANCE = new AmazonCredentials();
                }
            }
        }

        return AMAZON_CREDENTIALS_INSTANCE;
    }
}
