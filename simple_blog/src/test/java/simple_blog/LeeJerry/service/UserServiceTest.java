package simple_blog.LeeJerry.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import simple_blog.LeeJerry.repository.BoardRepository;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {


    @InjectMocks
    BoardService boardService;

    @Mock
    BoardRepository boardRepository;



}
