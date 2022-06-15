package simple_blog.LeeJerry.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import simple_blog.LeeJerry.service.ImageService;

@RestController
@RequestMapping("/api/{boardId}/image")
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }


    @PostMapping
    void uploadImage(@PathVariable Long boardId) {
        imageService.uploadImage(boardId);
    }

}
