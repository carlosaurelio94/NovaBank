const { createApp } = Vue

createApp({
    data() {
        return {
            accounts: [],
            accountCurrent: [],
            urlApi: "/api/clients/current",
            hora: "",
            transactions: [],
            numberFrom: "",
            destination: "",
            numberTo: "",
            numberToThird: "",
            amount: 0,
            description: "",
            contact: "",
            accountNumber: "",
            nickname: "",
            emailContact: ""
        }
    },
    created() {
        this.loadData(this.urlApi)
    },
    mounted() {

    },
    methods: {
        loadData(url) {
            axios
                .get(url)
                .then(res => {
                    this.clients = res.data
                    this.accounts = this.clients.account.sort((a, b) => { if (a.id < b.id) return -1 })
                    this.transactions = this.transactions.sort((a, b) => { if (a.date > b.date) return -1 })
                    this.contacts = this.clients.contact
                    console.log(this.contacts);
                    this.horaActual()
                    this.intervalo()
                })
                .catch(error => console.error(error.message))
        },
        modificarSaldo(saldo) {
            return new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' }).format(saldo);
        },
        horaActual() {
            let hora = new Date()
            let horaFinal = ""
            let horas = hora.getHours()
            if (horas < 10) {
                horas = "0" + horas
            }
            let minutos = hora.getMinutes()
            if (minutos < 10) {
                minutos = "0" + minutos
            }
            let segundos = hora.getSeconds()
            if (segundos < 10) {
                segundos = "0" + segundos
            }
            if (horas >= 12 && horas < 24) {
                this.hora = ""
                let horaTotal = horas + ":" + minutos + ":" + segundos;
                this.hora = horaTotal;
                horaFinal = this.hora
            } else {
                this.hora = ""
                let horaTotal = horas + ":" + minutos + ":" + segundos;
                this.hora = horaTotal;
                horaFinal = this.hora
            }
            return horaFinal
        },
        intervalo() {
            return setInterval(this.horaActual, 1000)
        },
        cerrarSesion() {
            axios.post('/api/logout').then(response => {
                localStorage.setItem("cookie", "false")
            })
            location.reload()
        },
        mayusculas() {
            let firstName = this.firstName.slice(0, 1)
            let FirstCase = firstName.toUpperCase()
            let firstName2 = this.firstName.slice(1)
            let lastName = this.lastName.slice(0, 1)
            let lastCase = lastName.toUpperCase()
            let lastName2 = this.lastName.slice(1)
            return FirstCase + firstName2 + " " + lastCase + lastName2
        },
        saldo() {
            this.accountCurrent = this.accounts.filter(account => account.number == this.numberFrom)
            return this.accountCurrent[0].balance
        },
        modificarSaldo(saldo) {
            return new Intl.NumberFormat('en-US', { style: 'currency', currency: 'USD' }).format(saldo);
        },
        agendarContacto() {
            let account = this.accountNumber
            let email = this.emailContact
            let nickname = this.nickname
            return axios.post('/api/clients/current/contacts', `accountNumber=${account}&email=${email}&nickname=${nickname}`).then(response => Swal.fire({
                text: 'Succesful Registration',
                title: 'You added to your contact list.',
                icon: 'success',
                confirmButtonColor: '#24cb24',
            })).then(response => window.location.assign("./transfers.html"))
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
        crearTransacciones() {
            let numberFrom = this.numberFrom
            let numberTo = this.numberTo
            let amount = this.amount
            if (isNaN(amount)) {
                amount = 0
            }
            let description = this.description
            let alert = Swal.fire({
                title: 'Are you sure?',
                text: "You won't be able to undo the transaction once you agree to it.",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#24cb24',
                cancelButtonColor: '#ff4545',
                confirmButtonText: 'Yes, I am sure'
            }).then(res => {
                if (res.isConfirmed == true) {
                    return axios.post('/api/transactions', `numberFrom=${numberFrom}&numberTo=${numberTo}&amount=${amount}&description=${description}`)
                        .then(result => 
                            Swal.fire({
                                title: 'Successful transfer',
                                text: "Your transfer has been sent",
                                icon: 'success',
                                confirmButtonColor: '#24cb24',
                            })
                        ).then(result => 
                                window.location.assign("./accounts.html")
                        )
                        .catch(error => Swal.fire({
                            icon: 'error',
                            title: 'Error' + error.response.status,
                            text: error.response.data,
                            confirmButtonColor: '#ff4545',

                        /*                 footer: '<a href="">Why do I have this issue?</a>'
                    */              })
                        )
                }
            })
        }
    },
    computed: {

    }
}).mount('#app')
