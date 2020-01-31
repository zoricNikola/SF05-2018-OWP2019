$(document).ready(function() {
	
    var app = new Vue({
        el: '#app',
        data: {
            title: '',
            selectedGenres: [],
            distributor: '',
            country: '',
            durationLow: undefined,
            durationHigh: undefined,
            yearLow: undefined,
            yearHigh: undefined,
            selectedSort: '',
            sortOptions: [
            	{ text: 'Sortiraj...', value: ''},
            	{ text: 'Automatski', value: ''},
            	{ text: 'Naziv rastuće', value: 'titleASC'},
            	{ text: 'Naziv opadajuće', value: 'titleDESC'},
            	{ text: 'Žanrovi rastuće', value: 'genresASC'},
                { text: 'Žanrovi opadajuće', value: 'genresDESC'},
                { text: 'Trajanje rastuće', value: 'durationASC' },
                { text: 'Trajanje opadajuće', value: 'durationDESC' },
                { text: 'Godina rastuće', value: 'yearASC' },
                { text: 'Godina opadajuće', value: 'yearDESC' },
                { text: 'Distributer rastuće', value: 'distributorASC' },
                { text: 'Distributer opadajuće', value: 'distributorDESC' },
                { text: 'Država rastuće', value: 'countryASC' },
                { text: 'Država opadajuće', value: 'countryDESC' },
            ],
            genreOptions: [],
            movies: []
        },
        methods: {
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
                if (this.selectedSort === '')
            		return this.movies;
            	else if (this.selectedSort === 'titleASC')
            		return _.orderBy(this.movies, 'title');
            	else if (this.selectedSort === 'titleDESC')
                    return _.orderBy(this.movies, 'title').reverse();
                else if (this.selectedSort === 'durationASC')
            		return _.orderBy(this.movies, 'duration');
            	else if (this.selectedSort === 'durationDESC')
                    return _.orderBy(this.movies, 'duration').reverse();
            	else if (this.selectedSort === 'genresASC')
            		return _.orderBy(this.movies, 'genres[0]');
            	else if (this.selectedSort === 'genresDESC')
                    return _.orderBy(this.movies, 'genres[0]').reverse();
                else if (this.selectedSort === 'yearASC')
            		return _.orderBy(this.movies, 'year');
            	else if (this.selectedSort === 'yearDESC')
                    return _.orderBy(this.movies, 'year').reverse();
                else if (this.selectedSort === 'distributorASC')
            		return _.orderBy(this.movies, 'distributor');
            	else if (this.selectedSort === 'distributorDESC')
                    return _.orderBy(this.movies, 'distributor').reverse();
                else if (this.selectedSort === 'countryASC')
            		return _.orderBy(this.movies, 'country');
            	else if (this.selectedSort === 'countryDESC')
            		return _.orderBy(this.movies, 'country').reverse();
            }
        },
        watch: {
	    	genreOptions: function(newValues, oldValues){
	    		this.$nextTick(function(){ $('#genresInput').selectpicker('refresh'); });
	    	},
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
    
    app.getGenres();
    app.getMovies();
})
