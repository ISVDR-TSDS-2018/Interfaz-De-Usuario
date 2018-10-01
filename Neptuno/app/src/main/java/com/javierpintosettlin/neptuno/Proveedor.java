package com.javierpintosettlin.neptuno;

/**
 * Created by Javier on 02/07/2018.
 */

public class Proveedor {
    String IdProveedor;
    String Nombre;

    @Override
    public String toString() {
        return IdProveedor + "-" + Nombre;
    }
}
