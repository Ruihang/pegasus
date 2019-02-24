const msgElId = '#passwordMessage';
const colorGREEN = "rgb(0, 128, 0)";
const colorRED = "rgb(255, 0, 0)";
const colorAtt = 'color';
function validatePasswords(){
	//Validate password security policy
	//N.A

	//Validate new password and confirmed new password is identical ?
	if ($(msgElId).css(colorAtt) != colorGREEN) 
		return false;

	if ($('#password').val() == $('#passwordNew').val()) 
		return false;

	return true;
}

function applyChangePassword(){
	if (validatePasswords() != true){
		return false;
	}

	var validated = false;
	/*console.log('Current password: ' + $('#password').val());
	console.log('New password: ' + $('#passwordNew').val());
	console.log('Confirm new password: ' + $('#passwordConfirm').val());*/

	//Validate new password and confirmed new password is identical ?
	/*if ($('#passwordNew').val() != $('#passwordConfirm').val()){
		 $('#message').html('Not Matching').css('color', 'red');
	}*/
	
	//Validate current password and new password
	//if (($('#password').val())&&($('#passwordNew').val()==$('#passwordConfirm').val()))
	$("#clientChangeSecurityForm").submit();
}

function cancelPasswordChange(){
	document.location.href="/";
}

$(document).ready( function () {
	$('#passwordNew, #passwordConfirm').on('keyup blur change', function () {
		if ($('#passwordNew').val() == $('#passwordConfirm').val()) {
			$(msgElId).html(passMatchedMsg).css(colorAtt, colorGREEN);
		} else {
			$(msgElId).html(passNotMatchedMsg).css(colorAtt, colorRED);
		}		    
	});
})
