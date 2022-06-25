package simple_blog.LeeJerry.service;


import static java.util.Optional.ofNullable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.amazonaws.services.s3.AmazonS3Client;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.mock.web.MockMultipartFile;
import simple_blog.LeeJerry.dto.BoardReq;
import simple_blog.LeeJerry.dto.BoardRes;
import simple_blog.LeeJerry.entity.BoardEntity;
import simple_blog.LeeJerry.entity.UserEntity;
import simple_blog.LeeJerry.exception.ErrorCode;
import simple_blog.LeeJerry.exception.NotFoundException;
import simple_blog.LeeJerry.exception.UnAuthorizedException;
import simple_blog.LeeJerry.repository.BoardRepository;

@ExtendWith(MockitoExtension.class)
public class BoardServiceTest {

    @InjectMocks
    BoardService boardService;

    @Mock
    BoardRepository boardRepository;

    @Mock
    AmazonS3Client amazonS3Client;

    @Test
    @DisplayName("findBoards : 모든 게시글들을 리스트 형태로 반환합니다.")
    public void findBoardsTest() throws Exception {
        //given
        List <BoardEntity> boardEntities = new ArrayList<>();
        BoardEntity board1 = BoardEntity.builder().id(1L).favoriteCount(1).userEntity(new UserEntity()).build();
        BoardEntity board2 = BoardEntity.builder().id(2L).favoriteCount(0).userEntity(new UserEntity()).build();
        boardEntities.add(board1);
        boardEntities.add(board2);

        given(boardRepository.findAll(Sort.by(Direction.DESC, "favoriteCount"))).willReturn(boardEntities);

        //when
        List <BoardRes> boardRes = boardService.findBoards();

        //then
        assertThat(boardRes.get(0).getId()).isEqualTo(1L);
        assertThat(boardRes.get(1).getFavoriteCount()).isEqualTo(0);
    }


    @Test
    @DisplayName("findBoard : 주어진 id에 해당하는 하나의 게시글을 응답 객체 형식으로 반환합니다.")
    public void findBoardTest() throws Exception {
        //given
        BoardEntity board = BoardEntity.builder().id(1L).favoriteCount(1).userEntity(new UserEntity()).viewCount(0).build();
        given(boardRepository.findById(1L)).willReturn(ofNullable(board));

        //when
        BoardRes boardRes = boardService.findBoard(1L);

        //then
        assertThat(boardRes.getId()).isEqualTo(1L);
        assertThat(boardRes.getFavoriteCount()).isEqualTo(1);
    }


    @Test
    @DisplayName("findBoard : 주어진 id에 해당하는 게시글은 조회수가 1 증가합니다.")
    public void findBoardViewCountTest() throws Exception {
        //given
        BoardEntity board = BoardEntity.builder().id(1L).userEntity(new UserEntity()).viewCount(1).build();

        given(boardRepository.findById(1L)).willReturn(ofNullable(board));

        //when
        BoardRes boardRes = boardService.findBoard(1L);

        //then
        assertThat(boardRes.getId()).isEqualTo(1L);
        assertThat(boardRes.getViewCount()).isEqualTo(2);
    }

    @Test
    @DisplayName("findBoard : 주어진 id에 해당하는 게시글을 찾을 수 없다면, 404 에러를 반환합니다.")
    public void findBoardWithNoBoardTest() throws Exception {
        //given
        given(boardRepository.findById(1L)).willReturn(Optional.empty());

        //when & Then
        NotFoundException notFoundException = assertThrows(NotFoundException.class, () -> boardService.findBoard(1L));
        assertThat(notFoundException.getError()).isEqualTo(ErrorCode.BOARD_NOT_FOUND);
    }


    @Test
    @DisplayName("findBoardEntity : 주어진 id에 해당하는 하나의 게시글을 엔티티 객체로 반환합니다.")
    public void findBoardEntityTest() throws Exception {
        //given
        BoardEntity board = BoardEntity.builder().id(1L).favoriteCount(1).userEntity(new UserEntity()).viewCount(0).build();

        given(boardRepository.findById(1L)).willReturn(ofNullable(board));

        //when
        BoardEntity boardEntity = boardService.findBoardEntity(1L);

        //then
        assertThat(boardEntity.getId()).isEqualTo(1L);
        assertThat(boardEntity.getFavoriteCount()).isEqualTo(1);
    }


    @Test
    @DisplayName("findBoardEntity : 주어진 id에 해당하는 게시글을 찾을 수 없다면, 404 에러를 반환합니다.")
    public void findBoardEntityWithNoBoardTest() throws Exception {
        //given
        given(boardRepository.findById(1L)).willReturn(Optional.empty());

        //when & Then
        NotFoundException notFoundException = assertThrows(NotFoundException.class, () -> boardService.findBoardEntity(1L));
        assertThat(notFoundException.getError()).isEqualTo(ErrorCode.BOARD_NOT_FOUND);
    }

