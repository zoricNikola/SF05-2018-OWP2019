$("#staticBackdrop").modal('show');
$(document).ready(function() {
    var username = window.location.search.slice(1).split('&')[0].split('=')[1];
	console.log(username);
	
	if (username == '')
		window.location.replace('Welcome.html');
	
    var app = new Vue({
        el: '#app',
        data: {
        	loggedInUser: { username: '', userRole: ''},
            user: {},
            registrationDate: '',
            newPassword: '',
            repeatedNewPassword: ''
        },
        methods: {
        	getLoggedInUser: function() {
	    		$.get('LoggedInUserServlet', function(data) {
	    	        console.log(data);
	    	        
	    	        if (data.status == 'success') {
	    	        	app.loggedInUser.username = data.loggedInUser.username;
	    	            app.loggedInUser.userRole = data.loggedInUser.userRole;
	    	        }
	    	        
	    	        if (app.loggedInUser.userRole != 'ADMIN' && app.loggedInUser.username != username) {
	    	        	window.location.replace('Welcome.html');
	    	        }
	    	    });
	    	},
            getUser: function(event) {

                $.get('UserServlet', {'username': username}, function(data) {
                    console.log(data);
                    
                    if (data.status == 'success') {
                        app.user = data.user;
                        app.registrationDate = data.user.registrationDate.dayOfMonth + 
                            '. ' + data.user.registrationDate.month + 
                            ' ' + data.user.registrationDate.year + '.';
                    }
                    
                });
            }
        },

    })
    
    app.getLoggedInUser();
    app.getUser();
    setTimeout(function(){ $("#staticBackdrop").modal('hide'); }, 1000);
});