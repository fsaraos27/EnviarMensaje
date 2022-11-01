package com.example.enviarmensaje;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

public class MainActivity extends AppCompatActivity {


    EditText editText;
    String payload;
    Button btnEnviar;

    static String MQTTHOST = "tcp://68.183.119.177:1883";
    static String USERNAME = "pancho";
    static String PASS = "pancho";
    String topicMensaje = "st/01";
    MqttAndroidClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editarTexto);
        btnEnviar = findViewById(R.id.enviarTexto);

        String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(getApplicationContext(), MQTTHOST, clientId);
        MqttConnectOptions opcion = new MqttConnectOptions();
        opcion.setUserName(USERNAME);
        opcion.setPassword(PASS.toCharArray());

        try {
            IMqttToken token = client.connect(opcion);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Toast.makeText(getBaseContext(), "CONECTADO MQTT", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Toast.makeText(getBaseContext(), "ERROR DE CONEXIÓN", Toast.LENGTH_LONG).show();

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void enviarMensaje(View view){
        String topic = topicMensaje;
        payload = editText.getText().toString();

        try{
            client.publish(topic, payload.getBytes(), 0 , false);
        } catch (MqttException e){
            e.printStackTrace();
        }
    }
}