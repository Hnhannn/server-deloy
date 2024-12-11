package com.yuki.Rest.Controller;

import com.yuki.dto.SliderShowDTO;
import com.yuki.entity.SliderShow;
import com.yuki.repositoty.SliderShowDAO;
import com.yuki.service.SliderShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/slidershows")
public class SliderShowController {

    @Autowired
    private SliderShowService sliderShowService;

    @Autowired
    private SliderShowDAO sliderShowDAO;

    @GetMapping
    public List<SliderShow> getAllSliderShow() {
        return sliderShowDAO.findAllByOrderByCurrentAsc();
    }
    @PostMapping
    public SliderShow createSliderShow(@RequestBody SliderShowDTO sliderShow) {
        return sliderShowService.createSliderShow(sliderShow);
    }

    @PutMapping("/updateOrder")
    public void updateSliderOrder(@RequestBody List<SliderShow> sliders) {
        sliderShowService.updateSliderOrder(sliders);
    }

    @DeleteMapping("/{sliderID}")
    public SliderShow deleteSliderShow(@PathVariable int sliderID) {
        return sliderShowService.deleteSliderShow(sliderID);
    }

    @PutMapping("/updateImage/{sliderID}")
    public SliderShow updateSliderShowImage(@PathVariable int sliderID, @RequestBody SliderShowDTO sliderShowDTO) {
        return sliderShowService.updateSliderShowImage(sliderID, sliderShowDTO);
    }
}
