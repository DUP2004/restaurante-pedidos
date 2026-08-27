// ===============================
// PEDIDOS
// ===============================

async function cargarPedidos() {

    try {

        const respuesta =
            await fetch("/api/pedidos");

        const pedidos =
            await respuesta.json();

        const contenedor =
            document.getElementById(
                "listaPedidos"
            );

        contenedor.innerHTML = "";


        if (pedidos.length === 0) {

            contenedor.innerHTML =
                "<p>No hay pedidos registrados.</p>";

            return;
        }


        pedidos.forEach(pedido => {

            let detalles = "";


            pedido.detalles.forEach(
                detalle => {

                    detalles += `

                        <p>

                            ${detalle.cantidad} x

                            ${detalle.producto.nombre}

                            - S/ ${detalle.precioUnitario}

                        </p>

                    `;

                }
            );


            contenedor.innerHTML += `

                <div class="card">

                    <h3>
                        Pedido #${pedido.id}
                    </h3>


                    <p>

                        Cliente:

                        ${pedido.cliente.nombre}

                        ${pedido.cliente.apellido}

                    </p>


                    <p>

                        Fecha:
                        ${pedido.fecha}

                    </p>


                    <p>

                        Estado actual:

                        <strong>
                            ${pedido.estado}
                        </strong>

                    </p>


                    <label>
                        Cambiar estado:
                    </label>


                    <select
                        onchange="
                            cambiarEstadoPedido(
                                ${pedido.id},
                                this.value
                            )
                        "
                    >

                        <option value="">
                            Seleccionar estado
                        </option>


                        <option
                            value="PENDIENTE"
                            ${
                                pedido.estado === "PENDIENTE"
                                    ? "selected"
                                    : ""
                            }
                        >
                            PENDIENTE
                        </option>


                        <option
                            value="PREPARANDO"
                            ${
                                pedido.estado === "PREPARANDO"
                                    ? "selected"
                                    : ""
                            }
                        >
                            PREPARANDO
                        </option>


                        <option
                            value="LISTO"
                            ${
                                pedido.estado === "LISTO"
                                    ? "selected"
                                    : ""
                            }
                        >
                            LISTO
                        </option>


                        <option
                            value="ENTREGADO"
                            ${
                                pedido.estado === "ENTREGADO"
                                    ? "selected"
                                    : ""
                            }
                        >
                            ENTREGADO
                        </option>


                        <option
                            value="CANCELADO"
                            ${
                                pedido.estado === "CANCELADO"
                                    ? "selected"
                                    : ""
                            }
                        >
                            CANCELADO
                        </option>

                    </select>


                    <hr>


                    <h4>
                        Productos
                    </h4>


                    ${detalles}


                    <p class="precio">

                        Total:
                        S/ ${pedido.total}

                    </p>


                    <div class="acciones">

                        <button
                            class="btn-eliminar"
                            onclick="
                                eliminarPedido(
                                    ${pedido.id}
                                )
                            "
                        >
                            🗑️ Eliminar pedido
                        </button>

                    </div>

                </div>

            `;

        });


    } catch (error) {

        console.error(error);

        document.getElementById(
            "listaPedidos"
        ).innerHTML =
            "<p>Error al cargar los pedidos.</p>";
    }
}


// ===============================
// CAMBIAR ESTADO
// ===============================

async function cambiarEstadoPedido(
    id,
    estado
) {

    if (!estado) {

        return;
    }


    try {

        const respuesta =
            await fetch(
                `/api/pedidos/${id}/estado?estado=${estado}`,
                {
                    method: "PUT"
                }
            );


        if (!respuesta.ok) {

            throw new Error(
                "Error al cambiar el estado"
            );
        }


        alert(
            "Estado actualizado correctamente"
        );


        cargarPedidos();


    } catch (error) {

        console.error(error);

        alert(
            "No se pudo actualizar el estado"
        );
    }
}


// ===============================
// ELIMINAR PEDIDO
// ===============================

async function eliminarPedido(id) {

    const confirmar =
        confirm(
            `¿Seguro que deseas eliminar el pedido #${id}?`
        );


    if (!confirmar) {

        return;
    }


    try {

        const respuesta =
            await fetch(
                `/api/pedidos/${id}`,
                {
                    method: "DELETE"
                }
            );


        if (!respuesta.ok) {

            throw new Error(
                "Error al eliminar el pedido"
            );
        }


        alert(
            `Pedido #${id} eliminado correctamente`
        );


        cargarPedidos();


    } catch (error) {

        console.error(error);

        alert(
            "No se pudo eliminar el pedido."
        );
    }
}


// ===============================
// FORMULARIO PEDIDOS
// ===============================

let clientesPedido = [];

let productosPedido = [];


