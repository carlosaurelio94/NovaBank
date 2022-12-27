const { createApp } = Vue

createApp({
    data() {
        return {
            message: "Hola",
            urlApi: 'http://localhost:8080/rest/clients',
            info: [],
            pagina: [],
            usuarios: [],
            firstName: "",
            lastName: "",
            email: "",
            editarLink: "",
            editarNombre:"",
            editarApellido: "",
            editarMail: "",
            urlPrev: "",
            urlNext: ""
        }
    },
    created() {
        this.loadData(this.urlApi);
    },
    mounted() {
    },
    methods: {
        loadData(url) {
            axios
                .get(url)
                .then(res => {
                    this.pagina = res
                    this.info = res.data._embedded.clients
                    this.urlNext = ""
                    this.urlNext = res.data._links.next.href
                    this.urlPrev = ""
                    this.urlPrev = res.data._links.prev.href
                })
                //.catch(err => console.error(err.message))
        },
        postClient(url, clientesNuevos) {
            axios
                .post(url, clientesNuevos)
                .then(()=>this.loadData())
                .then(()=>location.reload())
                .catch(error => console.error(error.message))
        },
        addClient() {
            {
                let cliente = {
                    firstName: this.firstName,
                    lastName: this.lastName,
                    email: this.email
                };
                this.postClient(this.urlApi, cliente)
                this.firstName = "";
                this.lastName = "";
                this.email = "";
            }
        },
        deleteClient(identificador) {
            axios
            .delete(identificador)
            .then(()=>location.reload())
        },
        putClient(identificador, clientesNuevos){
            axios
                .put(identificador, clientesNuevos)
                .then(()=>this.loadData())
                .then(()=>location.reload())
                .catch(error => console.error(error.message))
        },
        editar(identificador) {
            {
                let cliente = {
                    firstName: this.editarNombre,
                    lastName: this.editarApellido,
                    email: this.editarMail
                };
                this.putClient(identificador, cliente)
            }
        }
    },
}).mount('#app')