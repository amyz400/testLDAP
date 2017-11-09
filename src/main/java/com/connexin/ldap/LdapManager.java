package com.connexin.ldap;

import org.apache.directory.server.core.api.DirectoryService;
import org.apache.directory.server.core.factory.DefaultDirectoryServiceFactory;
import org.apache.directory.server.core.factory.DirectoryServiceFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.apache.directory.server.ldap.LdapServer;
import org.apache.directory.server.protocol.shared.store.LdifFileLoader;
import org.apache.directory.server.protocol.shared.transport.TcpTransport;
import org.apache.directory.server.protocol.shared.transport.Transport;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

/**
 * Created by aziring on 11/11/16.
 */
@SpringBootApplication
public class LdapManager {

    private static DirectoryService directoryService;

    private static LdapServer ldapServer;
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(LdapManager.class, args);

        try {
            ldapServer = createLdapServer();
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }


    }

    public static LdapServer createLdapServer() throws Exception{

        LdapServer ldapServer = new LdapServer ();
        ldapServer.setServiceName("DefaultLdapServer");
        ldapServer.setSearchBaseDn("dc=example,dc=com");


        // Read the transports
        Transport ldap = new TcpTransport("localhost", 9999);

        ldapServer.setTransports(ldap);
        // Associate the DS to this LdapServer
        DirectoryServiceFactory dsf = new DefaultDirectoryServiceFactory();
        dsf.init("SERVICE_FACTORY");
        directoryService = dsf.getDirectoryService();
        LdifFileLoader ldifLoader = new LdifFileLoader(directoryService.getAdminSession(),"ldap_entries.ldif");
        ldifLoader.execute();

        ldapServer.setDirectoryService(directoryService);
        // Propagate the anonymous flag to the DS
       // directoryService.setAllowAnonymousAccess(false);
        return ldapServer;
    }

}
