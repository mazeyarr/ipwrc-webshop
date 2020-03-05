package webshop.core.providers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import webshop.core.model.PemFile;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TokenProvider {
    public static volatile TokenProvider TOKEN_PROVIDER_INSTANCE;
    public static final String CLAIM_USER_ID_KEY = "user_id";

    public static final String ISSUER = "IIPSEN2-APP";
    private static final String KEY_ALGORITHM = "RSA";

    private static final int KEY_SIZE = 1048;

    private static final String PUBLIC_KEY_FILENAME = "id_rsa.pub";
    private static final String PUBLIC_KEY = "PUBLIC_KEY";

    private static final String PRIVATE_FILENAME = "id_rsa";
    private static final String PRIVATE_KEY = "PRIVATE_KEY";

    private Algorithm algorithm;
    private RSAPrivateKey privateKey;
    private RSAPublicKey publicKey;


    public TokenProvider() {
        Security.addProvider(new BouncyCastleProvider());

        try {
            if (hasPemFile(PUBLIC_KEY_FILENAME) && hasPemFile(PRIVATE_FILENAME)) {
                this.publicKey = loadPublicKeyFromFile();
                this.privateKey = loadPrivateKeyFromFile();
            } else {
                Map<String, Object> keys = generateKeyPair();
                this.privateKey = (RSAPrivateKey) Objects.requireNonNull(keys).get(PRIVATE_KEY);
                this.publicKey = (RSAPublicKey) Objects.requireNonNull(keys).get(PUBLIC_KEY);

                writePemFile(this.publicKey, "RSA PUBLIC KEY", PUBLIC_KEY_FILENAME);
                writePemFile(this.privateKey, "RSA PRIVATE KEY", PRIVATE_FILENAME);
            }

            this.algorithm = Algorithm.RSA256(this.publicKey, this.privateKey);
        } catch (IOException e) {
            System.err.println("Could not save keys to local file...");
        } catch (NoSuchAlgorithmException e) {
            System.err.println("TokenProvider: Algorithm does not exist!");
        } catch (InvalidKeySpecException e) {
            System.err.println("TokenProvider: Invalid key specification!");
        }
    }

    public static Map<String, Object> generateKeyPair() {
        try {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
            keyPairGen.initialize(KEY_SIZE);
            KeyPair keyPair = keyPairGen.generateKeyPair();

            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

            Map<String, Object> keyMap = new HashMap<>(2);

            keyMap.put(PUBLIC_KEY, publicKey);
            keyMap.put(PRIVATE_KEY, privateKey);

            return keyMap;
        } catch (Exception e) {
            System.err.println("Could not generate keypair...");
        }

        return null;
    }

    private RSAPublicKey loadPublicKeyFromFile() throws InvalidKeySpecException, NoSuchAlgorithmException {
        PemFile pemPublicKey = new PemFile(PUBLIC_KEY_FILENAME);
        byte[] publicKeyBytes = pemPublicKey.getPemObject().getContent();

        X509EncodedKeySpec publicKeySpec =
                new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory publicKeyFactory = KeyFactory.getInstance("RSA");

        return (RSAPublicKey) publicKeyFactory.generatePublic(publicKeySpec);
    }

    private RSAPrivateKey loadPrivateKeyFromFile() throws NoSuchAlgorithmException, InvalidKeySpecException {
        PemFile pemPrivateKey = new PemFile(PRIVATE_FILENAME);
        byte[] privateKeyBytes = pemPrivateKey.getPemObject().getContent();

        PKCS8EncodedKeySpec privateKeySpec =
                new PKCS8EncodedKeySpec(privateKeyBytes);
        KeyFactory privateKeyFactory = KeyFactory.getInstance("RSA");

        return (RSAPrivateKey) privateKeyFactory.generatePrivate(privateKeySpec);
    }

    public boolean hasPemFile(String filename) {
        try {
            new PemReader(new FileReader(filename));
        } catch (IOException e) {
            return false;
        }

        return true;
    }

    public void writePemFile(Key key, String description, String filename) throws IOException {
        PemFile pemFile = new PemFile(key, description);
        pemFile.write(filename);
    }

    public String generateToken(long id) {
        try {
            return JWT.create()
                    .withClaim(CLAIM_USER_ID_KEY, id)
                    .withIssuer(ISSUER)
                    .sign(getAlgorithm());
        } catch (JWTCreationException e) {
            System.err.println("Could not generate token");
        }

        return null;
    }

    public boolean verifyToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(getAlgorithm())
                    .withIssuer(ISSUER)
                    .build();

            verifier.verify(token);

            return true;
        } catch (JWTVerificationException e) {
            System.err.println("Could not verify token");
            return false;
        }
    }

    public DecodedJWT getDecodedJWT(String token) {
        try {
            return JWT.decode(token);
        } catch (JWTDecodeException e) {
            System.err.println("Could not decode token");
        }

        return null;
    }

    public RSAPrivateKey getPrivateKey() {
        return privateKey;
    }

    public RSAPublicKey getPublicKey() {
        return publicKey;
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public static TokenProvider getInstance() {
        if (TOKEN_PROVIDER_INSTANCE == null) {
            synchronized (TokenProvider.class) {
                if (TOKEN_PROVIDER_INSTANCE == null) {
                    TOKEN_PROVIDER_INSTANCE = new TokenProvider();
                }
            }
        }

        return TOKEN_PROVIDER_INSTANCE;
    }
}
