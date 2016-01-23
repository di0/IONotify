package com.develdio.reminderappsystem;

import com.develdio.reminderappcore.network.connection.server.Server;
import com.develdio.reminderappcore.network.connection.server.ServerProxyAsyncService;
import com.develdio.reminderappcore.network.connection.server.spi.ServerFactory;
import com.develdio.reminderappcore.services.ManagerService;

class ServerAsyncServiceTest extends ServerProxyAsyncService {

	public ServerAsyncServiceTest( Server server ) {

		super( server );
		super.listener( this );
	}

	public void onMessage( String newMessage ) {

		if ( newMessage.isEmpty() == false )
		{
			System.out.println( "OnMessage method: " + newMessage );
		}
	}
}

public class ServiceProviderTest {

	public static void main( String[] args ) {

		Server server = ServerFactory.createServer();
		ServerProxyAsyncService serviceAsync = new ServerAsyncServiceTest( server );
		ManagerService manager = ManagerService.setup( serviceAsync );
		manager.start();
	}
}
