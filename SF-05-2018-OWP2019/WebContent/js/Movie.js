$("#staticBackdrop").modal('show');
$(document).ready(function() {
    var id = window.location.search.slice(1).split('&')[0].split('=')[1];
	console.log(id);
	
    var app = new Vue({
        el: '#app',
        data: {
        	loggedInUser: { username: '', userRole: ''},
            movie: {},
            projections: [],
            timeLow: ''
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
            getMovie: function() {

                $.get('MovieServlet', {'id': id}, function(data) {
                    console.log(data);
                    
                    if (data.status == 'success') {
                        app.movie = data.movie;
                    }
                    
                });
            },
            genresToString: function(genres) {
            	var genresString = '';
            	if (typeof genres !== 'undefined') {
	                for (i = 0; i < genres.length; i++) {
	                    if (i == 0) {
	                        genresString = genres[i];
	                    } else {
	                        genresString = genresString.concat(', ', genres[i]);
	                    }
	                }
            	}
                return genresString;
            },
            actorsToString: function(actors) {
            	var actorsString = '';
            	if (typeof actors !== 'undefined') {
	                for (i = 0; i < actors.length; i++) {
	                    if (i == 0) {
	                    	actorsString = actors[i];
	                    } else {
	                    	actorsString = actorsString.concat(', ', actors[i]);
	                    }
	                }
            	}
                return actorsString;
            },
            getProjections: function(event) {

                var params = {
                    'movieID': id,
                    'timeLow': this.timeLow
                }

                $.get('FilterProjectionsServlet', params, function(data) {
                    console.log(data);
                    
                    if (data.status == 'success') {
                    	app.projections = _.sortBy(data.projections, ['time.year', 'time.monthValue', 'time.dayOfMonth', 'time.hour', 'time.minute']);
                    }
                    
                });

            },
            setTimesAtStart: function() {
            	var dateLow = new Date();
                dateLow.setHours(8);
                dateLow.setMinutes(0);
                
                var months = (dateLow.getMonth()+1).toString();
                if (dateLow.getMonth()+1 < 10)
                    months = '0' + months;

                var days = (dateLow.getDate()).toString();
                if (dateLow.getDate() < 10)
                    days = '0' + days;

                var hours = (dateLow.getHours()).toString();
                if (dateLow.getHours() < 10)
                    hours = '0' + hours;

                var minutes = (dateLow.getMinutes()).toString();
                if (dateLow.getMinutes() < 10)
                    minutes = '0' + minutes;
                
                app.timeLow = dateLow.getFullYear().toString() + '-' + months + '-' + days + ' ' + hours + ':' + minutes;          
            }
        },

    })
    
    app.getLoggedInUser();
    app.getMovie();
    app.setTimesAtStart();
    app.getProjections();
    setTimeout(function(){ $("#staticBackdrop").modal('hide'); }, 1000);
});
