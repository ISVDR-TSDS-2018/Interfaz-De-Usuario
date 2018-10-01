package com.javierpintosettlin.neptuno;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RequestQueue queue = Volley.newRequestQueue(this);

        String url = "http://192.168.5.10/neptuno/webservice.asmx/GetPedidos";

        StringRequest stringRequest = new StringRequest(
                StringRequest.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        String mostrar = "";
                        try {
                            ByteArrayInputStream input = new ByteArrayInputStream(response.getBytes("UTF-8"));

                            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                            DocumentBuilder builder = factory.newDocumentBuilder();
                            Document doc = builder.parse(input);

                            input.close();

                            doc.getDocumentElement().normalize();

                            NodeList nList = doc.getElementsByTagName("Pedidos");

                            for (int i = 0; i < nList.getLength(); i++) {
                                Node nNode = nList.item(i);

                                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                                    Element eElement = (Element) nNode;

                                    String idPedido = eElement.getElementsByTagName("IdPedido").item(0).getTextContent();
                                    String idCliente = eElement.getElementsByTagName("IdCliente").item(0).getTextContent();
                                    String idEmpleado = eElement.getElementsByTagName("IdEmpleado").item(0).getTextContent();
                                    String fecha = eElement.getElementsByTagName("FechaPedido").item(0).getTextContent();

                                    mostrar += idPedido + " - " + idCliente + ", " + idEmpleado + ", " + fecha + "\n";
                                }
                            }
                        }
                        catch (Exception e) {
                            Log.e("Error", e.getMessage());
                            mostrar=e.getMessage();
                        }

                        TextView lblResult = (TextView)findViewById(R.id.lblResult);
                        lblResult.setText(mostrar);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("Error", error.getMessage());
                        TextView lblResult = (TextView)findViewById(R.id.lblResult);
                        lblResult.setText(error.getMessage());
                    }
                });



        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }
}






