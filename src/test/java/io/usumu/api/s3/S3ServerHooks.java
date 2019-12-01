package io.usumu.api.s3;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.findify.s3mock.S3Mock;

public class S3ServerHooks {
    private final S3Mock api = new S3Mock.Builder().withPort(8001).withInMemoryBackend().build();

    @Before
    public void setUp() {
        api.start();
    }

    @After
    public void tearDown() {
        api.stop();
    }
}
