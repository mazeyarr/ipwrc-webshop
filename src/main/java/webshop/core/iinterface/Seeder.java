package webshop.core.iinterface;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public interface Seeder {
    default void run() {
        if (isAlreadySeeded()) {
            down();
        }

        up();
    }

    default void run(boolean downUp) {
        if (!downUp) {
            if (!isAlreadySeeded()) {
                up();
            }
        } else {
            run();
        }
    }

    boolean isAlreadySeeded();

    void up();

    void down();

    void log(long id);
}

