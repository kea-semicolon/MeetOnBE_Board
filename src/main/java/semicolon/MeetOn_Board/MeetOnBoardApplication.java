package semicolon.MeetOn_Board;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import semicolon.MeetOn_Board.global.logtrace.AopConfig;
import semicolon.MeetOn_Board.global.logtrace.LogTrace;
import semicolon.MeetOn_Board.global.logtrace.ThreadLocalLogTrace;

@Import(AopConfig.class)
@EnableDiscoveryClient
@EnableJpaAuditing
@SpringBootApplication
public class MeetOnBoardApplication {

	public static void main(String[] args) {
		SpringApplication.run(MeetOnBoardApplication.class, args);
	}

	@Bean
	public LogTrace logTrace() {
		return new ThreadLocalLogTrace();
	}

}
