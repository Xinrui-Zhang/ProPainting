package seu.se.practice2.propainting.config.thread_pool;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ForkJoinPool;

@Configuration
public class ForkJoinPoolConfigurer {

    @Bean
    public ForkJoinPool forkJoinPool() {
        return new ForkJoinPool();
    }
}
