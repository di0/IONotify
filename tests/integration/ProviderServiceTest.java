import com.develdio.reminderappcore.network.connection.server.ServerProxyAsyncService;
import com.develdio.reminderappcore.network.connection.server.spi.ServerFactory;
import com.develdio.reminderappcore.services.ManagerService;
import com.develdio.reminderappcore.network.connection.server.Server;

class TestServerAsyncService extends ServerProxyAsyncService
{
	public TestServerAsyncService( Server server )
	{
		super( server );
		super.listener( this );
	}

	public void up()
	{
		super.start();
	}

	public void onMessage( String newMessage )
	{
		System.out.println( "OnMessage method: " + newMessage );
	}
}

public class ProviderServiceTest
{
	public static void main( String[] args )
	{
		Server server = ServerFactory.createServer();
		ServerProxyAsyncService serviceAsync = new TestServerAsyncService( server );
		ManagerService manager = ManagerService.setup( serviceAsync );
		manager.start();
	}
}

