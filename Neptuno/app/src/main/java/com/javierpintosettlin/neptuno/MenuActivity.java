package com.javierpintosettlin.neptuno;

import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    public void Pedidos_Click(View view) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void Productos_Click(View view) {
        Intent i = new Intent(this, ProductoActivity.class);
        startActivity(i);
    }

    public void Notificacion_Click(View view) {

        Intent intent = new Intent(this, ProductoActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);


        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, NotificationChannel.DEFAULT_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Hola Mundo")
                .setContentText("¡¡¡Hola mundo de las Notificaciones!!!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(0, mBuilder.build());
    }
}
