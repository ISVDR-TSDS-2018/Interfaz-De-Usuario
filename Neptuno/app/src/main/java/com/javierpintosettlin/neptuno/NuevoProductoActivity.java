package com.javierpintosettlin.neptuno;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class NuevoProductoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_producto);

        //Cargar Spinner Proveedor
        CargarSpinnerProveedores();

        //Cargar Spinner Categoria
        CargarSpinnerCategorias();
    }

    private void CargarSpinnerProveedores() {

        //String url = "http://190.120.101.15/neptuno/webservice.asmx/GetProveedores";
        String url = "http://192.168.5.10/neptuno/webservice.asmx/GetProveedores";

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(
                StringRequest.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        List<Proveedor> ListaProveedor = new ArrayList<>();

                        ListaProveedor = ParseXMLResponseProveedor(response);

                        Spinner sp = findViewById(R.id.spnProveedor);

                        // Create an ArrayAdapter using the string array and a default spinner layout
                        ArrayAdapter<Proveedor> adapter = new ArrayAdapter<Proveedor>(getApplicationContext(),
                                android.R.layout.simple_spinner_item, ListaProveedor);
                        // Specify the layout to use when the list of choices appears
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        // Apply the adapter to the spinner
                        sp.setAdapter(adapter);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("Error", error.getMessage());
                    }
                });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void CargarSpinnerCategorias() {
        //String url = "http://190.120.101.15/neptuno/webservice.asmx/GetCategorias";
        String url = "http://192.168.5.10/neptuno/webservice.asmx/GetCategorias";

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(
                StringRequest.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        List<Categoria> ListaCategorias = new ArrayList<>();

                        ListaCategorias = ParseXMLResponseCategoria(response);

                        Spinner sp = findViewById(R.id.spnCategoria);

                        // Create an ArrayAdapter using the string array and a default spinner layout
                        ArrayAdapter<Categoria> adapter = new ArrayAdapter<Categoria>(getApplicationContext(),
                                android.R.layout.simple_spinner_item, ListaCategorias);
                        // Specify the layout to use when the list of choices appears
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        // Apply the adapter to the spinner
                        sp.setAdapter(adapter);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("Error", error.getMessage());
                    }
                });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void GuardarProductoClick(View view) {

        /*
        POST /neptuno/webservice.asmx/RegistrarProducto HTTP/1.1
        Host: 190.120.101.15
        Content-Type: application/x-www-form-urlencoded
        Content-Length: length

        user=string&Password=string&Nombre=string&idProveedor=string&idCategoria=string&cantXUnidad=string&precioUnidad=string&unidadesExistencia=string&nivelNuevoPedido=string

        HTTP/1.1 200 OK
        Content-Type: text/xml; charset=utf-8
        Content-Length: length

        <?xml version="1.0" encoding="utf-8"?>
        <boolean xmlns="http://tempuri.org/">boolean</boolean>
         */

        //String url = "http://190.120.101.15/neptuno/webservice.asmx/RegistrarProducto";
        String url = "http://192.168.5.10/neptuno/webservice.asmx/RegistrarProducto";

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(
                StringRequest.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Boolean correcto = false;
                        try {
                            ByteArrayInputStream input = new ByteArrayInputStream(response.getBytes("UTF-8"));

                            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                            DocumentBuilder builder = factory.newDocumentBuilder();
                            Document doc = builder.parse(input);

                            input.close();

                            doc.getDocumentElement().normalize();

                            String valor = doc.getElementsByTagName("boolean").item(0).getTextContent();
                            if(valor.equals("true"))
                                correcto=true;


                        }
                        catch (Exception ex)
                        {
                            Toast.makeText(getApplicationContext(), "ERROR: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        // controlar true o false
                        if (correcto) {
                            Toast.makeText(getApplicationContext(), "Producto Registrado con EXITO!!!", Toast.LENGTH_LONG).show();
                            finish();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "ERROR al Registrar", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "ERROR: " + error.toString(), Toast.LENGTH_LONG).show();
                        Log.e("Error", error.toString());
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                /*
                user=string&
                Password=string&
                Nombre=string&
                idProveedor=string&
                idCategoria=string&
                cantXUnidad=string&
                precioUnidad=string&
                unidadesExistencia=string&
                nivelNuevoPedido=string
                 */
                TextView txtNombre = findViewById(R.id.txtNombre);
                Spinner spnProveedor = findViewById(R.id.spnProveedor);
                Spinner spnCategoria = findViewById(R.id.spnCategoria);
                TextView txtCantXUn = findViewById(R.id.txtCantXUn);
                TextView txtPrecioUnidad = findViewById(R.id.txtPrecioUnidad);
                TextView txtUnidadesExistencia = findViewById(R.id.txtUnidadesExistencia);
                TextView txtNivelNuevoPedido = findViewById(R.id.txtNivelNuevoPedido);

                Map<String, String> params = new HashMap<String, String>();
                params.put("user", "admin");
                params.put("Password", "admin");
                params.put("Nombre", txtNombre.getText().toString());
                params.put("idProveedor", spnProveedor.getSelectedItem().toString().split("-")[0]);
                params.put("idCategoria", spnCategoria.getSelectedItem().toString().split("-")[0]);
                params.put("cantXUnidad", txtCantXUn.getText().toString());
                params.put("precioUnidad", txtPrecioUnidad.getText().toString());
                params.put("unidadesExistencia", txtUnidadesExistencia.getText().toString());
                params.put("nivelNuevoPedido", txtNivelNuevoPedido.getText().toString());
                return params;
            }
        }
                ;

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void CancelarNuevoProductoClick(View view) {
        finish();
    }

    private List<Proveedor> ParseXMLResponseProveedor(String response) {

        List<Proveedor> ListaProveedor = new ArrayList<Proveedor>();

        try {
                            /*

                             */

            ByteArrayInputStream input = new ByteArrayInputStream(response.getBytes("UTF-8"));

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(input);

            input.close();

            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("Proveedor");

            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    Proveedor p = new Proveedor();
                    p.IdProveedor = eElement.getElementsByTagName("IdProveedor").item(0).getTextContent();
                    p.Nombre = eElement.getElementsByTagName("Nombre").item(0).getTextContent();

                    ListaProveedor.add(p);
                }
            }

            return ListaProveedor;
        }
        catch (Exception e) {
            Log.e("Error", e.getMessage());
            return ListaProveedor;
        }
    }

    private List<Categoria> ParseXMLResponseCategoria(String response) {

        List<Categoria> ListaCategoria = new ArrayList<Categoria>();

        try {
                            /*
                            <ArrayOfCategoria>
                                <Categoria>
                                    <IdCategoria>1</IdCategoria>
                                    <Nombre>Bebidas</Nombre>
                                </Categoria>
                                <Categoria>
                                ...
                             */

            ByteArrayInputStream input = new ByteArrayInputStream(response.getBytes("UTF-8"));

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(input);

            input.close();

            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("Categoria");

            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    Categoria categoria = new Categoria();
                    categoria.IdCategoria = eElement.getElementsByTagName("IdCategoria").item(0).getTextContent();
                    categoria.Nombre = eElement.getElementsByTagName("Nombre").item(0).getTextContent();

                    ListaCategoria.add(categoria);
                }
            }

            return ListaCategoria;
        }
        catch (Exception e) {
            Log.e("Error", e.getMessage());
            return ListaCategoria;
        }
    }
}
