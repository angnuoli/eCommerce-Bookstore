/**
 * 
 */

function checkBillingAddress() {
	if ($("#theSameAsShippingAddress").is(":checked")) {
		$(".billingAddress").prop("disabled", true);
	} else {
		$(".billingAddress").prop("disabled", false);
	}
}

function checkPasswordMatch() {
	var password = $("#txtNewPassword").val();
	var confirmPassword = $("#txtConfirmPassword").val();

	if (length(password) == 0 && length(confirmPassword) == 0) {
		$("#checkPasswordMatch").html("");
		$("#updateUserInfoButton").prop('disabled', false);
	} else {
		if (password !== confirmPassword) {
			$("#checkPasswordMatch").html("Passwords do not match!");
			$("#updateUserInfoButton").prop('disabled', true);
		} else {			
			$("#updateUserInfoButton").prop('disabled', false);
			$("#checkPasswordMatch").html("");
		}
	}
}

$(document).ready(function(){
	$(".cartItemQty").on('change', function(){
		var id=this.id;
		$('#update-item-'+id).css('display', 'inline-block');
	});
	$(".billingAddress").prop("disabled", true);
	$("#theSameAsShippingAddress").on('click', checkBillingAddress);
	$("#txtConfirmPassword").keyup(checkPasswordMatch);
	$("#txtNewPassword").keyup(checkPasswordMatch);

});

