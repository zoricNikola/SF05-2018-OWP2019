$("#staticBackdrop").modal('show');
$(document).ready(function() {
	
	$.get('LoggedInUserServlet', function(data) {
        console.log(data);
        
        if (data.status == 'success') {
            window.location.replace('Welcome.html');
            return;
        }
    });

	var app = new Vue({
	    el: '#app',
	    data: {
	        username: '',
	        password: '',
	        message: '',
	        showMessage: false
	    },
	    methods: {
	        loginSubmit: function(event) {
	            console.log('Username: ' + this.username);
	        	console.log('Password: ' + this.password);
	            
	            if(!this.validation()) {
	                this.showMessage = true;
	                this.username = '';
	                this.password = '';
	            }
	            else {
	                this.message = '';
	                this.showMessage = false;
	                var params = {
	                        'username': this.username,
	                        'password': this.password
	                }
	                
	                $.post('LoginServlet', params, function(data) {
	                    console.log(data);
	                    
	                    if (data.status == 'failure') {
                            if (data.message == 'username') {
                                app.message = 'Pogrešno korisničko ime!';
                                app.username = '';
                                app.password = '';
                            }
                            else if (data.message == 'password') {
                                app.message = 'Pogrešna lozinka!';
                                app.password = '';
                            }
                            else if (data.message == 'deactivated') {
                            	app.message = 'Vaš nalog je deaktiviran!';
                            }
                            else
                                app.message = data.message;
                                
                            app.showMessage = true;
                            

	                    }
	                    else if (data.status == 'success') {
                            window.location.replace('Welcome.html');
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
	            else {
	                return true;
	            }
	            return false;
	        }
	    }
	})
	
	setTimeout(function(){ $("#staticBackdrop").modal('hide'); }, 1000);
});
