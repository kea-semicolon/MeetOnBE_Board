package semicolon.MeetOn_Board.domain.board.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import semicolon.MeetOn_Board.domain.board.dto.BoardMemberDto;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardMemberService {

    private final WebClient webClient;
    @Value("${app.gateway.url}")
    private String gateway;

    /**
     * 해당 게시글 작성 Member 존재 여부 파악
     * @param memberId
     * @param accessToken
     * @return
     */
    Boolean memberExists(Long memberId, String accessToken) {
        String uri = UriComponentsBuilder.fromUriString(gateway + "/member/find")
                .queryParam("memberId", memberId)
                .toUriString();
        return webClient.get()
                .uri(uri)
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();
    }

    /**
     * 유저 정보 가져오기
     * 유저 이름 검색 조건 포함
     * @param username
     * @param channelId
     * @param accessToken
     * @return
     */
    public List<BoardMemberDto> getMemberInfoList(String username, Long channelId, String accessToken) {
        String uri = UriComponentsBuilder.fromUriString(gateway + "/member/board/infoList")
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

    public BoardMemberDto getMemberInfo(Long memberId, String accessToken) {
        String uri = UriComponentsBuilder.fromUriString(gateway + "/member/board/info")
                .queryParam("memberId", memberId)
                .toUriString();
        return webClient.get()
                .uri(uri)
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .retrieve()
                .bodyToFlux(BoardMemberDto.class)
                .blockFirst();
    }
}
