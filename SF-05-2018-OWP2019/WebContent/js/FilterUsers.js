$(document).ready(function() {

    var app = new Vue({
        el: '#app',
        data: {
            username: '',
            userRole: '',
            users: []
        },
        methods: {
            getUsers: function(event) {
                console.log('username: ' + this.username);
                console.log('userRole: ' + this.userRole);

                var params = {
                    'username': this.username,
                    'userRole': this.userRole
                }

                $.get('FilterUsersServlet', params, function(data) {
                    console.log(data);
                    
                    app.users = data.filteredUsers;
                    
                });
            }
        },
        computed: {
            orderedUsers: function () {
                return this.users;
              }
        }
    })
    app.getUsers();
});