/*
 Name:		ConnectionServer.h
 Created:	3/22/2019 12:59:40 PM
 Author:	karim
*/
#ifndef __CONNSERVER_H
#define __CONNSERVER_H

#include <ESP8266WiFi.h>
#include <WiFiUdp.h>

#define UDP_RX_PACKET_MAX_SIZE 10


// Class for the connection. acts as a server
class ConnectionServer
{
  private:
	WiFiServer server;
	WiFiClient client;
	

  public:
  	//TCP server begins listening at port 
	ConnectionServer(uint16_t port) : server(port)
	{
		server.begin();
		server.setNoDelay(true);//Disable Nagle's algorithm to avoid unnecessary delays
	}

	
	//Waits till a client connects
	//returns true when a new client connects
	//returns false if there is a client already connected
	bool process()
	{
		while (!client) //Wait till a client is connected
		{
			client = server.available();
			if (client)
			{
				client.setTimeout(10000);
				return true;
			}
			delay(50);
		}
		return false;
	}

	//Send data
	bool sendData(int degree, int counts, int dir)
	{
		if (!client)
			return false;

		client.printf("%d,%d,%d", degree, counts, dir);
		client.flush(); //Make sure data send is send to client
		return true;
	}
	
	//return true if there a client connected 
	bool connected()
	{
		return client.connected();
	}
};

#endif
