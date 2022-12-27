const { createApp } = Vue

createApp({
    data() {
        return {
            message: "Hola",
            email: "",
            password: "",
            firstName: "",
            lastName: "",
            emailSignIn: "",
            passwordSignIn: "",
            passwordChange: ""
        }
    },
    created() {
    },
    mounted() {
    },
    methods: {
        iniciarSesion() {
            let email = this.emailSignIn.toLowerCase();
            let password = this.passwordSignIn
            return axios.post('/api/login', `email=${email}&password=${password}`)
            .then(response =>{
                localStorage.setItem("cookie", "true")
                window.location.assign("./accounts.html")
            } )
            .catch(function (error) {
                return Swal.fire({
                    icon: "error",
                    title: "Error " + error.response.status,
                    text: "Your email or password are incorrect. Try again",
                    confirmButtonColor: '#ff4545',
                 footer: '<a href="">Why do I have this issue?</a>'
               })
            })
        },
        registrarse() {
            let email = this.email.toLowerCase()
            let password = this.password
            let firstName = this.firstName.toLowerCase()
            let lastName = this.lastName.toLowerCase()
            return axios.post('/api/clients', `firstName=${firstName}&lastName=${lastName}&email=${email}&password=${password}`)
                .then(response => axios.post('/api/login', `email=${email}&password=${password}`))
                .then(res => {
                    localStorage.setItem("cookie", "true")
                })
                .then(response => Swal.fire({
                    text: 'Succesful Registration',
                    title: 'Welcome to Nova Bank',
                    icon: 'success',
                    confirmButtonColor: '#24cb24',
        }))
                .then(response => window.location.assign("./accounts.html"))
                .catch(function (error) {
                    return Swal.fire({
                        icon: "error",
                        title: "Error " + error.response.status,
                        text: error.response.data,
                        confirmButtonColor: '#ff4545',
/*                 footer: '<a href="">Why do I have this issue?</a>'
 */              })
                })
        },
        changePassword() {
            let password = this.passwordChange
            return axios.patch('/api/clients/current', `password=${password}`).then(response => Swal.fire({
                title: 'Password changed succesfully',
                text: 'We have already deleted the previous one, this will now be your only password.',
                icon: 'success',
                confirmButtonColor: '#24cb24'
        })).then(response => window.location.assign("./accounts.html"))
            .catch(function (error) {
                return Swal.fire({
                    icon: "error",
                    title: "Error " + error.response.status,
                    text: error.response.data,
                    confirmButtonColor: '#ff4545',
/*                 footer: '<a href="">Why do I have this issue?</a>'
*/              })
            })
        },
        signUp() {
            let container = document.getElementById('container');
            return container.classList.add("right-panel-active")
        },
        signIn() {
            let container = document.getElementById('container');
            return container.classList.remove("right-panel-active")
        },

    },
}).mount('#app')
