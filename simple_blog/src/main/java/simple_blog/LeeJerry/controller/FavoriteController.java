package simple_blog.LeeJerry.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import simple_blog.LeeJerry.auth.UserProxy;
import simple_blog.LeeJerry.service.FavoriteService;

@RestController
public class FavoriteController {

    final
    FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/api/board/{boardId}/favorite")
    void insertFavorite(@PathVariable Long boardId, @AuthenticationPrincipal UserProxy userProxy) {
        favoriteService.insertFavorite(boardId, userProxy);
    }
    @DeleteMapping("/api/board/{boardId}/favorite")
    void deleteFavorite(@PathVariable Long boardId, @AuthenticationPrincipal UserProxy userProxy) {
        favoriteService.deleteFavorite(boardId, userProxy);
    }
}
