package es.ricardo.lector;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * Created by toorop on 23/10/16.
 */

public class AudioLibro {
    private List<File> capitulos;
    private String nombre;
    private Date fecha;

    public List<File> getCapitulos() {
        return capitulos;
    }

    public void setCapitulos(List<File> capitulos) {
        this.capitulos = capitulos;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

}
