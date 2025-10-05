package com.springboot.shop.service.image;

import com.springboot.shop.dto.ImageDto;
import com.springboot.shop.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {
    Image getImageById(Long id);
    void deleteImageById(Long id);
    List<ImageDto> saveImages(List<MultipartFile> file, Long product_id);
    void updateImage(MultipartFile file, Long image_id);
}
