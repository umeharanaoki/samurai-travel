const stripe = Stripe('pk_test_51OCDA9IxEItBnivykKuV2wdxdz9cu95r5TdDKnNrl508nkxj2tnONkgWOdCFRHUmmAR4diHCzfuryJaiSw4IBBVR00WTmQ9Nkd');
const paymentButton = document.querySelector('#paymentButton');

paymentButton.addEventListener('click', () => {
	stripe.redirectToCheckout({
		sessionId: sessionId	
	})
});