package com.yuki.service;

import com.yuki.dto.SliderShowDTO;
import com.yuki.entity.SliderShow;
import com.yuki.entity.User;
import com.yuki.repositoty.SliderShowDAO;
import com.yuki.repositoty.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SliderShowService {
    @Autowired
    private SliderShowDAO sliderShowDAO;
    @Autowired
    private UserDAO userDAO;

    public SliderShow createSliderShow(SliderShowDTO sliderShowDTO) {
        if (sliderShowDTO == null) {
            throw new IllegalArgumentException("Invalid slider show data provided.");
        }

        User user = userDAO.findById(sliderShowDTO.getUserID())
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + sliderShowDTO.getUserID()));
        int maxCurrent = sliderShowDAO.findMaxCurrent().orElse(0);
        SliderShow sliderShow = new SliderShow();
        sliderShow.setImageUrl(sliderShowDTO.getImageUrl());
        sliderShow.setUser(user);
        sliderShow.setCurrent(maxCurrent + 1);
        sliderShow.setStatus(true);
        return sliderShowDAO.save(sliderShow);
    }

    public void updateSliderOrder(List<SliderShow> sliders) {
        // Lưu mảng slider đã thay đổi vào cơ sở dữ liệu
        for (int i = 0; i < sliders.size(); i++) {
            sliders.get(i).setCurrent(i);  // Cập nhật thứ tự
        }
        sliderShowDAO.saveAll(sliders);  // Lưu tất cả các slider vào database
    }

    public SliderShow updateSliderShowImage(int sliderID, SliderShowDTO sliderShowDTO) {
        SliderShow sliderShow = sliderShowDAO.findById(sliderID)
                .orElseThrow(() -> new IllegalArgumentException("Slider show not found with ID: " + sliderID));
        sliderShow.setImageUrl(sliderShowDTO.getImageUrl());
        return sliderShowDAO.save(sliderShow);
    }

    public SliderShow deleteSliderShow(int sliderID) {
        SliderShow sliderShow = sliderShowDAO.findById(sliderID)
                .orElseThrow(() -> new IllegalArgumentException("Slider show not found with ID: " + sliderID));
        sliderShow.setStatus(false);
        return sliderShowDAO.save(sliderShow);
    }
}
