$(document).ready(function() {
	
    var app = new Vue({
        el: '#app',
        data: {
            username: '',
            users: [],
            selectedSort: '',
            sortOptions: [
            	{ text: 'Sortiraj...', value: ''},
            	{ text: 'Automatski', value: ''},
            	{ text: 'Korisničko ime a...z', value: 'usernameASC'},
            	{ text: 'Korisničko ime z...a', value: 'usernameDESC'},
            	{ text: 'Uloga a...z', value: 'userRoleASC'},
            	{ text: 'Uloga z...a', value: 'userRoleDESC'}
            ],
            selectedUserRole: '',
            userRoleOptions: [
            	{ text: 'Uloga', value: ''},
            	{ text: 'Sve', value: ''},
            	{ text: 'Korisnik', value: 'USER' },
            	{ text: 'Admin', value: 'ADMIN' }
            ],
        },
        methods: {
            getUsers: function(event) {
                console.log('username: ' + this.username);
                console.log('userRole: ' + this.selectedUserRole);

                var params = {
                    'username': this.username,
                    'userRole': this.selectedUserRole
                }

                $.get('FilterUsersServlet', params, function(data) {
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
                    	app.users = data.filteredUsers;
                    }
                    
                });
            },
            refreshUsers: function(event) {
            	this.getUsers();
            }
        },
        computed: {
            orderedUsers: function () {
            	if (this.selectedSort === '')
            		return this.users;
            	else if (this.selectedSort === 'usernameASC')
            		return _.orderBy(this.users, 'username');
            	else if (this.selectedSort === 'usernameDESC')
            		return _.orderBy(this.users, 'username').reverse();
            	else if (this.selectedSort === 'userRoleASC')
            		return _.orderBy(this.users, 'userRole');
            	else if (this.selectedSort === 'userRoleDESC')
            		return _.orderBy(this.users, 'userRole').reverse();
              }
        }
    })
    
//    $('#usernameInput').on('keyup', function(event) {
//		app.getUsers();
//
//		event.preventDefault();
//		return false;
//	});
//    
//    $('#userRoleInput').on('change', function (event) {
//        app.getUsers();
//        
//        event.preventDefault();
//        return false;
//        
//    });
//    $('#sortInput').on('change', function (event) {
//        
//        
//        event.preventDefault();
//        return false;
//        
//    });
    
    app.getUsers();
});