    @Test
    @DisplayName("insertBoard : 이미지가 포함된 게시글 Request 객체를 받아 저장합니다.")
    public void insertBoardWithImageTest() throws Exception {
        //given
        BoardReq boardReq = BoardReq.builder().title("제목").body("본문").files(new MockMultipartFile("picture.jpg", (byte[]) null)).build();
        BoardEntity boardEntity = BoardEntity.builder().id(1L).title(boardReq.getTitle()).body(boardReq.getBody()).build();

        given(boardRepository.save(any())).willReturn(boardEntity);

        URL url = new URL("http://abc.com");
        given(amazonS3Client.getUrl(any(), any())).willReturn(url);
        given(boardRepository.findById(1L)).willReturn(ofNullable(boardEntity));

        //when
        boardService.insertBoard(boardReq);

        // Then
        verify(boardRepository).save(any());
        assertThat(boardRepository.findById(1L).get().getTitle()).isEqualTo("제목");
        assertThat(boardRepository.findById(1L).get().getBody()).isEqualTo("본문");
        assertThat(boardRepository.findById(1L).get().getImageUrl()).isEqualTo("http://abc.com");
    }

    @Test
    @DisplayName("insertBoard : 이미지가 포함되지 않은 게시글 Request 객체를 받아 저장합니다.")
    public void insertBoardWithNoImageTest() throws Exception {
        //given
        BoardReq boardReq = BoardReq.builder().title("제목").body("본문").build();
        BoardEntity boardEntity = BoardEntity.builder().id(1L).title(boardReq.getTitle()).body(boardReq.getBody()).build();

        given(boardRepository.save(any())).willReturn(boardEntity);
        given(boardRepository.findById(1L)).willReturn(ofNullable(boardEntity));

        //when
        boardService.insertBoard(boardReq);

        // Then
        verify(boardRepository).save(any());
        assertThat(boardRepository.findById(1L).get().getTitle()).isEqualTo("제목");
        assertThat(boardRepository.findById(1L).get().getBody()).isEqualTo("본문");
    }

    @Test
    @DisplayName("updateBoard : 해당 게시글이 로그인 된 사용자의 게시글이 아니라면, 401에러를 반환합니다.")
    public void updateBoardWithInvalidAuthorTest() throws Exception {
        //given
        BoardReq boardReq = BoardReq.builder().build();
        boardReq.setId(1L);
        UserEntity author = UserEntity.builder().id(2L).build();
        BoardEntity foundBoard = BoardEntity.builder().id(1L).userEntity(author).build();

        given(boardRepository.findById(1L)).willReturn(ofNullable(foundBoard));

        //when & Then
        UnAuthorizedException unAuthorizedException = assertThrows(UnAuthorizedException.class,
            () -> boardService.updateBoard(boardReq, 1L));
        assertThat(unAuthorizedException.getError()).isEqualTo(ErrorCode.NOT_AUTHOR);
    }


    @Test
    @DisplayName("updateBoard : 존재하지 않는 게시글이라면 404에러를 반환합니다.")
    public void updateBoardWithNoBoardTest() throws Exception {
        //given
        BoardReq boardReq = BoardReq.builder().build();
        boardReq.setId(1L);
        UserEntity author = UserEntity.builder().id(2L).build();

        given(boardRepository.findById(1L)).willThrow(new NotFoundException(ErrorCode.BOARD_NOT_FOUND));

        //when & Then
        NotFoundException notFoundException = assertThrows(NotFoundException.class,
            () -> boardService.updateBoard(boardReq, 1L));
        assertThat(notFoundException.getError()).isEqualTo(ErrorCode.BOARD_NOT_FOUND);
    }


    @Test
    @DisplayName("updateBoard : 이미지가 포함되어 있지 않은 게시글을 저장합니다.")
    public void updateBoardWithNoImageTest() throws Exception {
        //given
        BoardReq boardReq = BoardReq.builder().title("수정된 타이틀").body("수정된 본문").files(null).build();
        boardReq.setId(1L);
        UserEntity author = UserEntity.builder().id(1L).build();
        BoardEntity foundBoard = BoardEntity.builder().id(1L).title("기존 타이틀").body("기존 본문").userEntity(author).build();

        given(boardRepository.findById(1L)).willReturn(ofNullable(foundBoard));

        //when
        boardService.updateBoard(boardReq, 1L);

        //then
        assert foundBoard != null;
        verify(boardRepository).save(any());
        assertThat(foundBoard.getTitle()).isEqualTo("수정된 타이틀");
        assertThat(foundBoard.getBody()).isEqualTo("수정된 본문");
        assertThat(foundBoard.getImageUrl()).isEqualTo(null);
    }

