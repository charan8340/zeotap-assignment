package task2.engine.transform;

public class MapTransformer implements Transformer {
    public byte[] transform(String record) {
        return record.getBytes();
    }
}
