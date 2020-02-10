$("#staticBackdrop").modal('show');
$(document).ready(function() {
    var id = window.location.search.slice(1).split('&')[0].split('=')[1];
	console.log(id);
	
    var app = new Vue({
        el: '#app',
        data: {
        	loggedInUser: { username: '', userRole: ''},
            inPast: true,
            message: '',
            ticket: { id: 0, time: {}, user: {}, projection: {movie: {}, time: {}, projectionType: {}}, hall: {}, seat: {} },
        },
        methods: {
        	getLoggedInUser: function() {
	    		$.get('LoggedInUserServlet', function(data) {
	    	        console.log(data);
	    	        
	    	        if (data.status == 'success') {
	    	        	app.loggedInUser.username = data.loggedInUser.username;
                        app.loggedInUser.userRole = data.loggedInUser.userRole;
	    	            return;
	    	        }
	    	        else
	    	        	window.location.replace('Welcome.html');
	    	    });
	    	},
            getTicket: function() {

                $.get('TicketServlet', {'action': 'ticketID', 'id': id}, function(data) {
                    console.log(data);
                    
                    if (data.status == 'success') {
                        app.ticket = data.ticket;
                        app.inPast = data.inPast;
                        if (app.loggedInUser.userRole != 'ADMIN' && app.loggedInUser.username != app.ticket.user.username) {
                        	window.location.replace('Welcome.html');
                        }
                    }
                    
                });
            },
            deleteTicket: function(event) {
	        	$('#confirmDelete').modal('show');
	        },
	        deleteTicketSubmit: function(event) {
	        	console.log('ID: ' + id);
	        	
	        	var params = {
                        'action': 'delete',
                        'id': id,
                }
                
                $.post('TicketServlet', params, function(data) {
                    console.log(data);
                    
                    if (data.status == 'failure') {
                        app.message = 'Greška prilikom brisanja karte u bazi';
                        $('#messageModal').modal('show');
                    }
                    else if (data.status == 'success') {
                    	app.message = 'Uspešno ste obrisali kartu!';
                        $('#messageModal').modal('show');
                        $('#messageModal').on('hidden.bs.modal', function (e) {
                            window.location.replace('Welcome.html');
                        });
                    }
                });
	        },
        },
    })
    
    app.getLoggedInUser();
    app.getTicket();

    setTimeout(function(){ $("#staticBackdrop").modal('hide'); }, 1000);
});
