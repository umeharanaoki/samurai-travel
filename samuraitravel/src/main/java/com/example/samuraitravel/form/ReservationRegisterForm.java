package com.example.samuraitravel.form;

import lombok.AllArgsConstructor;
import lombok.Data;

// 予約内容の確認ページから登録処理（コントローラーのcreate()メソッド）にデータを渡す
@Data
@AllArgsConstructor
public class ReservationRegisterForm {
	private Integer houseId;
	
	private Integer userId;
	
	 private String checkinDate;    
     
     private String checkoutDate;    
     
     private Integer numberOfPeople;
     
     private Integer amount;    
}
