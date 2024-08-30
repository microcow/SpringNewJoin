package com.restdemo.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import com.restdemo.domain.Image;

@Mapper
public interface ImageMapper {

    @Insert("INSERT INTO sb_images (name, image_data, b_id) VALUES (#{name}, #{imageData}, #{b_id})")
    void insertImage(Image image);
}
