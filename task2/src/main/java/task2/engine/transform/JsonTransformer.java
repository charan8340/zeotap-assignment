package task2.engine.transform;

public class JsonTransformer implements Transformer {
    public byte[] transform(String record) {
        return record.getBytes();
    }
}
