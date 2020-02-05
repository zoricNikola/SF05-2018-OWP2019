$("#staticBackdrop").modal('show');
$(document).ready(function() {
	
    var app = new Vue({
        el: '#app',
        data: {
        	loggedInUser: { username: '', userRole: '' },
            title: '',
            distributor: '',
            country: '',
            durationLow: undefined,
            durationHigh: undefined,
            yearLow: undefined,
            yearHigh: undefined,
            sortOptions: [''],
            selectedSort: '',
            genreOptions: [],
            selectedGenres: [],
            movies: [],
            genreOption: '',
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
            getMovies: function(event) {
                console.log('title: ' + this.title);
                console.log('selectedGenres: ' + this.selectedGenres);
                console.log('distributor: ' + this.distributor);
                console.log('country: ' + this.country);
                console.log('durationLow: ' + this.durationLow);
                console.log('durationHigh: ' + this.durationHigh);
                console.log('yearLow: ' + this.yearLow);
                console.log('yearHigh: ' + this.yearHigh);

                var params = {
                    'title': this.title,
                    'genres': this.selectedGenres,
                    'distributor': this.distributor,
                    'country': this.country,
                    'durationLow': this.durationLow,
                    'durationHigh': this.durationHigh,
                    'yearLow': this.yearLow,
                    'yearHigh': this.yearHigh
                }

                $.get('FilterMoviesServlet', params, function(data) {
                    console.log(data);
                    
                    if (data.status == 'success') {
                    	app.movies = data.filteredMovies;
                    }
                    
                });
            },
            getGenres: function() {
                $.get('GenresServlet', {'searchType': 'all'}, function(data) {
                    console.log(data);

                    if (data.status == 'success') {
                        app.genreOptions = data.allGenres;
//                        this.$nextTick(function(){ $('#genresInput').selectpicker('refresh'); });
                    }
                })
            },
            getSortOptions: function() {
            	this.sortOptions = [
                	'Automatski',
                	'Naziv rastuće',
                	'Naziv opadajuće',
                	'Žanrovi rastuće',
                    'Žanrovi opadajuće',
                    'Trajanje rastuće',
                    'Trajanje opadajuće',
                    'Godina rastuće',
                    'Godina opadajuće',
                    'Distributer rastuće',
                    'Distributer opadajuće',
                    'Država rastuće',
                    'Država opadajuće',
                ];
            	this.selectedSort = 'Automatski';
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
            },
            refreshMovies: function(event){
            	
            	if (app.durationLow <= 0 || app.durationLow > 600) {
                    app.durationLow = undefined;
                }
            	if (app.durationHigh <= 0 || app.durationHigh > 600) {
                    app.durationHigh = undefined;
                }
            	if (app.yearLow <= 0 || app.yearLow > 2500) {
                    app.yearLow = undefined;
                }
            	if (app.yearHigh <= 0 || app.yearHigh > 2500) {
                    app.yearHigh = undefined;
                }
            	
            	this.getMovies();
            }
        },
        computed: {
            orderedMovies: function() {
                if (this.selectedSort === 'Automatski')
            		return this.movies;
            	else if (this.selectedSort === 'Naziv rastuće')
            		return _.orderBy(this.movies, 'title');
            	else if (this.selectedSort === 'Naziv opadajuće')
                    return _.orderBy(this.movies, 'title').reverse();
                else if (this.selectedSort === 'Trajanje rastuće')
            		return _.orderBy(this.movies, 'duration');
            	else if (this.selectedSort === 'Trajanje opadajuće')
                    return _.orderBy(this.movies, 'duration').reverse();
            	else if (this.selectedSort === 'Žanrovi rastuće')
            		return _.orderBy(this.movies, 'genres[0]');
            	else if (this.selectedSort === 'Žanrovi opadajuće')
                    return _.orderBy(this.movies, 'genres[0]').reverse();
                else if (this.selectedSort === 'Godina rastuće')
            		return _.orderBy(this.movies, 'year');
            	else if (this.selectedSort === 'Godina opadajuće')
                    return _.orderBy(this.movies, 'year').reverse();
                else if (this.selectedSort === 'Distributer rastuće')
            		return _.orderBy(this.movies, 'distributor');
            	else if (this.selectedSort === 'Distributer opadajuće')
                    return _.orderBy(this.movies, 'distributor').reverse();
                else if (this.selectedSort === 'Država rastuće')
            		return _.orderBy(this.movies, 'country');
            	else if (this.selectedSort === 'Država opadajuće')
            		return _.orderBy(this.movies, 'country').reverse();
            }
        },
        watch: {
	    	genreOptions: function(newValues, oldValues){
	    		this.$nextTick(function(){ $('#genresInput').selectpicker('refresh'); });
	    	},
	    	sortOptions: function(newValues, oldValues){
	    		this.$nextTick(function(){ $('#sortInput').selectpicker('refresh'); });
	    	}
	    }
    })

//    $('#titleInput').on('change keyup copy paste cut', function(event) {
//        event.preventDefault();
//        
//        app.getMovies();
//		
//		return false;
//    });
    
//    $('#distributorInput').on('change keyup copy paste cut', function(event) {
//		event.preventDefault();
//        
//        app.getMovies();
//		
//		return false;
//    });
    
//    $('#countryInput').on('change keyup copy paste cut', function(event) {
//		event.preventDefault();
//        
//        app.getMovies();
//		
//		return false;
//    });
    
//    $('#durationLowInput').on('change keyup copy paste cut', function(event) {
//        event.preventDefault();
//
//        if (app.durationLow <= 0 || app.durationLow > 600) {
//            app.durationLow = undefined;
//        }
//        app.getMovies();
//
//        return false;
//    });
    
//    $('#durationHighInput').on('change keyup copy paste cut', function(event) {
//		event.preventDefault();
//
//        if (app.durationHigh <= 0 || app.durationHigh > 600) {
//            app.durationHigh = undefined;
//        }
//        app.getMovies();
//        
//        return false;
//    });
    
//    $('#yearLowInput').on('change keyup copy paste cut', function(event) {
//		event.preventDefault();
//
//        if (app.yearLow <= 0 || app.yearLow > 2500) {
//            app.yearLow = undefined;
//        }
//        app.getMovies();
//        
//        return false;
//    });
    
//    $('#yearHighInput').on('change keyup copy paste cut', function(event) {
//		event.preventDefault();
//
//        if (app.yearHigh <= 0 || app.yearHigh > 2500) {
//            app.yearHigh = undefined;
//        }
//        app.getMovies();
//        
//        return false;
//	});
    
//    $('#genresInput').on('change', function (event) {
//        app.getMovies();
//        
//        event.preventDefault();
//        return false;
//    });
    
    app.getLoggedInUser();
    app.getGenres();
    app.getSortOptions();
    app.getMovies();
    setTimeout(function(){ $("#staticBackdrop").modal('hide'); }, 1000);
})
