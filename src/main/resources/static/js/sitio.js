// ============================================================
// SITIO PÚBLICO DEL RESTAURANTE
// Muestra la carta (desde /api), arma el pedido del cliente y
// lo envía al backend. También permite consultar el estado.
// ============================================================
(() => {
  "use strict";

  const C = window.RESTAURANTE || {};
  const MONEDA = C.moneda || "S/";

  const $ = (sel, raiz = document) => raiz.querySelector(sel);
  const $$ = (sel, raiz = document) => Array.from(raiz.querySelectorAll(sel));

  // Evita que texto con símbolos (< > &) rompa o inyecte HTML
  const esc = (t) => String(t ?? "").replace(/[&<>"']/g, (c) => (
    { "&": "&amp;", "<": "&lt;", ">": "&gt;", '"': "&quot;", "'": "&#39;" }[c]
  ));
  const dinero = (n) => `${MONEDA} ${Number(n).toFixed(2)}`;

  // localStorage puede fallar (modo privado): siempre con try/catch
  const almacen = {
    leer(clave) { try { return JSON.parse(localStorage.getItem(clave)); } catch { return null; } },
    guardar(clave, valor) { try { localStorage.setItem(clave, JSON.stringify(valor)); } catch { /* sin almacenamiento */ } }
  };

  const estado = {
    productos: [],
    categorias: [],
    carrito: new Map()   // productoId -> cantidad
  };
  let observador = null;

  // ------------------------------------------------------------
  // Textos del restaurante (config.js)
  // ------------------------------------------------------------
  function aplicarConfig() {
    $$("[data-cfg]").forEach((el) => {
      const valor = C[el.dataset.cfg];
      if (valor) el.textContent = valor;
    });
    if (C.nombre) document.title = `${C.nombre} | Carta y pedidos`;
    if (C.direccion) $("#cfgDireccion").textContent = C.direccion;
    if (C.telefono) {
      const tel = $("#cfgTelefono");
      tel.textContent = C.telefono;
      tel.href = "tel:" + String(C.telefono).replace(/[^\d+]/g, "");
    }
    if (Array.isArray(C.horario)) {
      $("#cfgHorario").innerHTML = C.horario
        .map(([dia, horas]) => `<dt>${esc(dia)}</dt><dd>${esc(horas)}</dd>`).join("");
    }
    if (Array.isArray(C.nosotros)) {
      $("#textoNosotros").innerHTML = C.nosotros.map((p) => `<p>${esc(p)}</p>`).join("");
    }
    $("#anio").textContent = new Date().getFullYear();
  }

  // ------------------------------------------------------------
  // Carta
  // ------------------------------------------------------------
  async function cargarCarta() {
    try {
      const [rp, rc] = await Promise.all([fetch("/api/productos"), fetch("/api/categorias")]);
      if (!rp.ok || !rc.ok) throw new Error("API");
      estado.productos = await rp.json();
      estado.categorias = await rc.json();
    } catch (error) {
      console.error(error);
      $("#listaCarta").innerHTML =
        `<p class="vacio">No pudimos cargar la carta. <button type="button" class="boton-texto" id="reintentar">Intentar de nuevo</button></p>`;
      $("#categoriasNav").innerHTML = "";
      $("#ticketLista").innerHTML = `<p class="ticket-vacio">La carta no está disponible en este momento.</p>`;
      return;
    }
    limpiarCarrito();
    renderCarta();
    renderTicket();
    renderCarrito();
  }

  // Quita del pedido los platos que ya no existen o se agotaron
  function limpiarCarrito() {
    let cambio = false;
    for (const id of Array.from(estado.carrito.keys())) {
      const p = estado.productos.find((x) => x.id === id);
      if (!p || !p.disponible) { estado.carrito.delete(id); cambio = true; }
    }
    if (cambio) guardarCarrito();
  }

  function grupos() {
    const mapa = new Map();
    estado.categorias.forEach((c) => mapa.set(c.id, { cat: c, items: [] }));
    estado.productos.forEach((p) => {
      if (!p.categoria) return;
      if (!mapa.has(p.categoria.id)) mapa.set(p.categoria.id, { cat: p.categoria, items: [] });
      mapa.get(p.categoria.id).items.push(p);
    });
    return Array.from(mapa.values()).filter((g) => g.items.length > 0);
  }

  function renderCarta() {
    const lista = $("#listaCarta");
    const nav = $("#categoriasNav");
    const visibles = grupos();

    if (observador) observador.disconnect();

    if (visibles.length === 0) {
      nav.innerHTML = "";
      lista.innerHTML = `<p class="vacio">Estamos preparando la carta. Vuelve en unos minutos.</p>`;
      return;
    }

    nav.innerHTML = visibles
      .map((g) => `<a href="#cat-${g.cat.id}">${esc(g.cat.nombre)}</a>`).join("");

    lista.innerHTML = visibles.map((g) => `
      <section class="categoria" id="cat-${g.cat.id}">
        <h3>${esc(g.cat.nombre)}</h3>
        ${g.cat.descripcion ? `<p class="categoria-desc">${esc(g.cat.descripcion)}</p>` : ""}
        <ul class="platos">${g.items.map(platoHTML).join("")}</ul>
      </section>`).join("");

    observarCategorias();
  }

  function platoHTML(p) {
    return `
      <li class="plato${p.disponible ? "" : " agotado"}" data-id="${p.id}">
        <div class="plato-fila">
          <h4>${esc(p.nombre)}</h4>
          <span class="puntos" aria-hidden="true"></span>
          <span class="precio">${dinero(p.precio)}</span>
        </div>
        ${p.descripcion ? `<p class="plato-desc">${esc(p.descripcion)}</p>` : ""}
        <div class="plato-accion">${controlHTML(p)}</div>
      </li>`;
  }

  // Botón "Agregar" o contador (− 2 +) según lo que haya en el pedido
  function controlHTML(p) {
    if (!p.disponible) return `<span class="etiqueta-agotado">Agotado hoy</span>`;
    const cantidad = estado.carrito.get(p.id) || 0;
    if (cantidad === 0) {
      return `<button type="button" class="btn-agregar" data-accion="sumar" data-id="${p.id}"
                aria-label="Agregar ${esc(p.nombre)} al pedido">Agregar</button>`;
    }
    return stepperHTML(p, cantidad);
  }

  function stepperHTML(p, cantidad) {
    return `<div class="stepper" role="group" aria-label="Cantidad de ${esc(p.nombre)}">
        <button type="button" data-accion="restar" data-id="${p.id}" aria-label="Quitar uno">&minus;</button>
        <output>${cantidad}</output>
        <button type="button" data-accion="sumar" data-id="${p.id}" aria-label="Agregar uno">+</button>
      </div>`;
  }

  function observarCategorias() {
    const enlaces = new Map($$("#categoriasNav a").map((a) => [a.getAttribute("href").slice(1), a]));
    observador = new IntersectionObserver((entradas) => {
      entradas.forEach((e) => {
        if (!e.isIntersecting) return;
        enlaces.forEach((a) => a.classList.remove("activa"));
        const enlace = enlaces.get(e.target.id);
        if (enlace) enlace.classList.add("activa");
      });
    }, { rootMargin: "-30% 0px -60% 0px" });
    $$(".categoria").forEach((s) => observador.observe(s));
  }

  function renderTicket() {
    const disponibles = estado.productos.filter((p) => p.disponible).slice(0, 4);
    $("#ticketLista").innerHTML = disponibles.length
      ? disponibles.map((p) => `
          <div class="ticket-fila">
            <span>${esc(p.nombre)}</span><span class="puntos" aria-hidden="true"></span>
            <span class="precio">${dinero(p.precio)}</span>
          </div>`).join("")
      : `<p class="ticket-vacio">Estamos armando la carta de hoy.</p>`;
  }

  // ------------------------------------------------------------
  // Pedido (carrito)
  // ------------------------------------------------------------
  function guardarCarrito() {
    almacen.guardar("carrito", Array.from(estado.carrito.entries()));
  }

  function cargarCarritoGuardado() {
    const guardado = almacen.leer("carrito");
    if (Array.isArray(guardado)) {
      guardado.forEach(([id, cant]) => {
        if (Number.isInteger(id) && Number.isInteger(cant) && cant > 0) estado.carrito.set(id, cant);
      });
    }
  }

  function cambiarCantidad(id, delta, contenedorSel, accion) {
    const actual = estado.carrito.get(id) || 0;
    const nueva = Math.max(0, Math.min(20, actual + delta));
    if (nueva === 0) estado.carrito.delete(id); else estado.carrito.set(id, nueva);
    guardarCarrito();

    // Actualiza solo el control del plato tocado (no toda la carta)
    const producto = estado.productos.find((p) => p.id === id);
    const celda = $(`.plato[data-id="${id}"] .plato-accion`);
    if (producto && celda) celda.innerHTML = controlHTML(producto);

    renderCarrito();
    if (producto) {
      $("#aviso").textContent = nueva > actual
        ? `${producto.nombre} agregado. Cantidad: ${nueva}.`
        : `${producto.nombre}: cantidad ${nueva}.`;
    }

    // Devuelve el foco al botón equivalente (para teclado y lectores de pantalla)
    const contenedor = $(contenedorSel);
    const destino = contenedor && (
      $(`[data-accion="${accion}"]`, contenedor) || $('[data-accion="sumar"]', contenedor)
    );
    if (destino) destino.focus();
  }

  function lineas() {
    return Array.from(estado.carrito.entries())
      .map(([id, cantidad]) => ({ p: estado.productos.find((x) => x.id === id), cantidad }))
      .filter((l) => l.p);
  }

  function renderCarrito() {
    const items = lineas();
    const cantidadTotal = items.reduce((s, l) => s + l.cantidad, 0);
    const total = items.reduce((s, l) => s + Number(l.p.precio) * l.cantidad, 0);

    $("#contadorCarrito").textContent = cantidadTotal;
    $("#btnCarrito").setAttribute("aria-label",
      cantidadTotal ? `Abrir mi pedido, ${cantidadTotal} ${cantidadTotal === 1 ? "plato" : "platos"}` : "Abrir mi pedido");

    const confirmando = !$("#carritoConfirmacion").hidden;
    $("#carritoVacio").hidden = items.length > 0 || confirmando;
    $("#formPedido").hidden = items.length === 0 || confirmando;

    $("#carritoLineas").innerHTML = items.map(({ p, cantidad }) => `
      <li class="linea" data-id="${p.id}">
        <div><strong>${esc(p.nombre)}</strong><small>${dinero(p.precio)} c/u</small></div>
        ${stepperHTML(p, cantidad)}
        <span class="precio linea-total">${dinero(Number(p.precio) * cantidad)}</span>
      </li>`).join("");

    $("#carritoTotal").textContent = dinero(total);
    $("#btnConfirmar").textContent = `Confirmar pedido · ${dinero(total)}`;
  }

  function mostrarError(texto) {
    const caja = $("#errorPedido");
    caja.textContent = texto;
    caja.hidden = !texto;
  }

  // ------------------------------------------------------------
  // Enviar pedido
  // ------------------------------------------------------------

  async function enviarPedido(evento) {
    evento.preventDefault();
    mostrarError("");

    const datos = {
      nombre: $("#cNombre").value.trim(),
      apellido: $("#cApellido").value.trim(),
      correo: $("#cCorreo").value.trim(),
      telefono: $("#cTelefono").value.trim()
    };

    if (!datos.nombre || !datos.apellido) return mostrarError("Escribe tu nombre y apellido.");
    if (!/^\S+@\S+\.\S+$/.test(datos.correo)) return mostrarError("Escribe un correo válido, por ejemplo nombre@correo.com.");
    if (!/^[0-9+\s()-]{6,}$/.test(datos.telefono)) return mostrarError("Escribe un teléfono válido, solo con números.");
    if (estado.carrito.size === 0) return mostrarError("Agrega al menos un plato.");

    const boton = $("#btnConfirmar");
    boton.disabled = true;
    boton.textContent = "Enviando pedido…";

    try {
      const detalles = Array.from(estado.carrito.entries())
        .map(([productoId, cantidad]) => ({ productoId, cantidad }));

      const respuesta = await fetch("/api/pedidos/publicos", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ cliente: datos, detalles })
      });
      if (!respuesta.ok) throw new Error("pedido");

      const pedido = await respuesta.json();
      almacen.guardar("cliente", datos);
      almacen.guardar("ultimoCodigoSeguimiento", pedido.codigoSeguimiento);
      estado.carrito.clear();
      guardarCarrito();
      mostrarConfirmacion(pedido);
    } catch (error) {
      console.error(error);
      await cargarCarta(); // por si algún plato se agotó mientras elegías
      mostrarError("No pudimos registrar tu pedido. Puede que algún plato se haya agotado: revisa tu pedido e inténtalo de nuevo.");
      renderCarrito();
    } finally {
      boton.disabled = false;
    }
  }

  function mostrarConfirmacion(pedido) {
    const caja = $("#carritoConfirmacion");
    const detalle = (pedido.detalles || []).map((d) =>
      `<li>${d.cantidad} × ${esc(d.producto.nombre)}</li>`).join("");
    caja.innerHTML = `
      <h3>Recibimos tu pedido</h3>
      <span class="numero">Pedido recibido</span>
      <p>Guarda este código privado para consultar el estado de tu pedido. No lo compartas con otras personas.</p>
      <p class="codigo-seguimiento"><strong>${esc(pedido.codigoSeguimiento)}</strong></p><ul class="seg-detalle">${detalle}</ul>
      <p class="total" style="margin-top:1rem"><span>Total</span><span>${dinero(pedido.total)}</span></p>
      <div class="botones">
        <button type="button" class="boton boton-primario" id="irSeguimiento">Seguir mi pedido</button>
        <button type="button" class="boton" data-cerrar>Seguir viendo la carta</button>
      </div>`;
    caja.hidden = false;
    renderCarrito();
    $("#irSeguimiento").focus();
  }

  // ------------------------------------------------------------
  // Seguimiento
  // ------------------------------------------------------------
  const TEXTOS = {
    PENDIENTE:  ["Recibimos tu pedido", "En cuanto la cocina esté libre, empieza a prepararlo."],
    PREPARANDO: ["Lo estamos preparando", "Estará listo en unos minutos."],
    LISTO:      ["Listo para recoger", "Puedes pasar por el local."],
    ENTREGADO:  ["Entregado", "Gracias por tu pedido."],
    CANCELADO:  ["Pedido cancelado", "Si crees que es un error, llámanos."]
  };
  const ETAPAS = [["PENDIENTE", "Recibido"], ["PREPARANDO", "Preparando"], ["LISTO", "Listo"], ["ENTREGADO", "Entregado"]];

  function seguimientoHTML(p) {
    const [titulo, texto] = TEXTOS[p.estado] || [p.estado, ""];
    const indice = ETAPAS.findIndex(([clave]) => clave === p.estado);
    const progreso = p.estado === "CANCELADO" ? "" : `
      <ol class="progreso" aria-label="Etapas del pedido">
        ${ETAPAS.map(([, nombre], i) =>
          `<li class="${i < indice ? "hecho" : i === indice ? "actual" : ""}">${nombre}</li>`).join("")}
      </ol>`;
    const detalle = (p.detalles || []).map((d) =>
      `<li>${d.cantidad} × ${esc(d.producto.nombre)}</li>`).join("");
    return `
      <p class="estado-titulo">Pedido N.º ${p.id}: ${esc(titulo)}</p>
      <p style="margin:0">${esc(texto)}</p>
      ${progreso}
      <ul class="seg-detalle">${detalle}</ul>
      <p class="total" style="margin:1rem 0 0"><span>Total</span><span>${dinero(p.total)}</span></p>`;
  }

  async function consultarPedido(codigo) {
    const salida = $("#segResultado");
    const valor = String(codigo || "").trim();
    if (!/^[0-9a-f-]{36}$/i.test(valor)) {
      salida.innerHTML = `<p class="error" role="alert">Introduce el código privado de seguimiento que recibiste al confirmar el pedido.</p>`;
      return;
    }
    salida.innerHTML = `<p class="nota">Buscando…</p>`;
    try {
      const r = await fetch(`/api/pedidos/seguimiento/${encodeURIComponent(valor)}`);
      if (!r.ok) {
        salida.innerHTML = `<p class="error" role="alert">No encontramos un pedido con ese código. Revisa que lo hayas copiado completo.</p>`;
        return;
      }
      salida.innerHTML = seguimientoHTML(await r.json());
    } catch (error) {
      console.error(error);
      salida.innerHTML = `<p class="error" role="alert">No pudimos conectar con el restaurante. Inténtalo en unos segundos.</p>`;
    }
  }

  function abrirSeguimiento(codigo) {
    const campo = $("#segNumero");
    const ultimo = numero ?? almacen.leer("ultimoCodigoSeguimiento");
    campo.value = ultimo || "";
    $("#segResultado").innerHTML = "";
    $("#carrito").close();
    $("#seguimiento").showModal();
    if (ultimo) consultarPedido(String(ultimo)); else campo.focus();
  }

  function abrirCarrito() {
    // Si quedó una confirmación abierta de un pedido anterior, vuelve a la vista normal
    $("#carritoConfirmacion").hidden = true;
    $("#carritoConfirmacion").innerHTML = "";
    mostrarError("");
    renderCarrito();
    $("#carrito").showModal();
  }

  function prellenarDatos() {
    const d = almacen.leer("cliente");
    if (!d) return;
    $("#cNombre").value = d.nombre || "";
    $("#cApellido").value = d.apellido || "";
    $("#cCorreo").value = d.correo || "";
    $("#cTelefono").value = d.telefono || "";
  }

  // ------------------------------------------------------------
  // Eventos
  // ------------------------------------------------------------
  document.addEventListener("click", (e) => {
    const sumaResta = e.target.closest("[data-accion]");
    if (sumaResta) {
      const id = Number(sumaResta.dataset.id);
      const accion = sumaResta.dataset.accion;
      const enCarrito = sumaResta.closest(".linea");
      const sel = enCarrito ? `.linea[data-id="${id}"]` : `.plato[data-id="${id}"]`;
      cambiarCantidad(id, accion === "sumar" ? 1 : -1, sel, accion);
      return;
    }
    if (e.target.closest("#reintentar")) { cargarCarta(); return; }
    if (e.target.closest("#btnCarrito") || e.target.closest("#btnHeroPedido")) { abrirCarrito(); return; }
    if (e.target.closest("#btnSeguir")) { abrirSeguimiento(); return; }
    if (e.target.closest("#irSeguimiento")) { abrirSeguimiento(); return; }

    const ir = e.target.closest("[data-cerrar-y-ir]");
    if (ir) {
      $("#carrito").close();
      const destino = $(ir.dataset.cerrarYIr);
      if (destino) destino.scrollIntoView();
      return;
    }
    const cerrar = e.target.closest("[data-cerrar]");
    if (cerrar) { const dlg = cerrar.closest("dialog"); if (dlg) dlg.close(); }
  });

  // Clic en el fondo oscuro cierra el diálogo
  $$("dialog").forEach((dlg) => dlg.addEventListener("click", (e) => { if (e.target === dlg) dlg.close(); }));

  $("#formPedido").addEventListener("submit", enviarPedido);
  $("#formSeguimiento").addEventListener("submit", (e) => {
    e.preventDefault();
    consultarPedido($("#segNumero").value.trim());
  });

  // ------------------------------------------------------------
  // Inicio
  // ------------------------------------------------------------
  aplicarConfig();
  cargarCarritoGuardado();
  prellenarDatos();
  renderCarrito();
  cargarCarta();
})();
