## RestOpenGov.js

RestOpenGov.js es un simple cliente en Javascript para correr consultas client-side. Dependencias: Necesita jQuery para funcionar.

### Demos
En la carpeta de demos se encuentran ejemplos de uso del cliente. Notar que sólo son para fines demostrativos y no pretenden ser aplicaciones para un usuario final.

### Instalación

1. Incluír el JS:
```
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.js"></script>
<script type="text/javascript" src="RestOpenGov.js"></script>
```

2. Ejecutar una búsqueda
```
<script type="text/javascript">   
RestOpenGov.search({ dataset: 'bafici', query: 'terror' }, function(results) {
    console.log(results);
});
</script>
```

## Licencia
Este software es distribuído bajo la licencia Apache 2.0: http://www.apache.org/licenses/LICENSE-2.0