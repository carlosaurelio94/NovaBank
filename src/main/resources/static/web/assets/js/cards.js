const { createApp } = Vue

createApp({
    data() {
        return {
            clientes: [],
            tarjetas: [],
            tarjetasCredito: [],
            tarjetasDebito: [],
            prestamos: [],
            numeros: [],
            password: "",
            firstName: "",
            urlApi: "/api/clients/current",
            hora: "",
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
                    this.tarjetas = res.data.card
                    this.clientes = res.data
                    this.firstName = res.data.firstName
                    this.lastName = res.data.lastName
                    this.tarjetas = this.tarjetas.sort((a, b) => { if (a.id < b.id) return -1 })
                    this.tarjetasCredito = this.tarjetas.filter(tarjeta => tarjeta.type == 'CREDIT' && tarjeta.validate == 'OK')
                    this.tarjetasDebito = this.tarjetas.filter(tarjeta => tarjeta.type == 'DEBIT' && tarjeta.validate == 'OK')
                    this.prestamos = res.data.loan
                    console.log(this.clientes);
                    this.horaActual()
                    this.intervalo()
                })
                .catch(error => console.error(error.message))
        },
        ocultarNumeros(id) {
            let cambiar = document.getElementById(id)
            let asteriscos = "**** **** **** "
            if (!cambiar.innerHTML.startsWith('****')) {
                cambiar.innerHTML = asteriscos + id.slice(15, 19)
            }
        },
        mostrarNumeros(id) {
            let cambiar = document.getElementById(id)
            
            cambiar.innerHTML = id.replaceAll("-", " ")
        },
        modificarFecha(fecha) {
            let fechaNueva = fecha.split("T")
            let fechaOtra = fechaNueva[0].split("-")
            return `${fechaOtra[2]}-${fechaOtra[1]}-${fechaOtra[0]}`
        },
        localDate() {
            let date = new Date()
            let currentDay = date.getDate()
            let currentMonth = date.getMonth()
            let currentYear = date.getFullYear()
            return `${currentYear}-${currentMonth+1}-${currentDay}`
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
        mayusculas() {
            let firstName = this.firstName.slice(0,1)
            let FirstCase = firstName.toUpperCase()
            let firstName2 = this.firstName.slice(1)
            let lastName = this.lastName.slice(0,1)
            let lastCase = lastName.toUpperCase()
            let lastName2 = this.lastName.slice(1)
            return FirstCase+firstName2+" "+lastCase+lastName2
        },
        cerrarSesion() {
            axios.post('/api/logout').then(response => {
                localStorage.setItem("cookie", "false")
            })
            location.reload()
        },
        deleteCard(cardNumber) {
            let alert = Swal.fire({
                title: 'Are you sure?',
                text: "You won't be able to recover your card once you agree to it.",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#24cb24',
                cancelButtonColor: '#ff4545',
                confirmButtonText: 'Yes, I am sure'
            }).then(res => {
                if (res.isConfirmed == true) {
                    return axios.patch('/api/cards', `number=${cardNumber}`)
                        .then(result =>
                            Swal.fire({
                                title: 'Successful delete',
                                text: "Your card has been deleted",
                                icon: 'success',
                                confirmButtonColor: '#24cb24',
                            })
                        ).then(result =>
                            window.location.assign("./accounts.html")
                        )
                        .catch(error => Swal.fire({
                            icon: 'error',
                            title: 'Error ' + error.response.status,
                            text: error.response.data,
                            confirmButtonColor: '#ff4545',
                        /*                 footer: '<a href="">Why do I have this issue?</a>'
                    */              })
                        )
                }
            })
        },
    },
    computed: {

    }
}).mount('#app')
