package simple_blog.LeeJerry.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import simple_blog.LeeJerry.service.FavoriteService;

@RestController
public class FavoriteController {

    final
    FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/api/board/{boardId}/favorite")
    void updateFavorite(@PathVariable Long boardId, Authentication authentication) {
        favoriteService.updateFavorite(boardId, authentication);
    }
}
