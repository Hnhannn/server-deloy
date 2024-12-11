package com.yuki.service;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Service
public class AlertService {
    private static final String MSG = "message";
    private static final String SUCCESS_ADD = "Thêm thành công";
    private static final String ERROR_ADD = "Thêm thất bại";
    private static final String ERROR_UPDATE = "Cập nhật thất bại";
    private static final String SUCCESS_UPDATE = "Cập nhật thành công";
    private static final String ERROR_DELETE = "Xóa thất bại";
    private static final String SUCCESS_DELETE = "Xóa thành công";

    public Model successAdd(Model model) {
        model.addAttribute(MSG, SUCCESS_ADD);
        return model;
    }

    public Model successUpdate(Model model) {
        model.addAttribute(MSG, SUCCESS_UPDATE);
        return model;
    }

    public Model successDelete(Model model) {
        model.addAttribute(MSG, SUCCESS_DELETE);
        return model;
    }

    public Model errorAdd(Model model) {
        model.addAttribute(MSG, ERROR_ADD);
        return model;
    }

    public Model errorUpdate(Model model) {
        model.addAttribute(MSG, ERROR_UPDATE);
        return model;
    }

    public Model errorDelete(Model model) {
        model.addAttribute(MSG, ERROR_DELETE);
        return model;
    }

    public RedirectAttributes successAddRedirect(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute(MSG, SUCCESS_ADD);
        return redirectAttributes;
    }

    public RedirectAttributes successUpdateRedirect(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute(MSG, SUCCESS_UPDATE);
        return redirectAttributes;
    }

    public RedirectAttributes successDeleteRedirect(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute(MSG, SUCCESS_DELETE);
        return redirectAttributes;
    }

    public RedirectAttributes errorAddRedirect(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute(MSG, ERROR_ADD);
        return redirectAttributes;
    }

    public RedirectAttributes errorUpdateRedirect(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute(MSG, ERROR_UPDATE);
        return redirectAttributes;
    }

    public RedirectAttributes errorDeleteRedirect(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute(MSG, ERROR_DELETE);
        return redirectAttributes;
    }
}
