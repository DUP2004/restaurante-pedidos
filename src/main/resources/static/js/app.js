// ===============================
// CARGAR PÁGINAS
// ===============================

async function cargarPagina(nombre) {

    const contenido = document.getElementById("contenido");

    document.querySelectorAll("nav button").forEach(boton => {
        boton.classList.toggle("activo", boton.dataset.pagina === nombre);
    });

    contenido.innerHTML = "Cargando...";

    try {

    const respuesta = await fetch(`/interfaces/${nombre}.html`);


        if (!respuesta.ok) {
            throw new Error("No se pudo cargar la página");
        }

        const html = await respuesta.text();

        contenido.innerHTML = html;


        // ===============================
        // CARGAR INFORMACIÓN DE LA SECCIÓN
        // ===============================

        if (nombre === "inicio") {
            cargarDashboard();
        }

        if (nombre === "productos") {
            cargarProductos();
        }

        if (nombre === "categorias") {
            cargarCategorias();
        }

        if (nombre === "clientes") {
            cargarClientes();
        }

        if (nombre === "pedidos") {
            cargarPedidos();
        }

    } catch (error) {

        console.error(error);

        contenido.innerHTML = `
            <p>
                Error al cargar la sección.
            </p>
        `;
    }
}


// ===============================
// INICIAR APLICACIÓN
// ===============================

document.addEventListener("DOMContentLoaded", () => {

    cargarPagina("inicio");

});