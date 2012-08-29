/**
 * ##library.name##
 * ##library.sentence##
 * ##library.url##
 *
 * Copyright ##copyright## ##author##
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General
 * Public License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA  02111-1307  USA
 * 
 * @author      ##author##
 * @modified    ##date##
 * @version     ##library.prettyVersion## (##library.version##)
 */

package net.hellonico.tumblr;


import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

import net.asplode.tumblr.PhotoPost;
import net.asplode.tumblr.TextPost;
import net.asplode.tumblr.Tumblr;
import net.hellonico.N;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.json.JSONObject;

import processing.core.PApplet;
import processing.core.PImage;

/**
 * This is a template class and can be used to start a new processing library or tool.
 * Make sure you rename this class as well as the name of the example package 'template' 
 * to your own library or tool naming convention.
 * 
 * @example Hello 
 * 
 * (the tag @example followed by the name of an example included in folder 'examples' will
 * automatically include the example in the javadoc.)
 *
 */

public class TumblrLibrary {
	
	
	PApplet myParent;
	
	public final static String VERSION = "##library.prettyVersion##";

	private Tumblr tumblr;
	
	String email;// = "hellonico@gmail.com";
	String password;// = "cmi2r3a";

	String key;// = "VFaIhr0R2xFrdBH1dXT9H4GpOI3p9kXHe2BmJYAbSLwdhFG8FB";
	String secret;// = "2Wz2qNCTiyMEYlmNY5zqKYJwBIlYd9D7cHBptjTqhJ1gLVwvvE";
	
	/**
	 * a Constructor, usually called in the setup() method in your sketch to
	 * initialize and start the library.
	 * 
	 * @example Hello
	 * @param theParent
	 */
	public TumblrLibrary(PApplet theParent) {
		
		myParent = theParent;
		
		try {
			// load settings
			Class klass = Class.forName("net.hellonico.potato.Potato");
			Constructor c = klass.getConstructor(PApplet.class);
			Object potato = c.newInstance(theParent);
			Method m = klass.getMethod("getSettings", String.class);
			HashMap settings = (HashMap) m.invoke(potato, "tumblr");
			
			// load password
			key = (String)settings.get("key");
			secret = (String)settings.get("secret");
			email = (String)settings.get("email");
			password = (String)settings.get("password");
			
		} catch (Exception e) {
			throw new RuntimeException("This is carrot day." + e.getMessage());
		}
		
		tumblr = new Tumblr(key, secret);
		tumblr.setCredentials(email, password);
	}
	
	public Tumblr getTumblr() {
		return tumblr;
	}
	private void print(Exception e) {this.myParent.println(e);}
	
	public JSONObject getDashboard() {
		try {
			return tumblr.getDashboard(true);
		} catch (Exception e) {
			print(e);
			return null;
		}
	}
	public int textPost(String title, String body) {
		try {
		TextPost post = new TextPost();
		post.setCredentials(email, password);
		post.setTitle(title);
		post.setBody(body);
		return post.postToTumblr();
		} catch (Exception e) {
			print(e);
			return 0;
		}
	}
	
	public int imagePost(PImage p, String caption) {
		try {
			PhotoPost post = new PhotoPost();
			post.setCredentials(email, password);
			post.setCaption(caption);
			ByteArrayBody body = new ByteArrayBody(N.compressImage(p), "image.jpg");
			
			Class superclass = post.getClass().getSuperclass();
			Field f = superclass.getDeclaredField("entity");
			f.setAccessible(true);
			MultipartEntity e = (MultipartEntity) f.get(post);
			e.addPart("data", body);
			return post.postToTumblr();
		} catch (Exception e) {
			print(e);
			return 0;
		}
	}

	public static String version() {
		return VERSION;
	}
}

