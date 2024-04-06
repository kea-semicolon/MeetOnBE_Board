package semicolon.MeetOn_Board.domain.board.application;

import jakarta.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockCookie;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.transaction.annotation.Transactional;
import semicolon.MeetOn_Board.domain.board.dao.BoardRepository;
import semicolon.MeetOn_Board.domain.board.domain.Board;
import semicolon.MeetOn_Board.domain.board.dto.BoardDto;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static semicolon.MeetOn_Board.domain.board.dto.BoardDto.*;

@Slf4j
@SpringBootTest
@ExtendWith(MockitoExtension.class)
@Transactional
public class BoardServiceTest {

    @Autowired
    BoardService boardService;

    @Autowired
    BoardRepository boardRepository;

    @MockBean
    BoardChannelService boardChannelService;

    @MockBean
    BoardMemberService boardMemberService;

    MockHttpServletResponse response;
    MockHttpServletRequest request;

    @BeforeEach
    void 세팅(){
        response = new MockHttpServletResponse();
        request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer test-token");
        createSetCookie("channelId", String.valueOf(6552));
        createSetCookie("memberId", String.valueOf(1));
    }

    @Test
    void 게시글_생성() {
        CreateRequestDto createRequestDto = CreateRequestDto.builder()
                .content("testContent")
                .title("testTitle")
                .isNotice(true)
                .build();
        when(boardChannelService.channelExists(6552L, "Bearer test-token")).thenReturn(true);
        when(boardMemberService.memberExists(1L, "Bearer test-token")).thenReturn(true);
        Long id = boardService.createBoard(createRequestDto, request);
        assertThat(id).isNotNull();
    }


    @Test
    void 게시글_수정() {
        Board board = Board.builder().content("tt").build();
        Board save = boardRepository.save(board);
        String bTitle = save.getTitle();
        UpdateRequestDto updateRequestDto = new UpdateRequestDto("test", "test2", true);
        boardService.updateBoard(save.getId(), updateRequestDto);
        assertThat(bTitle).isNotEqualTo(board.getTitle());
    }

    private void createSetCookie(String name, String value) {
        MockCookie mockCookie = new MockCookie(name, value);
        mockCookie.setPath("/");
        mockCookie.setHttpOnly(true);
        response.addCookie(mockCookie);
        Cookie[] cookies = response.getCookies();
        request.setCookies(cookies);
    }
}
