package com.javierpintosettlin.neptuno;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class ProductoActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Ir Nuevo Producto
                Intent i = new Intent(getApplicationContext(), NuevoProductoActivity.class);
                startActivity(i);

            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.cancel(0);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //String url = "http://190.120.101.15/neptuno/webservice.asmx/GetProductos";
        String url = "http://192.168.5.10/neptuno/webservice.asmx/GetProductos";

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(
                StringRequest.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        List<Producto> ListaProductos = new ArrayList<Producto>();

                        ListaProductos = ParseXMLResponse(response);

                        ListView lvProductos = findViewById(R.id.lvProductos);

                        ArrayAdapter<Producto> aas = new ArrayAdapter<Producto>(getApplicationContext(),
                                R.layout.producto_list_item, ListaProductos)
                        {
                            @NonNull
                            @Override
                            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                                LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                View rowView = inflater.inflate(R.layout.producto_list_item, parent, false);
                                TextView lblNombre = rowView.findViewById(R.id.li_lbl_nombre);
                                TextView lblPrecio = rowView.findViewById(R.id.li_lbl_precio);

                                lblNombre.setText(this.getItem(position).Nombre);
                                lblPrecio.setText(this.getItem(position).PrecioUnidad);

                                return rowView;
                            }

                            @Override
                            public long getItemId(int position) {
                                return Long.parseLong(this.getItem(position).IdProducto);
                            }
                        };

                        lvProductos.setAdapter(aas);

                        lvProductos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Toast.makeText(ProductoActivity.this, "posición:" + i + " - id:" + l,
                                        Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                        Log.e("Error", error.toString());
                    }
                });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private List<Producto> ParseXMLResponse(String response) {

        List<Producto> ListaProductos = new ArrayList<Producto>();

        try {
                            /*
                            <Productos>
                                <IdProducto>1</IdProducto>
                                <NombreProducto>Té Dharamsala</NombreProducto>
                                <PrecioUnidad>18</PrecioUnidad>
                            </Productos>
                            <Productos>
                                ...
                             */

            ByteArrayInputStream input = new ByteArrayInputStream(response.getBytes("UTF-8"));

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(input);

            input.close();

            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("Productos");

            for (int i = 0; i < nList.getLength(); i++) {
                Node nNode = nList.item(i);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    Producto p = new Producto();
                    p.IdProducto = eElement.getElementsByTagName("IdProducto").item(0).getTextContent();
                    p.Nombre = eElement.getElementsByTagName("NombreProducto").item(0).getTextContent();
                    p.PrecioUnidad = eElement.getElementsByTagName("PrecioUnidad").item(0).getTextContent();

                    ListaProductos.add(p);
                }
            }

            return ListaProductos;
        }
        catch (Exception e) {
            Log.e("Error", e.getMessage());
            return ListaProductos;
        }
    }
}
