$(document).ready(function() {

	var app = new Vue({
	    el: '#app',
	    data: {
	        username: '',
	        password: '',
	        repeatedPassword: '',
	        message: '',
	        showMessage: false
	    },
	    methods: {
	        registrationSubmit: function(event) {
	            console.log('Username: ' + this.username);
	        	console.log('Password: ' + this.password);
	            console.log('Repeated password: ' + this.repeatedPassword);
	            
	            if(!this.validation()) {
	                this.showMessage = true;
	                this.username = '';
	                this.password = '';
	                this.repeatedPassword = '';
	            }
	            else {
	                this.message = '';
	                this.showMessage = false;
	                var params = {
	                        'username': this.username,
	                        'password': this.password
	                }
	                
	                $.post('RegistrationServlet', params, function(data) {
	                    console.log(data);
	                    
	                    if (data.status == 'failure') {
	                        app.message = data.message;
	                        app.showMessage = true;
	                    }
	                    else if (data.status == 'success') {
	                        $('#messageModal').modal('show');
	                        $('#messageModal').on('hidden.bs.modal', function (e) {
	                            window.location.replace('Login.html');
	                        });
	                    }
	                });
	            }
	            
	            return false;
	        },
	        validation: function () {
	            if (this.username === '') {
	                this.message = 'Korisničko ime mora biti uneto!';
	            }
	            else if (this.password === '') {
	                this.message = 'Lozinka mora biti uneta!';
	            }
	            else if (this.password != this.repeatedPassword) {
	                this.message = 'Lozinke nisu iste!';
	            }
	            else if (this.username.match(/^[0-9]+$/)) {
	                this.message = 'Korisničko ime se ne može sastojati samo od brojeva!';	
	            }
	            else if (!this.username.match(/^[0-9a-z]+$/i)) {
	                this.message = 'Korisničko ime se može sastojati samo od slova i brojeva!';
	            }
	            else if (!this.password.match(/^[0-9a-z]+$/i)) {
	                this.message = 'Lozinka se može sastojati samo od slova i brojeva!';
	            }
	            else {
	                return true;
	            }
	            return false;
	        }
	    }
	})
});