// ===============================
// MOSTRAR FORMULARIO
// ===============================

async function mostrarFormularioPedido() {

    const formulario =
        document.getElementById(
            "formularioPedido"
        );


    formulario
        .classList
        .remove("oculto");


    await cargarClientesParaPedido();

    await cargarProductosParaPedido();


    document.getElementById(
        "detallesPedido"
    ).innerHTML = "";


    agregarProductoPedido();
}


// ===============================
// CARGAR CLIENTES
// ===============================

async function cargarClientesParaPedido() {

    try {

        const respuesta =
            await fetch(
                "/api/clientes"
            );


        clientesPedido =
            await respuesta.json();


        const select =
            document.getElementById(
                "pedidoCliente"
            );


        select.innerHTML = `

            <option value="">
                Seleccione un cliente
            </option>

        `;


        clientesPedido.forEach(
            cliente => {

                select.innerHTML += `

                    <option
                        value="${cliente.id}"
                    >

                        ${cliente.nombre}
                        ${cliente.apellido}

                    </option>

                `;

            }
        );


    } catch (error) {

        console.error(error);

        alert(
            "Error al cargar los clientes"
        );
    }
}


// ===============================
// CARGAR PRODUCTOS
// ===============================

async function cargarProductosParaPedido() {

    try {

        const respuesta =
            await fetch(
                "/api/productos"
            );


        productosPedido =
            await respuesta.json();


    } catch (error) {

        console.error(error);

        alert(
            "Error al cargar los productos"
        );
    }
}


// ===============================
// AGREGAR PRODUCTO
// ===============================

function agregarProductoPedido() {

    const contenedor =
        document.getElementById(
            "detallesPedido"
        );


    let opcionesProductos = `

        <option value="">
            Seleccione un producto
        </option>

    `;


    productosPedido.forEach(
        producto => {

            if (producto.disponible) {

                opcionesProductos += `

                    <option
                        value="${producto.id}"
                    >

                        ${producto.nombre}
                        -
                        S/ ${producto.precio}

                    </option>

                `;

            }

        }
    );


    const detalle =
        document.createElement(
            "div"
        );


    detalle.classList.add(
        "detalle-pedido"
    );


    detalle.innerHTML = `

        <select
            class="productoPedido"
        >

            ${opcionesProductos}

        </select>


        <input
            type="number"
            class="cantidadPedido"
            min="1"
            value="1"
        >


        <button
            type="button"
            class="btn-eliminar"
            onclick="
                this.parentElement.remove()
            "
        >
            X
        </button>

    `;


    contenedor.appendChild(
        detalle
    );
}


// ===============================
// GUARDAR PEDIDO
// ===============================

async function guardarPedido() {

    const clienteId =
        document.getElementById(
            "pedidoCliente"
        ).value;


    const productos =
        document.querySelectorAll(
            ".productoPedido"
        );


    const cantidades =
        document.querySelectorAll(
            ".cantidadPedido"
        );


    if (!clienteId) {

        alert(
            "Seleccione un cliente"
        );

        return;
    }


    const detalles = [];


    for (
        let i = 0;
        i < productos.length;
        i++
    ) {

        const productoId =
            productos[i].value;


        const cantidad =
            cantidades[i].value;


        if (!productoId) {

            alert(
                "Seleccione un producto"
            );

            return;
        }


        if (
            !cantidad ||
            cantidad <= 0
        ) {

            alert(
                "La cantidad debe ser mayor a 0"
            );

            return;
        }


        detalles.push({

            productoId:
                Number(productoId),

            cantidad:
                Number(cantidad)

        });
    }


    if (detalles.length === 0) {

        alert(
            "Agregue al menos un producto"
        );

        return;
    }


    const pedido = {

        clienteId:
            Number(clienteId),

        detalles:
            detalles

    };


    try {

        const respuesta =
            await fetch(
                "/api/pedidos",
                {

                    method: "POST",

                    headers: {
                        "Content-Type":
                            "application/json"
                    },

                    body:
                        JSON.stringify(
                            pedido
                        )
                }
            );


        if (!respuesta.ok) {

            throw new Error(
                "Error al crear el pedido"
            );
        }


        alert(
            "Pedido creado correctamente"
        );


        cancelarPedido();

        cargarPedidos();


    } catch (error) {

        console.error(error);

        alert(
            "No se pudo crear el pedido"
        );
    }
}


// ===============================
// CANCELAR PEDIDO
// ===============================

function cancelarPedido() {

    document
        .getElementById(
            "formularioPedido"
        )
        .classList
        .add("oculto");


    document.getElementById(
        "pedidoCliente"
    ).value = "";


    document.getElementById(
        "detallesPedido"
    ).innerHTML = "";
}