// ===============================
// DASHBOARD
// ===============================

async function cargarDashboard() {

    try {

        const respuestas = await Promise.all([

            fetch("/api/productos"),

            fetch("/api/categorias"),

            fetch("/api/clientes"),

            fetch("/api/pedidos")

        ]);


        const productos = await respuestas[0].json();

        const categorias = await respuestas[1].json();

        const clientes = await respuestas[2].json();

        const pedidos = await respuestas[3].json();


        document.getElementById("totalProductos").textContent =
            productos.length;

        document.getElementById("totalCategorias").textContent =
            categorias.length;

        document.getElementById("totalClientes").textContent =
            clientes.length;

        document.getElementById("totalPedidos").textContent =
            pedidos.length;


    } catch (error) {

        console.error(
            "Error al cargar el dashboard:",
            error
        );

    }

}