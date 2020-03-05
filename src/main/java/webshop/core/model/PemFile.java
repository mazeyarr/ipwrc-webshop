package webshop.core.model;


import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.bouncycastle.util.io.pem.PemWriter;

import java.io.*;
import java.security.Key;

public class PemFile {

    private PemObject pemObject;

    public PemFile (Key key, String description) {
        this.pemObject = new PemObject(description, key.getEncoded());
    }


    public PemFile(String filename) {
        try (PemReader pemReader = new PemReader(new InputStreamReader(new FileInputStream(filename)))) {
            this.pemObject = pemReader.readPemObject();
        } catch (FileNotFoundException fileNotFoundException) {
            System.err.println("PEM FILE (read): File not found!");
        } catch (IOException e) {
            System.err.println("PEM FILE (read): I/O exception when loading token file");
        }
    }

    public PemObject getPemObject() {
        return pemObject;
    }

    public void write(String filename) {
        try (PemWriter pemWriter = new PemWriter(
                new OutputStreamWriter(new FileOutputStream(filename)))
        ) {
            pemWriter.writeObject(this.pemObject);
        } catch (FileNotFoundException fileNotFoundException) {
            System.err.println("PEM FILE (write): File not found!");
        } catch (IOException e) {
            System.err.println("PEM FILE (write): I/O exception when loading token file");
        }
    }
}
