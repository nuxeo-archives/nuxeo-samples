package org.nuxeo.ecm.client.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Properties;

import org.nuxeo.ecm.core.client.Launcher;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.common.utils.FileUtils;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.api.PathRef;
import org.nuxeo.ecm.core.api.impl.blob.ByteArrayBlob;
import org.nuxeo.ecm.core.api.repository.RepositoryInstance;
import org.nuxeo.ecm.core.api.repository.RepositoryManager;
import org.nuxeo.ecm.core.api.security.ACE;
import org.nuxeo.ecm.core.api.security.ACL;
import org.nuxeo.ecm.core.api.security.SecurityConstants;
import org.nuxeo.ecm.core.api.security.impl.ACLImpl;
import org.nuxeo.ecm.core.api.security.impl.ACPImpl;
import org.nuxeo.ecm.core.client.DefaultLoginHandler;
import org.nuxeo.ecm.core.client.NuxeoClient;
import org.nuxeo.ecm.core.storage.sql.coremodel.BinaryTextListener;
import org.nuxeo.osgi.application.client.NuxeoApp;
import org.nuxeo.runtime.api.Framework;

public class CustomLauncher extends Launcher {

    private static final Log log = LogFactory.getLog(BinaryTextListener.class);

    //Should be written in external (properties file)
    private static String host;
    private static String username;
    private static String password;
    private static String filename;

    /**
     * Create an nuxeo application
     */
    protected static NuxeoApp setup() throws Exception {
        File home = Launcher.setProperties();
        System.setProperty("nuxeo.runner", CustomLauncher.class.getName());
        return new NuxeoApp(home, Launcher.class.getClassLoader());
    }

    /**
     * Handling a few uses
     */
    public void run() {
        NuxeoClient client = NuxeoClient.getInstance();
        RepositoryInstance repo = null;
        File f = null;
        try {

            //Connection to "local" nuxeo after to have launching the server
            client.setLoginHandler(new DefaultLoginHandler(username, password));
            client.connect(host, 62474);
            repo = client.openRepository();
            CoreSession coreSession = repo.getSession();

            DocumentModel parent = coreSession.getDocument(new PathRef("/default-domain/workspaces"));

            //Create a folder "My Workspace" in "workspaces"
            DocumentModel doc = coreSession.createDocumentModel(parent.getPathAsString(), "myws", "Workspace");
            doc.setPropertyValue("dc:title", "My Worspace");
            doc.setPropertyValue("dc:description", "testdoc");
            doc = coreSession.createDocument(doc);
            coreSession.save();

            //Create a file "My File" in "My Workspace"
            DocumentModel docModel = coreSession.createDocumentModel(doc.getPathAsString(), "myfile", "File");
            docModel.setPropertyValue("dc:title", "My File");
            docModel.setPropertyValue("dc:description", "testfile");
            docModel = coreSession.createDocument(docModel);
            coreSession.save();

            //Create the file content
            f = new File(filename);
            byte[] bytes = new byte[(int)f.length()];
            FileInputStream in = new FileInputStream(f);
            in.read(bytes);

            //Save the file content in "My File"
            Blob blob = new ByteArrayBlob(bytes, "application/rtf");
            docModel.setProperty("file", "filename", f.getName());
            docModel.setProperty("file", "content", blob);
            coreSession.saveDocument(docModel);
            coreSession.save();

            //According to configuration, the service is got in local or in remote, via her "faade" or not
            RepositoryManager rm = Framework.getService(RepositoryManager.class);
            System.out.println(rm.getRepositories());

            //Gives permissions to Bob on "My File"
            setRwPermissions(repo, "Bob", docModel, false);

            //Get all files in nuxeo database
            DocumentModelList list = coreSession.query("select * from File order by dc:created");
            System.out.println("Research files :");

            //Displays the first five results of the request or else less
            int size = (list.size() >= 5) ? 5 : list.size();
            ArrayList<Calendar> calendars =  new ArrayList<Calendar>();
            for (int i = 0; i < size; i++) {
                DocumentModel dm = list.get(i);
                calendars.add(dm.getProperty("dc:created").getValue(Calendar.class));
                String text = new SimpleDateFormat("dd/MM/yyyy hh:ss").format(calendars.get(i).getTime());
                System.out.println(dm.getTitle() + " " + dm.getPropertyValue("dc:description") + " " + text);
            }

            //Copy the file content of the first result in "tmp.txt"
            Blob b = (Blob)list.get(0).getPropertyValue("file:content");
            if (b != null){
                FileOutputStream fos = new FileOutputStream(new File("tmp.txt"));
                FileUtils.copy(b.getStream(), fos);
            }

            //Close the application
            repo.close();
        }
        catch(Exception e){
            log.error(e, e);
        }
    }


    /**
     *  Setting write permissions for a specific user
     */
    public void setRwPermissions(RepositoryInstance repo, String username, DocumentModel file, boolean overwrite) throws Exception {
       ACE ace = new ACE(username, SecurityConstants.WRITE, true);
       ACL acl = new ACLImpl();
       ACE[] aces = { ace };
       acl.setACEs(aces);

       ACPImpl acp = new ACPImpl();
       acp.addACL(acl);

       repo.setACP((DocumentRef)file.getParentRef(), acp, overwrite);
   }

    private static String getProperty(Properties props, String propName, String defaultValue) {
        String prop = props.getProperty(propName);
        if (prop == null && defaultValue == null) {
            throw new IllegalArgumentException("Missing configuration property " + propName);
        }
        return prop;
    }

    private static void configure(Properties props) {
        host = getProperty(props, "org.nuxeo.ecm.client.test.host", "127.0.0.1");
        username = getProperty(props, "org.nuxeo.ecm.client.test.username", "Administrator");
        password = getProperty(props, "org.nuxeo.ecm.client.test.password", "Administrator");
        filename = getProperty(props, "org.nuxeo.ecm.client.test.filename", "demo.rtf");
    }

    private static void configuration(String[] args) throws IOException{
        String configFileName = "";
        if (args.length == 0) {
            configFileName = "customLauncher.properties";
        }
        else {
            configFileName = args[0];
        }

        File configFile = null;
        // try to find it in classpath
        URL cfgFileURL = Thread.currentThread().getContextClassLoader().getResource(configFileName);
        if (cfgFileURL != null) {
            String filePath = cfgFileURL.getFile();
            configFile = new File(filePath);
        }
        else {
            // try to find it in current dir
            configFile = new File(configFileName);
        }

        Properties props = new Properties();
        InputStream is = null;
        try {
            is = new FileInputStream(configFileName);
            props.load(is);
            configure(props);
        }
        catch (FileNotFoundException e) {
            log.error(e, e);
        }
        catch (IOException e) {
            log.error(e, e);
        }
        finally {
            is.close();
        }
    }

    /**
     * Launch application
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        configuration(args);
        NuxeoApp app = setup();
        Collection<File> bundles = getBundles();
        app.start();

        //Deploy bundles if it doesn't exist
        if (bundles != null) {
            app.deployBundles(bundles);
        }

        //Load application
        ClassLoader cl = app.getLoader();
        ClassLoader oldcl = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(cl);
        try {
            String main = System.getProperty("nuxeo.runner");
            if (main == null) {
                throw new Error("No runnable specified");
            }
            Class<?> mc = app.getLoader().loadClass(main);
            Method m = mc.getMethod("run");
            m.invoke(mc.newInstance());
        } finally {
            Thread.currentThread().setContextClassLoader(oldcl);
            app.shutdown();
        }
    }

}
