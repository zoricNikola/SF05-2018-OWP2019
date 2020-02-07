$("#staticBackdrop").modal('show');
$(document).ready(function() {
    var id = window.location.search.slice(1).split('&')[0].split('=')[1];
	console.log(id);
	
    var app = new Vue({
        el: '#app',
        data: {
        	loggedInUser: { username: '', userRole: ''},
            projection: { movie: {}, time: {}, hall: {}, projectionType: {} },
            remainingTickets: 1,
            inPast: false,
            message: ''
        },
        methods: {
        	getLoggedInUser: function() {
	    		$.get('LoggedInUserServlet', function(data) {
	    	        console.log(data);
	    	        
	    	        if (data.status == 'success') {
	    	        	app.loggedInUser.username = data.loggedInUser.username;
                        app.loggedInUser.userRole = data.loggedInUser.userRole;
                        app.inPast = data.inPast;
	    	            return;
	    	        }
	    	    });
	    	},
            getProjection: function() {

                $.get('ProjectionServlet', {'id': id}, function(data) {
                    console.log(data);
                    
                    if (data.status == 'success') {
                        app.projection = data.projection;
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

    })
    
    app.getLoggedInUser();
    app.getProjection();

    setTimeout(function(){ $("#staticBackdrop").modal('hide'); }, 1000);
});
