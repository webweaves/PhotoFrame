package com.csw;

import java.util.UUID;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

@Named
@RequestScoped
public class PublishMessage {
	
	private IMqttClient publisher;
	
	private Boolean connected = false;
	
	private void publish(String topic) {
		if (!publisher.isConnected()) {
			connect();
		}
		
		try {
			publisher.publish(topic, new MqttMessage());
		} catch (MqttException e) {
			e.printStackTrace();
		} finally {
			try {
				connected = false;
				publisher.disconnect();
			} catch (MqttException e) {
				e.printStackTrace();
			}
		}
	}

	private void connect() {
		String publisherId = UUID.randomUUID().toString();
		try {
			publisher = new MqttClient("tcp://192.168.0.150:1883", publisherId);
			MqttConnectOptions options = new MqttConnectOptions();
			options.setAutomaticReconnect(true);
			options.setCleanSession(true);
			options.setConnectionTimeout(10);
			publisher.connect(options);
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	public void print() {
		publish("frame/print");
	}

	public void delete() {
		publish("frame/delete");
	}

	
	public static void main(String[] args) {
		new PublishMessage().publish("frame/print");
	}

	public Boolean getConnected() {
		return connected;
	}

	public void setConnected(Boolean connected) {
		this.connected = connected;
	}
}
