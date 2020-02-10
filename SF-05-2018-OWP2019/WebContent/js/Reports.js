$("#staticBackdrop").modal('show');
$(document).ready(function() {
	
    var app = new Vue({
        el: '#app',
        data: {
        	loggedInUser: { username: '', userRole: ''},
            sortOptions: [],
            sortOption: '',
            selectedSort: '',
            timeLow: '',
            timeHigh: '',
            reports: []
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
            getSortOptions: function() {
                this.sortOptions = ['Automatski', 'Broj projekcija rastuće', 'Broj projekcija opadajuće', 
                    'Broj prodatih karata rastuće', 'Broj prodatih karata opadajuće', 
                    'Ukupna cena prodatih karata rastuće', 'Ukupna cena prodatih karata opadajuće'];
                this.selectedSort = 'Automatski';
            },
            getReports: function(event) {
                console.log('timeLow: ' + this.timeLow);
                console.log('timeHigh: ' + this.timeHigh);

                var params = {
                    'timeLow': this.timeLow,
                    'timeHigh': this.timeHigh
                }

                $.get('ReportServlet', params, function(data) {
                    console.log(data);
                    
                    if (data.status == 'unauthenticated') {
                    	window.location.replace('Login.html');
                    	return;
                    }
                    
                    if (data.status == 'unauthorized') {
                    	window.location.replace('Welcome.html');
                    	return;
                    }
                    
                    if (data.status == 'success') {
                    	app.reports = [];
                    	var keys = Object.keys(data.reports);
                    	for (var i = 0; i < keys.length; i++) {
                    		app.reports.push(data.reports[keys[i]]);
                    	}
                    	console.log(app.reports);
                    }
                    
                });
            },
            refreshReports: function(event) {
            	this.getReports();
            }
        },
        computed: {
            orderedReports: function () {
            	if (this.selectedSort === 'Automatski')
            		return this.reports;
            	else if (this.selectedSort === 'Broj projekcija rastuće')
            		return _.orderBy(this.reports, 'numberOfProjections');
            	else if (this.selectedSort === 'Broj projekcija opadajuće')
            		return _.orderBy(this.reports, 'numberOfProjections').reverse();
            	else if (this.selectedSort === 'Broj prodatih karata rastuće')
            		return _.orderBy(this.reports, 'numberOfTickets');
            	else if (this.selectedSort === 'Broj prodatih karata opadajuće')
            		return _.orderBy(this.reports, 'numberOfTickets').reverse();
            	else if (this.selectedSort === 'Ukupna cena prodatih karata rastuće')
            		return _.orderBy(this.reports, 'totalPrice');
            	else if (this.selectedSort === 'Ukupna cena prodatih karata opadajuće')
            		return _.orderBy(this.reports, 'totalPrice').reverse();
            	else
            		return this.reports;
              },
              sumOfProjections: function() {
            	  var sum = 0;
            	  for (var i = 0; i < this.reports.length; i++) {
            		  sum += this.reports[i].numberOfProjections;
            	  }
            	  return sum;
              },
              sumOfTickets: function() {
            	  var sum = 0;
            	  for (var i = 0; i < this.reports.length; i++) {
            		  sum += this.reports[i].numberOfTickets;
            	  }
            	  return sum;
              },
              sumOfTotalPrice: function() {
            	  var sum = 0;
            	  for (var i = 0; i < this.reports.length; i++) {
            		  sum += this.reports[i].totalPrice;
            	  }
            	  return sum;
              },
        },
        watch: {
            sortOptions: function(newValues, oldValues){
	    		this.$nextTick(function(){ $('#sortInput').selectpicker('refresh'); });
	    	},
        }
    })
    
    app.getSortOptions();
    app.getLoggedInUser();
    app.getReports();
    setTimeout(function(){ $("#staticBackdrop").modal('hide'); }, 1000);
    
    $.datetimepicker.setDateFormatter('moment');
    var dateLow = new Date();
    $('#picker1').datetimepicker({
    	timepicker: false,
        datepicker: true,
        format: 'YYYY-MM-DD',
        yearStart: 2020,
        yearEnd: 2030,
        theme: 'dark',
        value: dateLow,
        defaultDate: new Date(),
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

                app.timeLow = d.getFullYear().toString() + '-' + months + '-' + days;

            } catch (error) {
                app.timeLow = '';
            }                
            console.log(app.timeLow);
            app.refreshReports();
        }
    })
    $('#toggle1').on('click', function(e) {
        $("#picker1").datetimepicker('toggle');
    })
    var dateHigh = new Date();
    $('#picker2').datetimepicker({
    	timepicker: false,
        datepicker: true,
        format: 'YYYY-MM-DD',
        yearStart: 2020,
        yearEnd: 2030,
        minTime: 8,
        theme: 'dark',
        value: dateHigh,
        defaultDate: new Date(),
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

                app.timeHigh = d.getFullYear().toString() + '-' + months + '-' + days;
                

            } catch (error) {
                app.timeHigh = '';
            }                
            console.log(app.timeHigh);
            app.refreshReports();
        }
    })
    $('#toggle2').on('click', function(e) {
        $("#picker2").datetimepicker('toggle');
    })
});