$(document).ready(function() {
	
	var id = window.location.search.slice(1).split('&')[0].split('=')[1];
	console.log(id);

	var app = new Vue({
	    el: '#app',
	    data: {
            title: '',
            director: '',
            selectedGenres: [],
            selectedActors: [],
            distributor: '',
            country: '',
            duration: undefined,
            year: undefined,
            description: '',
            genreOptions: [],
            newGenre: '',
            actorOptions: [],
            newActor: '',
            message: '',
            showMessage: false,
            action: ''
	    },
	    methods: {
	        updateMovieSubmit: function(event) {
	        	console.log('ID: ' + id);
	            console.log('title: ' + this.title);
	        	console.log('director: ' + this.director);
                console.log('selectedGenres: ' + this.selectedGenres);
                console.log('selectedActors: ' + this.selectedActors);
                console.log('distributor: ' + this.distributor);
                console.log('country: ' + this.country);
                console.log('duration: ' + this.duration);
                console.log('year: ' + this.year);
                console.log('description: ' + this.description);
	            
	            if(!this.validation()) {
	                this.showMessage = true;
	                
	            }
	            else {
	                this.message = '';
                    this.showMessage = false;
                    
	                var params = {
                            'action': 'update',
                            'id': id,
	                        'title': this.title,
                            'director': this.director,
                            'genres': this.selectedGenres,
                            'actors': this.selectedActors,
                            'distributor': this.distributor,
                            'country': this.country,
                            'duration': this.duration,
                            'year': this.year,
                            'description': this.description
	                }
	                
	                $.post('MovieServlet', params, function(data) {
	                    console.log(data);
	                    
	                    if (data.status == 'failure') {
	                        app.message = data.message;
	                        app.showMessage = true;
	                    }
	                    else if (data.status == 'success') {
	                    	app.action = 'izmenili';
	                        $('#messageModal').modal('show');
	                        $('#messageModal').on('hidden.bs.modal', function (e) {
	                            window.location.replace('FilterMovies.html');
	                        });
	                    }
	                });
	            }
	            return false;
	        },
	        deleteMovie: function(event) {
	        	$('#confirmDelete').modal('show');
	        },
	        deleteMovieSubmit: function(event) {
	        	console.log('ID: ' + id);
	        	
	        	var params = {
                        'action': 'delete',
                        'id': id,
                }
                
                $.post('MovieServlet', params, function(data) {
                    console.log(data);
                    
                    if (data.status == 'failure') {
                        app.message = data.message;
                        app.showMessage = true;
                    }
                    else if (data.status == 'success') {
                    	app.action = 'obrisali';
                        $('#messageModal').modal('show');
                        $('#messageModal').on('hidden.bs.modal', function (e) {
                            window.location.replace('FilterMovies.html');
                        });
                    }
                });
	        },
	        validation: function () {
	            if (this.title === '') {
	                this.message = 'Naziv filma mora biti unet!';
                }
                else if (this.director === '') {
	                this.message = 'Režiser mora biti unet!';
                }
                else if (typeof this.duration === 'undefined') {
	                this.message = 'Trajanje mora biti uneto!';
                }
                else if (typeof this.year === 'undefined') {
	                this.message = 'Godina proizvodnje mora biti uneta!';
                }
                else if (this.country === '') {
	                this.message = 'Zemlja porekla mora biti uneta!';	
	            }
	            else if (this.distributor === '') {
	                this.message = 'Distributer mora biti unet!';
	            }
	            else {
	                return true;
	            }
	            return false;
            },
            getGenres: function() {
                $.get('GenresServlet', {'searchType': 'all'}, function(data) {
                    console.log(data);

                    if (data.status == 'success') {
                    	app.genreOptions = data.allGenres;
                    }
                })
                return false;
            },
            addGenre: function(event) {
                if (this.newGenre === '')
                    return false;
                else {
                    // console.log(this.genreOptions);
                    this.genreOptions.push(this.newGenre);
                    this.selectedGenres.push(this.newGenre);
                    this.newGenre = '';
                    // console.log(this.genreOptions);
//                    this.$nextTick(function(){ $('#genresInput').selectpicker('refresh'); });
                    // $('#genresInput').selectpicker('refresh').trigger('change');
                    return true;
                }
            },
            getActors: function() {
                $.get('ActorServlet', {'searchType': 'movie', 'movie': id}, function(data) {
                    console.log(data);

                    if (data.status == 'success') {
                    	app.actorOptions = data.actors;
                    }
                })
                return false;
            },
            addActor: function(event) {
                if (this.newActor === '')
                    return false;
                else {
                    // console.log(this.actorOptions);
                    this.actorOptions.push(this.newActor);
                    this.selectedActors.push(this.newActor);
                    this.newActor = '';
                    // console.log(this.actorOptions);
//                    this.$nextTick(function(){ $('#actorsInput').selectpicker('refresh'); });
                    // $('#genresInput').selectpicker('refresh').trigger('change');
                    return true;
                }
            },
            getMovie: function() {

                $.get('MovieServlet', {'id': id}, function(data) {
                    console.log(data);
                    
                    if (data.status == 'success') {
                        app.title = data.movie.title;
                        app.director = data.movie.director;
                        
                        app.selectedGenres = data.movie.genres;
//                        this.$nextTick(function(){ $('#actorsInput').selectpicker('refresh'); });
                        
//                        app.actorOptions = data.movie.actors;
                        app.selectedActors = data.movie.actors;
//                        this.$nextTick(function(){ $('#actorsInput').selectpicker('refresh'); });
                        
                        app.duration = data.movie.duration;
                        app.year = data.movie.year;
                        app.country = data.movie.country;
                        app.distributor = data.movie.distributor;
                        app.description = data.movie.description;
                    }
                    
                });
            },
            checkNumber: function(event) {
            	if (app.duration <= 0 || app.duration > 600)
                    app.duration = undefined;
            	if (app.year <= 0 || app.year > 2500)
                    app.year = undefined;
            }
	    },
	    watch: {
	    	genreOptions: function(newValues, oldValues){
	    		this.$nextTick(function(){ $('#genresInput').selectpicker('refresh'); });
	    	},
	    	actorOptions: function(newValues, oldValues){
	    		this.$nextTick(function(){ $('#actorsInput').selectpicker('refresh'); });
	    	},
	    	selectedGenres: function(newValues, oldValues){
	    		this.$nextTick(function(){ $('#genresInput').selectpicker('refresh'); });
	    	},
	    	selectedActors: function(newValues, oldValues){
	    		this.$nextTick(function(){ $('#actorsInput').selectpicker('refresh'); });
	    	}
	    	
	    }
    })
    
//    $('#durationInput').on('change keyup copy paste cut', function(event) {
//        event.preventDefault();
//
//        if (app.duration <= 0 || app.duration > 600)
//            app.duration = undefined;
//
//        return false;
//    });
//
//    $('#yearInput').on('change keyup copy paste cut', function(event) {
//        event.preventDefault();
//
//        if (app.year <= 0 || app.year > 2500)
//            app.year = undefined;
//
//        return false;
//    });
    
    app.getGenres();
    app.getActors();
    app.getMovie();

});
