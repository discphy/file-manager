package com.discphy.manager.file.example.controller;

import com.discphy.manager.file.example.dto.ItemForm;
import com.discphy.manager.file.example.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class ItemWebController {

	private final ItemService itemService;

	@PostMapping("/file/upload")
	public String fileUpload(@ModelAttribute ItemForm form, RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("items", itemService.excelUpload(form.getExcelFile()));
		redirectAttributes.addFlashAttribute("files", itemService.fileUpload(form.getAttachFiles()));
		return "redirect:/sample/itemForm";
	}
}
