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
            repeatedNewPassword: '',
            userRoleOptions: ['USER', 'ADMIN'],
            selectedUserRole: 'USER',
            userRoleOption: '',
            message: '',
            tickets: [],
            ticket: { id: 0, time: {}, user: {} },
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
                        app.selectedUserRole = data.user.userRole;
                    }
                    
                });
            },
            updateUser: function(event) {
	        	$('#confirmUpdate').modal('show');
	        },
	        updateUserSubmit: function(event) {
	        	console.log('username: ' + username);
	        	
	        	if (this.newPassword != '' || this.selectedUserRole != this.user.userRole) {
	        		if (this.newPassword != this.repeatedNewPassword) {
	        			this.message = 'Nova lozinka i ponovljena nova lozinka nisu iste!';
	        			$('#messageModal').modal('show');
	        			return;
	        		}
	        		else {
	        			var params = {
	                            'action': 'update',
	                            'username': this.user.username,
	                            'newPassword': this.newPassword,
	                            'newUserRole': this.selectedUserRole
	                    }
	        			
	        			$.post('UserServlet', params, function(data) {
	                        console.log(data);
	                        
	                        if (data.status == 'failure') {
	                            app.message = data.message;
	                            $('#messageModal').modal('show');
	    	        			return;
	                        }
	                        else if (data.status == 'success') {
	                        	app.message = 'Uspešno ste izmenili nalog';
	                            $('#messageModal').modal('show');
	                            $('#messageModal').on('hidden.bs.modal', function (e) {
	                                window.location.replace('User.html?username=' + username);
	                            });
	                        }
	                    });
	        		}
	        	}
	        },
	        deleteUser: function(event) {
	        	$('#confirmDelete').modal('show');
	        },
	        deleteUserSubmit: function(event) {
	        	console.log('username: ' + username);
	        	
	        	var params = {
	        			'action': 'delete',
	        			'username': this.user.username
	        	}
	        	
	        	$.post('UserServlet', params, function(data) {
                    console.log(data);
                    
                    if (data.status == 'failure') {
                        app.message = 'Greška prilikom brisanja korisnika iz baze';
                        $('#messageModal').modal('show');
	        			return;
                    }
                    else if (data.status == 'success') {
                    	app.message = 'Uspešno ste obrisali nalog';
                        $('#messageModal').modal('show');
                        $('#messageModal').on('hidden.bs.modal', function (e) {
                            window.location.replace('FilterUsers.html');
                        });
                    }
                });
	        },
	        getTickets: function() {

                $.get('TicketServlet', {'action': 'user', 'username': username}, function(data) {
                    console.log(data);
                    
                    if (data.status == 'success') {
                        app.tickets = data.tickets;
                    }
                    
                });
            },
        },
        computed: {
			orderedTickets: function() {
    			return _.sortBy(this.tickets, ['time.year', 'time.monthValue', 'time.dayOfMonth', 'time.hour', 'time.minute']).reverse();
            },
        }

    })
    
    app.getLoggedInUser();
    app.getUser();
    setTimeout(function(){ $("#staticBackdrop").modal('hide'); }, 1000);
});