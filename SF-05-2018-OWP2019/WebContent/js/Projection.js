$("#staticBackdrop").modal('show');
$(document).ready(function() {
    var id = window.location.search.slice(1).split('&')[0].split('=')[1];
	console.log(id);
	
    var app = new Vue({
        el: '#app',
        data: {
        	loggedInUser: { username: '', userRole: ''},
            projection: { movie: {}, time: {}, hall: {}, projectionType: {} },
            remainingTickets: 0,
            inPast: true,
            message: '',
            tickets: [],
            ticket: { id: 0, time: {}, user: {} },
            sortOptions: ['Automatski', 'Korisnik rastuće', 'Korisnik opadajuće', 'Vreme rastuće', 'Vreme opadajuće'],
            selectedSort: 'Automatski',
            sortOption: ''
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
	    	    });
	    	},
            getProjection: function() {

                $.get('ProjectionServlet', {'id': id}, function(data) {
                    console.log(data);
                    
                    if (data.status == 'success') {
                        app.projection = data.projection;
                        app.inPast = data.inPast;
                        app.getSeats();
                    }
                    
                });
            },
            getTickets: function() {

                $.get('TicketServlet', {'action': 'projectionID', 'projectionID': id}, function(data) {
                    console.log(data);
                    
                    if (data.status == 'success') {
                        app.tickets = data.tickets;
                    }
                    
                });
            },
            getSeats: function() {

                $.get('SeatServlet', {'action': 'remainingSeats', 'projectionID': app.projection.id ,'hallID': app.projection.hall.id}, function(data) {
                    console.log(data);
                    
                    if (data.status == 'success') {
                        app.remainingTickets = data.seats.length;
                    }
                    
                });
            },
            deleteProjection: function(event) {
	        	$('#confirmDelete').modal('show');
	        },
	        deleteProjectionSubmit: function(event) {
	        	console.log('ID: ' + id);
	        	
	        	var params = {
                        'action': 'delete',
                        'id': id,
                }
                
                $.post('ProjectionServlet', params, function(data) {
                    console.log(data);
                    
                    if (data.status == 'failure') {
                        app.message = data.message;
                        $('#messageModal').modal('show');
                    }
                    else if (data.status == 'success') {
                    	app.message = 'Uspešno ste obrisali projekciju!';
                        $('#messageModal').modal('show');
                        $('#messageModal').on('hidden.bs.modal', function (e) {
                            window.location.replace('Welcome.html');
                        });
                    }
                });
	        },
        },
        computed: {
			orderedTickets: function() {
			            	
                if (this.selectedSort === 'Automatski')
            		return this.tickets;
            	else if (this.selectedSort === 'Korisnik rastuće')
            		return _.orderBy(this.tickets, 'user.username');
            	else if (this.selectedSort === 'Korisnik opadajuće')
            		return _.orderBy(this.tickets, 'user.username').reverse();
            	else if (this.selectedSort === 'Vreme rastuće')
        			return _.sortBy(this.tickets, ['time.year', 'time.monthValue', 'time.dayOfMonth', 'time.hour', 'time.minute']);
        		else if (this.selectedSort === 'Vreme opadajuće')
        			return _.sortBy(this.tickets, ['time.year', 'time.monthValue', 'time.dayOfMonth', 'time.hour', 'time.minute']).reverse();
        		else
        			return this.tickets;
            },
        }

    })
    
    app.getLoggedInUser();
    app.getProjection();
    app.getTickets();

    setTimeout(function(){ $("#staticBackdrop").modal('hide'); }, 1000);
});
