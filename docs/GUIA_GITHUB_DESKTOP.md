# Trabajar con GitHub Desktop desde Windows

## Antes de copiar la versión mejorada
1. Abre GitHub Desktop y selecciona el repositorio `restaurante-pedidos`.
2. Comprueba que estás en la rama `main`.
3. En la captura anterior GitHub Desktop indica que hay **2 commits en origin que no están en tu ordenador**. Pulsa **Pull origin** y espera a que termine. Esto descarga los cambios remotos.
4. Si GitHub muestra un conflicto, no elijas descartar cambios sin revisarlos. Conserva una copia de seguridad y resuelve el conflicto antes de continuar.

## Incorporar los cambios de Casa Sabor
1. Descarga y extrae el ZIP de esta entrega en una carpeta temporal.
2. Abre la carpeta interna `restaurante-pedidos-spring-boot`.
3. Copia su contenido (incluidos `src`, `docs`, `.github`, `pom.xml`, etc.) dentro de la carpeta local que GitHub Desktop muestra con **Show in Explorer**.
4. Cuando Windows pregunte, acepta combinar carpetas y reemplazar archivos. No reemplaces la carpeta `.git` ni borres el repositorio.
5. Vuelve a GitHub Desktop. En **Changes** deben aparecer los archivos modificados.
6. Revisa los cambios. En el campo **Summary** escribe `Mejora visual del panel Casa Sabor y documentación CI CD`.
7. Pulsa **Commit to main**.
8. Pulsa **Push origin** en la barra superior. No uses Pull otra vez si no hay nuevos commits remotos.

## Comprobar GitHub Actions
1. Abre el repositorio en GitHub desde **View on GitHub**.
2. Entra en la pestaña **Actions**.
3. Abre la ejecución más reciente del workflow.
4. Revisa cada paso. Una marca verde indica que ese paso terminó correctamente; una cruz roja indica que debes abrir el paso y leer el error.

## Qué significan los botones
- **Fetch origin**: consulta si hay cambios remotos; no descarga archivos.
- **Pull origin**: descarga e integra commits remotos en tu copia local.
- **Commit to main**: guarda un punto de control en tu ordenador; todavía no lo sube a GitHub.
- **Push origin**: sube tus commits locales a GitHub.

## Recomendación
Antes de empezar a editar en cada sesión: `Fetch origin`; si aparecen commits remotos, usa `Pull origin`. Después de editar: revisa, haz commit y finalmente `Push origin`.
