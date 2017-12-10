package io.prometheus.jmx;

import java.io.File;
import java.net.InetSocketAddress;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.HTTPServer;

public class WebServer {

   public static void main(String[] args) throws Exception {
     if (args.length < 2) {
      System.err.println("Usage: WebServer <[hostname:]port> <yaml configuration file> <jmx_URL>");
      System.exit(1);
     }

     String[] hostnamePort = args[0].split(":");
     int port;
     InetSocketAddress socket;
     
     if (hostnamePort.length == 2) {
       port = Integer.parseInt(hostnamePort[1]);
       socket = new InetSocketAddress(hostnamePort[0], port);
     } else {
       port = Integer.parseInt(hostnamePort[0]);
       socket = new InetSocketAddress(port);
     }

     JmxCollector jc = new JmxCollector(new File(args[1])).register();
     if( args.length == 5) {
      String urls [] = args[2].split(",");
      String jmxUrl = "";
      for(String url: urls) {
        if( jmxUrl.equals("")) {
          jmxUrl = "service:jmx:rmi:///jndi/rmi://" + url + "/jmxrmi";
        }
        else {
          jmxUrl = jmxUrl + ",service:jmx:rmi:///jndi/rmi://" + url + "/jmxrmi";
        }
        
      }      
      jc.setJmxURLUserPasswd(jmxUrl, args[3], args[4]);
    }
    
     new HTTPServer(socket, CollectorRegistry.defaultRegistry);
   }
}
