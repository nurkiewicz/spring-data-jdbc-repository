package com.nurkiewicz.jdbcrepository;

import org.junit.Assume;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author Tomasz Nurkiewicz
 * @since 12/20/12, 10:56 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public abstract class AbstractIntegrationTest {

	private final int databasePort;

	protected AbstractIntegrationTest() {
		this.databasePort = -1;
	}

	protected AbstractIntegrationTest(int databasePort) {
		this.databasePort = databasePort;
	}

	@BeforeTransaction
	public void ignoreIfDatabaseNotAvailable() {
		if (databasePort > 0) {
			try {
				final Socket socket = new Socket();
				socket.connect(new InetSocketAddress("localhost", databasePort));
				socket.close();
			} catch (IOException e) {
				Assume.assumeNoException(e);
			}
		}
	}

}
