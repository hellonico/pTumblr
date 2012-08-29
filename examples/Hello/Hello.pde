import net.hellonico.tumblr.*;
import net.hellonico.potato.*;
import processing.video.*;


TumblrLibrary t;
Capture cam;

void setup() {
  size(320, 240);
  smooth();

  t = new TumblrLibrary(this);
  cam = new Capture(this, 320, 240);  
  cam.start();
  
  PFont font = createFont("",40);
  textFont(font);
}

void draw() {
  if (cam.available()) {
    cam.read();
    image(cam, 0, 0);
    text("Click to upload to Flickr", 10, height - 13);
  }
}
void keyPressed() {
  text("Uploading", 10, height - 13);
  if(key == 't') 
     println(t.textPost("super", "Hello"));
  else
  if(key == 'i')
     println(t.imagePost(cam, "Hello")); 	
}
