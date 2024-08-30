package com.restdemo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.restdemo.domain.Image;
import com.restdemo.mapper.ImageMapper;

@Service
public class ImageServiceImpl implements ImageService {
	
	@Autowired
    private ImageMapper imageMapper;
	
	
	 public void saveImage(int b_id, String imageName, byte[] imageBytes) {
	        Image image = new Image();
	        image.setB_id(b_id); // sb_board의 b_id 참조
	        image.setName(imageName);
	        image.setImageData(imageBytes);

	        imageMapper.insertImage(image);
	    }
}
