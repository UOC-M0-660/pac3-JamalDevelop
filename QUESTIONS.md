# PARTE TEORICA

### Lifecycle

#### Explica el ciclo de vida de una Activity.

##### ¿Por qué vinculamos las tareas de red a los componentes UI de la aplicación?
Para que estén activos mientras lo esté nuestro componente UI y no consuma recursos innecesarios cuando por ejemplo, se destruye nuestro componente y la tarea sigue ejecutándose en segundo plano.

##### ¿Qué pasaría si intentamos actualizar la recyclerview con nuevos streams después de que el usuario haya cerrado la aplicación?
No haria nada. La aplicación estaría cerrada, o sea, destruida. Si estuviera en modo onPause() puede que prepare la app pero no los cargaría hasta que estuviera de nuevo abierta.

##### Describe brevemente los principales estados del ciclo de vida de una Activity.
* **onCreate()**: Se activa cuando el sistema crea la actividad por primera vez.
* **onStart()**: Nos indica que la actividad está a punto de ser mostrada al usuario. 
* **onResume()**: Se llama cuando la actividad va a comenzar a interactuar con el usuario.
* **onPause()**: Indica que la actividad está a punto de ser lanzada a segundo plano, normalmente porque otra actividad es lanzada. 
* **onStop()**: La actividad ya no va a ser visible para el usuario. Si hay poca memoria es posible que la actividad se destruya sin llamar a este método.
* **onDestroy()**: Se llama antes de que la actividad sea totalmente destruida.

---

### Paginación 

#### Explica el uso de paginación en la API de Twitch.

##### ¿Qué ventajas ofrece la paginación a la aplicación?
Principalmente agilidad. Una mejor gestión de memoria ya que sólo carga un número reducido de datos, normalmente los que aparecen en el área visible en primera instancia.

##### ¿Qué problemas puede tener la aplicación si no se utiliza paginación?
Puede quedarse corta y cargar solo los 20 primeros streams o puede bloquearse al quedarse sin memoria si se traen muchos para pintarlos en el recyclerView.

##### Lista algunos ejemplos de aplicaciones que usan paginación.
Facebook, Twitter, Instagram…las redes sociales en general son un buen ejemplo… aplicaciones que están en constante crecimiento y con información actualizada en tiempo real. 
