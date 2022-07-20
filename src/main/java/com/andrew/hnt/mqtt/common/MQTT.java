package com.andrew.hnt.mqtt.common;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MQTT implements MqttCallback {
	
	private static String Broker;
	private static String Client_ID;
	private static String UserName;
	private static String Password;
	private static MqttAsyncClient Client;
	private static MqttMessage message;
	private static MemoryPersistence persistence;
	private static MqttConnectOptions connOpts;
	private static String topic;
	
	public MQTT(String broker, String client_id, String userName, String password) {
		this.Broker = broker;
		this.Client_ID = client_id;
		this.UserName = userName;
		this.Password = password;
	}
	
	public void init(String topic) {
		this.topic = topic;
		this.persistence = new MemoryPersistence();
		
		try {
			Client = new MqttAsyncClient(this.Broker, this.Client_ID, this.persistence);
			Client.setCallback(this);
			
			connOpts = new MqttConnectOptions();
			connOpts.setUserName(this.UserName);
			connOpts.setPassword(this.Password.toCharArray());
			connOpts.setCleanSession(true);
			
			System.out.println("===== Connecting to broker : " + this.Broker);
			
			Client.connect(connOpts);
			
			System.out.println("===== Connect Status : Connected");
			
			message = new MqttMessage();
			
		} catch(MqttException mqe) {
			mqe.printStackTrace();
			System.out.println("===== Error reason : " + mqe.getReasonCode());
			System.out.println("===== Error message : " + mqe.getMessage());
			System.out.println("===== Error location : " + mqe.getLocalizedMessage());
			System.out.println("===== Error cause : " + mqe.getCause());
			System.out.println("===== Error exception : " + mqe.toString());
		}
		
		try {
			Thread.sleep(1000);
		} catch(InterruptedException ite) {
			ite.printStackTrace();
		}
		
		this.subscribe(0);
	}
	
	public void disconnect() {
		try {
			Client.disconnect();
			Client.close();
		} catch(MqttException mqe) {
			mqe.printStackTrace();
		}
	}
	
	public void publish(String msg, int qos) {
		message.setQos(qos);
		message.setPayload(msg.getBytes());
		
		try {
			Client.publish(topic,  message);
		} catch(MqttPersistenceException mpe) { 
			mpe.printStackTrace();
		} catch(MqttException mqe){
			mqe.printStackTrace();
		}
	}
	
	public void subscribe(int qos) {
		try {
			Client.subscribe(topic, qos);
		} catch(MqttException mqe) {
			mqe.printStackTrace();
		}
	}
	
	public String getTopic() {
		return topic;
	}
	
	@Override
	public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
		//System.out.println("===== Message Arrived : " + new String(mqttMessage.getPayload(), "UTF-8"));
		System.out.println("===== Topic : " + topic + " | Value : " + mqttMessage.toString());
		
		// 여기서 API 호출을 통해 기기 자동 연결 및 Influx DB로 데이터 입력 처리 추가 필요

	}
	
	@Override
	public void connectionLost(Throwable cause) {
		System.out.println("===== Lost Connection - " + cause.getCause());
	}
	
	@Override
	public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
		System.out.println("===== Message with " + iMqttDeliveryToken + " delivered.");
	}

}
