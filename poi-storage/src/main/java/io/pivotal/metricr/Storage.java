package io.pivotal.metricr;

import java.io.OutputStream;

public interface Storage {
    void storeInstanceData(CloudFoundryInstanceData instanceData);
    void writeTo(OutputStream outputStream);
    void close();
}
