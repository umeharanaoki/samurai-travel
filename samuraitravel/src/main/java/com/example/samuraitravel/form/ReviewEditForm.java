package com.example.samuraitravel.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReviewEditForm {
	@NotNull
	private Integer id;
	
	@NotBlank(message = "評価を選択してください。")
	private String evaluation;
	
	@NotBlank(message = "レビュー内容を入力してください。")
	private String reviewContents;
}
