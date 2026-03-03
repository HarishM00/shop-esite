package com.springboot.shop.service.image;

import com.springboot.shop.dto.ImageDto;
import com.springboot.shop.exceptions.ResourceNotFoundException;
import com.springboot.shop.model.Image;
import com.springboot.shop.model.Product;
import com.springboot.shop.repository.ImageRepository;
import com.springboot.shop.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService implements IImageService {

    private final ImageRepository imageRepository;
    private final ProductService productService;

    @Override
    public Image getImageById(Long id) {
        return imageRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("No image found with id "+id));
    }

    @Override
    public void deleteImageById(Long id) {
        imageRepository.findById(id)
                        .ifPresentOrElse(imageRepository::delete,
                                ()-> {throw new ResourceNotFoundException("No image found with id "+id);});
    }

    @Override
    public List<ImageDto> saveImages(List<MultipartFile> files, Long product_id) {
        Product product=productService.getProductById(product_id);
        List<ImageDto> savedImageDto=new ArrayList<>();
        for (MultipartFile file: files){
            try{
                Image image=new Image();
                image.setFileName(file.getOriginalFilename());
                image.setFileType(file.getContentType());
                image.setImage(new SerialBlob(file.getBytes()));
                image.setProduct(product);

                String url="api/v1/images/image/download/";
                String downloadUrl=url+image.getId();
                image.setDownloadUrl(downloadUrl);
                Image savedImage=imageRepository.save(image);

                savedImage.setDownloadUrl(url+savedImage.getId());
                imageRepository.save(savedImage);

                ImageDto imageDto=new ImageDto();
                imageDto.setImageId(savedImage.getId());
                imageDto.setImageName(savedImage.getFileName());
                imageDto.setDownloadUrl(savedImage.getDownloadUrl());

                savedImageDto.add(imageDto);

            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return savedImageDto;
    }

    @Override
    public void updateImage(MultipartFile file, Long image_id) {
        Image image=getImageById(image_id);
        try {
            image.setFileName(file.getOriginalFilename());
            image.setFileType(file.getContentType());
            image.setImage(new SerialBlob(file.getBytes()));
            imageRepository.save(image);
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