    @Test
    @DisplayName("updateBoard : 이미지가 있는 게시글에 새로 이미지를 저장합니다.")
    public void updateBoardImageWithExistingImageTest() throws Exception {
        //given
        BoardReq boardReq = BoardReq.builder().files(new MockMultipartFile("picture.jpg", (byte[]) null)).build();
        boardReq.setId(1L);
        UserEntity author = UserEntity.builder().id(1L).build();
        BoardEntity foundBoard = BoardEntity.builder().id(1L).userEntity(author).imageUrl(new URL("http://unchanged.com").toString()).build();

        given(boardRepository.findById(1L)).willReturn(ofNullable(foundBoard));
        given(amazonS3Client.getUrl(any(), any())).willReturn(new URL("http://changed.com"));

        //when
        boardService.updateBoard(boardReq, 1L);

        //then
        assert foundBoard != null;
        verify(boardRepository).save(any());
        assertThat(foundBoard.getImageUrl()).isEqualTo("http://changed.com");
    }

    @Test
    @DisplayName("updateBoard : 이미지가 없는 게시글에 새로 이미지를 저장합니다.")
    public void updateBoardImageWithNotExistingImageTest() throws Exception {
        //given
        BoardReq boardReq = BoardReq.builder().files(new MockMultipartFile("picture.jpg", (byte[]) null)).build();
        boardReq.setId(1L);
        UserEntity author = UserEntity.builder().id(1L).build();
        BoardEntity foundBoard = BoardEntity.builder().id(1L).userEntity(author).imageUrl(null).build();

        given(boardRepository.findById(1L)).willReturn(ofNullable(foundBoard));
        given(amazonS3Client.getUrl(any(), any())).willReturn(new URL("http://changed.com"));

        //when
        boardService.updateBoard(boardReq, 1L);

        //then
        assert foundBoard != null;
        verify(boardRepository).save(any());
        assertThat(foundBoard.getImageUrl()).isEqualTo("http://changed.com");
    }

    @Test
    @DisplayName("deleteBoard : 이미지가 없는 게시글을 삭제합니다.")
    public void deleteBoardWithNotExistingImageTest() throws Exception {
        //given
        UserEntity author = UserEntity.builder().id(1L).build();
        BoardEntity foundBoard = BoardEntity.builder().id(1L).userEntity(author).imageUrl(null).build();

        given(boardRepository.findById(1L)).willReturn(ofNullable(foundBoard));

        //when
        boardService.deleteBoard(1L, 1L);

        //then
        verify(boardRepository).deleteById(1L);
    }

    @Test
    @DisplayName("deleteBoard : 이미지가 존재하는 게시글을 삭제합니다.")
    public void deleteBoardWithExistingImageTest() throws Exception {
        //given
        UserEntity author = UserEntity.builder().id(1L).build();
        BoardEntity foundBoard = BoardEntity.builder().id(1L).userEntity(author).imageUrl(new URL("http://unchanged.com").toString()).build();

        given(boardRepository.findById(1L)).willReturn(ofNullable(foundBoard));

        //when
        boardService.deleteBoard(1L, 1L);

        //then
        verify(boardRepository).deleteById(1L);
    }

    @Test
    @DisplayName("deleteBoard : 해당 게시글이 존재하지 않으면 404 에러를 반환합니다.")
    public void deleteBoardWithNoExistingBoardTest() throws Exception {
        //given
        UserEntity author = UserEntity.builder().id(1L).build();
        BoardEntity foundBoard = BoardEntity.builder().id(1L).userEntity(author).imageUrl(new URL("http://unchanged.com").toString()).build();

        given(boardRepository.findById(1L)).willThrow(new NotFoundException(ErrorCode.BOARD_NOT_FOUND));

        //when & then
        NotFoundException notFoundException = assertThrows(NotFoundException.class, () -> boardService.deleteBoard(1L, 1L));
        assertThat(notFoundException.getError()).isEqualTo(ErrorCode.BOARD_NOT_FOUND);
    }

    @Test
    @DisplayName("deleteBoard : 해당 게시글의 작성자가 아니라면 401 에러를 반환합니다.")
    public void deleteBoardWithNoAuthorTest() throws Exception {
        //given
        UserEntity author = UserEntity.builder().id(2L).build();
        BoardEntity foundBoard = BoardEntity.builder().id(1L).userEntity(author).imageUrl(new URL("http://unchanged.com").toString()).build();

        given(boardRepository.findById(1L)).willReturn(ofNullable(foundBoard));

        //when & then
        UnAuthorizedException unAuthorizedException = assertThrows(UnAuthorizedException.class, () -> boardService.deleteBoard(1L, 1L));
        assertThat(unAuthorizedException.getError()).isEqualTo(ErrorCode.NOT_AUTHOR);
    }
}
