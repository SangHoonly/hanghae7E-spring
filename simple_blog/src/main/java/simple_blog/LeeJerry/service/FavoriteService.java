package simple_blog.LeeJerry.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import simple_blog.LeeJerry.auth.UserProxy;
import simple_blog.LeeJerry.entity.Board;
import simple_blog.LeeJerry.entity.FavoriteEntity;
import simple_blog.LeeJerry.entity.UserEntity;
import simple_blog.LeeJerry.repository.BoardRepository;
import simple_blog.LeeJerry.repository.FavoriteRepository;
import simple_blog.LeeJerry.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    final BoardRepository boardRepository;
    final UserRepository userRepository;
    final FavoriteRepository favoriteRepository;


    public void updateFavorite(Long boardId, Authentication authentication) {
        UserProxy userProxy = (UserProxy) authentication.getPrincipal();

        if (userProxy == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        Board board = boardRepository.findById(boardId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        UserEntity userEntity = userRepository.findById(userProxy.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));

        FavoriteEntity favoriteEntity = favoriteRepository.findByBoardIdAndUserEntityId(board.getId(), userEntity.getId())
                                                .orElse(FavoriteEntity.builder().board(board).userEntity(userEntity).build());

        if (favoriteEntity.getId() != null) favoriteEntity.update();



        favoriteRepository.save(favoriteEntity);
    }
}
