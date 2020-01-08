$(document).ready(function() {
	var usernameInput = $('#usernameInput');
	var passwordInput = $('#passwordInput');
	var repeatedPasswordInput = $('#repeatedPasswordInput');
	
	var messageParagraph = $('#messageParagraph');
	
	$('#registrationSubmit').on('click', function(event) {
		event.preventDefault();
		
		var username = usernameInput.val().trim();
		var password = passwordInput.val().trim();
		var repeatedPassword = repeatedPasswordInput.val().trim();
		console.log('Username: ' + username);
		console.log('Password: ' + password);
		console.log('Repeated password: ' + repeatedPassword);
		
		if (username === '') {
			messageParagraph.text('Korisničko ime mora biti uneto!');
		}
		else if (password === '') {
			messageParagraph.text('Lozinka mora biti uneta!');
		}
		else if (repeatedPassword === '') {
			messageParagraph.text('Ponovljena lozinka mora biti uneta!');
		}
		else if (password != repeatedPassword) {
			messageParagraph.text('Lozinke nisu iste!');
		}
		else if (username.match(/^[0-9]+$/)) {
			messageParagraph.text('Korisničko ime se ne može sastojaeti samo od brojeva!');	
		}
		else if (!username.match(/^[0-9a-z]+$/i)) {
			messageParagraph.text('Korisničko ime se može sastojaeti samo od slova i brojeva!');
		}
		else if (!password.match(/^[0-9a-z]+$/i)) {
			messageParagraph.text('Lozinka se može sastojaeti samo od slova i brojeva!');
		}
		else {
			messageParagraph.text('');
			var params = {
					'username': username,
					'password': password
			}
			
			$.post('RegistrationServlet', params, function(data) {
				console.log(data);
				
				if (data.status == 'failure') {
					messageParagraph.text(data.message);
				}
				else if (data.status == 'success') {
					window.location.replace('Welcome.html');
				}
			});
		}
		
		return false;
	});
});