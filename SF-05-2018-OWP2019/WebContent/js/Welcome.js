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
            projectionTypeOptions: ['2D', '3D', '4D'],
            selectedProjectionTypes: [],
            projectionTypeOption: '',
            movies: [],
            hallOptions: ['Hall1', 'Hall2', 'Hall3'],
            selectedHalls: [],
            hallOption: '',
            sortOption: '',
            timeLow: '',
            timeHigh: ''
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
                return;
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
	    }
    })

    app.getLoggedInUser();
    //app.getSortoptions();
    
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
                app.timeLow = d.getFullYear().toString() + '-' + d.getMonth().toString() + '-' + d.getDate().toString() + ' ' + d.getHours().toString() + ':' + d.getMinutes().toString();

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
                app.timeHigh = d.getFullYear().toString() + '-' + d.getMonth().toString() + '-' + d.getDate().toString() + ' ' + d.getHours().toString() + ':' + d.getMinutes().toString();

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
