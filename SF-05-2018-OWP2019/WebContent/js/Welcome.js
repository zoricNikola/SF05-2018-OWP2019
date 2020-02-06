$("#staticBackdrop").modal('show');

$(document).ready(function() {
	
    var app = new Vue({
        el: '#app',
        data: {
        	loggedInUser: { username: '', userRole: '' },
            movie: '',
            priceLow: undefined,
            priceHigh: undefined,
            sortOptions: [''],
            selectedSort: '',
            projectionTypeOptions: [],
            selectedProjectionTypes: [],
            projectionTypeOption: {id: 0, name: ''},
            hallOptions: [],
            selectedHalls: [],
            hallOption: {id: 0, name: ''},
            sortOption: '',
            timeLow: '',
            timeHigh: '',
            projectionsMap: {}
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
            refreshProjections: function(event){
            	console.log('movie: ' + this.movie);
                console.log('projectionTypes: ' + this.selectedProjectionTypes);
                console.log(typeof(this.selectedProjectionTypes[0]));
                console.log('halls: ' + this.selectedHalls);
                console.log('timeLow: ' + this.timeLow);
                console.log('timeHigh: ' + this.timeHigh);
                console.log('priceLow: ' + this.priceLow);
                console.log('priceHigh: ' + this.priceHigh);
                
                this.getProjections();
                
                return;
            },
            getProjections: function(event) {

                var params = {
                    'movie': this.movie,
                    'projectionTypes': this.selectedProjectionTypes,
                    'halls': this.selectedHalls,
                    'timeLow': this.timeLow,
                    'timeHigh': this.timeHigh,
                    'priceLow': this.priceLow,
                    'priceHigh': this.priceHigh
                }

                $.get('FilterProjectionsServlet', params, function(data) {
                    console.log(data);
                    
                    if (data.status == 'success') {
                    	app.projectionsMap = data.projectionsMap;
                    	for (var key in app.projectionsMap){
                    		  console.log( key );
                    		  console.log(app.projectionsMap[key]);
                    		  console.log(app.projectionsMap[key][0]);
                    		  console.log(app.projectionsMap[key][0].time);
                    		}
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
            getProjectionTypes: function() {
                $.get('ProjectionTypeServlet', {'searchType': 'all'}, function(data) {
                    console.log(data);

                    if (data.status == 'success') {
                        app.projectionTypeOptions = data.allProjectionTypes;
                    }
                })
            },
            getHalls: function() {
                $.get('HallServlet', {'searchType': 'all'}, function(data) {
                    console.log(data);

                    if (data.status == 'success') {
                        app.hallOptions = data.allHalls;
                    }
                })
            },
            getSortoptions: function() {
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
                this.sortOption = 'Automatski';
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
            sortOptions: function(newValues, oldValues){
	    		this.$nextTick(function(){ $('#sortInput').selectpicker('refresh'); });
	    	},
	    	projectionTypeOptions: function(newValues, oldValues){
	    		this.$nextTick(function(){ $('#projectionTypesInput').selectpicker('refresh'); });
	    	},
	    	hallOptions: function(newValues, oldValues){
	    		this.$nextTick(function(){ $('#hallsInput').selectpicker('refresh'); });
	    	},
	    }
    })

    app.getLoggedInUser();
    app.getSortoptions();
    app.getProjectionTypes();
    app.getHalls();
    app.getProjections();
    
    setTimeout(function(){ $("#staticBackdrop").modal('hide'); }, 1000);

    $.datetimepicker.setDateFormatter('moment');
    $('#picker1').datetimepicker({
        timepicker: true,
        datepicker: true,
        format: 'YYYY-MM-DD H:m',
        yearStart: 2020,
        yearEnd: 2030,
        minTime: 8,
        theme: 'dark',
        defaultDate: new Date(),
        defaultTime: '8:00',
        step: 5,
        onShow: function(ct){
            this.setOptions({
                maxDate: $('#picker2').val() ? $('#picker2').val() : false
            })
        },
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
                
                app.timeLow = d.getFullYear().toString() + '-' + months + '-' + days + ' ' + hours + ':' + minutes;

            } catch (error) {
                app.timeLow = '';
            }                
            console.log(app.timeLow);
            app.refreshProjections();
        }
    })
    $('#toggle1').on('click', function(e) {
        $("#picker1").datetimepicker('toggle');
    })
    $('#picker2').datetimepicker({
        timepicker: true,
        datepicker: true,
        format: 'YYYY-MM-DD H:mm',
        yearStart: 2020,
        yearEnd: 2030,
        minTime: 8,
        theme: 'dark',
        defaultDate: new Date(),
        defaultTime: '23:59',
        step: 5,
        onShow: function(ct){
            this.setOptions({
                minDate: $('#picker1').val() ? $('#picker1').val() : false
            })
        },
        onChangeDateTime: function(ct){
            try {
                var d = $('#picker2').datetimepicker('getValue');
                
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
                
                app.timeHigh = d.getFullYear().toString() + '-' + months + '-' + days + ' ' + hours + ':' + minutes;
                

            } catch (error) {
                app.timeHigh = '';
            }                
            console.log(app.timeHigh);
            app.refreshProjections();
        }
    })
    $('#toggle2').on('click', function(e) {
        $("#picker2").datetimepicker('toggle');
    })
})
