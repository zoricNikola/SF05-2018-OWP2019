$("#staticBackdrop").modal('show');
$(document).ready(function() {
    var projectionId = window.location.search.slice(1).split('&')[0].split('=')[1];
	console.log(projectionId);
	
    var app = new Vue({
        el: '#app',
        data: {
        	loggedInUser: { username: '', userRole: ''},
            projection: { movie: {}, time: {}, hall: {}, projectionType: {} },
            inPast: true,
            message: '',
            seatOptions: [],
            seatOption: {},
            selectedSeats: [],
            showMessage: false
        },
        methods: {
        	getLoggedInUser: function() {
	    		$.get('LoggedInUserServlet', function(data) {
	    	        console.log(data);
	    	        
	    	        if (data.status != 'success') {
	    	            window.location.replace('Login.html');
	    	            return;
	    	        }
	    	        if (data.loggedInUser.userRole != 'USER') {
	    	            window.location.replace('Welcome.html');
	    	            return;
	    	        }
	    	        if (data.status == 'success') {
	    	        	app.loggedInUser.username = data.loggedInUser.username;
	    	            app.loggedInUser.userRole = data.loggedInUser.userRole;
	    	            return;
	    	        }
	    	    });
	    	},
            getProjection: function() {

                $.get('ProjectionServlet', {'id': projectionId}, function(data) {
                    console.log(data);
                    
                    if (data.status == 'success') {
                        app.projection = data.projection;
                        app.inPast = data.inPast;
                        app.getSeats();
                    }
                    
                });
            },
            getSeats: function() {

                $.get('SeatServlet', {'action': 'remainingSeats', 'projectionID': app.projection.id ,'hallID': app.projection.hall.id}, function(data) {
                    console.log(data);
                    
                    if (data.status == 'success') {
                        app.seatOptions = data.seats;
                    }
                    
                });
            },
            seatsToString: function() {
            	var seatsString = '';
                for (i = 0; i < this.selectedSeats.length; i++) {
                    if (i == 0) {
                    	seatsString = this.selectedSeats[i].toString();
                    } else {
                    	seatsString = seatsString.concat(', ', this.selectedSeats[i].toString());
                    }
                }
                return seatsString;
            },
            selectionChanged: function(event) {
            	if (this.selectedSeats.length > 0) {
        			this.showMessage = false;
            	}
            	else {
            		this.message = 'Morate izabrati barem jedno sedište';
            		this.showMessage = true;
            	}
            },
            buyTicket: function(event) {
            	if (this.selectedSeats.length < 1) {
            		this.message = 'Morate izabrati barem jedno sedište';
            		this.showMessage = true;
            	}
            	else {
            		if (this.selectedSeats.length > 1) {
            			for(var i = 1; i < this.selectedSeats.length; i++) {
            				if (this.selectedSeats[i-1] + 1 != this.selectedSeats[i]) {
            					this.message = 'Izabrana sedišta moraju biti jedno do drugog!';
            					this.showMessage = true;
            					return;
            				}
            			}
        			}
            		$('#confirmBuy').modal('show');
            	}
	        },
	        buyTicketSubmit: function(event) {
        		console.log('projectionID: ' + this.projection.id);
	        	console.log('seatNumbers: ' + this.selectedSeats);
	        	console.log('hallID: ' + this.projection.hall.id);
	        	console.log('userUsername: ' + this.loggedInUser.username);
	        	
	        	var params = {
                        'action': 'add',
                        'projectionID': this.projection.id,
                        'seatNumbers': this.selectedSeats,
                        'hallID': this.projection.hall.id,
                }
	        	
	        	$.post('TicketServlet', params, function(data) {
	        		console.log(data);
                    
	        		if (data.status == 'failure') {
	        			if (data.message == 'seatReserved') {
	        				app.message = 'Jedno ili više odabranih sedišta je već rezervisano...';
	        				$('#messageModal').on('hidden.bs.modal', function (e) {
	                            window.location.replace('BuyTicket.html?projection=' + projectionID);
	                            return;
	                        });
	        			}
                        app.message = 'Greška prilikom dodavanja karte u bazu';
                        $('#messageModal').modal('show');
                    }
	        		
	        		else if (data.status == 'success') {
                    	app.message = 'Uspešno ste kupili kartu(e)!';
                        $('#messageModal').modal('show');
                        $('#messageModal').on('hidden.bs.modal', function (e) {
                            window.location.replace('Welcome.html');
                        });
                    }
	        		
                });
	        },
        },
        watch: {
            seatOptions: function(newValues, oldValues){
	    		this.$nextTick(function(){ $('#seatsInput').selectpicker('refresh'); });
	    	},
	    }

    })
    
     app.getLoggedInUser();
     app.getProjection();

    setTimeout(function(){ $("#staticBackdrop").modal('hide'); }, 1000);
});
