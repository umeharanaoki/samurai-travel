let maxDate = new Date();
// 最大範囲を3ヶ月後に
maxDate = maxDate.setMonth(maxDate.getMonth() + 3);

flatpickr('#fromCheckinDateToCheckoutDate', {
	mode: 'range',
	locale: 'ja',
	minDate: 'today',
	maxDate: maxDate
});