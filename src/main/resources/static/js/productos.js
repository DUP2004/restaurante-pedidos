// Gestión administrativa de productos e imágenes.
const MAX_IMAGEN_PRODUCTO = 3 * 1024 * 1024;

async function cargarProductos() {
    try {
        const respuesta = await fetch("/api/productos");
        if (!respuesta.ok) throw new Error("No se pudieron cargar los productos");
        const productos = await respuesta.json();
        const contenedor = document.getElementById("listaProductos");
        contenedor.innerHTML = "";
        if (!productos.length) { contenedor.textContent = "No hay productos registrados."; return; }
        productos.forEach(producto => {
            const card = document.createElement("article");
            card.className = "card";
            const imagen = document.createElement("img");
            imagen.className = "producto-admin-imagen";
            imagen.alt = "Imagen de " + producto.nombre;
            imagen.src = producto.imagenUrl || "";
            imagen.hidden = !producto.imagenUrl;
            const titulo = document.createElement("h3"); titulo.textContent = producto.nombre;
            const descripcion = document.createElement("p"); descripcion.textContent = producto.descripcion || "Sin descripción";
            const precio = document.createElement("p"); precio.className = "precio"; precio.textContent = "S/ " + producto.precio;
            const categoria = document.createElement("p"); categoria.textContent = "Categoría: " + (producto.categoria?.nombre || "");
            const estado = document.createElement("p"); estado.className = "estado"; estado.textContent = producto.disponible ? "Disponible" : "No disponible";
            const acciones = document.createElement("div"); acciones.className = "acciones";
            const editar = document.createElement("button"); editar.className = "btn-editar"; editar.textContent = "Editar"; editar.onclick = () => editarProducto(producto.id);
            const eliminar = document.createElement("button"); eliminar.className = "btn-eliminar"; eliminar.textContent = "Eliminar"; eliminar.onclick = () => eliminarProducto(producto.id);
            acciones.append(editar, eliminar);
            card.append(imagen, titulo, descripcion, precio, categoria, estado, acciones);
            contenedor.append(card);
        });
    } catch (error) {
        console.error(error);
        document.getElementById("listaProductos").textContent = "Error al cargar los productos.";
    }
}

async function cargarCategoriasProducto(categoriaSeleccionada = "") {
    try {
        const respuesta = await fetch("/api/categorias");
        if (!respuesta.ok) throw new Error();
        const categorias = await respuesta.json();
        const select = document.getElementById("productoCategoria");
        select.replaceChildren(new Option("Seleccione una categoría", ""));
        categorias.forEach(categoria => select.add(new Option(categoria.nombre, categoria.id, false, categoria.id == categoriaSeleccionada)));
    } catch (error) { console.error(error); alert("Error al cargar las categorías."); }
}

function limpiarPreviewProducto() {
    const preview = document.getElementById("productoImagenPreview");
    if (preview.dataset.objectUrl) URL.revokeObjectURL(preview.dataset.objectUrl);
    preview.removeAttribute("src"); delete preview.dataset.objectUrl; preview.hidden = true;
    document.getElementById("productoImagen").value = "";
    document.getElementById("productoImagenEstado").textContent = "La imagen se puede cargar después de guardar el producto.";
}

function configurarPreviewProducto() {
    const campo = document.getElementById("productoImagen");
    campo.onchange = () => {
        const archivo = campo.files[0];
        limpiarPreviewProducto();
        if (!archivo) return;
        if (!["image/jpeg", "image/png", "image/webp"].includes(archivo.type) || archivo.size > MAX_IMAGEN_PRODUCTO) {
            campo.value = "";
            alert("Selecciona una imagen JPEG, PNG o WebP de máximo 3 MB.");
            return;
        }
        const url = URL.createObjectURL(archivo);
        const preview = document.getElementById("productoImagenPreview");
        preview.src = url; preview.dataset.objectUrl = url; preview.hidden = false;
        document.getElementById("productoImagenEstado").textContent = archivo.name;
    };
}

