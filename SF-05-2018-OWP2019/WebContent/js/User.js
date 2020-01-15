$(document).ready(function() {
    var username = window.location.search.slice(1).split('&')[0].split('=')[1];
	console.log(username);
	
    var app = new Vue({
        el: '#app',
        data: {
            user: {},
            registrationDate: '',
            newPassword: '',
            repeatedNewPassword: ''
        },
        methods: {
            getUser: function(event) {

                $.get('UserServlet', {'username': username}, function(data) {
                    console.log(data);
                    
                    if (data.status == 'success') {
                        app.user = data.user;
                        app.registrationDate = data.user.registrationDate.dayOfMonth + 
                            '. ' + data.user.registrationDate.month + 
                            ' ' + data.user.registrationDate.year + '.';
                    }
                    
                });
            }
        },

    })
    
    app.getUser();
});