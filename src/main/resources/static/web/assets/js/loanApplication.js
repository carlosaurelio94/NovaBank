const { createApp } = Vue

createApp({
    data() {
        return {
            clientLoans: [],
            accounts: [],
            loans: [],
            loanId: "",
            loanPayments: "",
            loanAmount: "",
            destinationAccount: "",
            arrayPayments: [],
            finalAmount: "",
            final: ""
        }
    },
    created() {
        this.loadData("/api/clients/current")
        this.loadData2("/api/loans")
    },
    mounted() {

    },
    methods: {
        loadData(url) {
            axios
                .get(url)
                .then(res => {
                    this.accounts = res.data.account
                    /* this.horaActual()
                    this.intervalo() */
                })
                .catch(error => console.error(error.message))
        },
        loadData2(url) {
            axios
                .get(url)
                .then(res => {
                    this.loans = res.data.sort((a, b) => { if (a.id < b.id) return -1 })
                    console.log(this.loans);
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
        createLoan() {
            let loanId = this.loanId
            let loanPayments = this.loanPayments
            let loanAmount = this.loanAmount
            let destinationAccount = this.destinationAccount
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
                    return axios.post('/api/loans', { loanId: loanId, amount: loanAmount, payments: loanPayments, 
                        destinationAccount: destinationAccount })
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
                            title: 'Error ' + error.response.status,
                            text: error.response.data,
                            confirmButtonColor: '#ff4545',
                        /*                 footer: '<a href="">Why do I have this issue?</a>'
                    */              })
                        )
                }
            })
        },
        installments(loanAmount, loanRate, loanPayments) {
            finalAmount = loanAmount*loanRate
            console.log(this.loans);
            return finalInstallment = finalAmount/loanPayments
        },
        interest(loanRate) {
            if(loanRate == 1.4) {
                return "40%"
            }
            if(loanRate == 1.3) {
                return "30%"
            }
            if(loanRate == 1.2) {
                return "20%"
            }
        }
    },
    computed: {
        payments() {
            let loanId = this.loanId
            let loans = this.loans.filter(loan => loan.id == loanId)
            let payments = []
            payments = loans.map(loan => loan.payments)
            this.arrayPayments = payments[0]
        },
    }
}).mount('#app')
