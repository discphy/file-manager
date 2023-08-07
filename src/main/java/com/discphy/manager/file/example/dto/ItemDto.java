package com.discphy.manager.file.example.dto;

import com.discphy.manager.file.excel.annotation.ExcelColumn;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ItemDto {

	@ExcelColumn(headerName = "아이디")
	private Long id;

	@ExcelColumn(headerName = "이름")
	private String name;

	private int price;

	@ExcelColumn(headerName = "수량")
	private int quantity;

	public ItemDto(Long id, String name, int price, int quantity) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.quantity = quantity;
	}
}
