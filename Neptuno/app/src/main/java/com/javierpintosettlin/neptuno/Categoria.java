package com.javierpintosettlin.neptuno;

/**
 * Created by Javier on 03/07/2018.
 */

public class Categoria {
    String IdCategoria;
    String Nombre;

    @Override
    public String toString() {
        return IdCategoria + "-" + Nombre;
    }
}
