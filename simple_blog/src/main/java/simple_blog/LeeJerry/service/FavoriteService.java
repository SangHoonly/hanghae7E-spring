package simple_blog.LeeJerry.service;

import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import simple_blog.LeeJerry.auth.UserProxy;
import simple_blog.LeeJerry.entity.Board;
import simple_blog.LeeJerry.entity.FavoriteEntity;
import simple_blog.LeeJerry.entity.UserEntity;
import simple_blog.LeeJerry.exception.AbstractException;
import simple_blog.LeeJerry.exception.ErrorCode;
import simple_blog.LeeJerry.exception.InvalidException;
import simple_blog.LeeJerry.exception.NotFoundException;
import simple_blog.LeeJerry.repository.BoardRepository;
import simple_blog.LeeJerry.repository.FavoriteRepository;
import simple_blog.LeeJerry.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    final BoardRepository boardRepository;
    final UserRepository userRepository;
    final FavoriteRepository favoriteRepository;

    @Transactional
    public void insertFavorite(Long boardId, UserProxy userProxy) throws AbstractException {
        UserEntity userEntity = userRepository.findById(userProxy.getId()).orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new NotFoundException(ErrorCode.BOARD_NOT_FOUND));

        if (favoriteRepository.findByBoardIdAndUserEntityId(boardId, userProxy.getId()).isPresent()) throw new InvalidException(ErrorCode.FAVORITE_ALREADY_EXIST);

        FavoriteEntity favoriteEntity = FavoriteEntity.builder().userEntity(userEntity).board(board).build();
        favoriteRepository.save(favoriteEntity);
        board.increseFavorite();
    }

    @Transactional
    public void deleteFavorite(Long boardId, UserProxy userProxy) throws AbstractException {
        FavoriteEntity favoriteEntity = favoriteRepository.findByBoardIdAndUserEntityId(boardId, userProxy.getId()).orElseThrow(() -> new NotFoundException(ErrorCode.FAVORITE_NOT_FOUND));
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new NotFoundException(ErrorCode.BOARD_NOT_FOUND));

        favoriteRepository.delete(favoriteEntity);
        board.decreseFavorite();
    }
}
