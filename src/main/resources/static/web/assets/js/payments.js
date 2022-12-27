const { createApp } = Vue

createApp({
    data() {
        return {
            cardNumber: "",
            cardCvv: "",
            paymentAmount: "",
            paymentDescription: "",
        }
    },
    created() {
    },
    mounted() {
    },
    methods: {
        cerrarSesion() {
            axios.post('/api/logout').then(response => {
                localStorage.setItem("cookie", "false")
            })
            location.reload()
        },
        novaPayments() {
            let number = this.cardNumber
            let cvv = this.cardCvv
            let amount = this.paymentAmount
            let description = this.paymentDescription
            let alert = Swal.fire({
                title: 'Are you sure?',
                text: "You won't be able to undo the transaction once you agree to it.",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#24cb24',
                cancelButtonColor: '#ff4545',
                confirmButtonText: 'Yes, I am sure'
            }).then(res => {
                console.log(res);
                if (res) {
                    return axios.post('https://nova-bank-production-45f5.up.railway.app/api/payments',
                     { number: number, cvv: cvv, amount: amount, description: description })
                    /* .then(result => {
                        if(result.status == 201) {
                            return axios.post('')
                        }
                    }) */
                        .then(result =>
                            Swal.fire({
                                title: 'Successful payment',
                                text: "Your payment was successfull",
                                icon: 'success',
                                confirmButtonColor: '#24cb24',
                            })
                        ) .then(result =>
                            window.location.assign("./index.html")
                        )
                         .catch(error => Swal.fire({
                            icon: 'error',
                            title: 'Error ' + error.response.status,
                            text: error.response.data,
                            confirmButtonColor: '#ff4545',
                        })
                        )

                }
            })
        },

    },
    computed: {
    }
}).mount('#app')
