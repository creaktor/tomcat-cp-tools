/**
 * 
 */
package ralam.cp.hikari;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Set;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.naming.spi.ObjectFactory;
import javax.sql.DataSource;

import ralam.cp.crypto.PasswordCrypto;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.util.PropertyElf;

/**
 * @author ralam
 *
 */
public class HikariEncJNDIFactory implements ObjectFactory{
	   @Override
	   public Object getObjectInstance(Object obj, Name name, Context nameCtx, Hashtable<?, ?> environment) throws Exception
	   {
	      // We only know how to deal with <code>javax.naming.Reference</code> that specify a class name of "javax.sql.DataSource"
	      if ((obj == null) || !(obj instanceof Reference)) {
	         return null;
	      }

	      Reference ref = (Reference) obj;
	      if (!"javax.sql.DataSource".equals(ref.getClassName())) {
	         throw new NamingException(ref.getClassName() + " is not a valid class name/type for this JNDI factory.");
	      }

	      Set<String> hikariPropSet = PropertyElf.getPropertyNames(HikariConfig.class);

	      Properties properties = new Properties();
	      Enumeration<RefAddr> enumeration = ref.getAll();
	      while (enumeration.hasMoreElements()) {
	         RefAddr element = enumeration.nextElement();
	         String type = element.getType();
	         if (type.startsWith("dataSource.") || hikariPropSet.contains(type)) {
	        	 if(type.equalsIgnoreCase("password")){
		        	 properties.setProperty(type, PasswordCrypto.decrypt(null, null, element.getContent().toString()));
	        	 }else{
	        		 properties.setProperty(type, element.getContent().toString());
	        	 }
	        	 
	            
	         }
	      }

	      return createDataSource(properties, nameCtx);
	   }

	   private DataSource createDataSource(Properties properties, Context context) throws NamingException
	   {
	      if (properties.getProperty("dataSourceJNDI") != null) {
	         return lookupJndiDataSource(properties, context);
	      }

	      return new HikariDataSource(new HikariConfig(properties));
	   }

	   private DataSource lookupJndiDataSource(Properties properties, Context context) throws NamingException
	   {
	      if (context == null) {
	         throw new RuntimeException("dataSourceJNDI property is configured, but local JNDI context is null.");
	      }

	      String jndiName = properties.getProperty("dataSourceJNDI");
	      DataSource jndiDS = (DataSource) context.lookup(jndiName);
	      if (jndiDS == null) {
	         context = new InitialContext();
	         jndiDS = (DataSource) context.lookup(jndiName);
	         context.close();
	      }

	      if (jndiDS != null) {
	         HikariConfig config = new HikariConfig(properties);
	         config.setDataSource(jndiDS);
	         return new HikariDataSource(config);
	      }

	      return null;
	   }
}