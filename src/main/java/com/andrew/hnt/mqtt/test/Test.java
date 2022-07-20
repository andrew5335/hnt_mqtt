package com.andrew.hnt.mqtt.test;

import java.util.UUID;

import com.andrew.hnt.mqtt.common.MQTT;

public class Test {
	
	public static void main(String[] args) {
		String MqttServer1 = "tcp://hntnas.diskstation.me:1883";
		String MqttServer2 = "";
		String client_id = "";
		String userName = "hnt1";
		String password = "abcde";
		String topic = "#";
		String msg = "";
		String readMsg = "";
		
		client_id = UUID.randomUUID().toString();
		
		MQTT read = new MQTT(MqttServer1, client_id, userName, password);
		read.init(topic);
		//readMsg = read.getTopic();
		//System.out.println("===== readMsg : " + readMsg);
		sleep(1000);
	}
	
	static void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch(InterruptedException ite) {
			ite.printStackTrace();
		}
	}

}
