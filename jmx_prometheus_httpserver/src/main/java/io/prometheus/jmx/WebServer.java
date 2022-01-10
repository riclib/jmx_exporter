package io.prometheus.jmx;

import java.io.File;
import java.net.InetSocketAddress;
import java.util.Base64;

import org.jasypt.util.*;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.HTTPServer;


public class WebServer {

   public static void main(String[] args) throws Exception {
     TextEncryptor textEncryptor = new TextEncryptor();
     textEncryptor.setPassword("VivaOBenfas!");

     if (args.length < 2) {
      System.err.println("Usage: WebServer <[hostname:]port> <yaml configuration file> <jmx_URL> or Webserver encrypt passwd");
      System.exit(1);
     }
     if (args[0].equals("encrypt") ) {
       String passwd = textEncryptor.encrypt(args[1]);
       System.err.println("encrypted passwd is: E:" + passwd);
       System.exit(0);
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
     if( args.length >= 3) { // JMX urls and potentially user and passwords in command line
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
      if( args.length == 5) {      //received user and password in command line
        String passwd = args[4];
        if (passwd.startsWith("E:")) {
            passwd = passwd.substring(2);
            passwd = textEncryptor.decrypt(passwd);
        }
        jc.setJmxURLUserPasswd(jmxUrl, args[3], passwd);
      } else {
        jc.setJmxUrl(jmxUrl);
      }
    }
    
     new HTTPServer(socket, CollectorRegistry.defaultRegistry);
   }
}
