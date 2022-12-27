const { createApp } = Vue

createApp({
    data() {
        return {
            tarjetas: [],
            clientes: [],
            cuentas: [],
            balances: [],
            urlApi: "/api/clients/current",
            /*eliminar las comillas dobles para usar el xml*/
            urlApiXML: "'/clients/current',{headers:{'accept':'application/xml'}}",
            hora: "",
            prestamos: [],
            transacciones: [],
            password: "",
            firstName: "",
            prueba: "",
            cookie: "",
            lastName: "",
            tarjetasCredito: [],
            tarjetasDebito: [],
        }
    },
    created() {
        /* if (window.location.pathname != "/web/index.html" && window.location.pathname != "/web/accountsAll.html" && window.location.pathname != "/web/cardsAll.html") {
            return this.loadData(this.urlApi)
        }  */
        this.cookie = JSON.parse(localStorage.getItem("cookie"))
        console.log(this.cookie);
        if (this.cookie) {
            return this.loadData(this.urlApi)
        }
        /*         this.loadData(this.urlApi)*/

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
                    this.tarjetas = this.clientes.card
                    this.tarjetas = this.tarjetas.filter(tarjeta => tarjeta.validate == "OK")
                    this.tarjetasCredito = this.tarjetas.filter(tarjeta => tarjeta.type == 'CREDIT')
                    this.tarjetasDebito = this.tarjetas.filter(tarjeta => tarjeta.type == 'DEBIT')
                    this.firstName = res.data.firstName
                    this.lastName = res.data.lastName
                    this.cuentas = this.clientes.account.sort((a, b) => { if (a.id < b.id) return -1 })
                    this.prestamos = this.prestamos.sort((a, b) => { if (a.id < b.id) return -1 })
                    this.transacciones = this.transacciones.sort((a, b) => { if (a.date > b.date) return -1 })
                    console.log(this.cuentas.transaction);
                    this.horaActual()
                    this.intervalo()

                })
            //.catch(error => console.error(error.message))
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
            return `${fechaOtra[2]}-${fechaOtra[1]}-${fechaOtra[0]}`
        },

        saldo(id) {
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

        enviarFoto() {
            axios.post()
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
        sumasTotales() {
            let montos = this.cuentas.map(tra => tra.balance)
            let sumaMontos = montos.reduce((a, b) => { return a + b })

            return sumaMontos
        },
        cerrarSesion() {
            axios.post('/api/logout').then(response => {
                localStorage.setItem("cookie", "false")
            })
            location.reload()
        },
        crearCuentas() {
            return axios.post('/api/clients/current/accounts').then(res => window.location.reload()).catch(error => console.error(error))
        },
        cambiarPassword() {

        },
        localDate() {
            let date = new Date()
            let currentDay = date.getDate()
            let currentMonth = date.getMonth()
            let currentYear = date.getFullYear()
            return `${currentYear}-${currentMonth + 1}-${currentDay}`
        },
        mayusculas() {
            let firstName = this.firstName.slice(0, 1)
            let FirstCase = firstName.toUpperCase()
            let firstName2 = this.firstName.slice(1)
            let lastName = this.lastName.slice(0, 1)
            let lastCase = lastName.toUpperCase()
            let lastName2 = this.lastName.slice(1)
            return FirstCase + firstName2 + " " + lastCase + lastName2
        }
    },
    computed: {

    }
}).mount('#app')
