// ===============================
// CATEGORÍAS
// ===============================

async function cargarCategorias() {

    try {

        const respuesta =
            await fetch("/api/categorias");

        const categorias =
            await respuesta.json();

        const contenedor =
            document.getElementById("listaCategorias");

        contenedor.innerHTML = "";


        if (categorias.length === 0) {

            contenedor.innerHTML =
                "<p>No hay categorías registradas.</p>";

            return;
        }


        categorias.forEach(categoria => {

            contenedor.innerHTML += `

                <div class="card">

                    <h3>
                        ${categoria.nombre}
                    </h3>

                    <p>
                        ${
                            categoria.descripcion ||
                            "Sin descripción"
                        }
                    </p>


                    <div class="acciones">

                        <button
                            class="btn-editar"
                            onclick="editarCategoria(
                                ${categoria.id},
                                '${categoria.nombre.replace(/'/g, "\\'")}',
                                '${(categoria.descripcion || "").replace(/'/g, "\\'")}'
                            )"
                        >
                            Editar
                        </button>


                        <button
                            class="btn-eliminar"
                            onclick="eliminarCategoria(
                                ${categoria.id}
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
            "listaCategorias"
        ).innerHTML =
            "<p>Error al cargar las categorías.</p>";
    }
}


// ===============================
// MOSTRAR FORMULARIO
// ===============================

function mostrarFormularioCategoria() {

    document.getElementById(
        "formularioCategoria"
    )
        .classList
        .remove("oculto");


    document.getElementById(
        "tituloFormularioCategoria"
    )
        .textContent =
            "Nueva categoría";


    document.getElementById(
        "categoriaId"
    ).value = "";


    document.getElementById(
        "categoriaNombre"
    ).value = "";


    document.getElementById(
        "categoriaDescripcion"
    ).value = "";
}


// ===============================
// CANCELAR FORMULARIO
// ===============================

function cancelarFormularioCategoria() {

    document.getElementById(
        "formularioCategoria"
    )
        .classList
        .add("oculto");


    document.getElementById(
        "categoriaId"
    ).value = "";


    document.getElementById(
        "categoriaNombre"
    ).value = "";


    document.getElementById(
        "categoriaDescripcion"
    ).value = "";
}


// ===============================
// EDITAR CATEGORÍA
// ===============================

function editarCategoria(
    id,
    nombre,
    descripcion
) {

    document.getElementById(
        "formularioCategoria"
    )
        .classList
        .remove("oculto");


    document.getElementById(
        "tituloFormularioCategoria"
    )
        .textContent =
            "Editar categoría";


    document.getElementById(
        "categoriaId"
    ).value = id;


    document.getElementById(
        "categoriaNombre"
    ).value = nombre;


    document.getElementById(
        "categoriaDescripcion"
    ).value =
        descripcion;
}


// ===============================
// GUARDAR CATEGORÍA
// ===============================

async function guardarCategoria() {

    const id =
        document.getElementById(
            "categoriaId"
        ).value;


    const nombre =
        document.getElementById(
            "categoriaNombre"
        ).value;


    const descripcion =
        document.getElementById(
            "categoriaDescripcion"
        ).value;


    if (!nombre.trim()) {

        alert(
            "El nombre de la categoría es obligatorio."
        );

        return;
    }


    const categoria = {

        nombre: nombre,

        descripcion: descripcion

    };


    try {

        let respuesta;


        if (!id) {

            respuesta =
                await fetch(
                    "/api/categorias",
                    {

                        method: "POST",

                        headers: {
                            "Content-Type":
                                "application/json"
                        },

                        body:
                            JSON.stringify(
                                categoria
                            )
                    }
                );

        } else {

            respuesta =
                await fetch(
                    `/api/categorias/${id}`,
                    {

                        method: "PUT",

                        headers: {
                            "Content-Type":
                                "application/json"
                        },

                        body:
                            JSON.stringify(
                                categoria
                            )
                    }
                );
        }


        if (!respuesta.ok) {

            throw new Error(
                "Error al guardar la categoría"
            );
        }


        cancelarFormularioCategoria();

        cargarCategorias();


        alert(
            "Categoría guardada correctamente."
        );


    } catch (error) {

        console.error(error);

        alert(
            "Ocurrió un error al guardar la categoría."
        );
    }
}


// ===============================
// ELIMINAR CATEGORÍA
// ===============================

async function eliminarCategoria(id) {

    const confirmar =
        confirm(
            "¿Seguro que deseas eliminar esta categoría?"
        );


    if (!confirmar) {

        return;
    }


    try {

        const respuesta =
            await fetch(
                `/api/categorias/${id}`,
                {
                    method: "DELETE"
                }
            );


        if (!respuesta.ok) {

            throw new Error(
                "Error al eliminar"
            );
        }


        cargarCategorias();

        alert(
            "Categoría eliminada correctamente."
        );


    } catch (error) {

        console.error(error);

        alert(
            "No se pudo eliminar la categoría. Es posible que tenga productos asociados."
        );
    }
}