package pl.rozart.lab.loadbalancer;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.websession.WebSessionFilter;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.communication.tcp.TcpCommunicationSpi;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.TcpDiscoveryIpFinder;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;
import org.apache.ignite.spi.loadbalancing.roundrobin.RoundRobinLoadBalancingSpi;
import org.apache.ignite.startup.servlet.ServletContextListenerStartup;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class LoadBalancerApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(LoadBalancerApplication.class, args);
		Ignite ignite= Ignition.start(igniteConfiguration());
	}

	public static IgniteConfiguration igniteConfiguration() {
		IgniteConfiguration igniteConfiguration = new IgniteConfiguration();

		igniteConfiguration.setClientMode(false);

//		RoundRobinLoadBalancingSpi loadBalancingSpi = new RoundRobinLoadBalancingSpi();
//		loadBalancingSpi.setPerTask(true);
//		igniteConfiguration.setLoadBalancingSpi(loadBalancingSpi);

		TcpCommunicationSpi communicationSpi = new TcpCommunicationSpi();
		communicationSpi.setLocalPort(6543);
		igniteConfiguration.setCommunicationSpi(communicationSpi);

//		TcpDiscoverySpi discoverySpi = new TcpDiscoverySpi();
//		TcpDiscoveryIpFinder ipFinder = new TcpDiscoveryVmIpFinder();
//		List<InetSocketAddress> addresses = new ArrayList<>();
//		InetSocketAddress address1 = new InetSocketAddress("127.0.0.1", 47100);
//		InetSocketAddress address2 = new InetSocketAddress("127.0.0.1", 47500);
//		addresses.add(address1);
//		addresses.add(address2);
//		ipFinder.registerAddresses(addresses);
//		discoverySpi.setIpFinder(ipFinder);
//		igniteConfiguration.setDiscoverySpi(discoverySpi);

		CacheConfiguration cacheConfiguration = new CacheConfiguration();
		cacheConfiguration.setName("balancerCache");
		cacheConfiguration.setCacheMode(CacheMode.REPLICATED);
		cacheConfiguration.setBackups(1);
		igniteConfiguration.setCacheConfiguration(cacheConfiguration);

		return igniteConfiguration;
	}

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		servletContext.addFilter("IgniteWebSessionFilter", igniteWebSessionFilter());
		servletContext.addListener(ServletContextListenerStartup.class);
		super.onStartup(servletContext);
	}

	public WebSessionFilter igniteWebSessionFilter() {
		WebSessionFilter webSessionFilter = new WebSessionFilter();
		return webSessionFilter;
	}

}