async function mostrarFormularioProducto() {
    document.getElementById("formularioProducto").classList.remove("oculto");
    document.getElementById("tituloFormularioProducto").textContent = "Nuevo producto";
    ["productoId", "productoNombre", "productoDescripcion", "productoPrecio"].forEach(id => document.getElementById(id).value = "");
    document.getElementById("productoDisponible").checked = true;
    limpiarPreviewProducto(); configurarPreviewProducto();
    await cargarCategoriasProducto();
}
function cancelarFormularioProducto() {
    document.getElementById("formularioProducto").classList.add("oculto");
    ["productoId", "productoNombre", "productoDescripcion", "productoPrecio", "productoCategoria"].forEach(id => document.getElementById(id).value = "");
    document.getElementById("productoDisponible").checked = true;
    limpiarPreviewProducto();
}
async function editarProducto(id) {
    try {
        const respuesta = await fetch(`/api/productos/${id}`);
        if (!respuesta.ok) throw new Error();
        const producto = await respuesta.json();
        document.getElementById("formularioProducto").classList.remove("oculto");
        document.getElementById("tituloFormularioProducto").textContent = "Editar producto";
        document.getElementById("productoId").value = producto.id;
        document.getElementById("productoNombre").value = producto.nombre;
        document.getElementById("productoDescripcion").value = producto.descripcion || "";
        document.getElementById("productoPrecio").value = producto.precio;
        document.getElementById("productoDisponible").checked = producto.disponible;
        limpiarPreviewProducto(); configurarPreviewProducto();
        await cargarCategoriasProducto(producto.categoria.id);
        if (producto.imagenUrl) {
            const preview = document.getElementById("productoImagenPreview");
            preview.src = producto.imagenUrl; preview.hidden = false;
            document.getElementById("productoImagenEstado").textContent = "Imagen actual. Selecciona otra para reemplazarla.";
        }
    } catch (error) { console.error(error); alert("Error al cargar el producto."); }
}
async function guardarProducto() {
    const id = document.getElementById("productoId").value;
    const nombre = document.getElementById("productoNombre").value.trim();
    const precio = document.getElementById("productoPrecio").value;
    const categoriaId = document.getElementById("productoCategoria").value;
    const archivo = document.getElementById("productoImagen").files[0];
    if (!nombre) return alert("El nombre es obligatorio.");
    if (!precio || !Number.isFinite(Number(precio)) || Number(precio) < 0) return alert("Ingrese un precio válido.");
    if (!categoriaId) return alert("Seleccione una categoría.");
    if (archivo && (!["image/jpeg", "image/png", "image/webp"].includes(archivo.type) || archivo.size > MAX_IMAGEN_PRODUCTO)) return alert("La imagen debe ser JPEG, PNG o WebP y no superar 3 MB.");
    const producto = { nombre, descripcion: document.getElementById("productoDescripcion").value.trim(), precio: Number(precio), disponible: document.getElementById("productoDisponible").checked, categoria: { id: Number(categoriaId) } };
    try {
        const respuesta = await fetch(id ? `/api/productos/${id}` : "/api/productos", { method: id ? "PUT" : "POST", headers: { "Content-Type": "application/json" }, body: JSON.stringify(producto) });
        if (!respuesta.ok) throw new Error("Error al guardar el producto");
        const guardado = await respuesta.json();
        if (archivo) {
            const datos = new FormData(); datos.append("archivo", archivo);
            const subida = await fetch(`/api/productos/${guardado.id}/imagen`, { method: "POST", body: datos });
            if (!subida.ok) throw new Error("El producto se guardó, pero no se pudo subir la imagen. Inténtalo desde Editar.");
        }
        cancelarFormularioProducto(); await cargarProductos(); alert("Producto guardado correctamente.");
    } catch (error) { console.error(error); alert(error.message || "Ocurrió un error al guardar el producto."); }
}
async function eliminarProducto(id) {
    if (!confirm("¿Seguro que deseas eliminar este producto?")) return;
    try {
        const respuesta = await fetch(`/api/productos/${id}`, { method: "DELETE" });
        if (!respuesta.ok) throw new Error();
        await cargarProductos(); alert("Producto eliminado correctamente.");
    } catch (error) { console.error(error); alert("No se pudo eliminar el producto. Es posible que esté asociado a un pedido."); }
}
