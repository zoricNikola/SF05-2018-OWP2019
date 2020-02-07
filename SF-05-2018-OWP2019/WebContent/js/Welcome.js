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
                    'movieTitle': this.movie,
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
                    'Naziv filma rastuće',
                    'Naziv filma opadajuće',
                    'Cena rastuće',
                    'Cena opadajuće',
                    'Tipovi projekcija rastuće',
                    'Tipovi projekcija opadajuće',
                    'Sale rastuće',
                    'Sale opadajuće',
                    'Vreme rastuće',
                    'Vreme opadajuće',
                ];
                this.selectedSort = 'Automatski';
            },
            sortedProjections: function(projections){
        		if (this.selectedSort === 'Automatski')
        			return _.sortBy(projections, ['time.year', 'time.monthValue', 'time.dayOfMonth', 'time.hour', 'time.minute']);
        		else if (this.selectedSort === 'Cena rastuće')
        			return _.orderBy(projections, 'price');
        		else if (this.selectedSort === 'Cena opadajuće')
        			return _.orderBy(projections, 'price').reverse();
        		else if (this.selectedSort === 'Tipovi projekcija rastuće')
        			return _.orderBy(projections, 'projectionType.id');
        		else if (this.selectedSort === 'Tipovi projekcija opadajuće')
        			return _.orderBy(projections, 'projectionType.id').reverse();
        		else if (this.selectedSort === 'Sale rastuće')
        			return _.orderBy(projections, 'hall.id');
        		else if (this.selectedSort === 'Sale opadajuće')
        			return _.orderBy(projections, 'hall.id').reverse();
        		else if (this.selectedSort === 'Vreme rastuće')
        			return _.sortBy(projections, ['time.year', 'time.monthValue', 'time.dayOfMonth', 'time.hour', 'time.minute']);
        		else if (this.selectedSort === 'Vreme opadajuće')
        			return _.sortBy(projections, ['time.year', 'time.monthValue', 'time.dayOfMonth', 'time.hour', 'time.minute']).reverse();
        		else
        			return _.sortBy(projections, ['time.year', 'time.monthValue', 'time.dayOfMonth', 'time.hour', 'time.minute']);
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
                
                var dateHigh = new Date();
                dateHigh.setHours(23);
                dateHigh.setMinutes(59);
                
                months = (dateHigh.getMonth()+1).toString();
                if (dateHigh.getMonth()+1 < 10)
                    months = '0' + months;

                days = (dateHigh.getDate()).toString();
                if (dateHigh.getDate() < 10)
                    days = '0' + days;

                hours = (dateHigh.getHours()).toString();
                if (dateHigh.getHours() < 10)
                    hours = '0' + hours;

                minutes = (dateHigh.getMinutes()).toString();
                if (dateHigh.getMinutes() < 10)
                    minutes = '0' + minutes;
                
                app.timeHigh = dateHigh.getFullYear().toString() + '-' + months + '-' + days + ' ' + hours + ':' + minutes;
                
            }
        },
        computed: {
            orderedMovies: function() {
            	
                if (this.selectedSort === 'Automatski')
            		return this.projectionsMap;
            	else if (this.selectedSort === 'Naziv filma rastuće')
            		return _.orderBy(this.projectionsMap, 'keys').reverse();
            	else if (this.selectedSort === 'Naziv filma opadajuće')
                    return _.orderBy(this.projectionsMap, 'keys');
            	else
            		return this.projectionsMap;
            },
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
    app.setTimesAtStart();
    app.getSortoptions();
    app.getProjectionTypes();
    app.getHalls();
    app.getProjections();
    
    setTimeout(function(){ $("#staticBackdrop").modal('hide'); }, 1000);

    $.datetimepicker.setDateFormatter('moment');
    var dateLow = new Date();
    dateLow.setHours(8);
    dateLow.setMinutes(0);
    $('#picker1').datetimepicker({
        timepicker: true,
        datepicker: true,
        format: 'YYYY-MM-DD H:m',
        yearStart: 2020,
        yearEnd: 2030,
        minTime: 8,
        theme: 'dark',
        value: dateLow,
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
    var dateHigh = new Date();
    dateHigh.setHours(23);
    dateHigh.setMinutes(59);
    $('#picker2').datetimepicker({
        timepicker: true,
        datepicker: true,
        format: 'YYYY-MM-DD H:mm',
        yearStart: 2020,
        yearEnd: 2030,
        minTime: 8,
        theme: 'dark',
        value: dateHigh,
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
