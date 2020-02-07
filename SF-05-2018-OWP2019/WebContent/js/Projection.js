$("#staticBackdrop").modal('show');
$(document).ready(function() {
    var id = window.location.search.slice(1).split('&')[0].split('=')[1];
	console.log(id);
	
    var app = new Vue({
        el: '#app',
        data: {
        	loggedInUser: { username: '', userRole: ''},
            projection: { movie: {}, time: {}, hall: {} },
            remainingTickets: 1,
            inPast: false
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
        },

    })
    
    app.getLoggedInUser();
    app.getProjection();

    setTimeout(function(){ $("#staticBackdrop").modal('hide'); }, 1000);
});
