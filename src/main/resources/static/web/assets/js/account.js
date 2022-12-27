const { createApp } = Vue

createApp({
    data() {
        return {
            id: new URLSearchParams(location.search).get('id'),
            urlApi: "/api/accounts/",
            hora: "",
            transacciones: [],
            balances: [],
            saldoActual: 0,
            numeroCuenta: "",
            tipoTransaccion: "",
            nuevo: {},
            dateFrom: "",
            dateTo: "",
            accountNumber: ""
        }
    },
    created() {
        this.loadData(this.urlApi + this.id)
    },
    mounted() {
    },
    methods: {
        loadData(url) {
            axios
                .get(url)
                .then(res => {
                    this.otro = res
                    this.accountNumber = res.data.number
                    this.saldoActual = res.data.balance
                    this.transacciones = res.data.transaction
                    this.transacciones = this.transacciones.sort((a, b) => { if (a.date > b.date) return -1 })
                    this.transaction = res.data.transaction
                    /* this.horaActual()
                    this.intervalo() */
                    console.log(this.accountNumber);
                })
                .catch(error => {
                    if (error.response.data == "THIS IS NOT YOUR ACCOUNT") {
                        window.location.assign("./accounts.html")
                    }
                }
                )
        },
        ocultarSaldo(id) {
            let cambiar = document.getElementById(id)
            let asteriscos = "*******"
            if (this.balances.length) {
                let nuevo = { saldo: cambiar.innerHTML, id: id }
                let separador = this.balances.filter(bal => bal.id == nuevo.id)
                if (!separador.length) {
                    this.balances.push(nuevo)
                }
            } else {
                this.balances.push({ saldo: cambiar.innerHTML, id: id })
            }
            cambiar.innerHTML = asteriscos
        },
        modal(tra) {
            this.nuevo = tra
        },
        mostrarSaldo(id) {
            let cambiar = document.getElementById(id)
            if (cambiar.innerHTML == "*******") {
                let separador = this.balances.filter(bal => bal.id == id)
                cambiar.innerHTML = separador[0].saldo
            }
        },
        modificarFecha(fecha) {
            let fechaNueva = fecha.split("T")
            let fechaOtra = fechaNueva[0].split("-")
            let horaNueva = fechaNueva[1].split(".")
            let horaFinal = horaNueva[0]
            return `${fechaOtra[2]}-${fechaOtra[1]}-${fechaOtra[0]} / ${horaFinal}`
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
        sumasTotales(balances) {
            let montos = this.transacciones.map(tra => tra.amount)
            let sumaMontos = montos.reduce((a, b) => { return a + b })
            let final = balances + sumaMontos
            return final
        },
        generatePdf() {
            let accountNumber = this.accountNumber
            let dateFrom = this.dateFrom
            let dateTo = this.dateTo
            console.log(accountNumber)
            console.log(typeof dateFrom)
            console.log(typeof dateTo)
            /* return axios.post('/api/pdf/generate', `number=VIN001&dateFrom=2022-11-17&dateTo=2022-11-22`)
                .then(result =>
                    console.log(result)
                )
                .catch(error => console.log(error)
                ) */
            return axios.get('/api/pdf/generate', {
                params: {
                    number: accountNumber,
                    dateFrom: dateFrom,
                    dateTo: dateTo
                }
            }).then(result => window.location.assign(result.request.responseURL)
            )
            .catch(error => console.log(error)
            )
            },
        cerrarSesion() {
            axios.post('/api/logout').then(response => {
                localStorage.setItem("cookie", "false")
            })
            location.reload()
        },
    },
    computed: {
        filtroFecha() {

        }
    }
}).mount('#app')
