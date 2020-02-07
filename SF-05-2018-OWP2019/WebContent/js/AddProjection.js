$("#staticBackdrop").modal('show');
$(document).ready(function() {
	
	var app = new Vue({
	    el: '#app',
	    data: {
	    	loggedInUser: { username: '', userRole: ''},
            movieTitleSearch: '',
            time: '',
            price: undefined,
            message: '',
            showMessage: false,
            projectionTypeOptions: [],
            filteredProjectionTypes: [],
            selectedProjectionType: undefined,
            projectionTypeOption: {id: 0, name: ''},
            hallOptions: [],
            filteredHalls: [],
            selectedHall: undefined,
            hallOption: {id: 0, name: ''},
            movies: [],
            filteredMovies: [],
            selectedMovie: undefined,
            movie: {},
            message: ''
	    },
	    methods: {
	    	getLoggedInUser: function() {
	    		$.get('LoggedInUserServlet', function(data) {
	    	        console.log(data);
	    	        
	    	        if (data.status != 'success') {
	    	            window.location.replace('Login.html');
	    	            return;
	    	        }
	    	        if (data.loggedInUser.userRole != 'ADMIN') {
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
	        addProjectionSubmit: function(event) {
	            console.log('movieID: ' + this.selectedMovie);
	        	console.log('time: ' + this.time);
                console.log('price: ' + this.price);
                console.log('projectionTypeID: ' + this.selectedProjectionType);
                console.log('hallID: ' + this.selectedHall);
	            
	            if(!this.validation()) {
	                this.showMessage = true;
	                
	            }
	            else {
	                this.message = '';
                    this.showMessage = false;
                    
	                var params = {
                            'action': 'add',
	                        'movieID': this.selectedMovie,
                            'time': this.time,
                            'price': this.price,
                            'projectionTypeID': this.selectedProjectionType,
                            'hallID': this.selectedHall
	                }
	                
	                $.post('ProjectionServlet', params, function(data) {
	                    console.log(data);
	                    
	                    if (data.status == 'failure') {
	                    	if (data.message == 'timeInPast')
	                    		app.message = 'Izabrano vreme je u prošlosti!';
	                    	else if (data.message == 'timeOverlaps')
	                    		app.message = 'Izabrano vreme za izabranu salu je zauzeto!';
	                    	$('#messageModal').modal('show');
	                    }
	                    else if (data.status == 'success') {
	                    	app.message = 'Uspešno ste dodali novu projekciju!';
	                        $('#messageModal').modal('show');
	                        $('#messageModal').on('hidden.bs.modal', function (e) {
	                            window.location.replace('Welcome.html');
	                        });
	                    }
	                });
	            }
	            return false;
	        },
	        validation: function () {
	            if (typeof this.selectedMovie === 'undefined' || this.selectedMovie < 1) {
	                this.message = 'Morate izabrati film!';
                }
                else if (this.time === '') {
	                this.message = 'Vreme mora biti izabrano!';
                }
                else if (typeof this.price === 'undefined') {
	                this.message = 'Cena mora biti uneta!';
                }
                else if (typeof this.selectedProjectionType === 'undefined' || this.selectedProjectionType < 1) {
	                this.message = 'Morate izabrati tip projekcije!';
                }
                else if (typeof this.selectedHall === 'undefined' || this.selectedHall < 1) {
	                this.message = 'Morate izabrati salu!';
                }
	            else {
	                return true;
	            }
	            return false;
            },
            checkNumber: function(event) {
            	if (app.price <= 0)
                    app.price = undefined;
            },
            getProjectionTypes: function() {
                $.get('ProjectionTypeServlet', {'searchType': 'all'}, function(data) {
                    console.log(data);

                    if (data.status == 'success') {
                        app.projectionTypeOptions = data.allProjectionTypes;
                        app.filteredProjectionTypes = data.allProjectionTypes;
                    }
                })
            },
            getHalls: function() {
                $.get('HallServlet', {'searchType': 'all'}, function(data) {
                    console.log(data);

                    if (data.status == 'success') {
                        app.hallOptions = data.allHalls;
                        app.filteredHalls = data.allHalls;
                    }
                })
            },
            getMovies: function(event) {

                $.get('FilterMoviesServlet', function(data) {
                    console.log(data);
                    
                    if (data.status == 'success') {
                    	app.movies = data.filteredMovies;
                    	app.filteredMovies = data.filteredMovies;
                    }
                    
                });
            },
            searchMovies: function(event) {
            	this.filteredMovies = [];
            	for (var i = 0; i < this.movies.length; i++) {
            		if (this.movies[i].title.toLowerCase().includes(this.movieTitleSearch.toLowerCase())) {
            			this.filteredMovies.push(this.movies[i]);
            		}
            	}
            },
            refreshHalls: function(event) {
            	this.filteredHalls = [];
            	for (var i = 0; i < this.hallOptions.length; i++) {
            		for (var j = 0; j < this.hallOptions[i].projectionTypes.length; j++) {
            			if (this.hallOptions[i].projectionTypes[j].id == this.selectedProjectionType) {
            				this.filteredHalls.push(this.hallOptions[i]);
            				break;
            			}
            		}
            	}
            },
            refreshProjectionTypes: function(event) {
            	for (var i = 0; i < this.filteredHalls.length; i++) {
            		if (this.filteredHalls[i].id == this.selectedHall) {
            			this.filteredProjectionTypes = this.filteredHalls[i].projectionTypes;
            		}
            	}
            },
            resetSelections: function(event) {
            	this.selectedProjectionType = undefined;
            	this.filteredProjectionTypes = this.projectionTypeOptions;
            	this.$nextTick(function(){ $('#projectionTypeInput').selectpicker('refresh'); });
            	this.selectedHall = undefined;
            	this.filteredHalls = this.hallOptions;
            	this.$nextTick(function(){ $('#hallInput').selectpicker('refresh'); });
            	this.movieTitleSearch = '';
            	this.filteredMovies = this.movies;
            	this.$nextTick(function(){ $('#movieInput').selectpicker('refresh'); });
            }
	    },
	    watch: {
	    	filteredMovies: function(newValues, oldValues){
	    		this.$nextTick(function(){ $('#movieInput').selectpicker('refresh'); });
	    	},
	    	filteredHalls: function(newValues, oldValues){
	    		this.$nextTick(function(){ $('#hallInput').selectpicker('refresh'); });
	    	},
	    	filteredProjectionTypes: function(newValues, oldValues){
	    		this.$nextTick(function(){ $('#projectionTypeInput').selectpicker('refresh'); });
	    	}
	    }
        
    })
    
	app.getLoggedInUser();
	app.getProjectionTypes();
	app.getHalls();
	app.getMovies();
    setTimeout(function(){ $("#staticBackdrop").modal('hide'); }, 1000);
    
    $.datetimepicker.setDateFormatter('moment');

    $('#picker1').datetimepicker({
        timepicker: true,
        datepicker: true,
        format: 'YYYY-MM-DD H:m',
        yearStart: 2020,
        yearEnd: 2030,
        theme: 'dark',
        minDate: new Date(),
        step: 5,
        onChangeDateTime: function(ct){
            try {
                var d = $('#picker1').datetimepicker('getValue');
                
                var months = (d.getMonth()+1).toString();
                if (d.getMonth()+1 < 10)
                    months = '0' + months;

                var days = (d.getDate()).toString();
                if (d.getDate() < 10)
                    days = '0' + days;

                var hours = (d.getHours()).toString();
                if (d.getHours() < 10)
                    hours = '0' + hours;

                var minutes = (d.getMinutes()).toString();
                if (d.getMinutes() < 10)
                    minutes = '0' + minutes;
                
                app.time = d.getFullYear().toString() + '-' + months + '-' + days + ' ' + hours + ':' + minutes;

            } catch (error) {
                app.time = '';
            }                
            console.log(app.time);
        }
    })
    $('#toggle1').on('click', function(e) {
        $("#picker1").datetimepicker('toggle');
    })

});
