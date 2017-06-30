package airbnb;

import java.io.*;
import java.net.*;

public class TCPServerThrottle implements Runnable{

	private ServerSocket serverSocket;
	
	private double accelerationConstant = 0.0;
	
	private long last = System.currentTimeMillis();
	
	private double curSpeed = 0.0;
	
	private double throttle = 0.0;
	
	public TCPServerThrottle(int port) throws IOException {
		serverSocket = new ServerSocket(port);
	}
	
	// naive update, does not consider other stuff
	private void updateSpeed(long time) {
		if(accelerationConstant < 0) {
			time /= 1000;
			while(time > 0 && curSpeed >= 0) {
				curSpeed += accelerationConstant;
				time--;
			}
		} else {
			time /= 1000;
			while(time > 0) {
				curSpeed += accelerationConstant;
				time--;
			}
		}
	}
	
	@Override
	public void run() {
			try {
				Socket server = serverSocket.accept();
				while(true) {
					DataInputStream in = new DataInputStream(server.getInputStream());
					DataOutputStream out = new DataOutputStream(server.getOutputStream());
					
					long diffTime = System.currentTimeMillis() - last;
					last = System.currentTimeMillis();
					
					updateSpeed(diffTime);

					String str = in.readUTF();
					String[] res = str.split(" ");
					if(res[0].equals("STATUS")) {
						System.out.println("GOT STATUS Inquiry");
						System.out.println();
						out.writeUTF(throttle + " " + curSpeed);
					} else {
						double newThrottle = Double.parseDouble(res[1]);

						System.out.println("Incoming throttle: " + newThrottle + ", old throttle:" + throttle);

						if(newThrottle < throttle) {
							accelerationConstant -= 0.5 * (throttle - newThrottle);
						} else {
							accelerationConstant += 0.5 * (newThrottle - throttle);
						}
						throttle = newThrottle;
						System.out.println("accelerationConstant: " + accelerationConstant);
						System.out.println();
					}
				}
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	public static void main(String[] args) throws IOException {
		TCPServerThrottle c = new TCPServerThrottle(6603);
		c.run();
	}
}
