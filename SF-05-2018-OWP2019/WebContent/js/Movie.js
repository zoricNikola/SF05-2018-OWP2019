$(document).ready(function() {
    var id = window.location.search.slice(1).split('&')[0].split('=')[1];
	console.log(id);
	
    var app = new Vue({
        el: '#app',
        data: {
            movie: {}
        },
        methods: {
            getMovie: function(event) {

                $.get('MovieServlet', {'id': id}, function(data) {
                    console.log(data);
                    
                    if (data.status == 'success') {
                        app.movie = data.movie;
                    }
                    
                });
            },
            genresToString: function(genres) {
            	var genresString = '';
                for (i = 0; i < genres.length; i++) {
                    if (i == 0) {
                        genresString = genres[i];
                    } else {
                        genresString = genresString.concat(', ', genres[i]);
                    }
                }
                return genresString;
            }
        },

    })
    
    app.getMovie();
});
