// ===============================
// PRODUCTOS
// ===============================

async function cargarProductos() {

    try {

        const respuesta = await fetch("/api/productos");

        const productos = await respuesta.json();

        const contenedor =
            document.getElementById("listaProductos");

        contenedor.innerHTML = "";

        if (productos.length === 0) {

            contenedor.innerHTML =
                "<p>No hay productos registrados.</p>";

            return;
        }

        productos.forEach(producto => {

            contenedor.innerHTML += `
                <div class="card">

                    <h3>${producto.nombre}</h3>

                    <p>
                        ${producto.descripcion || "Sin descripción"}
                    </p>

                    <p class="precio">
                        S/ ${producto.precio}
                    </p>

                    <p>
                        Categoría:
                        ${producto.categoria.nombre}
                    </p>

                    <p class="estado">
                        ${
                            producto.disponible
                                ? "Disponible"
                                : "No disponible"
                        }
                    </p>

                    <div class="acciones">

                        <button
                            class="btn-editar"
                            onclick="editarProducto(${producto.id})"
                        >
                            Editar
                        </button>

                        <button
                            class="btn-eliminar"
                            onclick="eliminarProducto(${producto.id})"
                        >
                            Eliminar
                        </button>

                    </div>

                </div>
            `;

        });

    } catch (error) {

        console.error(error);

        document.getElementById("listaProductos").innerHTML =
            "<p>Error al cargar los productos.</p>";
    }
}


// ===============================
// CARGAR CATEGORÍAS EN SELECT
// ===============================

async function cargarCategoriasProducto(
    categoriaSeleccionada = ""
) {

    try {

        const respuesta =
            await fetch("/api/categorias");

        const categorias =
            await respuesta.json();

        const select =
            document.getElementById("productoCategoria");

        select.innerHTML =
            '<option value="">Seleccione una categoría</option>';

        categorias.forEach(categoria => {

            const seleccionada =
                categoria.id == categoriaSeleccionada
                    ? "selected"
                    : "";

            select.innerHTML += `
                <option
                    value="${categoria.id}"
                    ${seleccionada}
                >
                    ${categoria.nombre}
                </option>
            `;

        });

    } catch (error) {

        console.error(error);

        alert("Error al cargar las categorías.");
    }
}


// ===============================
// MOSTRAR FORMULARIO
// ===============================

async function mostrarFormularioProducto() {

    document.getElementById("formularioProducto")
        .classList.remove("oculto");

    document.getElementById("tituloFormularioProducto")
        .textContent = "Nuevo producto";

    document.getElementById("productoId").value = "";

    document.getElementById("productoNombre").value = "";

    document.getElementById("productoDescripcion").value = "";

    document.getElementById("productoPrecio").value = "";

    document.getElementById("productoDisponible").checked = true;

    await cargarCategoriasProducto();
}


// ===============================
// CANCELAR FORMULARIO
// ===============================

function cancelarFormularioProducto() {

    document.getElementById("formularioProducto")
        .classList.add("oculto");

    document.getElementById("productoId").value = "";

    document.getElementById("productoNombre").value = "";

    document.getElementById("productoDescripcion").value = "";

    document.getElementById("productoPrecio").value = "";

    document.getElementById("productoCategoria").value = "";

    document.getElementById("productoDisponible").checked = true;
}


// ===============================
// EDITAR PRODUCTO
// ===============================

async function editarProducto(id) {

    try {

        const respuesta =
            await fetch(`/api/productos/${id}`);

        if (!respuesta.ok) {

            throw new Error(
                "No se pudo obtener el producto"
            );
        }

        const producto =
            await respuesta.json();

        document.getElementById("formularioProducto")
            .classList.remove("oculto");

        document.getElementById("tituloFormularioProducto")
            .textContent = "Editar producto";

        document.getElementById("productoId").value =
            producto.id;

        document.getElementById("productoNombre").value =
            producto.nombre;

        document.getElementById("productoDescripcion").value =
            producto.descripcion || "";

        document.getElementById("productoPrecio").value =
            producto.precio;

        document.getElementById("productoDisponible").checked =
            producto.disponible;

        await cargarCategoriasProducto(
            producto.categoria.id
        );

    } catch (error) {

        console.error(error);

        alert("Error al cargar el producto.");
    }
}


// ===============================
// GUARDAR PRODUCTO
// ===============================

async function guardarProducto() {

    const id =
        document.getElementById("productoId").value;

    const nombre =
        document.getElementById("productoNombre").value;

    const descripcion =
        document.getElementById("productoDescripcion").value;

    const precio =
        document.getElementById("productoPrecio").value;

    const categoriaId =
        document.getElementById("productoCategoria").value;

    const disponible =
        document.getElementById("productoDisponible").checked;


    if (!nombre.trim()) {

        alert("El nombre es obligatorio.");

        return;
    }


    if (!precio || Number(precio) < 0) {

        alert("Ingrese un precio válido.");

        return;
    }


    if (!categoriaId) {

        alert("Seleccione una categoría.");

        return;
    }


    const producto = {

        nombre: nombre,

        descripcion: descripcion,

        precio: Number(precio),

        disponible: disponible,

        categoria: {
            id: Number(categoriaId)
        }
    };


    try {

        let respuesta;


        if (!id) {

            respuesta =
                await fetch("/api/productos", {

                    method: "POST",

                    headers: {
                        "Content-Type":
                            "application/json"
                    },

                    body:
                        JSON.stringify(producto)
                });

        } else {

            respuesta =
                await fetch(
                    `/api/productos/${id}`,
                    {

                        method: "PUT",

                        headers: {
                            "Content-Type":
                                "application/json"
                        },

                        body:
                            JSON.stringify(producto)
                    }
                );
        }


        if (!respuesta.ok) {

            throw new Error(
                "Error al guardar el producto"
            );
        }


        cancelarFormularioProducto();

        cargarProductos();

        alert(
            "Producto guardado correctamente."
        );


    } catch (error) {

        console.error(error);

        alert(
            "Ocurrió un error al guardar el producto."
        );
    }
}


// ===============================
// ELIMINAR PRODUCTO
// ===============================

async function eliminarProducto(id) {

    const confirmar =
        confirm(
            "¿Seguro que deseas eliminar este producto?"
        );


    if (!confirmar) {

        return;
    }


    try {

        const respuesta =
            await fetch(
                `/api/productos/${id}`,
                {
                    method: "DELETE"
                }
            );


        if (!respuesta.ok) {

            throw new Error(
                "Error al eliminar producto"
            );
        }


        cargarProductos();

        alert(
            "Producto eliminado correctamente."
        );


    } catch (error) {

        console.error(error);

        alert(
            "No se pudo eliminar el producto. Es posible que esté asociado a un pedido."
        );
    }
}