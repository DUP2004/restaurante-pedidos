# Guía para configurar publicación y despliegue

## A. Subir el proyecto a GitHub
1. Crear un repositorio en GitHub.
2. Abrir una terminal en la carpeta del proyecto (donde está `pom.xml`).
3. Ejecutar, reemplazando la URL por la de tu repositorio:

```bash
git init
git add .
git commit -m "Preparar CI y CD de Casa Sabor"
git branch -M main
git remote add origin https://github.com/USUARIO/REPOSITORIO.git
git push -u origin main
```

4. En GitHub, abrir la pestaña **Actions**. El workflow `Integracion continua` debe iniciar y ejecutar las pruebas.
5. En **Settings > Actions > General**, verificar que los workflows tengan permiso para leer el repositorio. El workflow CD necesita permiso de escritura de paquetes, ya declarado en el YAML.

## B. Publicación automática de imagen Docker (GHCR)
El workflow `.github/workflows/cd.yml` ejecuta pruebas y, si pasan, construye y publica la imagen en GitHub Container Registry (`ghcr.io`) al hacer push a `main` o publicar una etiqueta `v*`. Usa `GITHUB_TOKEN`; no hace falta crear un token personal para esta publicación.

La imagen queda con las etiquetas `latest` y el SHA del commit. Puede ser necesario cambiar la visibilidad del paquete en GitHub Packages según el uso que se le dará.

## C. Despliegue real a un servidor por SSH
La publicación de la imagen no significa que ya esté instalada en un servidor. Para desplegar en una VPS necesitas:
- Una VPS Linux con Docker y Docker Compose instalados.
- Un usuario SSH que pueda ejecutar Docker.
- Un directorio de despliegue, por ejemplo `/opt/casa-sabor`.
- Un `docker-compose.yml` de producción que use la imagen publicada y variables de entorno seguras.

En GitHub entra a **Settings > Secrets and variables > Actions > New repository secret**. Crea estos secretos:

| Secreto | Valor |
|---|---|
| `DEPLOY_HOST` | IP o dominio público de tu servidor. |
| `DEPLOY_USER` | Usuario SSH del servidor. |
| `DEPLOY_SSH_KEY` | Clave privada SSH completa; nunca la subas al repositorio. |
| `DEPLOY_PATH` | Ruta absoluta de la carpeta de despliegue. |

La clave pública correspondiente debe estar autorizada en `~/.ssh/authorized_keys` del servidor. Comprueba primero que puedes conectarte desde tu equipo con SSH.

**Importante:** el workflow incluido publica la imagen Docker; no ejecuta todavía la conexión SSH ni consume esos cuatro secretos. El despliegue SSH debe añadirse cuando tengas servidor, compose de producción y credenciales. No agregues secretos hasta tener esos datos. No uses `localhost` como `DEPLOY_HOST`.

## D. Variables de producción
No publiques contraseñas en `application.properties`. Configura en el servidor las variables `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`, `ADMIN_USERNAME` y `ADMIN_PASSWORD`. Usa claves fuertes y restringe el acceso a los secretos.
