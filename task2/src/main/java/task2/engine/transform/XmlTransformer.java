package task2.engine.transform;

public class XmlTransformer implements Transformer {
    public byte[] transform(String record) {
        return ("<data>" + record + "</data>").getBytes();
    }
}