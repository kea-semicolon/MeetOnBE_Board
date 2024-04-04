package semicolon.MeetOn_Board.domain.board.application;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import semicolon.MeetOn_Board.domain.board.dto.BoardMemberDto;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardMemberService {

    private final WebClient webClient;

    Boolean memberExists(Long memberId, String accessToken) {
        String uri = UriComponentsBuilder.fromUriString("http://localhost:8000/member/find")
                .queryParam("memberId", memberId)
                .toUriString();
        return webClient.get()
                .uri(uri)
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();
    }

    public List<BoardMemberDto> getMemberInfo(String username, Long channelId, String accessToken) {
        String uri = UriComponentsBuilder.fromUriString("http://localhost:8000/member/board/info")
                .queryParam("username", username)
                .queryParam("channelId", channelId)
                .toUriString();
        return webClient.get()
                .uri(uri)
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .retrieve()
                .bodyToFlux(BoardMemberDto.class)
                .collectList()
                .block();
    }
}
