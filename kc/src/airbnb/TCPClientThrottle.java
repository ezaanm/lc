package airbnb;
import java.io.*;
import java.net.*;
// https://tinyurl.com/y9jdorkf
// https://docs.oracle.com/javase/tutorial/networking/sockets/
/**
 * http://www.1point3acres.com/bbs/thread-212911-1-1.html
 * http://www.1point3acres.com/bbs/thread-165457-1-1.html
 *
写echo TCP client， 向面试官的server发请求， 读回数据。
地里比较少人说这种， 我来详细说一下， 情境是这样的： 想象你开车， 
踩下油门，车会加速，放开油门，车会减速。 client向server发的请求有以下2种： 
（a）STATUS --表示查询现在车的速度和踩下踏板的压力； 
（b）THROTTLE 50.1 --- 这条指令是“THROTTLE” 加上一个数字， 表示我现在将踩油门的压力调为50.1


EXAMPLE: 比如在telnet中
STATUS
0.0 0.0     (这行是server返回的， 第一个数字表示压力，第二个数字表示速度)  
THROTTLE 50.1 (这个指令发出 server没有返回).
STATUS
50.1 3.75   (可以看到速度在缓慢上升)
STATUS     (过几秒后，你又发STATUS指令过去).
50.1 15.98   (速度依旧上升。。。)

对就是这样，像是简单物理实验。

写完TCP client后，要求是写一个方法将速度控制到达一个target speed
最后一个问题是求这个 delta力 和 delta速度 之间的函数关系

probably more detailed analysis before coding
 *
 */
public class TCPClientThrottle {
	
	// we need to dynamically reduce the acceleration when approaching the target
	// we can make some assumptions here:
	// 1) Any throttle corresponds to a constant speed. i.e it will stablize at a speed eventually
	// 2) Given a throttle number, car will approach a target speed
	// 3) The wider diff between given throttle and current speed is, the greater accerlation (negative or position) it will have
	// So there are two ways of doing this:
	// 1) Very naievly, we apply a throttle, wait for an extended period of time until accerlation appoaches 0 and note down the speed
	//    do this using binary search until reaching the target speed
	// 2) Get a rough understanding of the thrttole-speed relation and apply the final throttle based on the target speed
	public void stablizeNaive(String ip, int port, double target) throws Exception {
		double lower = 0;
		double higher = 1000;
		double eps = 0.00000001;
		while(true) {
			double mid = (lower + higher) / 2;
			changeSpeed(ip, port, mid);

			Thread.sleep(2000); // sleep 2 seconds to allow car react

			while(true) {
				double[] firstTry = getSpeed(ip, port);
				Thread.sleep(500); // sleep 500 ms.
				double[] secondTry = getSpeed(ip, port);
				if(Math.abs(secondTry[1] - firstTry[1]) < eps) {
					// reached terminal speed
					break;
				}
			}

			// get the speed and prepare for binary search
			double[] res = getSpeed(ip, port);
			if(Math.abs(res[1] - target) < eps) {
				continue;
			} else {
				if(res[1] > target) {
					higher = mid-1;
				} else {
					lower = mid+1;
				}
			}
		}
	}

	public double[] getSpeed(String ip, int port) throws Exception {
		Socket socket = new Socket(ip, port);
		DataOutputStream output = new DataOutputStream(socket.getOutputStream());
		output.writeUTF("STATUS");
		DataInputStream input = new DataInputStream(socket.getInputStream());
		String res = input.readUTF();
		double throttle = Double.parseDouble(res.split(" ")[0]);
		double speed = Double.parseDouble(res.split(" ")[1]);
		socket.close();
		return new double[]{throttle, speed};
	}

	public void changeSpeed(String ip, int port, double target) throws Exception {
		Socket socket = new Socket(ip, port);
		DataOutputStream output = new DataOutputStream(socket.getOutputStream());
		String instruction = "THROTTLE " + String.valueOf(target);
		output.writeUTF(instruction);
		socket.close();
	}
}
