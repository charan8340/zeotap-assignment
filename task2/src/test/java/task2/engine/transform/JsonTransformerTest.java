package task2.engine.transform;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JsonTransformerTest {

    @Test
    void convertsStringToBytes() {
        Transformer transformer = new JsonTransformer();

        byte[] result = transformer.transform("{\"id\":1}");

        assertNotNull(result);
        assertTrue(result.length > 0);
    }
}
