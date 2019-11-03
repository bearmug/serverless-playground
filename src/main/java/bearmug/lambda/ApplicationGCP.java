package bearmug.lambda;

import io.micronaut.context.env.Environment;
import io.micronaut.runtime.Micronaut;

public class ApplicationGCP {
    public static void main(String[] args) {
        Micronaut.build(args)
                .deduceEnvironment(false)
                .environments(Environment.GOOGLE_COMPUTE)
                .start();
    }
}
