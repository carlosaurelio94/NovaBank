const { createApp } = Vue

createApp({
    data() {
        return {
            message: 'Hello Vue!',
            clientes: [],
            cuentas: [],
            balances: [],
            urlApi: "/api/clients/current",
            hora: "",
            prestamos: [],
            tarjetas: [],
            tarjetasCredito: [],
            tarjetasDebito: [],
            color: "GOLD",
            tipo: "CREDIT",
            nombreCompleto: ""


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
                    this.clientes = res.data
                    this.prestamos = res.data.loan
                    this.cuentas = this.clientes.account.sort((a, b) => { if (a.id < b.id) return -1 })
                    this.prestamos = this.prestamos.sort((a, b) => { if (a.id < b.id) return -1 })
                    this.nombreCompleto = this.clientes.firstName + " " + this.clientes.lastName
                    this.tarjetas = res.data.card
                    this.tarjetasCredito = this.tarjetas.filter(tarjeta => tarjeta.type == 'CREDIT')
                    this.tarjetasDebito = this.tarjetas.filter(tarjeta => tarjeta.type == 'DEBIT')
                    console.log(this.cuentas);
                    this.horaActual()
                    this.intervalo()
                })
                .catch(error => console.error(error.message))
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
        crearTarjetas() {
            let color = this.color
            let tipo = this.tipo
            let numero = this.number
            return axios.post('/api/clients/current/cards', `type=${tipo}&color=${color}&number=${numero}`)
                .then(response => Swal.fire({
                    text: 'Card created!',
                    title: 'Now you can use your card!',
                    icon: 'success',
                    confirmButtonColor: '#24cb24',
                }))
                .then(res => window.location.assign("./cards.html")).catch(error => Swal.fire({
                    icon: "error",
                    title: "Error " + error.response.status,
                    text: error.response.data,
                    confirmButtonColor: '#ff4545',
/*                 footer: '<a href="">Why do I have this issue?</a>'
 */            }))
        }
    },
    computed: {

    }
}).mount('#app')
