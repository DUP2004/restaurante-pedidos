// ===============================
// CLIENTES
// ===============================

async function cargarClientes() {

    try {

        const respuesta =
            await fetch("/api/clientes");

        const clientes =
            await respuesta.json();

        const contenedor =
            document.getElementById("listaClientes");

        contenedor.innerHTML = "";


        if (clientes.length === 0) {

            contenedor.innerHTML =
                "<p>No hay clientes registrados.</p>";

            return;
        }


        clientes.forEach(cliente => {

            contenedor.innerHTML += `

                <div class="card">

                    <h3>
                        ${cliente.nombre}
                        ${cliente.apellido}
                    </h3>


                    <p>
                        Correo:
                        ${cliente.correo}
                    </p>


                    <p>
                        Teléfono:
                        ${cliente.telefono}
                    </p>


                    <div class="acciones">

                        <button
                            class="btn-editar"
                            onclick="editarCliente(
                                ${cliente.id},
                                '${cliente.nombre.replace(/'/g, "\\'")}',
                                '${cliente.apellido.replace(/'/g, "\\'")}',
                                '${cliente.correo.replace(/'/g, "\\'")}',
                                '${cliente.telefono.replace(/'/g, "\\'")}'
                            )"
                        >
                            Editar
                        </button>


                        <button
                            class="btn-eliminar"
                            onclick="eliminarCliente(
                                ${cliente.id}
                            )"
                        >
                            Eliminar
                        </button>

                    </div>

                </div>

            `;

        });


    } catch (error) {

        console.error(error);

        document.getElementById(
            "listaClientes"
        ).innerHTML =
            "<p>Error al cargar los clientes.</p>";
    }
}


// ===============================
// MOSTRAR FORMULARIO
// ===============================

function mostrarFormularioCliente() {

    document.getElementById(
        "formularioCliente"
    )
        .classList
        .remove("oculto");


    document.getElementById(
        "tituloFormularioCliente"
    )
        .textContent =
            "Nuevo cliente";


    document.getElementById(
        "clienteId"
    ).value = "";


    document.getElementById(
        "clienteNombre"
    ).value = "";


    document.getElementById(
        "clienteApellido"
    ).value = "";


    document.getElementById(
        "clienteCorreo"
    ).value = "";


    document.getElementById(
        "clienteTelefono"
    ).value = "";
}


// ===============================
// CANCELAR FORMULARIO
// ===============================

function cancelarFormularioCliente() {

    document.getElementById(
        "formularioCliente"
    )
        .classList
        .add("oculto");


    document.getElementById(
        "clienteId"
    ).value = "";


    document.getElementById(
        "clienteNombre"
    ).value = "";


    document.getElementById(
        "clienteApellido"
    ).value = "";


    document.getElementById(
        "clienteCorreo"
    ).value = "";


    document.getElementById(
        "clienteTelefono"
    ).value = "";
}


// ===============================
// EDITAR CLIENTE
// ===============================

function editarCliente(
    id,
    nombre,
    apellido,
    correo,
    telefono
) {

    document.getElementById(
        "formularioCliente"
    )
        .classList
        .remove("oculto");


    document.getElementById(
        "tituloFormularioCliente"
    )
        .textContent =
            "Editar cliente";


    document.getElementById(
        "clienteId"
    ).value = id;


    document.getElementById(
        "clienteNombre"
    ).value = nombre;


    document.getElementById(
        "clienteApellido"
    ).value = apellido;


    document.getElementById(
        "clienteCorreo"
    ).value = correo;


    document.getElementById(
        "clienteTelefono"
    ).value = telefono;
}


// ===============================
// GUARDAR CLIENTE
// ===============================

async function guardarCliente() {

    const id =
        document.getElementById(
            "clienteId"
        ).value;


    const nombre =
        document.getElementById(
            "clienteNombre"
        ).value;


    const apellido =
        document.getElementById(
            "clienteApellido"
        ).value;


    const correo =
        document.getElementById(
            "clienteCorreo"
        ).value;


    const telefono =
        document.getElementById(
            "clienteTelefono"
        ).value;


    if (
        !nombre.trim() ||
        !apellido.trim()
    ) {

        alert(
            "El nombre y apellido son obligatorios."
        );

        return;
    }


    const cliente = {

        nombre: nombre,

        apellido: apellido,

        correo: correo,

        telefono: telefono

    };


    try {

        let respuesta;


        if (!id) {

            respuesta =
                await fetch(
                    "/api/clientes",
                    {

                        method: "POST",

                        headers: {
                            "Content-Type":
                                "application/json"
                        },

                        body:
                            JSON.stringify(
                                cliente
                            )
                    }
                );

        } else {

            respuesta =
                await fetch(
                    `/api/clientes/${id}`,
                    {

                        method: "PUT",

                        headers: {
                            "Content-Type":
                                "application/json"
                        },

                        body:
                            JSON.stringify(
                                cliente
                            )
                    }
                );
        }


        if (!respuesta.ok) {

            throw new Error(
                "Error al guardar el cliente"
            );
        }


        cancelarFormularioCliente();

        cargarClientes();


        alert(
            "Cliente guardado correctamente."
        );


    } catch (error) {

        console.error(error);

        alert(
            "Ocurrió un error al guardar el cliente."
        );
    }
}


// ===============================
// ELIMINAR CLIENTE
// ===============================

async function eliminarCliente(id) {

    const confirmar =
        confirm(
            "¿Seguro que deseas eliminar este cliente?"
        );


    if (!confirmar) {

        return;
    }


    try {

        const respuesta =
            await fetch(
                `/api/clientes/${id}`,
                {
                    method: "DELETE"
                }
            );


        if (!respuesta.ok) {

            throw new Error(
                "Error al eliminar cliente"
            );
        }


        cargarClientes();

        alert(
            "Cliente eliminado correctamente."
        );


    } catch (error) {

        console.error(error);

        alert(
            "No se pudo eliminar el cliente. Es posible que tenga pedidos asociados."
        );
    }
}